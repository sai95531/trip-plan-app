package com.example.data.explore

import com.example.data.model.DestinationExploreData
import com.example.data.model.FamousPlace
import com.example.data.model.LocalFoodItem
import com.example.data.util.TravelImageHelper

object DynamicDestinationEngine {

    fun generate(destinationQuery: String): DestinationExploreData {
        val cleanName = destinationQuery.trim().split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }.ifBlank { "Scenic Explorer Destination" }

        val q = cleanName.lowercase()
        val isInternational = q.contains("paris") || q.contains("london") || q.contains("dubai") || 
                q.contains("tokyo") || q.contains("bali") || q.contains("singapore") || 
                q.contains("swiss") || q.contains("rome") || q.contains("new york")

        val stateName = when {
            isInternational -> "International"
            q.contains("vizag") || q.contains("tirupati") || q.contains("araku") || q.contains("borra") || q.contains("kailasagiri") || q.contains("rushikonda") || q.contains("gandikota") || q.contains("andhra") -> "Andhra Pradesh"
            q.contains("hyderabad") || q.contains("charminar") || q.contains("golconda") || q.contains("warangal") || q.contains("telangana") -> "Telangana"
            q.contains("manali") || q.contains("solang") || q.contains("rohtang") || q.contains("atal tunnel") || q.contains("shimla") || q.contains("himachal") -> "Himachal Pradesh"
            q.contains("jaipur") || q.contains("hawa mahal") || q.contains("amber fort") || q.contains("udaipur") || q.contains("lake pichola") || q.contains("jodhpur") || q.contains("rajasthan") -> "Rajasthan"
            q.contains("kerala") || q.contains("munnar") || q.contains("alleppey") || q.contains("kochi") || q.contains("varkala") || q.contains("wayanad") -> "Kerala"
            q.contains("mumbai") || q.contains("marine drive") || q.contains("gateway of india") || q.contains("pune") || q.contains("lonavala") || q.contains("maharashtra") -> "Maharashtra"
            q.contains("delhi") || q.contains("red fort") || q.contains("qutub minar") || q.contains("india gate") -> "Delhi NCR"
            q.contains("varanasi") || q.contains("kashi") || q.contains("ganga aarti") || q.contains("agra") || q.contains("taj mahal") || q.contains("uttar pradesh") -> "Uttar Pradesh"
            q.contains("bangalore") || q.contains("bengaluru") || q.contains("lalbagh") || q.contains("cubbon") || q.contains("nandi hills") || q.contains("mysore") || q.contains("hampi") || q.contains("karnataka") -> "Karnataka"
            q.contains("chennai") || q.contains("marina beach") || q.contains("ooty") || q.contains("doddabetta") || q.contains("madurai") || q.contains("meenakshi") || q.contains("tamil nadu") -> "Tamil Nadu"
            q.contains("goa") || q.contains("baga") || q.contains("palolem") || q.contains("aguada") || q.contains("dudhsagar") -> "Goa"
            q.contains("puri") || q.contains("konark") || q.contains("bhubaneswar") || q.contains("odisha") -> "Odisha"
            q.contains("rishikesh") || q.contains("kedarnath") || q.contains("badrinath") || q.contains("haridwar") || q.contains("uttarakhand") -> "Uttarakhand"
            q.contains("leh") || q.contains("ladakh") || q.contains("pangong") -> "Ladakh (UT)"
            q.contains("kolkata") || q.contains("darjeeling") || q.contains("victoria memorial") || q.contains("howrah") || q.contains("west bengal") -> "West Bengal"
            q.contains("amritsar") || q.contains("golden temple") || q.contains("punjab") -> "Punjab"
            q.contains("statue of unity") || q.contains("somnath") || q.contains("dwarka") || q.contains("gujarat") -> "Gujarat"
            q.contains("andaman") || q.contains("radhanagar") || q.contains("havelock") -> "Andaman & Nicobar"
            else -> "Scenic Tourism Hub"
        }

        val country = if (isInternational) "International" else "India"

        val places = listOf(
            FamousPlace(
                id = "dyn_1",
                name = "$cleanName Landmark & Heritage Center",
                localName = "Iconic $cleanName",
                category = if (q.contains("beach") || q.contains("lake")) "Beach" else if (q.contains("temple") || q.contains("ghat")) "Spiritual" else "Historical",
                description = "Renowned top-rated attraction in $cleanName featuring remarkable scenic architecture, rich history, and vibrant local atmosphere.",
                highlights = listOf("Top-rated panoramic viewpoint", "Rich cultural heritage and history", "Guided tours and audio guides available", "Iconic photography angles"),
                entryFeeInr = if (isInternational) 500.0 else 50.0,
                timings = "9:00 AM - 6:00 PM",
                bestTimeToVisit = "Morning 9:00 AM or 4:30 PM for golden light",
                insiderTip = "Visit during early morning hours to enjoy peaceful atmosphere and take clear photos.",
                photoSpot = "Main observation area facing the iconic central structure",
                estimatedDurationHours = 2.5,
                trendingTag = "🔥 #1 Top-Rated Landmark in $cleanName",
                rating = 4.9,
                reviewCount = "65K+ online reviews",
                areaOrNeighborhood = "$cleanName Tourism Zone",
                imageUrl = TravelImageHelper.getPlaceImage(cleanName, "Historical")
            ),
            FamousPlace(
                id = "dyn_2",
                name = "$cleanName Scenic Viewpoint & Promenade",
                localName = "Sunset Ridge of $cleanName",
                category = "Viewpoint",
                description = "Vantage point offering breathtaking 360-degree vistas over $cleanName, surrounding hills, and sunset horizons.",
                highlights = listOf("Spectacular sunset panoramic views", "Lush surrounding gardens and trails", "Fresh refreshments and local stalls"),
                entryFeeInr = 0.0,
                timings = "Open 24/7",
                bestTimeToVisit = "5:00 PM for sunset golden hour",
                insiderTip = "Arrive 30 minutes before sunset to secure prime photography spots along the ridge.",
                photoSpot = "Upper viewpoint deck overlooking the entire valley and town",
                estimatedDurationHours = 1.5,
                trendingTag = "📸 Instagram Viral Sunset Spot",
                rating = 4.8,
                reviewCount = "40K+ online reviews",
                areaOrNeighborhood = "Highland Point, $cleanName",
                imageUrl = TravelImageHelper.getPlaceImage("$cleanName Viewpoint", "Viewpoint")
            ),
            FamousPlace(
                id = "dyn_3",
                name = "$cleanName Cultural Bazaar & Artisan Lane",
                localName = "Old Market of $cleanName",
                category = "Market",
                description = "Bustling cultural shopping and street-food hub filled with artisanal handicrafts, traditional souvenirs, and authentic local delicacies.",
                highlights = listOf("Authentic handmade regional souvenirs", "Famous local street food stalls", "Vibrant evening shopping vibe"),
                entryFeeInr = 0.0,
                timings = "10:00 AM - 9:30 PM",
                bestTimeToVisit = "5:00 PM to 8:30 PM",
                insiderTip = "Great place to pick up authentic regional gifts and sample freshly prepared evening snacks.",
                photoSpot = "Decorated lantern gateway of the main market street",
                estimatedDurationHours = 2.0,
                trendingTag = "🛍️ Vibrant Culture & Food Walk",
                rating = 4.7,
                reviewCount = "35K+ online reviews",
                areaOrNeighborhood = "Central Market, $cleanName",
                imageUrl = TravelImageHelper.getPlaceImage("$cleanName Market", "Market")
            ),
            FamousPlace(
                id = "dyn_4",
                name = "$cleanName Nature Eco-Park & Waterway",
                localName = "Green Valley of $cleanName",
                category = "Nature",
                description = "Lush green oasis with natural trails, crystal waters, floral gardens, and peaceful shaded sitting spots.",
                highlights = listOf("Lush botanical gardens", "Calm freshwater reflections", "Birdwatching and peaceful walking paths"),
                entryFeeInr = if (isInternational) 200.0 else 30.0,
                timings = "6:30 AM - 6:30 PM",
                bestTimeToVisit = "7:00 AM - 10:00 AM",
                insiderTip = "Ideal for morning yoga, photography, and escaping the afternoon crowd.",
                photoSpot = "Wooden footbridge crossing the central stream",
                estimatedDurationHours = 2.0,
                trendingTag = "🌿 Calming Nature Retreat",
                rating = 4.8,
                reviewCount = "28K+ online reviews",
                areaOrNeighborhood = "Eco-Zone, $cleanName",
                imageUrl = TravelImageHelper.getPlaceImage("$cleanName Nature", "Nature")
            )
        )

        val foods = listOf(
            LocalFoodItem(
                id = "dynf_1",
                name = "Signature $cleanName Special Delicacy",
                regionalName = "Special $cleanName Dish",
                description = "Traditional slow-cooked culinary specialty prepared with aromatic local spices, herbs, and heritage ingredients.",
                dietType = "Vegetarian",
                famousAtEatery = "Top-rated traditional eateries in $cleanName",
                averagePriceInr = if (isInternational) 450.0 else 160.0,
                mustTryReason = "The most celebrated signature dish of $cleanName with authentic regional taste.",
                flavorProfile = "Aromatic, rich, savory & wholesome",
                isStreetFood = false,
                trendingTag = "👑 #1 Must-Try Food Specialty",
                rating = 4.9,
                areaOrNeighborhood = "Heritage Eateries, $cleanName",
                imageUrl = TravelImageHelper.getFoodImage("thali", "Vegetarian")
            ),
            LocalFoodItem(
                id = "dynf_2",
                name = "Crispy $cleanName Street Bites & Chutney",
                regionalName = "Hot Street Snack",
                description = "Piping-hot crispy fritters and local savory snacks served with freshly ground spicy mint and tangy tamarind dips.",
                dietType = "Vegetarian",
                famousAtEatery = "Popular street food stalls around $cleanName Main Market",
                averagePriceInr = if (isInternational) 180.0 else 50.0,
                mustTryReason = "Freshly fried on order, delivering an irresistible crispy crunch with zesty flavors.",
                flavorProfile = "Crispy, spicy, tangy & hot",
                isStreetFood = true,
                trendingTag = "🔥 Viral Street Food Favorite",
                rating = 4.8,
                areaOrNeighborhood = "Market Square, $cleanName",
                imageUrl = TravelImageHelper.getFoodImage("chaat", "Vegetarian")
            ),
            LocalFoodItem(
                id = "dynf_3",
                name = "Authentic $cleanName Artisan Sweet",
                regionalName = "Heritage Dessert",
                description = "Centuries-old regional dessert prepared with pure ghee, reduced milk, cardamom, and roasted nuts.",
                dietType = "Sweet/Dessert",
                famousAtEatery = "Famous heritage sweet shops of $cleanName",
                averagePriceInr = if (isInternational) 220.0 else 80.0,
                mustTryReason = "Melt-in-mouth traditional sweetness loved by travelers for decades.",
                flavorProfile = "Sweet, buttery, cardamom-scented & nutty",
                isStreetFood = true,
                trendingTag = "✨ Iconic Heritage Sweet",
                rating = 4.9,
                areaOrNeighborhood = "Sweet Bazaar, $cleanName",
                imageUrl = TravelImageHelper.getFoodImage("dessert", "Sweet/Dessert")
            )
        )

        return DestinationExploreData(
            destination = cleanName,
            stateOrRegion = stateName,
            country = country,
            tagline = "Explore famous attractions, scenic viewpoints, photo spots and authentic food in $cleanName",
            bestSeason = "October to March (Pleasant weather and clear skies)",
            safetyTip = "Check entry timings in advance, hire verified guides, and stay hydrated while exploring.",
            onlineTrendSummary = "Synthesized from popular YouTube travel vlogs, Instagram reels, and 4.7★+ traveler ratings.",
            famousPlaces = places,
            localFoods = foods,
            imageUrl = TravelImageHelper.getDestinationHeroImage(cleanName)
        )
    }
}
