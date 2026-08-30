package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpenseEntity
import com.example.data.model.TripEntity
import com.example.data.util.DebtSettlement
import com.example.data.util.GroupSplitHelper
import com.example.data.util.GroupSplitSummary
import com.example.data.util.MemberSpending
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GroupSplitSection(
    trip: TripEntity,
    expenses: List<ExpenseEntity>,
    onAddExpense: () -> Unit,
    onRecordSettlement: (from: String, to: String, amount: Double) -> Unit,
    onUpdateGroupMembers: (newMembersCsv: String) -> Unit
) {
    val context = LocalContext.current
    val splitSummary = remember(trip, expenses) {
        GroupSplitHelper.calculateGroupSplit(trip, expenses)
    }

    var showManageMembersDialog by remember { mutableStateOf(false) }
    var showSettleUpDialog by remember { mutableStateOf<DebtSettlement?>(null) }

    val userSpending = splitSummary.memberSpendings.find { it.name.equals("You", ignoreCase = true) }
        ?: splitSummary.memberSpendings.firstOrNull()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Banner Card: Personal Status & Quick Share
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF312E81),
                                Color(0xFF4338CA)
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Group Bill Split",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${trip.membersList.size} Travelers • ${expenses.size} Expenses",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                val report = GroupSplitHelper.generateShareableSummaryText(trip, splitSummary)
                                shareGroupText(context, report, "Share Group Bill Split")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Split",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Personal Net Balance Pill
                    userSpending?.let { user ->
                        val net = user.netBalance
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = when {
                                net > 0.05 -> Color(0xFF059669).copy(alpha = 0.25f)
                                net < -0.05 -> Color(0xFFDC2626).copy(alpha = 0.25f)
                                else -> Color.White.copy(alpha = 0.15f)
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when {
                                    net > 0.05 -> Color(0xFF34D399)
                                    net < -0.05 -> Color(0xFFF87171)
                                    else -> Color.White.copy(alpha = 0.3f)
                                }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = when {
                                            net > 0.05 -> Icons.Default.CallReceived
                                            net < -0.05 -> Icons.Default.CallMade
                                            else -> Icons.Default.CheckCircle
                                        },
                                        contentDescription = null,
                                        tint = when {
                                            net > 0.05 -> Color(0xFF34D399)
                                            net < -0.05 -> Color(0xFFF87171)
                                            else -> Color.White
                                        },
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = when {
                                                net > 0.05 -> "You are owed"
                                                net < -0.05 -> "You owe to group"
                                                else -> "All Settled Up!"
                                            },
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.85f),
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "${trip.currencySymbol}${String.format(Locale.US, "%.2f", abs(net))}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Paid: ${trip.currencySymbol}${String.format(Locale.US, "%.2f", user.totalPaid)}",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    Text(
                                        text = "Share: ${trip.currencySymbol}${String.format(Locale.US, "%.2f", user.totalShare)}",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons inside banner
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showManageMembersDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Members", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val report = GroupSplitHelper.generateShareableSummaryText(trip, splitSummary)
                                shareGroupText(context, report, "Share Settlement Report")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share Bill", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Simplified Debt Settlements (Who owes whom)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🤝 Who Owes Whom (Settlements)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${splitSummary.settlements.size} pending",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (splitSummary.settlements.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Everyone is all settled up!",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                } else {
                    splitSummary.settlements.forEach { settlement ->
                        SettlementRowItem(
                            settlement = settlement,
                            currencySymbol = trip.currencySymbol,
                            onSettleUp = { showSettleUpDialog = settlement }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Section: Member Spending Balances List
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📊 Member Breakdown",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total: ${trip.currencySymbol}${String.format(Locale.US, "%.2f", splitSummary.totalTripExpense)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                splitSummary.memberSpendings.forEach { member ->
                    MemberBalanceRow(
                        member = member,
                        currencySymbol = trip.currencySymbol,
                        totalTripExpense = splitSummary.totalTripExpense
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
                    )
                }
            }
        }
    }

    // Settle Up Confirmation Dialog
    showSettleUpDialog?.let { settlement ->
        AlertDialog(
            onDismissRequest = { showSettleUpDialog = null },
            title = { Text("Record Settlement") },
            text = {
                Text(
                    "Log a settlement where ${settlement.fromMember} pays ${settlement.toMember} ${trip.currencySymbol}${String.format(Locale.US, "%.2f", settlement.amount)}?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRecordSettlement(settlement.fromMember, settlement.toMember, settlement.amount)
                        showSettleUpDialog = null
                    }
                ) {
                    Text("Confirm Settlement")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettleUpDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Manage Members Dialog
    if (showManageMembersDialog) {
        ManageGroupMembersDialog(
            currentMembersCsv = trip.groupMembers,
            onSave = { newCsv ->
                onUpdateGroupMembers(newCsv)
                showManageMembersDialog = false
            },
            onDismiss = { showManageMembersDialog = false }
        )
    }
}

@Composable
private fun SettlementRowItem(
    settlement: DebtSettlement,
    currencySymbol: String,
    onSettleUp: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                MemberAvatarChip(name = settlement.fromMember)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                MemberAvatarChip(name = settlement.toMember)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$currencySymbol${String.format(Locale.US, "%.2f", settlement.amount)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = onSettleUp,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Settle", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MemberBalanceRow(
    member: MemberSpending,
    currencySymbol: String,
    totalTripExpense: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MemberAvatarChip(name = member.name)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Paid: $currencySymbol${String.format(Locale.US, "%.2f", member.totalPaid)} • Share: $currencySymbol${String.format(Locale.US, "%.2f", member.totalShare)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        val net = member.netBalance
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = when {
                net > 0.05 -> Color(0xFF10B981).copy(alpha = 0.15f)
                net < -0.05 -> Color(0xFFEF4444).copy(alpha = 0.15f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ) {
            Text(
                text = when {
                    net > 0.05 -> "+$currencySymbol${String.format(Locale.US, "%.2f", net)}"
                    net < -0.05 -> "-$currencySymbol${String.format(Locale.US, "%.2f", abs(net))}"
                    else -> "Settled"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    net > 0.05 -> Color(0xFF059669)
                    net < -0.05 -> Color(0xFFDC2626)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun MemberAvatarChip(name: String) {
    val cleanName = name.trim().ifBlank { "You" }
    val initial = cleanName.first().uppercase()
    val bgColor = remember(cleanName) {
        val colors = listOf(
            Color(0xFF6366F1),
            Color(0xFFEC4899),
            Color(0xFF10B981),
            Color(0xFFF59E0B),
            Color(0xFF8B5CF6),
            Color(0xFF06B6D4)
        )
        colors[abs(cleanName.hashCode()) % colors.size]
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = cleanName,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ManageGroupMembersDialog(
    currentMembersCsv: String,
    onSave: (newCsv: String) -> Unit,
    onDismiss: () -> Unit
) {
    var newMemberName by remember { mutableStateOf("") }
    val members = remember {
        mutableStateOf(
            currentMembersCsv.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .ifEmpty { listOf("You") }
        )
    }

    val presetNames = listOf("Alex", "Priya", "Rahul", "Sophia", "Liam", "Emma", "David", "Ananya")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("👥 Group Members") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Add companions traveling on this trip to split shared expenses.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newMemberName,
                        onValueChange = { newMemberName = it },
                        placeholder = { Text("Enter name (e.g. Maya)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Button(
                        onClick = {
                            val trimmed = newMemberName.trim()
                            if (trimmed.isNotBlank() && !members.value.contains(trimmed)) {
                                members.value = members.value + trimmed
                                newMemberName = ""
                            }
                        },
                        enabled = newMemberName.isNotBlank(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Add")
                    }
                }

                Text("Quick Add Suggestions:", style = MaterialTheme.typography.labelSmall)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    presetNames.forEach { name ->
                        if (!members.value.contains(name)) {
                            FilterChip(
                                selected = false,
                                onClick = {
                                    members.value = members.value + name
                                },
                                label = { Text("+ $name", fontSize = 11.sp) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("Current Travelers:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    members.value.forEach { m ->
                        FilterChip(
                            selected = true,
                            onClick = {
                                if (members.value.size > 1 && !m.equals("You", ignoreCase = true)) {
                                    members.value = members.value.filter { it != m }
                                }
                            },
                            label = { Text(if (m == "You") "You (Host)" else "$m ✕", fontSize = 12.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(members.value.joinToString(", "))
                }
            ) {
                Text("Save Travelers")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun shareGroupText(context: Context, text: String, title: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, title))
}
