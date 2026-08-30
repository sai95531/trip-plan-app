package com.example.data.sample

import com.example.data.model.ExpenseCategory
import com.example.data.model.ExpenseEntity
import com.example.data.model.TripEntity
import java.util.concurrent.TimeUnit

object SampleData {

    data class SampleReceiptPreset(
        val title: String,
        val merchant: String,
        val category: ExpenseCategory,
        val amount: Double,
        val rawReceiptText: String,
        val description: String,
        val itemsPreview: String
    )

    val sampleReceiptPresets = listOf(
        SampleReceiptPreset(
            title = "🍜 Kyoto Ramen & Gyoza",
            merchant = "Ramen Sen-no-Kaze Kyoto",
            category = ExpenseCategory.FOOD_DINING,
            amount = 32.50,
            rawReceiptText = """
                RAMEN SEN-NO-KAZE KYOTO
                Nakagyo Ward, Kyoto, Japan
                TEL: 075-255-0181
                Date: 2026-08-15  Time: 19:42
                Table: 04  Server: Kenji
                --------------------------------
                1x Special Tonkotsu Ramen    $14.00
                1x Spicy Miso Ramen          $13.50
                1x Pan-Fried Pork Gyoza (6pc) $5.00
                --------------------------------
                SUBTOTAL:                    $32.50
                TAX (Included 10%):           $2.95
                TOTAL:                       $32.50
                PAYMENT: VISA Contactless ****4821
                Thank you for visiting Kyoto!
            """.trimIndent(),
            description = "Dinner at famous ramen shop in Kyoto downtown with itemized bowls and side dishes.",
            itemsPreview = "2x Ramen bowls, 1x Pork Gyoza"
        ),
        SampleReceiptPreset(
            title = "🚅 Shinkansen Bullet Train",
            merchant = "JR Central Railway",
            category = ExpenseCategory.TRANSPORTATION,
            amount = 138.00,
            rawReceiptText = """
                JAPAN RAILWAYS GROUP - JR CENTRAL
                Tokyo Station Ticket Gate #4
                Date: 2026-08-16 08:15 AM
                Ticket No: TK-88921-X
                --------------------------------
                SHINKANSEN NOZOMI #217
                Tokyo (08:30) -> Kyoto (10:45)
                Car: 06  Seat: 12A (Window)
                Reserved Seat Express Fare:  $138.00
                --------------------------------
                TOTAL AMOUNT:                $138.00
                METHOD: Mastercard Chip ****9012
                Have a pleasant journey!
            """.trimIndent(),
            description = "High speed bullet train tickets from Tokyo to Kyoto.",
            itemsPreview = "Reserved Seat Nozomi Express Ticket"
        ),
        SampleReceiptPreset(
            title = "🏨 Gracery Shinjuku Hotel",
            merchant = "Hotel Gracery Shinjuku",
            category = ExpenseCategory.LODGING,
            amount = 450.00,
            rawReceiptText = """
                HOTEL GRACERY SHINJUKU
                1-19-1 Kabukicho, Shinjuku City, Tokyo
                Folio ID: HG-2026-9931
                Check-in: 2026-08-14  Check-out: 2026-08-17
                Guest: Alex Turner (Room 1804)
                --------------------------------
                3 Nights Deluxe Twin Room   $410.00
                City Accommodation Tax       $10.00
                2x Breakfast Buffet Voucher  $30.00
                --------------------------------
                TOTAL BILLED:                $450.00
                PAID VIA: AMEX ****1004
                Thank you for staying with us.
            """.trimIndent(),
            description = "3-night accommodation in central Tokyo with breakfast buffet.",
            itemsPreview = "3 Nights Stay + Breakfast Vouchers"
        ),
        SampleReceiptPreset(
            title = "🎟️ teamLab Borderless Digital Museum",
            merchant = "teamLab Planets & Borderless",
            category = ExpenseCategory.ACTIVITIES,
            amount = 76.00,
            rawReceiptText = """
                teamLab Borderless Tokyo
                Azabudai Hills Garden Plaza B
                Order ID: TL-8829104
                Date: 2026-08-17 14:00 Entry
                --------------------------------
                2x Adult General Admission    $76.00
                --------------------------------
                TOTAL:                       $76.00
                PAYMENT: Apple Pay (Visa ****3321)
                No re-entry allowed.
            """.trimIndent(),
            description = "Interactive digital art museum admission for two people.",
            itemsPreview = "2x General Admission Passes"
        ),
        SampleReceiptPreset(
            title = "☕ Blue Bottle Roastery & Pastries",
            merchant = "Blue Bottle Coffee Kiyosumi",
            category = ExpenseCategory.CAFE_SNACKS,
            amount = 18.50,
            rawReceiptText = """
                BLUE BOTTLE COFFEE
                Kiyosumi-Shirakawa Roastery
                Date: 2026-08-18 10:15 AM
                Register: 01
                --------------------------------
                1x Bella Donovan Single Drip   $6.50
                1x Iced Oat Milk Latte         $7.00
                1x Almond Butter Cardamom Cake $5.00
                --------------------------------
                TOTAL:                         $18.50
                PAID: Cash Received $20.00
                CHANGE: $1.50
            """.trimIndent(),
            description = "Morning pour-over specialty coffee and pastries in Tokyo.",
            itemsPreview = "Drip Coffee, Iced Latte, Cardamom Cake"
        ),
        SampleReceiptPreset(
            title = "🛍️ Akihabara Electronics & Souvenirs",
            merchant = "Yodobashi Camera Akiba",
            category = ExpenseCategory.SHOPPING,
            amount = 124.00,
            rawReceiptText = """
                YODOBASHI CAMERA MULTIMEDIA AKIBA
                1-1 Kanda Hanaokacho, Chiyoda City
                TAX FREE TRANSACTION (Passport verified)
                Date: 2026-08-19 16:30
                --------------------------------
                1x Anker 100W Travel GaN Charger  $49.00
                2x Studio Ghibli Wooden Music Box $55.00
                1x Traditional Matcha Tea Kit     $20.00
                --------------------------------
                SUBTOTAL:                        $124.00
                TAX FREE EXEMPTION:              -$12.40
                FINAL CHARGED:                   $124.00
                PAYMENT: Visa ****7712
            """.trimIndent(),
            description = "Travel accessories, Ghibli souvenir music boxes and authentic matcha tea kit.",
            itemsPreview = "GaN Travel Charger, Ghibli Music Box, Matcha Kit"
        )
    )

    fun createInitialTrips(): List<TripEntity> {
        val now = System.currentTimeMillis()
        val oneDay = TimeUnit.DAYS.toMillis(1)

        val japanTrip = TripEntity(
            id = 1,
            name = "Japan Autumn Adventure",
            destination = "Tokyo, Kyoto & Osaka",
            startDate = now - (3 * oneDay),
            endDate = now + (7 * oneDay),
            budget = 3200.0,
            currencySymbol = "$",
            currencyCode = "USD",
            tripType = "Vacation",
            colorHex = 0xFF6366F1,
            groupMembers = "You, Alex, Priya, Rahul",
            createdAt = now - (5 * oneDay)
        )

        val euroTrip = TripEntity(
            id = 2,
            name = "Swiss Alps & Paris",
            destination = "Switzerland & France",
            startDate = now + (20 * oneDay),
            endDate = now + (32 * oneDay),
            budget = 4800.0,
            currencySymbol = "€",
            currencyCode = "EUR",
            tripType = "Backpacking",
            colorHex = 0xFF0EA5E9,
            groupMembers = "You, Sophia, Liam",
            createdAt = now - (2 * oneDay)
        )

        return listOf(japanTrip, euroTrip)
    }

    fun createInitialExpenses(): List<ExpenseEntity> {
        val now = System.currentTimeMillis()
        val oneDay = TimeUnit.DAYS.toMillis(1)

        return listOf(
            ExpenseEntity(
                id = 1,
                tripId = 1,
                title = "Hotel Gracery Shinjuku",
                amount = 450.0,
                category = ExpenseCategory.LODGING.displayName,
                date = now - (3 * oneDay),
                paymentMethod = "Credit Card",
                notes = "3 nights stay in Shinjuku, central location near Godzilla head",
                isAiParsed = true,
                tags = "Hotel,Tokyo,Lodging",
                itemsJson = """[{"name":"3 Nights Deluxe Twin Room","quantity":1,"price":410.0},{"name":"Breakfast Buffet","quantity":2,"price":30.0},{"name":"City Tax","quantity":1,"price":10.0}]""",
                location = "Shinjuku, Tokyo",
                paidBy = "You",
                splitType = "EQUAL",
                splitWith = "You, Alex, Priya, Rahul"
            ),
            ExpenseEntity(
                id = 2,
                tripId = 1,
                title = "Shinkansen Bullet Train",
                amount = 138.0,
                category = ExpenseCategory.TRANSPORTATION.displayName,
                date = now - (2 * oneDay),
                paymentMethod = "Credit Card",
                notes = "Tokyo to Kyoto reserved window seat on Nozomi",
                isAiParsed = true,
                tags = "Train,JR,Kyoto",
                itemsJson = """[{"name":"Shinkansen Nozomi Reserved Seat","quantity":1,"price":138.0}]""",
                location = "Tokyo Station",
                paidBy = "Alex",
                splitType = "EQUAL",
                splitWith = "You, Alex, Priya, Rahul"
            ),
            ExpenseEntity(
                id = 3,
                tripId = 1,
                title = "Ramen Sen-no-Kaze",
                amount = 32.50,
                category = ExpenseCategory.FOOD_DINING.displayName,
                date = now - (2 * oneDay) + 3600000 * 5,
                paymentMethod = "Credit Card",
                notes = "Tonkotsu & spicy miso ramen with handmade gyoza",
                isAiParsed = true,
                tags = "Dinner,Ramen,Kyoto",
                itemsJson = """[{"name":"Special Tonkotsu Ramen","quantity":1,"price":14.0},{"name":"Spicy Miso Ramen","quantity":1,"price":13.5},{"name":"Pork Gyoza","quantity":1,"price":5.0}]""",
                location = "Downtown Kyoto",
                paidBy = "Priya",
                splitType = "EQUAL",
                splitWith = "You, Alex, Priya, Rahul"
            ),
            ExpenseEntity(
                id = 4,
                tripId = 1,
                title = "teamLab Borderless Museum",
                amount = 76.0,
                category = ExpenseCategory.ACTIVITIES.displayName,
                date = now - oneDay,
                paymentMethod = "Mobile Pay",
                notes = "2 adult passes to digital light art exhibits",
                isAiParsed = true,
                tags = "Museum,Art,Tickets",
                itemsJson = """[{"name":"Adult General Admission","quantity":2,"price":76.0}]""",
                location = "Azabudai Hills, Tokyo",
                paidBy = "Rahul",
                splitType = "EQUAL",
                splitWith = "You, Alex, Priya, Rahul"
            ),
            ExpenseEntity(
                id = 5,
                tripId = 1,
                title = "Blue Bottle Coffee & Pastry",
                amount = 18.50,
                category = ExpenseCategory.CAFE_SNACKS.displayName,
                date = now - 3600000 * 8,
                paymentMethod = "Cash",
                notes = "Morning drip coffee and cardamom pastry",
                isAiParsed = true,
                tags = "Coffee,Breakfast",
                itemsJson = """[{"name":"Single Drip Coffee","quantity":1,"price":6.5},{"name":"Iced Oat Latte","quantity":1,"price":7.0},{"name":"Cardamom Cake","quantity":1,"price":5.0}]""",
                location = "Kiyosumi Roastery",
                paidBy = "You",
                splitType = "EQUAL",
                splitWith = "You, Alex"
            ),
            ExpenseEntity(
                id = 6,
                tripId = 1,
                title = "Akihabara Electronics & Gifts",
                amount = 124.0,
                category = ExpenseCategory.SHOPPING.displayName,
                date = now - 3600000 * 2,
                paymentMethod = "Credit Card",
                notes = "Travel charger, Ghibli souvenir music box, matcha tea",
                isAiParsed = true,
                tags = "Souvenirs,Shopping,Ghibli",
                itemsJson = """[{"name":"Anker 100W GaN Charger","quantity":1,"price":49.0},{"name":"Studio Ghibli Music Box","quantity":2,"price":55.0},{"name":"Matcha Tea Kit","quantity":1,"price":20.0}]""",
                location = "Yodobashi Akiba",
                paidBy = "You",
                splitType = "YOU_ONLY",
                splitWith = "You"
            )
        )
    }
}
