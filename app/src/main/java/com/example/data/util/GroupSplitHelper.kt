package com.example.data.util

import com.example.data.model.ExpenseEntity
import com.example.data.model.TripEntity
import java.util.Locale
import kotlin.math.abs
import kotlin.math.min

data class MemberSpending(
    val name: String,
    val totalPaid: Double,
    val totalShare: Double,
    val netBalance: Double // Positive = Owed to them (Gets back), Negative = They owe
)

data class DebtSettlement(
    val fromMember: String,
    val toMember: String,
    val amount: Double
)

data class GroupSplitSummary(
    val totalTripExpense: Double,
    val memberSpendings: List<MemberSpending>,
    val settlements: List<DebtSettlement>,
    val currencySymbol: String
)

object GroupSplitHelper {

    fun calculateGroupSplit(trip: TripEntity, expenses: List<ExpenseEntity>): GroupSplitSummary {
        val members = trip.membersList.ifEmpty { listOf("You") }
        val currencySymbol = trip.currencySymbol

        val paidMap = mutableMapOf<String, Double>()
        val shareMap = mutableMapOf<String, Double>()

        // Initialize all known members
        members.forEach { m ->
            paidMap[m] = 0.0
            shareMap[m] = 0.0
        }

        var totalTripExpense = 0.0

        expenses.forEach { exp ->
            totalTripExpense += exp.amount
            val payer = exp.paidBy.trim().ifBlank { "You" }
            paidMap[payer] = (paidMap[payer] ?: 0.0) + exp.amount

            val splitMembers = exp.getSplitMembers(members)
            if (splitMembers.isNotEmpty()) {
                val perPersonShare = exp.amount / splitMembers.size
                splitMembers.forEach { member ->
                    shareMap[member] = (shareMap[member] ?: 0.0) + perPersonShare
                }
            } else {
                shareMap[payer] = (shareMap[payer] ?: 0.0) + exp.amount
            }
        }

        // Collect all active members (including any custom payer/member added in expenses)
        val allMemberNames = (members + paidMap.keys + shareMap.keys).distinct().filter { it.isNotBlank() }

        val memberSpendings = allMemberNames.map { member ->
            val paid = paidMap[member] ?: 0.0
            val share = shareMap[member] ?: 0.0
            val net = paid - share
            MemberSpending(
                name = member,
                totalPaid = paid,
                totalShare = share,
                netBalance = net
            )
        }.sortedByDescending { it.totalPaid }

        // Compute Minimal Cash Flow Debt Settlements (Splitwise Algorithm)
        val settlements = computeSettlements(memberSpendings)

        return GroupSplitSummary(
            totalTripExpense = totalTripExpense,
            memberSpendings = memberSpendings,
            settlements = settlements,
            currencySymbol = currencySymbol
        )
    }

    private fun computeSettlements(spendings: List<MemberSpending>): List<DebtSettlement> {
        // Debtors (owe money, net < -0.01)
        val debtors = mutableListOf<Pair<String, Double>>()
        // Creditors (get money back, net > 0.01)
        val creditors = mutableListOf<Pair<String, Double>>()

        spendings.forEach { s ->
            if (s.netBalance < -0.05) {
                debtors.add(Pair(s.name, abs(s.netBalance)))
            } else if (s.netBalance > 0.05) {
                creditors.add(Pair(s.name, s.netBalance))
            }
        }

        // Sort both for greedy matching
        debtors.sortByDescending { it.second }
        creditors.sortByDescending { it.second }

        val settlements = mutableListOf<DebtSettlement>()
        var dIdx = 0
        var cIdx = 0

        val debtorsWork = debtors.toMutableList()
        val creditorsWork = creditors.toMutableList()

        while (dIdx < debtorsWork.size && cIdx < creditorsWork.size) {
            val (debtor, dAmount) = debtorsWork[dIdx]
            val (creditor, cAmount) = creditorsWork[cIdx]

            val settleAmount = min(dAmount, cAmount)
            if (settleAmount > 0.01) {
                settlements.add(
                    DebtSettlement(
                        fromMember = debtor,
                        toMember = creditor,
                        amount = settleAmount
                    )
                )
            }

            debtorsWork[dIdx] = Pair(debtor, dAmount - settleAmount)
            creditorsWork[cIdx] = Pair(creditor, cAmount - settleAmount)

            if (debtorsWork[dIdx].second <= 0.01) dIdx++
            if (creditorsWork[cIdx].second <= 0.01) cIdx++
        }

        return settlements
    }

    fun generateShareableSummaryText(trip: TripEntity, summary: GroupSplitSummary): String {
        return buildString {
            appendLine("👥 *TRIP GROUP BILL SPLIT & SETTLEMENT*")
            appendLine("✈️ Trip: ${trip.name} (${trip.destination})")
            appendLine("💰 Total Group Expenses: ${trip.currencySymbol}${String.format(Locale.US, "%.2f", summary.totalTripExpense)}")
            appendLine("👥 Group Members: ${trip.groupMembers}")
            appendLine("")
            appendLine("📊 *Member Balances:*")
            summary.memberSpendings.forEach { m ->
                val status = when {
                    m.netBalance > 0.05 -> "🟢 Gets back ${trip.currencySymbol}${String.format(Locale.US, "%.2f", m.netBalance)}"
                    m.netBalance < -0.05 -> "🔴 Owes ${trip.currencySymbol}${String.format(Locale.US, "%.2f", abs(m.netBalance))}"
                    else -> "⚪ Settled (₹0)"
                }
                appendLine("• *${m.name}*: Paid ${trip.currencySymbol}${String.format(Locale.US, "%.2f", m.totalPaid)} | Share: ${trip.currencySymbol}${String.format(Locale.US, "%.2f", m.totalShare)} ($status)")
            }

            appendLine("")
            if (summary.settlements.isNotEmpty()) {
                appendLine("🤝 *Smart Settlements (Who Owes Whom):*")
                summary.settlements.forEach { s ->
                    appendLine("👉 *${s.fromMember}* pays *${s.toMember}* ➡️ ${trip.currencySymbol}${String.format(Locale.US, "%.2f", s.amount)}")
                }
            } else {
                appendLine("🎉 *All balances are perfectly settled!*")
            }

            appendLine("")
            appendLine("Tracked & Split via Trip Planner & Expense Tracker")
        }
    }
}
