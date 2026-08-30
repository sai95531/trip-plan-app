package com.example.data.explore

import com.example.data.model.DestinationExploreData
import com.example.data.model.FamousPlace
import com.example.data.model.LocalFoodItem

object DestinationCatalog {

    val popularDestinations = listOf(
        "Vizag",
        "Guntur",
        "Andhra Pradesh",
        "Odisha",
        "Telangana",
        "Bangalore",
        "Goa",
        "Kashmir",
        "Ladakh",
        "Manali",
        "Jaipur",
        "Kerala",
        "Tamil Nadu",
        "Meghalaya",
        "Sikkim",
        "Uttarakhand",
        "Gujarat",
        "Hyderabad",
        "Mumbai",
        "Delhi",
        "Varanasi",
        "Udaipur",
        "Ooty",
        "Rishikesh",
        "Pondicherry",
        "Darjeeling",
        "Andaman",
        "Kolkata",
        "Chennai",
        "Agra",
        "Amritsar",
        "Shimla",
        "Switzerland",
        "Dubai",
        "Bali",
        "Tokyo",
        "Singapore",
        "Paris"
    )

    fun getDestination(destinationQuery: String): DestinationExploreData {
        val rawQuery = destinationQuery.trim()
        val query = rawQuery.lowercase()

        val baseData = when {
            // Specific Cities & Hotspots First
            query.contains("vizag") || query.contains("visakha") || query.contains("waltair") || query.contains("araku") || query.contains("kailasagiri") || query.contains("rushikonda") -> AndhraAndVizagDestinations.vizagData
            query.contains("guntur") || query.contains("kondaveedu") || query.contains("uppalapadu") || query.contains("amaravathi") || query.contains("amaravati") || query.contains("undavalli") || query.contains("kotappakonda") -> AndhraAndVizagDestinations.gunturData
            query.contains("agra") || query.contains("taj mahal") -> agraData
            query.contains("manali") || query.contains("solang") || query.contains("rohtang") || query.contains("atal tunnel") -> manaliData
            query.contains("jaipur") || query.contains("hawa mahal") || query.contains("amber fort") -> jaipurData
            query.contains("udaipur") || query.contains("lake pichola") -> udaipurData
            query.contains("varanasi") || query.contains("banaras") || query.contains("kashi") || query.contains("ganga aarti") -> varanasiData
            query.contains("rishikesh") || query.contains("haridwar") -> rishikeshData
            query.contains("ooty") || query.contains("nilgiri") || query.contains("doddabetta") -> ootyData
            query.contains("darjeeling") || query.contains("tiger hill") -> darjeelingData
            query.contains("amritsar") || query.contains("golden temple") -> amritsarData
            query.contains("goa") || query.contains("baga") || query.contains("palolem") || query.contains("aguada") || query.contains("dudhsagar") -> goaData
            query.contains("leh") || query.contains("ladakh") || query.contains("pangong") || query.contains("nubra") -> ladakhData
            query.contains("andaman") || query.contains("havelock") || query.contains("radhanagar") -> andamanData
            query.contains("mumbai") || query.contains("bombay") || query.contains("marine drive") || query.contains("gateway of india") -> mumbaiData
            query.contains("delhi") || query.contains("new delhi") || query.contains("red fort") || query.contains("qutub") || query.contains("india gate") -> delhiData
            query.contains("hyderabad") || query.contains("charminar") || query.contains("golconda") -> hyderabadData
            query.contains("bangalore") || query.contains("bengaluru") || query.contains("lalbagh") || query.contains("cubbon") || query.contains("nandi hills") -> bangaloreData
            query.contains("chennai") || query.contains("madras") || query.contains("marina beach") -> chennaiData
            query.contains("kolkata") || query.contains("calcutta") || query.contains("victoria memorial") || query.contains("howrah") -> kolkataData
            query.contains("pondicherry") || query.contains("puducherry") || query.contains("auroville") -> pondicherryData
            query.contains("shimla") -> shimlaData
            query.contains("paris") || query.contains("eiffel") -> parisData
            query.contains("dubai") || query.contains("burj") -> WorldDestinations.dubaiData
            query.contains("bali") || query.contains("ubud") -> WorldDestinations.baliData
            query.contains("tokyo") || query.contains("fuji") -> WorldDestinations.tokyoData
            query.contains("switzerland") || query.contains("swiss") || query.contains("interlaken") -> WorldDestinations.switzerlandData
            query.contains("singapore") -> singaporeData

            // Regional & State Catalogs
            query.contains("kashmir") || query.contains("srinagar") || query.contains("gulmarg") || query.contains("pahalgam") -> AllIndiaNorthAndWestDestinations.kashmirData
            query.contains("uttarakhand") || query.contains("kedarnath") || query.contains("badrinath") || query.contains("nainital") || query.contains("mussoorie") || query.contains("auli") -> AllIndiaNorthAndWestDestinations.uttarakhandData
            query.contains("odisha") || query.contains("orissa") || query.contains("puri") || query.contains("bhubaneswar") || query.contains("konark") || query.contains("chilika") -> OdishaAndEastDestinations.odishaData
            query.contains("gujarat") || query.contains("kutch") || query.contains("statue of unity") || query.contains("somnath") || query.contains("gir") || query.contains("dwarka") || query.contains("ahmedabad") -> AllIndiaNorthAndWestDestinations.gujaratData
            query.contains("tamil nadu") || query.contains("tamilnadu") || query.contains("madurai") || query.contains("rameswaram") || query.contains("kanyakumari") || query.contains("mahabalipuram") || query.contains("thanjavur") -> AllIndiaSouthAndEastDestinations.tamilNaduData
            query.contains("meghalaya") || query.contains("shillong") || query.contains("cherrapunji") || query.contains("dawki") -> AllIndiaSouthAndEastDestinations.meghalayaData
            query.contains("sikkim") || query.contains("gangtok") || query.contains("nathula") || query.contains("tsomgo") || query.contains("pelling") -> AllIndiaSouthAndEastDestinations.sikkimData
            query.contains("kerala") || query.contains("munnar") || query.contains("kochi") || query.contains("alleppey") || query.contains("wayanad") || query.contains("varkala") -> keralaData
            query.contains("telangana") || query.contains("warangal") || query.contains("ramappa") -> SouthIndiaDestinations.telanganaData
            query.contains("andhra") || query.contains("tirupati") || query.contains("vijayawada") || query.contains("gandikota") || query.contains("lepakshi") || query.contains("srisailam") || query.contains("rajahmundry") -> AndhraAndVizagDestinations.andhraPradeshData

            // Dynamic Generator with instant response for any other tourist spot or place worldwide
            else -> DynamicDestinationEngine.generate(rawQuery.ifBlank { "Scenic Explorer Destination" })
        }

        // Attach photos and ensure destination name reflects what user searched if searching a specific spot
        val formattedTitle = if (rawQuery.isNotBlank() && !baseData.destination.contains(rawQuery, ignoreCase = true) && !query.contains("state", ignoreCase = true)) {
            val titleCased = rawQuery.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            "$titleCased (${baseData.destination})"
        } else {
            baseData.destination
        }

        val enrichedPlaces = baseData.famousPlaces.map { place ->
            place.copy(
                imageUrl = place.imageUrl ?: com.example.data.util.TravelImageHelper.getPlaceImage(place.name, place.category)
            )
        }

        val enrichedFoods = baseData.localFoods.map { food ->
            food.copy(
                imageUrl = food.imageUrl ?: com.example.data.util.TravelImageHelper.getFoodImage(food.name, food.dietType)
            )
        }

        return baseData.copy(
            destination = formattedTitle,
            imageUrl = baseData.imageUrl ?: com.example.data.util.TravelImageHelper.getDestinationHeroImage(formattedTitle),
            famousPlaces = enrichedPlaces,
            localFoods = enrichedFoods
        )
    }

    private val goaData = DestinationExploreData(
        destination = "Goa",
        stateOrRegion = "Goa",
        country = "India",
        tagline = "Golden sun-kissed beaches, Portuguese colonial churches, susegad soul & seaside shacks",
        bestSeason = "November to February (Sunny with cool sea breezes)",
        safetyTip = "Rent a scooter (₹350-500/day) with a helmet, and avoid swimming during red flag ocean warnings.",
        onlineTrendSummary = "Synthesized from top YouTube Goa road trip vlogs, Instagram beach cafes, and coastal seafood guides.",
        famousPlaces = listOf(
            FamousPlace("g1", "Aguada Fort & Lighthouse", "फोर्ट अगुआडा", "Historical", "17th-century Portuguese fortress overlooking the vast Arabian Sea with a freshwater spring and lighthouse.", listOf("Ancient Portuguese lighthouse", "Arabian sea panoramas", "Aguada jail museum"), 50.0, "9:30 AM - 5:30 PM", "4:30 PM for sunset breeze", "Check out the Aguada Jail Museum next door for Goa's freedom history.", "Fort ramparts edge facing the ocean", 2.0, "🔥 Instagram Coastal Sunset Classic", 4.6, "70K+ reviews", "Candolim / Sinquerim"),
            FamousPlace("g2", "Dudhsagar Waterfalls", "दूधसागर जलप्रपात", "Nature", "Magnificent four-tiered 310m waterfall resembling an overflowing sea of white milk tumbling down the Western Ghats.", listOf("4x4 Jungle Jeep Safari", "Railway bridge over falls", "Natural jungle pool"), 850.0, "7:00 AM - 3:00 PM", "Morning (Monsoon or Post-monsoon)", "Book the mandatory forest jeep slot online at Kulem station.", "Jeep drop-off pool looking up at the train bridge", 4.5, "🌿 Viral Jungle Safari Hit on YouTube", 4.7, "65K+ reviews", "Mollem National Park"),
            FamousPlace("g3", "Basilica of Bom Jesus & Old Goa", "बोम जीसस की बेसिलिका", "Historical", "UNESCO World Heritage Baroque church holding the 450-year-old sacred relics of St. Francis Xavier.", listOf("Baroque gold-gilded altar", "Laterite stone facade", "Se Cathedral across the road"), 0.0, "9:00 AM - 6:30 PM", "Morning 9:30 AM", "Dress respectfully covering shoulders and knees.", "Basilica red laterite facade & central nave", 2.0, "🏛️ UNESCO World Heritage Landmark", 4.8, "80K+ reviews", "Old Goa"),
            FamousPlace("g4", "Palolem Beach & Butterfly Beach Kayak", "पालोलेम बीच", "Nature", "Crescent-shaped white sand beach in South Goa with calm turquoise waters, coconut palms, and dolphin spotting.", listOf("Kayaking to Butterfly Beach", "Silent Noise party", "Dolphin spotting boat rides"), 0.0, "All day", "Late afternoon to twilight", "Rent a kayak for ₹300/hr during low tide to paddle to secluded coves.", "South end rock bridge facing sunset", 3.5, "🔥 Top Peaceful Beach on Instagram", 4.8, "55K+ reviews", "Canacona / South Goa"),
            FamousPlace("g5", "Fontainhas Latin Quarter", "फॉन्टेनहास", "Cultural", "Picturesque Portuguese colonial neighborhood with pastel yellow, blue, and terracotta-roofed heritage villas.", listOf("Portuguese colonial architecture", "Art galleries & traditional bakeries", "Wishing well"), 0.0, "8:00 AM - 7:00 PM", "Early morning 8:00 AM or 4:00 PM", "Visit 31st January Road for serene, uncrowded street photography.", "Yellow heritage villa corner & blue doorway", 2.0, "🔥 Instagram's Most Aesthetic Street Walk", 4.7, "35K+ reviews", "Panjim"),
            FamousPlace("g6", "Chapora Fort (Dil Chahta Hai Fort)", "चपोरा किला", "Viewpoint", "Historic hilltop fort ramparts offering spectacular sunset vistas over Vagator Beach and the Ozran coastline.", listOf("Vagator beach coastline view", "Dil Chahta Hai iconic ramparts", "Sunset over Arabian Sea"), 0.0, "Sunrise to Sunset", "5:00 PM for Golden Hour", "Wear sturdy footwear for the 10-minute stone climb from the parking lot.", "Sea-facing ramparts overlooking Vagator", 2.0, "📹 Bollywood & Travel Vlog Legend", 4.6, "50K+ reviews", "Vagator"),
            FamousPlace("g7", "Anjuna Flea Market & Curlies Shack", "अंजुना", "Market", "Vibrant seaside hippie flea market with handmade jewelry, bohemian dresses, spices, and trance beach shacks.", listOf("Bohemian craft stalls", "Beachside acoustic music", "Sunset shack dining"), 0.0, "Wednesday 9:00 AM - Sunset", "4:00 PM to 7:00 PM", "Great for handicrafts, macrame, and spices.", "Beachside shack deck facing waves", 2.5, "✨ Cult Flea Market Experience", 4.5, "40K+ reviews", "Anjuna")
        ),
        localFoods = listOf(
            LocalFoodItem("gf1", "Goan Fish Curry Thali", "गोअन फिश थाली", "Steamed Goan red rice, Kingfish (Surmai) in coconut-kokum curry, fried prawn rava fry, kismur dry fish, and sol kadhi.", "Non-Veg", "Ritz Classic (Panaji) & Anand Seafood (Anjuna)", 280.0, "The beating heart of Goan coastal culinary heritage.", "Tangy, spicy, coconut-rich & crispy", true, "👑 The Ultimate Goan Classic", 4.9, "Panaji / Anjuna"),
            LocalFoodItem("gf2", "Goan Ros Omelette", "रोस ऑमलेट", "Fluffy egg omelette submerged in rich, fiery, aromatic xacuti chicken/vegetable gravy, served with fresh warm Goan poi bread.", "Non-Veg", "Ravi Ros Omelette Stall, Panaji Church Square", 80.0, "Goa's most iconic late-night street food ritual.", "Savory, spicy, rich gravy & hearty", true, "🔥 Viral Midnight Street Food on YouTube", 4.8, "Panaji Church Square"),
            LocalFoodItem("gf3", "Bebinca with Vanilla Ice Cream", "बेबिंका", "Traditional 7-to-16 layered Goan dessert made with rich coconut milk, egg yolks, jaggery, and nutmeg.", "Sweet/Dessert", "Confeitaria 31 De Janeiro, Fontainhas", 150.0, "Known as the Queen of Goan Desserts since Portuguese times.", "Caramelized, coconutty, soft & warm-cold paired", true, "🏛️ 100-Year Heritage Bakery Creation", 4.9, "Fontainhas, Panaji"),
            LocalFoodItem("gf4", "Prawn Balchão & Poi Bread", "प्रॉन बालचाओ", "Juicy ocean prawns cooked in a fiery, sweet-and-sour tangy pickle paste with palm vinegar and warm Goan poi.", "Non-Veg", "Fisherman's Wharf & Mum's Kitchen", 420.0, "Authentic Indo-Portuguese fusion delicacy.", "Fiery, tangy, vinegary & robust", false, "⭐ Authentic Goan Heritage", 4.8, "Panaji / Cavelossim"),
            LocalFoodItem("gf5", "Sol Kadhi & Kokum Refresher", "सोल कढ़ी", "Refreshing digestive drink made with pure coconut milk, fresh red kokum extract, garlic, and green chillies.", "Beverage", "Kokni Kanteen & Ritz Classic", 60.0, "Cooling natural digestive after hearty coastal seafood.", "Tart, tangy, creamy & cooling", true, "🌿 Authentic Coastal Digestive", 4.8, "Panaji")
        )
    )

    private val bangaloreData = DestinationExploreData(
        destination = "Bangalore (Bengaluru)",
        stateOrRegion = "Karnataka",
        country = "India",
        tagline = "Silicon Valley & Garden City: Lush botanical parks, royal palaces, craft breweries & legendary dosas",
        bestSeason = "Pleasant year-round (Best: October to March)",
        safetyTip = "Use Namma Metro or ride-hailing apps (Namma Yatri / Uber) for seamless travel across city traffic.",
        onlineTrendSummary = "Synthesized from 40+ trending YouTube Bangalore food & travel vlogs, Instagram reel hotspots, Google 4.6★+ reviews, and Reddit r/bangalore recommendations.",
        famousPlaces = listOf(
            FamousPlace("blr1", "Lalbagh Botanical Garden & Glass House", "ಲಾಲ್‌ಬಾಗ್", "Nature", "240-acre botanical haven with over 1,800 exotic plant species, a historic 3000-million-year-old rock, and Victorian-style Glass House.", listOf("Victorian Glass House", "Flower show venue", "Lalbagh Lake & Lotus pond", "Kempe Gowda Tower"), 30.0, "6:00 AM - 7:00 PM", "6:30 AM - 9:00 AM for birdwatching", "Visit during sunrise for peaceful walks and zero vehicle noise.", "Glass House front lawn & Lalbagh Rock", 2.5, "🌿 Trending Nature & Morning Reel Spot", 4.7, "45K+ reviews", "Mavalli / South Bangalore"),
            FamousPlace("blr2", "Bangalore Palace", "ಬೆಂಗಳೂರು ಅರಮನೆ", "Historical", "Magnificent 19th-century Tudor-style royal palace inspired by Windsor Castle, featuring fortified towers, stained glass, and royal hunting galleries.", listOf("Tudor-revival architecture", "Maharaja's courtyard", "Vintage oil paintings", "Royal ballroom"), 250.0, "10:00 AM - 5:30 PM", "10:30 AM - 1:00 PM", "Rent the multimedia audio guide to hear gripping stories of the Wadiyar dynasty.", "Palace main facade & Durbar Hall", 2.5, "👑 Royal Landmark Pick on YouTube", 4.5, "35K+ reviews", "Vasanth Nagar"),
            FamousPlace("blr3", "Cubbon Park & State Central Library", "ಕಬ್ಬನ್ ಪಾರ್ಕ್", "Nature", "300-acre green lung in the heart of Bengaluru with bamboo groves, colonial red-brick buildings, and Sunday open-air pet & musical gatherings.", listOf("Seshadri Iyer Memorial Library", "Bamboo grove trails", "Lotus pond", "Sunday dog park"), 0.0, "6:00 AM - 8:00 PM", "Morning 7:00 AM or late afternoon", "Traffic is banned inside on Sundays, making it paradise for cycling and acoustic music.", "Red Memorial Library archway & Bamboo glade", 2.0, "🔥 Instagram Aesthetic Greenery Hub", 4.7, "50K+ reviews", "Central Bangalore"),
            FamousPlace("blr4", "Vidhana Soudha", "ವಿಧಾನ ಸೌಧ", "Historical", "India's largest legislative building built in Neo-Dravidian architecture with imposing granite pillars and the four-headed Ashoka lion crest.", listOf("Neo-Dravidian granite facade", "Illuminated evening lights", "High Court (Attara Kacheri) opposite"), 0.0, "Outer view open 24/7 (Lit on Sundays & holidays 6:30-8:30 PM)", "Sunset to 7:30 PM", "View the stunning amber illumination from Cubbon Park gate on Sunday evenings.", "Front lawn opposite High Court", 1.0, "⭐ 4.8★ Iconic City Emblem", 4.8, "30K+ reviews", "Ambedkar Veedhi"),
            FamousPlace("blr5", "Nandi Hills (Sunrise Point)", "ನಂದಿ ಬೆಟ್ಟ", "Viewpoint", "Ancient fortress at 4,851 ft elevation offering world-famous sea-of-clouds sunrise vistas, Tipu's Drop cliff, and Bhoga Nandeeshwara temple base.", listOf("Sea of clouds sunrise", "Tipu's Drop", "Amrita Sarovar lake", "Bhoga Nandeeshwara ancient temple"), 50.0, "6:00 AM - 6:00 PM", "5:30 AM (Strictly before 6:30 AM for sunrise)", "Start driving from the city by 4:00 AM on weekends to beat the highway queue.", "Cliff edge viewpoint facing east", 4.0, "🔥 Viral YouTube & Instagram Sunrise Spot", 4.6, "90K+ reviews", "Chikkaballapur (45km north)")
        ),
        localFoods = listOf(
            LocalFoodItem("blrf1", "Benne Masala Dosa", "ಬೆಣ್ಣೆ ಮಸಾಲ ದೋಸೆ", "Crispy, golden-brown fermented rice crepe slathered with pure white freshly churned butter (benne), spiced potato filling, and fiery red coconut chutney.", "Vegetarian", "CTR (Shri Sagar) Malleshwaram & Vidyarthi Bhavan Gandhi Bazaar", 90.0, "Bengaluru's undisputed world-famous breakfast icon with 80+ years of legacy.", "Crisp exterior, cloud-soft interior, buttery & spicy", true, "👑 Top Viral Food Reel Legend (80-Yr Stall)", 4.9, "Malleshwaram / Gandhi Bazaar"),
            LocalFoodItem("blrf2", "Idli Vada with Filter Coffee", "ತಟ್ಟೆ ಇಡ್ಲಿ - ವಡೆ - ಫಿಲ್ಟರ್ ಕಾಫಿ", "Steamed cloud-soft button or Tatte idlis served with crispy crunchy medu vada, dunked in overflowing fresh coconut chutney and piping hot brass tumbler filter kaapi.", "Vegetarian", "Brahmin's Coffee Bar (Shankarapuram) & Veena Stores (Malleshwaram)", 80.0, "The golden standard of South Indian morning comfort breakfast.", "Silky, crispy, creamy coconutty & aromatic", true, "⭐ Cult Favorite on YouTube Food Vlogs", 4.9, "Shankarapuram / Malleshwaram"),
            LocalFoodItem("blrf3", "Bangalore Donne Biryani", "ದೊನ್ನೆ ಬಿರಿಯಾನಿ", "Seeraga Samba short-grain fragrant rice cooked with succulent chicken or mutton, infused with fresh mint, coriander, and green chillies, served in palm-leaf cups (Donne).", "Non-Veg", "Shivaji Military Hotel (Jayanagar) & Ranganna Military Hotel", 220.0, "Centuries-old military hotel heritage created for Maratha soldiers.", "Herbal, spicy, peppery & intensely flavorful", true, "🔥 Viral YouTube Non-Veg Food Pick", 4.7, "Jayanagar / Koramangala"),
            LocalFoodItem("blrf4", "Corner House 'Death by Chocolate' (DBC)", "ಡೆತ್ ಬೈ ಚಾಕೊಲೇಟ್", "Decadent warm gooey chocolate cake loaded with scoops of vanilla ice cream, drenched in molten hot fudge sauce, toasted roasted peanuts, and a cherry.", "Sweet/Dessert", "Corner House Ice Creams (Residency Road, Indiranagar, Jayanagar)", 260.0, "Bengaluru's legendary dessert obsession that every local and traveler swears by.", "Rich, gooey, chocolaty, crunchy & warm-cold contrast", true, "👑 Bangalore's #1 Iconic Dessert Tradition", 4.9, "Multiple Outlets across Bengaluru")
        )
    )

    private val manaliData = DestinationExploreData(
        destination = "Manali & Solang",
        stateOrRegion = "Himachal Pradesh",
        country = "India",
        tagline = "Snow-capped Himalayan peaks, pine forests, adventure valleys and bohemian cafes",
        bestSeason = "March to June (Pleasant) or Dec to Feb (Snowfall)",
        safetyTip = "Carry warm thermal layers even in summer and book Rohtang Pass permits in advance.",
        onlineTrendSummary = "Aggregated from trending YouTube Himachal road-trip vlogs, Instagram reels, and mountain trek reviews.",
        famousPlaces = listOf(
            FamousPlace("m1", "Rohtang Pass (13,058 ft)", "रोहतांग दर्रा", "Nature", "Iconic high mountain pass connecting Kullu to Lahaul & Spiti with panoramic glaciers and snow scooter thrills.", listOf("Glacier viewpoints", "Snow scooter & skiing", "Pir Panjal mountain range"), 500.0, "6:00 AM - 4:00 PM", "Early morning 6:00 AM", "Green NGT permit must be booked 1 day prior online.", "Snow view deck on top ridge", 5.0, "🔥 Viral Snow Adventure on YouTube", 4.7, "45K+ reviews", "45km from Manali"),
            FamousPlace("m2", "Solang Valley", "सोलंग घाटी", "Adventure", "Himalayan action capital offering tandem paragliding, ATV quad biking, zorbing, and ropeway cable cars.", listOf("Tandem paragliding over pine trees", "Anjani Mahadev snow trek", "Solang ropeway"), 350.0, "9:00 AM - 6:00 PM", "10:00 AM - 2:00 PM", "Book tandem paragliding only from certified pilots with Go-Pro recordings.", "Ropeway top station peak", 3.5, "📹 Paragliding Epic on Instagram Reels", 4.6, "50K+ reviews", "Solang"),
            FamousPlace("m3", "Hadimba Temple", "हडिम्बा देवी मंदिर", "Historical", "Ancient 1553 AD pagoda-style wooden temple built without nails, nestled within towering giant deodar cedar forests.", listOf("Carved wooden pagoda", "Sacred deodar cedar grove", "Angora rabbit petting"), 50.0, "8:00 AM - 6:00 PM", "Morning before 9:30 AM", "Walk into the serene cedar woods behind the temple for ethereal sunbeam photos.", "Deodar forest trail & wooden doorway", 1.5, "⭐ 4.6★ Sacred Forest Landmark", 4.6, "60K+ reviews", "Dhungri Village"),
            FamousPlace("m4", "Atal Tunnel & Sissu Lahaul Valley", "अटल टनल और सिस्सू", "Adventure", "World's longest highway tunnel above 10,000 ft opening into the dramatic stark mountains and frozen waterfalls of Lahaul.", listOf("Atal Tunnel portal", "Sissu cascading waterfall", "Lahaul willow river valley"), 0.0, "Open 24/7 (Weather permitting)", "Morning 8:00 AM", "The weather on Lahaul side is often dramatically sunny even when Manali has clouds.", "Sissu waterfall suspension bridge", 4.0, "🔥 Viral YouTube Road Trip Milestone", 4.9, "55K+ reviews", "Lahaul & Spiti")
        ),
        localFoods = listOf(
            LocalFoodItem("mf1", "Himachali Siddu with Pure Ghee", "सिड्डू", "Steamed wheat flour bun stuffed with poppy seeds, walnuts, and mountain spices, served drenched in golden desi ghee and mint chutney.", "Vegetarian", "Chopsticks, Old Manali Local Dhabas & Dawat Siddu Stall", 120.0, "Himachal's most revered traditional mountain comfort delicacy.", "Rich, nutty, warm & deeply comforting", true, "👑 Top Himachal Street Food Heritage", 4.9, "Old Manali"),
            LocalFoodItem("mf2", "Kullu Trout Fish Fry", "कुल्लू ट्राउट", "Fresh cold-water mountain river trout pan-fried in butter with local lemon-garlic herbs and served with potato wedges.", "Non-Veg", "Johnson's Cafe & Bar, Old Manali", 650.0, "Caught fresh from Beas and Tirthan mountain streams.", "Delicate, buttery, flaky & crispy", false, "⭐ High-End Foodie Favorite", 4.8, "Circuit House Road"),
            LocalFoodItem("mf3", "Tibetan Thukpa & Steamed Momos", "थुकपा और मोमोज़", "Piping hot noodle broth packed with mountain veggies and herbs, alongside juicy thin-skinned steamed dumplings and fiery chilli dip.", "Vegetarian", "Mount View Restaurant & Tibet Kitchen, Mall Road", 160.0, "The ultimate heartwarming bowl on a chilly Himalayan night.", "Savory, warming, peppery & aromatic", true, "🔥 Viral Comfort Food on YouTube", 4.8, "Mall Road")
        )
    )

    private val jaipurData = DestinationExploreData(
        destination = "Jaipur (The Pink City)",
        stateOrRegion = "Rajasthan",
        country = "India",
        tagline = "The Royal Pink City: Grand hilltop fortresses, royal palaces, stepwells and vibrant bazaars",
        bestSeason = "October to March",
        safetyTip = "Buy the Composite Monument Ticket at Amber Fort to save time and 40% cost across key sights.",
        onlineTrendSummary = "Synthesized from YouTube travel vlogs, Instagram royal palace reels, and Rajasthani culinary heritage guides.",
        famousPlaces = listOf(
            FamousPlace("j1", "Amber Fort & Palace", "आमेर का किला", "Historical", "Majestic 16th-century hilltop fortress featuring the breathtaking Sheesh Mahal (Palace of Mirrors) and Maota lake reflection.", listOf("Sheesh Mahal mirror mosaics", "Ganesh Pol gateway", "Maota Lake view", "Diwan-e-Aam"), 200.0, "8:00 AM - 5:30 PM, 6:30 PM - 9:00 PM", "Morning 8:30 AM", "Visit the world-famous Sheesh Mahal where a single candle illuminates the entire hall.", "Ganesh Pol doorway & Maota Lake terrace", 3.5, "👑 YouTube Royal Wonder Feature", 4.8, "120K+ reviews", "Amer"),
            FamousPlace("j2", "Hawa Mahal (Palace of Winds)", "हवा महल", "Historical", "Iconic 5-story pink sandstone honeycomb facade with 953 ornate jharokhas built for royal women to observe street parades unseen.", listOf("953 pink lattice windows", "Narrow spiraling ramps", "View of City Palace from top"), 50.0, "9:00 AM - 5:00 PM", "Early morning for golden sunlit facade", "Head to Tattoo Cafe or Wind View Cafe across the road for the famous front facade view.", "Opposite rooftop cafe balconies", 1.5, "🔥 #1 Most Photographed Landmark in Jaipur", 4.7, "95K+ reviews", "Badi Choupad"),
            FamousPlace("j3", "Nahargarh Fort & Padao Sunset", "नाहरगढ़ किला", "Viewpoint", "Hilltop bastion providing breathtaking 360-degree sunset panoramas over the entire pink city from the Aravalli hills.", listOf("Padao open-air cafe sunset", "Stepwell Baori (Rang De Basanti location)", "Madhavendra Bhawan royal suites"), 100.0, "10:00 AM - 10:00 PM", "5:00 PM for Golden Hour Sunset", "Watch the city lights turn on across the plains after twilight from the fort ramparts.", "Padao cafe edge & stepwell steps", 2.5, "🔥 Viral Sunset Reel Spot on Instagram", 4.7, "65K+ reviews", "Aravalli Hills")
        ),
        localFoods = listOf(
            LocalFoodItem("jf1", "Royal Dal Baati Churma", "दाल बाटी चूरमा", "Baked whole wheat dough balls dipped in pure desi ghee, served with spicy panchmel dal, gatte ki sabzi, and sweet jaggery churma.", "Vegetarian", "Laxmi Mishthan Bhandar (LMB) & Chokhi Dhani", 350.0, "Rajasthan's signature royal meal that represents centuries of desert culinary genius.", "Rich, ghee-laden, spicy & wholesome", true, "👑 The Ultimate Rajasthani Signature", 4.9, "Johari Bazaar"),
            LocalFoodItem("jf2", "Pyaaz Kachori & Mirchi Vada", "प्याज़ कचौड़ी", "Flaky golden deep-fried pastry packed with spiced caramelized onions, potatoes, and spices, served with sweet tamarind dip.", "Vegetarian", "Rawat Mishthan Bhandar, Station Road", 60.0, "Over 12,000 kachoris sold every single day at Rawat's iconic counter.", "Fiery, tangy, crispy & ultra-flaky", true, "🔥 Viral Street Food Legend on YouTube", 4.9, "Station Road")
        )
    )

    private val keralaData = DestinationExploreData(
        destination = "Kerala (God's Own Country)",
        stateOrRegion = "Kerala",
        country = "India",
        tagline = "Emerald backwaters, rolling tea gardens in Munnar, colonial Fort Kochi & ayurvedic serenity",
        bestSeason = "September to March",
        safetyTip = "Book government-approved houseboats at Alleppey finishing point to ensure fair rates and safe navigation.",
        onlineTrendSummary = "Synthesized from Kerala backwater vlog journeys, Munnar tea plantation reels, and spice coast culinary explorations.",
        famousPlaces = listOf(
            FamousPlace("k1", "Alleppey Backwaters & Houseboat Cruise", "ആലപ്പുഴ കായൽ", "Nature", "Tranquil network of palm-fringed canals, lagoons, and paddy fields navigated on traditional kettuvallam houseboats.", listOf("Kettuvallam luxury houseboat", "Paddy fields below sea level", "Vembanad Lake sunset"), 2500.0, "Day cruises 11:30 AM - 5:30 PM", "12:00 PM to Sunset", "Book an overnight houseboat with fresh karimeen fish fry prepared by onboard chef.", "Houseboat front sundeck overlooking palm shores", 5.0, "🔥 World-Famous Waterways Experience", 4.9, "85K+ reviews", "Alappuzha"),
            FamousPlace("k2", "Munnar Tea Gardens & Top Station", "മൂന്നാർ", "Nature", "Sprawling emerald tea estates carpeted over misty hills at 6,000 ft elevation with Neelakurinji flower blooms.", listOf("Tata Tea Museum", "Top Station cloud panorama", "Mattupetty Dam & Eco point"), 100.0, "9:00 AM - 6:00 PM", "Early morning 7:00 AM", "Visit Top Station on a clear morning to witness the sea of clouds rolling into Tamil Nadu plains.", "Kolukkumalai peak & tea carpet ridges", 4.0, "🌿 India's #1 Mountain Greenery Hub", 4.8, "95K+ reviews", "Idukki District"),
            FamousPlace("k3", "Fort Kochi & Chinese Fishing Nets", "ഫോർട്ട് കൊച്ചി", "Cultural", "Historic coastal port town with centuries-old cantilevered Chinese fishing nets, Jewish synagogue, and vibrant art cafes.", listOf("Cheena Vala (Chinese nets)", "Jew Town & Mattancherry Palace", "Kochi-Muziris Biennale art spaces"), 0.0, "All day", "5:00 PM for sunset net silhouettes", "Watch local fishermen operate the heavy wooden counterweights of the fishing nets.", "Promenade walkway at twilight", 2.5, "📸 Photographer's Iconic Coastal Frame", 4.7, "60K+ reviews", "Kochi")
        ),
        localFoods = listOf(
            LocalFoodItem("kf1", "Kerala Sadhya on Banana Leaf", "കേരള സദ്യ", "Grand feast of 24+ vegetarian delicacies served on a plantain leaf including Avial, Thoran, Olan, Sambar, and Payasam.", "Vegetarian", "Mothers Veg Plaza (Trivandrum) & Paragon Restaurant (Kochi)", 280.0, "Traditional royal harvest and festival banquet of Kerala.", "Coconut-rich, tangy, sour, spicy & sweet balance", true, "👑 Unmatched 24-Dish Culinary Heritage", 4.9, "Kochi / Trivandrum"),
            LocalFoodItem("kf2", "Karimeen Pollichathu", "കരിമീൻ പൊള്ളിച്ചത്", "Pearl spot fish marinated in fiery crushed shallots, curry leaves, and spices, slow-roasted wrapped inside a smoked banana leaf.", "Non-Veg", "Grand Pavilion & Backwater Dhabas in Kumarakom", 450.0, "Kerala's official state fish cooked in ancient backwater tradition.", "Smoky, spicy, tender & tangy with coconut vinegar", false, "⭐ Ultimate Backwater Seafood Dish", 4.9, "Alleppey / Kumarakom"),
            LocalFoodItem("kf3", "Malabar Parotta with Beef Roast / Chicken Curry", "മലബാർ പൊറോട്ട", "Flaky, multi-layered spiral flatbread served with slow-roasted spicy dark beef roast or rich Malabar coconut chicken curry.", "Non-Veg", "Paragon Restaurant (Calicut/Kochi) & Rahmath", 220.0, "The most celebrated everyday comfort pairing across God's Own Country.", "Flaky, buttery, peppery, aromatic & hearty", true, "🔥 Viral Malabar Food Tradition", 4.9, "Calicut / Kochi")
        )
    )

    private val mumbaiData = DestinationExploreData(
        destination = "Mumbai (The City of Dreams)",
        stateOrRegion = "Maharashtra",
        country = "India",
        tagline = "The City of Dreams: Marine Drive queen's necklace, Bollywood glitz, Gateway of India & street food legends",
        bestSeason = "November to February",
        safetyTip = "Use local trains during non-peak hours (11 AM to 4 PM) and always hail black-and-yellow (Kaali-Peeli) metered cabs.",
        onlineTrendSummary = "Synthesized from Mumbai local train journeys, Marine Drive sunset reels, and South Bombay heritage cafe walks.",
        famousPlaces = listOf(
            FamousPlace("mum1", "Gateway of India & Taj Mahal Palace Hotel", "गेटवे ऑफ इंडिया", "Historical", "Colonial basalt arch built in 1924 overlooking Mumbai harbor, facing the legendary 1903 Taj Mahal Palace Hotel.", listOf("Gateway basalt arch", "Arabian sea ferry harbour", "Taj heritage architecture"), 0.0, "Open 24/7", "Morning 7:30 AM or late evening", "Take a 1-hour scenic ferry ride from the jetty into the harbor.", "Gateway plaza facing Taj Hotel dome", 1.5, "👑 Iconic Emblem of Bombay", 4.7, "130K+ reviews", "Colaba"),
            FamousPlace("mum2", "Marine Drive & Queen's Necklace", "मरीन ड्राइव", "Viewpoint", "3.6-kilometer-long C-shaped boulevard along Netaji Subhash Chandra Bose Road offering unmatched sea views and glittering night curve.", listOf("Tetrapod coastal sea wall", "Art Deco heritage buildings", "Girgaon Chowpatty beach"), 0.0, "Open 24/7", "5:30 PM for sunset to 10:00 PM for night lights", "Sit on the promenade tetrapods with hot cutting chai and feel the Arabian Sea breeze.", "Marine Drive curve facing Malabar Hill at dusk", 2.0, "🔥 Instagram #1 Bombay Sunset Spot", 4.8, "160K+ reviews", "South Mumbai"),
            FamousPlace("mum3", "Chhatrapati Shivaji Maharaj Terminus (CSMT)", "सीएसएमटी", "Historical", "UNESCO World Heritage Victorian Gothic railway headquarters featuring gargoyles, stained glass, and grand central dome.", listOf("Victorian Italianate Gothic facade", "Heritage gallery museum", "Night illumination"), 0.0, "24/7 (Illuminated 7:00 PM - 11:00 PM)", "Evening 7:30 PM for illuminated colors", "View the full facade from the heritage viewing balcony opposite the road.", "Pedestrian viewing deck opposite CSMT", 1.0, "🏛️ UNESCO World Heritage Masterpiece", 4.7, "110K+ reviews", "Fort")
        ),
        localFoods = listOf(
            LocalFoodItem("mumf1", "Mumbai Vada Pav & Chutney", "वडा पाव", "Golden fried spiced potato fritter sandwiched in soft pav bread with spicy dry garlic chutney, green chilli, and sweet tamarind sauce.", "Vegetarian", "Ashok Vada Pav (Kirti College Dadar) & Aram Vada Pav (CSMT)", 30.0, "The undisputed lifeline snack of 20 million Mumbaikars.", "Crispy exterior, soft inside, fiery garlic spice", true, "👑 The True Soul of Mumbai Street Food", 4.9, "Dadar / CSMT"),
            LocalFoodItem("mumf2", "Pav Bhaji with Extra Amul Butter", "पाव भाजी", "Mashed spiced mixed vegetable curry cooked on a giant iron tawa with copious slabs of golden Amul butter, served with toasted buttered pav.", "Vegetarian", "Sardar Refreshments (Tardeo) & Cannon Pav Bhaji (CSMT)", 180.0, "Invented in Mumbai in the 1850s for midnight textile mill workers.", "Rich, buttery, tangy, spicy & deeply flavorful", true, "🔥 Legendary Bombay Midnight Comfort", 4.8, "Tardeo / Juhu Beach"),
            LocalFoodItem("mumf3", "Irani Chai & Bun Maska", "इराणी चहा आणि बन मस्का", "Thick, sweet, slow-brewed cardamom milk tea served with warm crusty bread slathered with pure butter, dunked directly into the tea cup.", "Vegetarian", "Kyani & Co. (Marine Lines) & Britannia & Co. (Ballard Estate)", 70.0, "100+ year old Parsi-Irani heritage bakery tradition.", "Sweet, creamy, buttery & comforting", true, "🏛️ 120-Year Heritage Irani Cafe Legacy", 4.8, "Marine Lines / Fort")
        )
    )

    private val delhiData = DestinationExploreData(
        destination = "Delhi (National Capital)",
        stateOrRegion = "Delhi NCR",
        country = "India",
        tagline = "Mughal grandeur, colonial avenues, bustling Chandni Chowk bazaars & unmatched street food",
        bestSeason = "October to March",
        safetyTip = "Use the world-class Delhi Metro network to easily bypass traffic congestion across Old and New Delhi.",
        onlineTrendSummary = "Synthesized from Old Delhi food walks, historical Mughal monument tours, and modern cafe vlogs.",
        famousPlaces = listOf(
            FamousPlace("del1", "Qutub Minar & Mehrauli Complex", "क़ुतुब मीनार", "Historical", "73-meter tall UNESCO World Heritage red sandstone minaret built in 1192 AD, surrounded by ancient 4th-century iron pillar.", listOf("73m Fluted minaret", "Rust-resistant Iron Pillar", "Ala'i Minar"), 50.0, "7:00 AM - 9:00 PM", "Morning 8:00 AM or illuminated evening", "Notice the 1600-year-old iron pillar that has never rusted.", "Central lawn looking up at the minaret", 2.0, "🏛️ UNESCO World Heritage Wonder", 4.7, "100K+ reviews", "Mehrauli"),
            FamousPlace("del2", "Red Fort (Lal Qila)", "लाल किला", "Historical", "Magnificent 17th-century Mughal fortified palace of red sandstone, seat of Mughal emperors and national flag hoisting.", listOf("Lahori Gate", "Diwan-i-Aam & Diwan-i-Khas", "Sound & Light evening show"), 50.0, "9:30 AM - 4:30 PM (Closed Mondays)", "10:00 AM", "Pair your visit with a walking food tour in Chandni Chowk across the road.", "Lahori Gate red sandstone ramparts", 2.5, "👑 Imperial Mughal Capital Landmark", 4.6, "115K+ reviews", "Old Delhi"),
            FamousPlace("del3", "India Gate & Kartavya Path", "इण्डिया गेट", "Historical", "42-meter high triumphal arch war memorial surrounded by sprawling lawns, fountains, and the National War Memorial.", listOf("Amar Jawan Jyoti", "National War Memorial", "Kartavya Path promenade"), 0.0, "Open 24/7", "7:00 PM for illuminated fountain walk", "Enjoy evening ice cream on the lawns with the lit arch in the background.", "Central vista promenade looking towards arch", 1.5, "⭐ Iconic National Monument", 4.7, "140K+ reviews", "Central Delhi")
        ),
        localFoods = listOf(
            LocalFoodItem("delf1", "Delhi Chole Bhature with Pickled Green Chilli", "छोले भटूरे", "Piping hot puffed golden deep-fried bread paired with dark, tangy, slow-simmered chickpea curry, paneer, and pickled onions.", "Vegetarian", "Sita Ram Diwan Chand (Paharganj) & Chache Di Hatti (Kamla Nagar)", 110.0, "Delhi's undisputed king of weekend breakfast rituals.", "Crispy, fluffy, tangy, spicy & rich", true, "👑 #1 Legendary Delhi Breakfast", 4.9, "Paharganj / Kamla Nagar"),
            LocalFoodItem("delf2", "Old Delhi Butter Chicken & Mughlai Naan", "बटर चिकन", "Tandoori grilled chicken pieces simmered in a velvety, rich tomato, cashew, and butter gravy with fenugreek leaves.", "Non-Veg", "Moti Mahal (Daryaganj) & Karim's (Jama Masjid)", 420.0, "The birthplace of butter chicken (Murgh Makhani) in 1947.", "Creamy, mild, smoky, rich & buttery", false, "⭐ World-Famous Delhi Invention", 4.8, "Daryaganj / Jama Masjid"),
            LocalFoodItem("delf3", "Chandni Chowk Dahi Bhalla & Chaat", "दही भल्ला चाट", "Soft lentil dumplings soaked in thick sweetened yogurt, topped with roasted cumin, tangy tamarind saunth, and mint chutney.", "Vegetarian", "Natraj Dahi Bhalle Corner (Chandni Chowk since 1940)", 80.0, "80+ years of serving Delhi's crispiest and creamiest street chaat.", "Cooling, sweet, tangy, soft & spiced", true, "🔥 Viral YouTube Old Delhi Chaat Hit", 4.8, "Chandni Chowk")
        )
    )

    private val hyderabadData = DestinationExploreData(
        destination = "Hyderabad (City of Pearls)",
        stateOrRegion = "Telangana",
        country = "India",
        tagline = "City of Pearls: Majestic Charminar, Golconda fort acoustics, royal palaces & world-famous Dum Biryani",
        bestSeason = "October to March",
        safetyTip = "Bargain respectfully for pearl necklaces at Laad Bazaar and sample fresh Irani chai with Osmania biscuits.",
        onlineTrendSummary = "Synthesized from Nizami food trails, Charminar night reels, and heritage fort explorations.",
        famousPlaces = listOf(
            FamousPlace("hyd1", "Charminar & Laad Bazaar", "చార్మినార్", "Historical", "1591 AD four-minareted grand monument in the heart of old Hyderabad, surrounded by sparkling lac bangle and pearl markets.", listOf("Four 56m fluted minarets", "Upper mosque view", "Laad Bazaar pearl & bangle shops"), 25.0, "9:30 AM - 5:30 PM", "5:00 PM for golden sunset illumination", "Climb the minarets for a 360-degree look at the historic Old City.", "Nimrah Cafe balcony with Charminar frame", 2.0, "👑 Legendary Monument of the Nizams", 4.7, "110K+ reviews", "Old City"),
            FamousPlace("hyd2", "Golconda Fort & Sound Light Show", "గోల్కొండ కోట", "Historical", "Massive 16th-century fortress known for magical acoustic engineering (a clap at the entrance is heard at the hilltop pavilion 1km away).", listOf("Acoustic clapping portico", "Fateh Rahben cannon", "Bala Hissar royal palace"), 25.0, "9:00 AM - 5:30 PM", "3:30 PM to stay for the 6:30 PM light show", "Test the clapping acoustic effect at the grand entrance Fateh Darwaza.", "Bala Hissar top pavilion overlooking Hyderabad skyline", 3.5, "🏛️ Engineering Wonder on YouTube", 4.6, "95K+ reviews", "Golconda")
        ),
        localFoods = listOf(
            LocalFoodItem("hydf1", "Hyderabadi Dum Biryani with Mirchi ka Salan", "హైదరాబాదీ దమ్ బిర్యానీ", "Aged long-grain basmati rice and tender spiced meat cooked together on slow coal fire (Dum) in sealed handi, served with tangy chilli gravy.", "Non-Veg", "Bawarchi (RTC X Roads), Paradise & Shadab (Old City)", 290.0, "The world standard of royal biryani created in Nizami royal kitchens.", "Fragrant, saffron-infused, deeply spiced & succulent", true, "👑 World-Famous Hyderabadi Legend", 4.9, "RTC X Roads / Charminar"),
            LocalFoodItem("hydf2", "Irani Chai & Osmania Biscuits", "ఇరానీ చాయ్", "Creamy, dense milk tea slow-brewed on samovar with evaporated milk, paired with sweet-salty melt-in-mouth Osmania biscuits.", "Beverage", "Nimrah Cafe and Bakery (Directly facing Charminar)", 40.0, "Nizami evening ritual with unmatched view of illuminated Charminar.", "Rich, velvety, cardamom-sweet & buttery salty biscuits", true, "🔥 #1 Iconic Evening Street Drink", 4.9, "Charminar")
        )
    )

    private val varanasiData = DestinationExploreData(
        destination = "Varanasi (Kashi / Banaras)",
        stateOrRegion = "Uttar Pradesh",
        country = "India",
        tagline = "One of the oldest continuously inhabited cities on Earth: Sacred Ganga Ghats, evening Maha Aarti & narrow heritage lanes",
        bestSeason = "October to March",
        safetyTip = "Take an early dawn rowboat on the Ganges (6:00 AM) and keep valuables secure in crowded alleyways.",
        onlineTrendSummary = "Synthesized from sacred Ganga Aarti ceremonies, dawn boat journeys, and legendary Banarasi food trails.",
        famousPlaces = listOf(
            FamousPlace("v1", "Dashashwamedh Ghat & Evening Ganga Aarti", "दशाश्वमेध घाट", "Spiritual", "The most spectacular riverfront ghat where priests perform the synchronized choreographed Maha Aarti with massive brass fire lamps.", listOf("Synchronized fire lamp aarti", "Flowing diya offerings on Ganges", "Ghat boat views"), 0.0, "Aarti starts at 6:30 PM daily", "5:30 PM to secure prime front boat seating", "Book a wooden rowboat to watch the mesmerizing aarti reflection from the river waters.", "Boat looking towards priests on ghat steps", 2.0, "🔥 World-Famous Spiritual Experience", 4.9, "130K+ reviews", "Dashashwamedh"),
            FamousPlace("v2", "Kashi Vishwanath Temple & Corridor", "काशी विश्वनाथ मंदिर", "Spiritual", "Ancient golden-spire Jyotirlinga temple dedicated to Lord Shiva, connected to the holy river via a grand marble corridor.", listOf("Gold-plated spire", "Ganga-Vishwanath Corridor", "Spiritual chanting halls"), 0.0, "3:00 AM - 11:00 PM", "Early morning 5:00 AM Mangala Aarti", "Locker facility is available at the entrance; phones are not permitted inside inner sanctum.", "Corridor riverfront viewpoint", 2.5, "⭐ Sacred Heart of Kashi", 4.8, "150K+ reviews", "Lahori Tola"),
            FamousPlace("v3", "Assi Ghat Morning Subah-e-Banaras", "अस्सी घाट", "Cultural", "Southernmost sacred ghat known for sunrise Vedic chants, classical music performances, morning yoga, and lemon tea.", listOf("Subah-e-Banaras classical music", "Sunrise yoga sessions", "Pappu Chai stall conversations"), 0.0, "Open 24/7", "5:30 AM for sunrise music and chants", "Start your morning here with a cup of lemon masala chai and sit on the river steps.", "Assi Ghat river steps at sunrise", 2.0, "🌿 Peaceful Dawn Serenity", 4.8, "65K+ reviews", "Assi")
        ),
        localFoods = listOf(
            LocalFoodItem("vf1", "Banarasi Kachori Sabzi & Jalebi", "कचौड़ी सब्ज़ी और जलेबी", "Crispy deep-fried lentil kachoris served with spicy potato-heeng gravy, accompanied by hot, syrupy saffron jalebis.", "Vegetarian", "Ram Bhandar (Thatheri Bazaar) & Chachi Ki Dukan (Lanka)", 60.0, "The timeless morning breakfast of Banaras for over a century.", "Crispy, heeng-spiced, tangy & sweet contrast", true, "👑 The Signature Banarasi Breakfast", 4.9, "Thatheri Bazaar / Lanka"),
            LocalFoodItem("vf2", "Blue Lassi & Malaiyo (Winter Sweet)", "मलाईयो और बनारसी लस्सी", "Thick churned creamy yogurt lassi topped with fresh fruits, pomegranate, and seasonal winter cloud-froth Malaiyo infused with saffron.", "Sweet/Dessert", "Blue Lassi Shop (Manikarnika Gali) & Shreeji Sweets (Godowlia)", 80.0, "Whipped dew-chilled milk foam delicacy with pistachios.", "Cloud-soft, aromatic saffron, rich & melt-in-mouth", true, "🔥 Viral Instagram Food Hit", 4.9, "Godowlia / Manikarnika")
        )
    )

    private val udaipurData = DestinationExploreData(
        destination = "Udaipur (The City of Lakes)",
        stateOrRegion = "Rajasthan",
        country = "India",
        tagline = "Venice of the East: Romantic marble palaces, Lake Pichola boat cruises & regal Aravalli sunsets",
        bestSeason = "September to March",
        safetyTip = "Take the sunset boat cruise from City Palace jetty to Jagmandir Island for the most breathtaking palace reflections.",
        onlineTrendSummary = "Synthesized from Lake Pichola luxury heritage reels, royal palace walks, and Rajasthani sunset guides.",
        famousPlaces = listOf(
            FamousPlace("u1", "City Palace & Lake Pichola Complex", "उदयपुर सिटी पैलेस", "Historical", "Rajasthan's largest royal palace complex standing on Lake Pichola's eastern bank with ornate peacock courtyards and mirror mosaics.", listOf("Mor Chowk (Peacock courtyard)", "Zenana Mahal", "Lake Pichola boat jetty", "Vintage car museum"), 300.0, "9:00 AM - 5:30 PM", "9:30 AM for uncrowded photography", "Take the combined boat ticket to visit Jagmandir Island palace.", "Tripolia Gate & Lake Pichola balcony", 3.5, "👑 Grandest Palace of Rajasthan", 4.8, "95K+ reviews", "Old City"),
            FamousPlace("u2", "Jagmandir Island Palace & Lake Cruise", "जग मंदिर", "Nature", "17th-century island marble palace built in the middle of Lake Pichola with life-sized marble elephants and courtyard cafes.", listOf("Carved marble elephants", "360-degree lake panorama", "Gul Mahal domed pavilion"), 450.0, "10:00 AM - 6:00 PM", "4:30 PM for sunset golden reflection", "Enjoy evening coffee at the island cafe as the setting sun paints City Palace golden.", "Marble elephant pier facing City Palace", 2.0, "🔥 Instagram #1 Romantic Island Frame", 4.7, "50K+ reviews", "Lake Pichola"),
            FamousPlace("u3", "Sajjangarh Monsoon Palace Sunset", "सज्जनगढ़ मानसून पैलेस", "Viewpoint", "Hilltop fortress perched 3,100 ft high in the Aravalli hills built by Maharana Sajjan Singh to track monsoon clouds.", listOf("360-degree Aravalli mountain views", "Sensational sunset panorama", "Biological park safari"), 110.0, "9:00 AM - 6:00 PM", "5:00 PM strictly for sunset", "Watch the entire lake city turn into a glowing miniature model at dusk.", "Top terrace railing overlooking Udaipur lakes", 2.0, "🔥 Viral Sunset Viewpoint on YouTube", 4.6, "45K+ reviews", "Sajjangarh")
        ),
        localFoods = listOf(
            LocalFoodItem("uf1", "Mewari Dal Baati & Gatte ki Sabzi", "मेवाड़ी दाल बाटी", "Stone-baked wheat baatis drenched in pure desi ghee, served with spicy mixed Mewari lentils, garlic chutney, and gram flour gatta curry.", "Vegetarian", "Krishna Dal Bati Restro & Traditional Heritage Thali (Old City)", 320.0, "Authentic Mewar royal feast that sustained desert warrior kings.", "Rich, ghee-loaded, spicy, wholesome & satisfying", true, "👑 Royal Mewar Culinary Icon", 4.9, "Old City / Bhatt Ji Ki Bari"),
            LocalFoodItem("uf2", "Fateh Sagar Cold Coffee & Kulhad Chai", "कुल्हड़ चाय और कोल्ड कॉफ़ी", "Frothy creamy blended cold coffee and saffron kulhad chai enjoyed along the lakeside promenade overlooking the ripples.", "Beverage", "Bombay Market Food Stalls & Vinod Fast Food, Fateh Sagar Paal", 70.0, "Udaipur's favorite sunset evening hangout with lively lake breeze.", "Frothy, chocolaty, chilled & refreshing", true, "⭐ Local Favorite Evening Hangout", 4.8, "Fateh Sagar Paal")
        )
    )

    private val ootyData = DestinationExploreData(
        destination = "Ooty & Nilgiris",
        stateOrRegion = "Tamil Nadu",
        country = "India",
        tagline = "Queen of Hill Stations: UNESCO toy train, emerald tea valleys, botanical gardens & eucalyptus mist",
        bestSeason = "March to June (Pleasant summer) or Oct to Feb (Misty chill)",
        safetyTip = "Book Nilgiri Mountain Railway (Toy Train) tickets on IRCTC 30 days in advance as seats fill up instantly.",
        onlineTrendSummary = "Synthesized from Nilgiri toy train journeys, tea estate pine walks, and homemade chocolate trails.",
        famousPlaces = listOf(
            FamousPlace("o1", "Nilgiri Mountain Railway (UNESCO Toy Train)", "நீலகிரி மலை ரயில்", "Adventure", "Historic 1908 steam-hauled rack-and-pinion railway passing through 208 curves, 16 tunnels, and 250 bridges amidst pine forests.", listOf("Vintage Swiss-engineered steam engine", "Scenic valley viaducts", "Coonoor tea slopes"), 200.0, "Departs Ooty & Mettupalayam daily", "Morning 9:15 AM ride Ooty to Coonoor", "Sit on the right side when traveling from Mettupalayam to Ooty for the best valley vistas.", "Window frame overlooking emerald tea slopes", 3.0, "🏛️ UNESCO World Heritage Train Ride", 4.9, "60K+ reviews", "Ooty Railway Station"),
            FamousPlace("o2", "Ooty Botanical Gardens & Rose Garden", "தாவரவியல் பூங்கா", "Nature", "55-acre terraced garden established in 1848 with 1,000+ exotic flora species, Italian garden, and a 20-million-year-old fossil tree.", listOf("Terraced Italian garden", "20-million-year-old fossil tree", "Orchidarium"), 40.0, "7:00 AM - 6:30 PM", "8:30 AM for fresh morning dew", "Take a peaceful walk through the fern house and upper terraced lawns.", "Central green lawn and floral clock", 2.5, "🌿 Century-Old Victorian Flora Wonder", 4.6, "75K+ reviews", "Vannarapettai"),
            FamousPlace("o3", "Doddabetta Peak (8,652 ft)", "தொட்டபெட்டா", "Viewpoint", "The highest summit in the Nilgiri Mountains with a telescope house offering panoramic views of Bandipur forests and Coimbatore plains.", listOf("Highest peak in Nilgiris", "Telescope viewing dome", "Surrounding shola forests"), 20.0, "9:00 AM - 6:00 PM", "Early morning 9:00 AM before cloud cover", "Carry a light jacket as winds at the summit are chilly year-round.", "Telescope observatory view deck", 2.0, "⭐ Highest Peak in Southern India", 4.5, "55K+ reviews", "Doddabetta")
        ),
        localFoods = listOf(
            LocalFoodItem("of1", "Homemade Nilgiri Fudge & Truffles", "நீலகிரி சாக்லேட்", "Artisanal handcrafted dark chocolate, rum-and-raisin fudge, and roasted almond bars made with fresh mountain cocoa and milk.", "Sweet/Dessert", "King Star Handmade Chocolates (Commercial Rd) & Moddy's Confectionery", 180.0, "Ooty's famous 70-year confectionary tradition since colonial times.", "Rich, velvety, dark cocoa & nutty crunch", true, "👑 Famous Ooty Heritage Confection", 4.9, "Commercial Road / Charing Cross"),
            LocalFoodItem("of2", "Nilgiri Green Spiced Chicken / Veg Curry", "நீலகிரி குருமா", "Succulent chicken or vegetables cooked in a fragrant emerald gravy of fresh mint, coriander, coconut, and mountain green peppercorns.", "Non-Veg", "Earl's Secret & Quality Restaurant", 260.0, "Fragrant regional curry unique to Nilgiri high altitude spices.", "Herbal, peppery, aromatic, rich & comforting", false, "⭐ Authentic Hill Station Specialty", 4.7, "Havelock Road")
        )
    )

    private val rishikeshData = DestinationExploreData(
        destination = "Rishikesh & Haridwar",
        stateOrRegion = "Uttarakhand",
        country = "India",
        tagline = "Yoga Capital of the World: White-water rafting on the Ganga, Beatles Ashram & divine Triveni Ghat Aarti",
        bestSeason = "September to November & March to May",
        safetyTip = "Only book rafting with certified licensed operators providing Grade-III+ helmets and life jackets.",
        onlineTrendSummary = "Synthesized from Ganga rafting adventures, cliff jump reels, and sacred evening river aartis.",
        famousPlaces = listOf(
            FamousPlace("r1", "White Water River Rafting (Kaudiyala to Marine Drive)", "गंगा रिवर राफ्टिंग", "Adventure", "Thrilling Grade III and IV rapids (Roller Coaster, Golf Course, Club House) on the crystalline turquoise Ganges.", listOf("Roller Coaster & Golf Course rapids", "Cliff jumping point", "Bodysurfing in clear river"), 1000.0, "7:00 AM - 3:00 PM (Sept to June)", "Morning 8:30 AM for clear sunshine", "Choose the 16km Marine Drive to Shivpuri or 24km Kaudiyala stretch for maximum adrenaline.", "Raft tackling big wave in Roller Coaster", 4.0, "🔥 India's #1 Adventure Water Sport", 4.9, "80K+ reviews", "Shivpuri / Tapovan"),
            FamousPlace("r2", "Triveni Ghat Evening Maha Aarti", "त्रिवेणी घाट", "Spiritual", "Sacred confluence of Ganga, Yamuna, and Saraswati where priests chant ancient Vedic hymns with flaming golden lamps.", listOf("Vedic chanting & conch blowing", "Floating marigold leaf lamps", "Ganga cleansing dip"), 0.0, "Aarti starts at 6:00 PM daily", "5:15 PM to sit on ghat steps", "Release a biodegradable leaf boat with flowers and small oil lamp into the current.", "Ghat steps overlooking lighted lamps", 2.0, "✨ Divine Spiritual Energy Hub", 4.8, "60K+ reviews", "Mayakund"),
            FamousPlace("r3", "Beatles Ashram (Chaurasi Kutia)", "बीटल्स आश्रम", "Cultural", "Abandoned 1968 Maharishi Mahesh Yogi ashram where The Beatles stayed and composed the White Album, full of vibrant graffiti murals.", listOf("Beatles Cathedral graffiti hall", "Meditation stone igloos", "Rajaji Tiger Reserve surroundings"), 150.0, "9:00 AM - 4:30 PM", "10:00 AM - 1:00 PM", "Walk inside the stone meditation igloos (kutiyas) and photograph the Beatles pop-art murals.", "Graffiti-painted Beatles Cathedral hall", 2.5, "🎨 Cult Pop-Culture Legend", 4.6, "35K+ reviews", "Swarg Ashram")
        ),
        localFoods = listOf(
            LocalFoodItem("rf1", "Rishikesh Ayurvedic Satvik Thali & Lemon Ginger Tea", "सात्विक थाली", "Freshly prepared vegetarian meal cooked with mountain herbs, rock salt, and cow ghee, without onion and garlic.", "Vegetarian", "Chotiwala Restaurant (Swarg Ashram) & Tat Cafe", 220.0, "Pure Ayurvedic wholesome food aligned with yogic mindfulness.", "Gentle, wholesome, herbaceous, nourishing & aromatic", true, "👑 Authentic Yogic Cuisine", 4.8, "Swarg Ashram / Tapovan"),
            LocalFoodItem("rf2", "Woodfired Shakshuka & Vegan Smoothie Bowls", "स्मूदी बाउल", "Rich poached eggs or tofu in spiced tomato-bell pepper skillet served with warm sourdough bread and dragonfruit bowls.", "Vegetarian", "Little Buddha Cafe & The 60's Cafe Delmar", 280.0, "Famous riverside bohemian traveler cafe culture overlooking turquoise Ganga.", "Fresh, tangy, zesty, nutty & nourishing", true, "☕ Top Traveler Cafe Hit on Instagram", 4.8, "Laxman Jhula / Tapovan")
        )
    )

    private val pondicherryData = DestinationExploreData(
        destination = "Pondicherry (Puducherry)",
        stateOrRegion = "Puducherry",
        country = "India",
        tagline = "The French Riviera of the East: Colonial French quarters, seaside Promenade, croissants & Auroville peace",
        bestSeason = "October to March",
        safetyTip = "Rent a vintage bicycle to explore the colorful bougainvillea lanes of White Town peacefully.",
        onlineTrendSummary = "Synthesized from French colony walks, Auroville meditation dome visits, and seaside cafe vlogs.",
        famousPlaces = listOf(
            FamousPlace("pon1", "White Town (French Quarter) & Promenade Beach", "வைட் டவுன்", "Cultural", "Grid-planned colonial neighborhood with mustard-yellow French villas, arched gates, bougainvillea, and seaside promenade.", listOf("Mustard-yellow French villas", "Seaside Goubert Avenue promenade", "French War Memorial"), 0.0, "Open 24/7 (Promenade closed to vehicles 6 PM - 7:30 AM)", "Early morning 6:30 AM for empty lanes", "Vehicle traffic is banned on the beach road every evening, making it ideal for walking.", "Rue Romain Rolland pastel villa corners", 3.0, "🔥 Instagram Aesthetic Boulevard Hub", 4.8, "70K+ reviews", "White Town"),
            FamousPlace("pon2", "Matrimandir & Auroville Universal Township", "மாத்ரிமந்திர்", "Spiritual", "Universal township featuring a massive golden geodesic sphere dedicated to peace, human unity, and silent concentration.", listOf("Golden geodesic dome", "Banyan tree amphitheatre", "Auroville organic cafes & crafts"), 0.0, "9:00 AM - 4:00 PM (Pass required for inner chamber)", "Morning 9:00 AM for view point pass", "Inner meditation chamber pass must be booked 2-3 days prior at the visitor centre.", "Matrimandir golden dome from viewing point", 3.5, "✨ Global Architectural Landmark of Peace", 4.7, "55K+ reviews", "Auroville")
        ),
        localFoods = listOf(
            LocalFoodItem("ponf1", "French Butter Croissant & Cafe au Lait", "பிரெஞ்சு க்ரோசண்ட்", "Flaky, multi-layered golden butter croissant baked fresh daily, served with dark French roast coffee with steamed milk.", "Sweet/Dessert", "Baker Street (Bussy Street) & Coromandel Cafe", 140.0, "Authentic French patisserie recipe perfected across generations.", "Flaky exterior, airy buttery layers, sweet & rich", true, "👑 Iconic French Quarter Morning Ritual", 4.9, "Bussy Street / Romain Rolland"),
            LocalFoodItem("ponf2", "Creole Prawn / Fish Curry with Baguette", "கிரியோல் மீன் கறி", "Unique Indo-French coastal fusion curry cooked with mild spices, coconut milk, fresh bay leaves, and French bread.", "Non-Veg", "Carte Blanche (Hotel de l'Orient) & Villa Shanti", 380.0, "Rare culinary bridge between French elegance and Tamil coastal spice.", "Velvety, aromatic, mild & coconut-infused", false, "⭐ Rare Franco-Tamil Culinary Discovery", 4.8, "White Town")
        )
    )

    private val darjeelingData = DestinationExploreData(
        destination = "Darjeeling & Tiger Hill",
        stateOrRegion = "West Bengal",
        country = "India",
        tagline = "Queen of the Hills: Kanchenjunga sunrise, world-famous First Flush champagne tea & heritage toy train",
        bestSeason = "March to May (Clear skies) or October to December (Snow peaks)",
        safetyTip = "Start from hotel by 4:00 AM for Tiger Hill to secure a prime view of the sunrise hitting Mt. Kanchenjunga.",
        onlineTrendSummary = "Synthesized from Kanchenjunga sunrise vlogs, heritage tea garden walks, and Himalayan cafe trails.",
        famousPlaces = listOf(
            FamousPlace("dar1", "Tiger Hill (8,482 ft) Kanchenjunga Sunrise", "টাইগার হিল", "Viewpoint", "World-renowned vantage point where the rising sun illuminates the twin peaks of Mt. Kanchenjunga in fiery gold and pink hues.", listOf("Mt. Kanchenjunga golden glow", "Glimpse of Mt. Everest horizon", "Senchal wildlife sanctuary"), 50.0, "4:00 AM - 7:00 AM", "4:30 AM (Check sunrise time daily)", "Watch the color shift from steel grey to burnt orange on the snow peak summit.", "Observatory tower upper deck", 2.5, "🔥 #1 Mountain Sunrise Wonder in India", 4.8, "65K+ reviews", "11km from Darjeeling"),
            FamousPlace("dar2", "Darjeeling Himalayan Railway (Joy Ride)", "টয় ট্রেন", "Historical", "Century-old 2-foot narrow gauge heritage steam train climbing around the iconic Batasia Loop engineering marvel.", listOf("Batasia Loop panoramic turn", "Ghoom highest railway station", "Vintage steam locomotive whistle"), 1000.0, "Steam train joyrides daily from Darjeeling", "Morning 9:00 AM joyride", "The train circles the Batasia war memorial giving a 360-degree look at the snow peaks.", "Batasia Loop spiral track overlooking Kanchenjunga", 2.0, "🏛️ UNESCO World Heritage Joy Ride", 4.7, "55K+ reviews", "Darjeeling / Ghoom")
        ),
        localFoods = listOf(
            LocalFoodItem("darf1", "Darjeeling First Flush Muscatel Tea & Scones", "দার্জিলিং চা", "World-renowned single-estate orthodox black tea known as the Champagne of Teas, brewed delicate golden with floral notes.", "Beverage", "Nathmulls Tea Lounge & Glenary's Bakery (since 1910)", 160.0, "Plucked from 150-year-old high elevation Himalayan tea bushes.", "Floral, fruity, delicate muscatel aroma & light body", true, "👑 World's Most Celebrated Champagne Tea", 4.9, "Mall Road / Nehru Road"),
            LocalFoodItem("darf2", "Steamed Tibetan Pork / Chicken Momos with Dalle Chilli", "মোমো এবং ডাল্লে চাটনি", "Thin-skinned juicy dumplings stuffed with minced meat and herbs, served with fiery round red Dalle Khursani mountain chilli dip.", "Non-Veg", "Kunga Restaurant & Keventers on Gandhi Road", 140.0, "The soul of Himalayan hill station comfort food on a chilly evening.", "Juicy, savory, piping hot & fiercely spicy chilli dip", true, "🔥 Viral YouTube Hill Station Comfort Dish", 4.9, "Gandhi Road / Chauk Bazaar")
        )
    )

    private val ladakhData = DestinationExploreData(
        destination = "Leh Ladakh",
        stateOrRegion = "Ladakh (UT)",
        country = "India",
        tagline = "Land of High Passes: Turquoise Pangong Lake, Nubra sand dunes, Khardung La & ancient Tibetan gompas",
        bestSeason = "May to September (Highway passes open)",
        safetyTip = "Mandatory 48-hour acclimatization in Leh (11,500 ft) before traveling to higher passes like Khardung La or Pangong.",
        onlineTrendSummary = "Synthesized from epic Ladakh motorcycle expeditions, Pangong blue lake reels, and Nubra desert star-gazing.",
        famousPlaces = listOf(
            FamousPlace("leh1", "Pangong Tso Lake (14,270 ft)", "பாங்கோங் ஏரி", "Nature", "134km long high-altitude endorheic lake that changes color from emerald green to deep cobalt blue under clear skies.", listOf("Color-changing saline waters", "Bollywood 3-Idiots point", "Surrounding Changchenmo mountain reflections"), 0.0, "Day trips / Overnight camping", "Morning 10:00 AM - 3:00 PM for brightest blue colors", "Overnight stay in Lukung or Spangmik gives unbelievable Milky Way stargazing.", "Lakeside stone cairn looking down the blue channel", 5.0, "🔥 World-Famous High Altitude Blue Wonder", 4.9, "110K+ reviews", "Changthang (140km from Leh)"),
            FamousPlace("leh2", "Nubra Valley & Hunder Sand Dunes", "நுப்ரா பள்ளத்தாக்கு", "Nature", "Surreal high-altitude desert valley surrounded by snow mountains, featuring double-humped Bactrian camel safaris.", listOf("Double-humped Bactrian camels", "Diskit Monastery giant Buddha", "Shyok river valley"), 0.0, "Open daily", "5:00 PM for sunset on white sand dunes", "Take a ride on the rare double-humped camels that traversed the historic Silk Route.", "Sand dunes facing Diskit mountains at sunset", 4.0, "🌿 Silk Route Desert Mirage", 4.8, "75K+ reviews", "Nubra Valley"),
            FamousPlace("leh3", "Khardung La Pass (17,582 ft)", "கார்துங் லா", "Adventure", "One of the highest motorable mountain passes in the world connecting Indus valley to Nubra, covered in prayer flags.", listOf("World's highest motorable pass sign", "Tibetan colorful prayer flags", "Glacier mountain wall"), 0.0, "Morning to Afternoon", "Morning 10:00 AM", "Do not stay at the pass for more than 20 minutes due to low oxygen levels.", "Yellow milestone sign board with prayer flags", 1.5, "⭐ Milestone Trophy for World Travelers", 4.7, "90K+ reviews", "40km north of Leh")
        ),
        localFoods = listOf(
            LocalFoodItem("lehf1", "Ladakhi Skyu & Thukpa with Tingmo", "ஸ்கியூ", "Hearty traditional Ladakhi stew of hand-kneaded thumb pasta cooked with root vegetables, dried cheese (Chhurpi), and soft steamed buns.", "Vegetarian", "Gesmo Restaurant & The Tibetan Kitchen, Fort Road", 210.0, "Centuries-old mountain winter comfort dish that provides long-lasting energy.", "Savory, warming, thick, wholesome & deeply nourishing", true, "👑 Traditional Ladakhi Heritage Meal", 4.9, "Fort Road / Main Bazaar"),
            LocalFoodItem("lehf2", "Butter Tea (Gur Gur Chai) & Apricot Jam", "வெண்ணெய் தேநீர்", "Savory tea churned with yak butter, Himalayan pink salt, and milk, paired with fresh organic Sham valley apricot jam on bread.", "Beverage", "Lhamsa Cafe & Old Town Local Kitchens", 80.0, "Essential for high-altitude hydration and keeping the body warm in cold winds.", "Savory, buttery, salty, warming & soothing", true, "⭐ Authentic Himalayan Culture Drink", 4.7, "Leh Main Bazaar")
        )
    )

    private val andamanData = DestinationExploreData(
        destination = "Andaman & Nicobar (Havelock / Swaraj Dweep)",
        stateOrRegion = "Andaman and Nicobar Islands",
        country = "India",
        tagline = "Tropical Island Paradise: Asia's best Radhanagar Beach, turquoise coral reefs, scuba diving & mangrove sea kayaking",
        bestSeason = "October to May",
        safetyTip = "Book government or private Makruzz/Nautika ferry tickets in advance between Port Blair and Havelock Island.",
        onlineTrendSummary = "Synthesized from scuba diving reels, bioluminescence night kayaking, and white sand beach vlogs.",
        famousPlaces = listOf(
            FamousPlace("and1", "Radhanagar Beach (Beach No. 7)", "ராதாநகர் கடற்கரை", "Nature", "Voted Asia's Best Beach by TIME Magazine; pristine powdery white sands flanked by lush tropical mahua rainforests.", listOf("Powdery white sand crescent", "Crystal clear turquoise swimming waters", "Legendary sunset spectacle"), 0.0, "6:00 AM - 6:00 PM", "3:30 PM to 6:00 PM for sunset", "Stay till dusk to witness the spectacular violet-orange reflection on the calm waves.", "Mahua forest tree line framing the white waves", 3.5, "🔥 Voted Asia's #1 Most Beautiful Beach", 4.9, "90K+ reviews", "Havelock Island"),
            FamousPlace("and2", "Elephant Beach & Coral Reef Scuba", "எலிபெண்ட் பீச்", "Adventure", "Snorkeling and scuba haven teeming with colorful sea anemones, clownfish, turtles, and sea walking helmets.", listOf("PADI certified introductory scuba dive", "Glass bottom boat rides", "Underwater sea walk"), 1500.0, "8:00 AM - 3:00 PM", "Morning 8:30 AM for highest water clarity", "Trek through the 2km jungle trail from the road or take a 20-min speed boat.", "Underwater coral reef dive video", 4.0, "🌊 Premier Scuba & Snorkel Paradise", 4.8, "65K+ reviews", "Havelock Island"),
            FamousPlace("and3", "Cellular Jail National Memorial (Kala Pani)", "செல்லுலார் சிறை", "Historical", "Colonial three-pronged prison in Port Blair where Indian freedom fighters were exiled, with moving evening light & sound show.", listOf("Seven-winged panopticon architecture", "Freedom fighter memorial cells", "Sound & light narration"), 50.0, "9:00 AM - 5:00 PM (Light show 6 PM & 7 PM)", "3:30 PM followed by light show", "Visit Veer Savarkar's cell on the top floor and attend the stirring evening light show.", "Central watchtower looking down the prison wings", 2.5, "🏛️ National Freedom Heritage Memorial", 4.8, "85K+ reviews", "Port Blair")
        ),
        localFoods = listOf(
            LocalFoodItem("andf1", "Fresh Grilled Lobster & Red Snapper", "வறுத்த கடல் நண்டு", "Catch-of-the-day Andaman seafood marinated in lemon butter garlic sauce, slow-grilled over hot coals on the beach.", "Non-Veg", "Something Different (A Beachside Cafe) & Full Moon Cafe", 750.0, "Straight from the ocean waters surrounding the islands.", "Smoky, succulent, buttery, flaky & garlic-infused", false, "👑 King of Tropical Island Beach Dinners", 4.9, "Havelock Beach No. 5"),
            LocalFoodItem("andf2", "Tender Coconut & Island Mango Mocktail", "இளநீர் ஜூஸ்", "Fresh king coconut water blended with passionfruit or Alphonso mango pulp, served ice-cold under the palm trees.", "Beverage", "Radhanagar beach shack stalls", 80.0, "Pure tropical refreshing elixir after hours of swimming.", "Sweet, refreshing, electrolyte-rich & naturally cooling", true, "🌿 100% Pure Island Coconut Hydration", 4.8, "Radhanagar Beach")
        )
    )

    private val kolkataData = DestinationExploreData(
        destination = "Kolkata (City of Joy)",
        stateOrRegion = "West Bengal",
        country = "India",
        tagline = "City of Joy: Victoria Memorial marble grandeur, Howrah Bridge, yellow taxis, tramcars & legendary sweets",
        bestSeason = "October to March (Grand Durga Puja in autumn)",
        safetyTip = "Hop on the historic electric tram from Esplanade for a nostalgic vintage tour of colonial North Kolkata.",
        onlineTrendSummary = "Synthesized from North Kolkata heritage food trails, Victoria Memorial walks, and cultural adda sessions.",
        famousPlaces = listOf(
            FamousPlace("kol1", "Victoria Memorial & Maidan", "ভিক্টোরিয়া মেমোরিয়াল", "Historical", "Magnificent white Makrana marble monument built between 1906 and 1921, set within 64 acres of lush gardens and reflection lakes.", listOf("White Makrana marble dome", "Royal art gallery & miniature paintings", "Evening light & sound show"), 50.0, "10:00 AM - 6:00 PM (Gardens 5:30 AM - 6:30 PM)", "Morning 7:00 AM in gardens or late afternoon", "Photograph the reflection of the illuminated central dome in the south lake at twilight.", "South lake water reflection of the marble palace", 2.5, "👑 Imperial Marble Icon of Kolkata", 4.8, "130K+ reviews", "Maidan"),
            FamousPlace("kol2", "Howrah Bridge & Mullick Ghat Flower Market", "হাওড়া ব্রিজ", "Historical", "World's busiest cantilever bridge over the Hooghly river, adjacent to India's largest and most vibrant flower market.", listOf("Cantilever engineering without nuts/bolts", "Hooghly river ferry ride", "Mullick Ghat flower auction"), 0.0, "Bridge open 24/7 (Flower market 4:30 AM - 11:00 AM)", "Early morning 5:30 AM at flower market", "Take a ₹10 public ferry ride from Howrah to Fairlie Place for panoramic bridge photos from the water.", "Hooghly ferry deck looking up at bridge span", 2.0, "📸 Photographer's Dream Color Spectacle", 4.7, "95K+ reviews", "Hooghly Riverfront")
        ),
        localFoods = listOf(
            LocalFoodItem("kolf1", "Kolkata Kathi Roll (Mutton / Chicken / Egg)", "কাঠি রোল", "Flaky paratha layered with fried egg, filled with charcoal-grilled spiced mutton boti or chicken, sliced onions, and lime juice.", "Non-Veg", "Nizam's (New Market - Birthplace in 1932) & Kusum Rolls (Park St)", 90.0, "Invented at Nizam's in 1932 for British patrons on the go.", "Crispy paratha, succulent smoky kebab, tangy & peppery", true, "👑 World-Famous Street Food Invention", 4.9, "New Market / Park Street"),
            LocalFoodItem("kolf2", "Authentic Spongy Rasgulla & Mishti Doi", "রসগোল্লা ও মিষ্টি দই", "Cloud-soft cottage cheese balls cooked in sugar syrup, paired with caramelized earthen-pot sweet pink yogurt.", "Sweet/Dessert", "K.C. Das (Esplanade since 1866), Balaram Mullick & Mithai", 60.0, "Nobin Chandra Das invented the spongy Rasgulla in Kolkata in 1868.", "Spongy, syrupy, caramelized, creamy & delicate", true, "🏛️ 150-Year Heritage Sweet Monument", 4.9, "Esplanade / Bhawanipore")
        )
    )

    private val chennaiData = DestinationExploreData(
        destination = "Chennai (Cultural Capital of South India)",
        stateOrRegion = "Tamil Nadu",
        country = "India",
        tagline = "Gateway to the South: Marina Beach promenade, 7th-century Kapaleeshwarar temple & brass filter coffee",
        bestSeason = "November to February",
        safetyTip = "Visit Marina Beach at 5:00 PM for cool evening sea breezes and try freshly roasted beach corn and sundal.",
        onlineTrendSummary = "Synthesized from Mylapore temple walks, classical music season trails, and Marina Beach evening vlogs.",
        famousPlaces = listOf(
            FamousPlace("che1", "Kapaleeshwarar Temple (Mylapore)", "கபாலீஸ்வரர் கோவில்", "Spiritual", "7th-century Dravidian temple with a 120-ft rainbow gopuram tower adorned with hundreds of mythological stucco sculptures.", listOf("120-ft Rainbow Gopuram", "Temple sacred tank (Theppam)", "Mylapore heritage shopping streets"), 0.0, "6:00 AM - 12:30 PM, 4:00 PM - 9:00 PM", "Morning 6:30 AM or 5:30 PM Aarti", "Walk through the bustling streets around the temple for brass lamps and jasmine garlands.", "East Gopuram towering entrance from courtyard", 2.0, "🏛️ 7th-Century Dravidian Masterpiece", 4.8, "75K+ reviews", "Mylapore"),
            FamousPlace("che2", "Marina Beach (World's 2nd Longest Urban Beach)", "மெரினா கடற்கரை", "Nature", "13-kilometer long natural urban sandy beach along the Bay of Bengal with historic statues and lively evening carnival.", listOf("Lighthouse observation deck", "Triumph of Labour statue", "Evening beach food stalls"), 0.0, "Open 24/7 (Lighthouse 10 AM - 1 PM, 3 PM - 5:30 PM)", "5:00 PM to 8:00 PM for sea breeze", "Climb the modern elevator inside the Marina Lighthouse for a 360-degree coast view.", "Lighthouse top deck looking down the coastline", 2.0, "🌊 World's 2nd Longest Urban Beach", 4.6, "110K+ reviews", "Triplicane / Santhome")
        ),
        localFoods = listOf(
            LocalFoodItem("chef1", "Crispy Ghee Podi Dosa & Filter Coffee", "நெய் பொடி தோசை", "Paper-thin golden crispy fermented crepe coated generously with spicy gunpowder podi and desi ghee, served with 3 chutneys.", "Vegetarian", "Murugan Idli Shop & Rayar's Mess (Mylapore)", 100.0, "The gold standard of South Indian evening tiffin breakfast.", "Crisp exterior, aromatic ghee, fiery spicy podi & soothing chutneys", true, "👑 The Ultimate South Indian Tiffin", 4.9, "Mylapore / T. Nagar"),
            LocalFoodItem("chef2", "Marina Beach Sundal & Murukku Sandwich", "சுண்டல்", "Boiled spiced chickpeas tossed with grated fresh coconut, mustard seeds, curry leaves, and raw mango slivers.", "Vegetarian", "Marina Beach mobile vendors & Alsa Mall Sandwich Stall", 40.0, "Quintessential evening sea-breeze snack enjoyed by generations.", "Tangy raw mango, savory, coconutty, warm & crunchy", true, "⭐ Legendary Beachside Ritual", 4.7, "Marina Beach / Egmore")
        )
    )

    private val agraData = DestinationExploreData(
        destination = "Agra (City of the Taj)",
        stateOrRegion = "Uttar Pradesh",
        country = "India",
        tagline = "Home to the Taj Mahal: Eternal monument of love, massive red sandstone fortresses & royal Mughlai cuisine",
        bestSeason = "October to March",
        safetyTip = "Book Taj Mahal sunrise tickets online on ASI portal to enter promptly at 6:00 AM before tour bus crowds arrive.",
        onlineTrendSummary = "Synthesized from Taj Mahal sunrise guides, Agra Fort walks, and famous Petha sweet discovery vlogs.",
        famousPlaces = listOf(
            FamousPlace("agr1", "Taj Mahal (UNESCO World Wonder)", "ताज महल", "Historical", "World-renowned 17th-century white marble mausoleum built by Mughal Emperor Shah Jahan for his wife Mumtaz Mahal.", listOf("White marble central dome", "Intricate pietra dura floral inlays", "Charbagh Persian reflection gardens", "Yamuna river backdrop"), 50.0, "Sunrise to Sunset (Closed Fridays)", "Dawn 6:00 AM (Sunrise glow on white marble)", "Enter via the East Gate at 5:45 AM to experience the serene ethereal golden sunrise without crowds.", "Central marble reflection bench (Diana Bench)", 3.0, "👑 7 Wonders of the World Landmark", 4.9, "250K+ reviews", "Tajganj"),
            FamousPlace("agr2", "Agra Fort (Lal Qila of Agra)", "आगरा का किला", "Historical", "Vast 16th-century red sandstone walled city built by Emperor Akbar with Diwan-i-Khas and view of the Taj across the river.", listOf("Jahangiri Mahal", "Sheesh Mahal mirror palace", "Musamman Burj where Shah Jahan was imprisoned"), 50.0, "6:00 AM - 6:00 PM", "Late afternoon 3:30 PM", "Stand at the Musamman Burj marble balcony for the historic view of the Taj Mahal across the Yamuna.", "Musamman Burj balcony with Taj in background", 2.5, "🏛️ UNESCO World Heritage Red Fortress", 4.7, "110K+ reviews", "Agra Fort Station")
        ),
        localFoods = listOf(
            LocalFoodItem("agrf1", "Agra Famous Angoori Petha & Kesar Petha", "आगरा का पेठा", "Translucent soft sweet delicacy made from ash gourd (winter melon) soaked in saffron and cardamom syrup.", "Sweet/Dessert", "Panchi Petha (Hari Parvat & Sadar Bazaar)", 120.0, "Created in the royal Mughal kitchens during the construction of the Taj Mahal.", "Juicy, sweet, aromatic saffron, cooling & translucent", true, "👑 World-Famous Heritage Confection of Agra", 4.9, "Sadar Bazaar / Hari Parvat"),
            LocalFoodItem("agrf2", "Bedmi Puri & Spicy Aloo Sabzi with Jalebi", "बेड़मी पूरी और आलू सब्ज़ी", "Crispy deep-fried whole wheat puris stuffed with spiced urad dal paste, served with fiery potato fenugreek curry.", "Vegetarian", "Deviram Sweets (Pratappura) & Seth Gali vendors", 60.0, "Agra's timeless morning breakfast favorite for locals and travelers.", "Flaky, spicy, tangy & piping hot", true, "🔥 Top Agra Morning Street Food", 4.8, "Pratappura / Sadar Bazaar")
        )
    )

    private val amritsarData = DestinationExploreData(
        destination = "Amritsar (The Holy City)",
        stateOrRegion = "Punjab",
        country = "India",
        tagline = "Spiritual Heart of Punjab: Golden Temple serenity, world's largest free kitchen (Langar) & Wagah border energy",
        bestSeason = "October to March",
        safetyTip = "Cover your head with a scarf or bandana before entering the Golden Temple complex and wash feet in the holy pool.",
        onlineTrendSummary = "Synthesized from Golden Temple night illumination vlogs, Langar kitchen operations, and Wagah Border ceremony reels.",
        famousPlaces = listOf(
            FamousPlace("amr1", "Golden Temple (Sri Harmandir Sahib)", "ਸ੍ਰੀ ਹਰਿਮੰਦਰ ਸਾਹਿਬ", "Spiritual", "Most sacred Sikh shrine with pure gold-gilded sanctum standing in the holy Amrit Sarovar pool, open to all humans.", listOf("Gold-gilded sanctum sanctorum", "Amrit Sarovar holy water pool", "Guru ka Langar serving 100K+ free meals daily", "Akal Takht"), 0.0, "Open 24/7 (Langar runs continuously)", "Early morning 4:30 AM (Palki Sahib) and Night 9:00 PM for illumination", "Volunteer at the Langar kitchen for 30 minutes peeling garlic or serving rotis for a soul-enriching experience.", "Marble Parikrama walkway facing golden reflection", 3.5, "✨ World's Most Welcoming Spiritual Wonder", 4.9, "280K+ reviews", "Heritage Street"),
            FamousPlace("amr2", "Wagah Border Beating Retreat Ceremony", "ਵਾਹਗਾ ਬਾਰਡਰ", "Cultural", "Electrifying daily military ceremony featuring high-stepping drill maneuvers by Indian BSF and Pakistan Rangers.", listOf("High-stepping drill choreography", "Patriotic stadium cheers", "National flag lowering at sunset"), 0.0, "Ceremony starts 4:30 PM (Winter) / 5:30 PM (Summer)", "Reach stadium by 3:00 PM to secure prime center seats", "Carry your valid Passport / Voter ID card as security checks are strict.", "Stadium stands facing the international border gate", 3.0, "🔥 Patriotic Spectacle of India", 4.8, "130K+ reviews", "Attari-Wagah (30km from city)")
        ),
        localFoods = listOf(
            LocalFoodItem("amrf1", "Amritsari Crispy Stuffed Kulcha with Chole", "ਅੰਮ੍ਰਿਤਸਰੀ ਕੁਲਚਾ", "Flaky multi-layered tandoor-baked flatbread stuffed with spiced potatoes and onions, smothered with white butter, served with chole.", "Vegetarian", "Bhai Kulwant Singh Kulchian Wale & Pehalwan Kulcha", 90.0, "Crushed by hand to reveal its 100 flaky layers right before serving.", "Super-crispy, flaky, buttery, spicy & soulful", true, "👑 #1 Legendary Food Icon of Punjab", 4.9, "Heritage Street / Golden Temple"),
            LocalFoodItem("amrf2", "Amritsari Malai Lassi in Brass Glass", "ਅੰम੍ਰਿਤਸਰੀ ਲੱਸੀ", "Thick churned sweet yogurt drink topped with a massive 2-inch layer of rich clotted milk cream (malai) and butter.", "Beverage", "Gian Chand Lassi (Opposite Regent Cinema) & Ahuja Lassi", 70.0, "Served in giant heavy brass glasses; so thick you need a spoon.", "Ultra-thick, creamy, sweet, velvety & heavenly", true, "⭐ Pure Punjabi Nectar", 4.9, "Regent Cinema / Katra Ahluwalia")
        )
    )

    private val shimlaData = DestinationExploreData(
        destination = "Shimla & Kufri",
        stateOrRegion = "Himachal Pradesh",
        country = "India",
        tagline = "Summer Capital of British India: Heritage Mall Road, Christ Church, pine-covered ridges & snowy Kufri",
        bestSeason = "March to June (Summer breeze) or December to February (Snowfall)",
        safetyTip = "Walk on the pedestrian Mall Road and take the Jakhu ropeway cable car to avoid monkey encounters on the hill trek.",
        onlineTrendSummary = "Synthesized from Mall Road twilight walks, Kufri snow adventure vlogs, and Jakhoo Temple cable car reels.",
        famousPlaces = listOf(
            FamousPlace("sh1", "The Ridge & Neo-Gothic Christ Church", "द रिज और क्राइस्ट चर्च", "Historical", "Sprawling open pedestrian ridge in the heart of Shimla featuring 1857 yellow Christ Church with stained-glass windows.", listOf("1857 Neo-Gothic Christ Church", "Tudor-style library building", "Panoramic snow mountain views"), 0.0, "Open 24/7 (Church open 8:00 AM - 6:00 PM)", "5:00 PM for sunset golden hour", "Enjoy a warm cup of coffee while watching twilight colors over the church clock tower.", "Ridge open square with Christ Church in background", 2.0, "👑 Iconic Emblem of Shimla", 4.7, "90K+ reviews", "The Ridge / Mall Road"),
            FamousPlace("sh2", "Kufri Snow Adventure Valley & Mahasu Peak", "कुफरी", "Adventure", "High altitude mountain resort at 8,600 ft offering horse riding through pine forests, go-karting, and winter skiing.", listOf("Mahasu Peak panoramic viewpoint", "Winter snow skiing & sledging", "Himalayan Nature Park zoo"), 200.0, "9:00 AM - 6:00 PM", "Morning 10:00 AM", "Visit between Dec and Feb to experience thick white snowfall and skiing.", "Mahasu Peak looking towards Great Himalayan Range", 3.5, "❄️ Snow Wonder of Himachal", 4.5, "60K+ reviews", "Kufri (16km from Shimla)")
        ),
        localFoods = listOf(
            LocalFoodItem("shf1", "Himachali Dham (Chana Madra & Sweet Rice)", "हिमाचली धाम", "Traditional festive banquet featuring chickpeas slow-cooked in rich yogurt-cardamom gravy (Madra), Khatta, and sweet rice.", "Vegetarian", "Himachali Rasoi (Mall Road) & Ashiana", 240.0, "Centuries-old royal kitchen feast prepared by hereditary Brahmin chefs (Botis).", "Rich, creamy yogurt gravy, tangy, sweet & aromatic", true, "👑 Authentic Royal Culinary Heritage of Himachal", 4.8, "Mall Road / Middle Bazaar"),
            LocalFoodItem("shf2", "Fresh Woodfired Cinnamon Buns & Hot Cocoa", "दालचीनी बन", "Warm freshly baked cinnamon rolls glazed with vanilla sugar, paired with rich molten hot chocolate overlooking the snow valley.", "Sweet/Dessert", "Wake & Bake Cafe & Honey Hut on Mall Road", 160.0, "The cozy companion to cold mountain breezes on Mall Road.", "Warm, cinnamon-spiced, sweet, comforting & decadent", true, "☕ Top Hill Station Cafe Ritual", 4.8, "The Mall Road")
        )
    )

    private val dubaiData = DestinationExploreData(
        destination = "Dubai",
        stateOrRegion = "Dubai Emirate",
        country = "United Arab Emirates",
        tagline = "City of the Future: World's tallest Burj Khalifa, desert dune bashing, luxury marinas & futuristic souks",
        bestSeason = "November to March",
        safetyTip = "Use the driverless Dubai Metro with a Silver Nol card to easily reach Dubai Mall, Marina, and old Deira souks.",
        onlineTrendSummary = "Synthesized from Burj Khalifa light shows, desert safari quad bike vlogs, and Dubai Marina luxury yacht reels.",
        famousPlaces = listOf(
            FamousPlace("dxb1", "Burj Khalifa (Level 124/125/148)", "برج خليفة", "Viewpoint", "World's tallest building standing 828 meters high with high-speed double-decker elevators and 360-degree observation deck.", listOf("Level 124 & 125 At The Top deck", "Dubai Fountain choreographed show", "World's fastest elevator (10m/s)"), 3800.0, "8:30 AM - 11:00 PM", "5:00 PM to 6:30 PM (Golden hour into city night lights)", "Book non-prime sunset slots online in advance to save up to 40% on ticket price.", "360-degree glass view deck facing the coast", 2.5, "👑 World's #1 Tallest Architectural Wonder", 4.9, "180K+ reviews", "Downtown Dubai"),
            FamousPlace("dxb2", "Desert Safari & Red Dune Quad Biking", "سفاري صحراوي", "Adventure", "Thrilling 4x4 land cruiser dune bashing in Lahbab red sands, camel riding, sandboarding, and Bedouin camp dinner show.", listOf("4x4 Red dune bashing", "Sandboarding down 100ft dunes", "Tanoura & fire dance show with BBQ"), 2200.0, "3:00 PM - 9:30 PM", "Afternoon 3:30 PM pickup", "Wear sunglasses and sandals; sunset over the desert dunes is breathtaking.", "Sunset over Arabian desert red ridge", 5.0, "🔥 Top Thrill Adventure in the Middle East", 4.8, "120K+ reviews", "Lahbab Desert")
        ),
        localFoods = listOf(
            LocalFoodItem("dxbf1", "Emirati Lamb Mandi / Machboos", "مندي لحم", "Fragrant spiced basmati rice slow-cooked with tender, fall-off-the-bone lamb shoulder, toasted nuts, and fiery tomato salsa.", "Non-Veg", "Al Khayma Heritage Restaurant & Zam Zam Mandi", 650.0, "Traditional celebratory Arabian feast of nomadic Bedouin culture.", "Smoky, succulent, aromatic, cardamom-scented & rich", false, "👑 King of Arabian Hospitality Feasts", 4.9, "Al Fahidi / Deira"),
            LocalFoodItem("dxbf2", "Kunafa with Molten Mozzarella & Pistachio", "كنافة بالجبن", "Golden shredded phyllo pastry baked with gooey stretchy sweet cheese, soaked in orange blossom syrup and crushed green pistachios.", "Sweet/Dessert", "Firas Sweets & Al Samadi Sweets", 280.0, "Eaten piping hot; the dramatic cheese stretch is world-famous.", "Crispy crust, molten gooey cheese, floral sweet & nutty", true, "🔥 Viral Middle Eastern Dessert Hit", 4.9, "Jumeirah / Al Rigga")
        )
    )

    private val singaporeData = DestinationExploreData(
        destination = "Singapore",
        stateOrRegion = "Singapore",
        country = "Singapore",
        tagline = "Garden City of the World: Supertree Grove, Marina Bay Sands infinity pool & Michelin-starred hawker street food",
        bestSeason = "November to January & June to August",
        safetyTip = "Purchase a Singapore Tourist Pass (STP) for unlimited travel across the lightning-fast MRT train network.",
        onlineTrendSummary = "Synthesized from Gardens by the Bay light shows, Jewel Changi indoor waterfall reels, and hawker centre foodie guides.",
        famousPlaces = listOf(
            FamousPlace("sin1", "Gardens by the Bay & Supertree Grove", "滨海湾花园", "Nature", "Futuristic 250-acre nature park with 16-story vertical Supertree gardens, Flower Dome, and mist-filled Cloud Forest waterfall.", listOf("Cloud Forest 35m indoor waterfall", "Supertree Grove Garden Rhapsody show", "Flower Dome glass conservatory"), 2200.0, "9:00 AM - 9:00 PM (Supertree show 7:45 PM & 8:45 PM)", "5:00 PM to see both daylight and light show", "The evening Garden Rhapsody music and light show at Supertrees is completely free.", "OCBC Skyway suspension bridge looking at Marina Bay", 3.5, "🌿 World's #1 Futuristic Botanical Garden", 4.9, "160K+ reviews", "Marina Bay"),
            FamousPlace("sin2", "Marina Bay Sands SkyPark Observation Deck", "滨海湾金沙", "Viewpoint", "Cantilevered sky terrace perched 57 stories high on top of three iconic hotel towers overlooking Singapore Strait.", listOf("57th floor panoramic view deck", "Spectra water & laser show view", "Sky high cocktail bars"), 1800.0, "11:00 AM - 9:00 PM", "6:30 PM for sunset into glittering city skyline", "Look down on the illuminated Supertrees and busy Singapore Strait shipping channels.", "SkyPark cantilever deck facing CBD towers", 2.0, "🔥 Instagram #1 Modern Skyline Panorama", 4.8, "110K+ reviews", "Marina Bay")
        ),
        localFoods = listOf(
            LocalFoodItem("sinf1", "Hainanese Chicken Rice with Chilli Sauce", "海南鸡饭", "Poached succulent tender chicken served over fragrant rice cooked in chicken broth, ginger, and garlic, with piquant chilli dip.", "Non-Veg", "Tian Tian Hainanese Chicken Rice (Maxwell Hawker) & Boon Tong Kee", 350.0, "Singapore's national dish; recognized with Michelin Bib Gourmand.", "Silky chicken, rich aromatic rice, spicy ginger chilli", true, "👑 Singapore's Undisputed National Dish", 4.9, "Maxwell Food Centre / Chinatown"),
            LocalFoodItem("sinf2", "Singapore Chilli Crab with Fried Mantou Buns", "辣椒螃蟹", "Sweet and savory ocean crab tossed in a luscious, semi-thick, spicy and sweet tomato-chilli sauce with ribbon egg drops.", "Non-Veg", "Jumbo Seafood (Clarke Quay) & Mellben Seafood", 2500.0, "Dip golden deep-fried mantou buns into the rich egg-chilli gravy.", "Tangy, sweet, spicy, savory, meaty & luscious", false, "⭐ Ultimate Iconic Singapore Feast", 4.9, "Clarke Quay / East Coast")
        )
    )

    private val parisData = DestinationExploreData(
        destination = "Paris (The City of Light)",
        stateOrRegion = "Île-de-France",
        country = "France",
        tagline = "The City of Light: Eiffel Tower sparkle, Louvre art treasures, Seine river cruises & pavement sidewalk cafes",
        bestSeason = "April to June & September to November",
        safetyTip = "Use the Paris Métro with Navigo Easy contactless pass and beware of pickpockets near major tourist monuments.",
        onlineTrendSummary = "Synthesized from Eiffel Tower sparkle reels, Montmartre cobblestone walks, and Parisian patisserie bakery tours.",
        famousPlaces = listOf(
            FamousPlace("par1", "Eiffel Tower (Tour Eiffel)", "Tour Eiffel", "Historical", "Iconic 330-meter wrought-iron lattice tower built for the 1889 World's Fair, sparkling with 20,000 golden lights every hour after dark.", listOf("Summit view deck at 276m", "Hourly golden light sparkle show", "Champ de Mars lawns"), 2800.0, "9:30 AM - 11:45 PM", "Sunset to 9:00 PM for the first hourly sparkle", "Head to Place du Trocadéro across the Seine for the most iconic full-frame photo.", "Trocadéro terrace looking across Seine to the tower", 3.0, "👑 World's #1 Most Recognizable Landmark", 4.8, "320K+ reviews", "Champ de Mars / 7th Arr."),
            FamousPlace("par2", "Louvre Museum & Glass Pyramid", "Musée du Louvre", "Cultural", "World's largest art museum housed in former royal palace holding 35,000 masterworks including Leonardo da Vinci's Mona Lisa.", listOf("Mona Lisa painting", "Venus de Milo statue", "I.M. Pei glass pyramid courtyard"), 1900.0, "9:00 AM - 6:00 PM (Wed & Fri open till 9:45 PM, Closed Tuesdays)", "Wednesday or Friday evening for uncrowded halls", "Book timed-entry tickets online in advance to bypass the 2-hour ticket queue.", "Louvre glass pyramid illuminated at twilight", 4.0, "🏛️ World's Greatest Art Treasury", 4.7, "250K+ reviews", "1st Arrondissement")
        ),
        localFoods = listOf(
            LocalFoodItem("parf1", "Fresh Parisian Baguette with Camembert & Butter", "Baguette Tradition", "Crusty golden artisan sourdough baguette with open honeycomb crumb, paired with creamy French brie or salted butter.", "Vegetarian", "Du Pain et des Idées & Le Grenier à Pain (Montmartre)", 180.0, "UNESCO-listed French culinary heritage baked fresh multiple times daily.", "Super-crispy crust, soft airy crumb, buttery & creamy", true, "👑 UNESCO Intangible Cultural Heritage", 4.9, "10th / 18th Arrondissement"),
            LocalFoodItem("parf2", "French Onion Soup Gratinée", "Soupe à l'oignon", "Rich slow-caramelized beef-broth onion soup topped with toasted baguette croutons and a thick bubbling crust of melted Gruyère cheese.", "Non-Veg", "Bouillon Chartier & Les Deux Magots (Saint-Germain)", 750.0, "Classic French bistro comfort food that warms the soul on a cool evening.", "Deeply savory, caramelized, sweet, cheesy & rich", false, "⭐ Timeless French Bistro Classic", 4.8, "Saint-Germain-des-Prés")
        )
    )

    private val tokyoData = DestinationExploreData(
        destination = "Tokyo",
        stateOrRegion = "Kanto",
        country = "Japan",
        tagline = "Futuristic Metropolis & Ancient Traditions: Shibuya Crossing, historic Senso-ji temple, anime hubs & world-class ramen",
        bestSeason = "March to May (Cherry blossoms) & September to November (Autumn foliage)",
        safetyTip = "Get a digital Suica / Pasmo card on your phone for seamless tap-and-go travel on Tokyo's punctual train network.",
        onlineTrendSummary = "Synthesized from Shibuya scramble reels, Akihabara tech explorations, and Tsukiji outer market food crawls.",
        famousPlaces = listOf(
            FamousPlace("tyo1", "Shibuya Scramble Crossing & Hachiko Statue", "渋谷スクランブル交差点", "Cultural", "World's busiest pedestrian intersection where up to 3,000 people cross simultaneously with neon video billboards.", listOf("Multi-directional pedestrian scramble", "Loyal dog Hachiko bronze statue", "Shibuya Sky 360-degree rooftop deck"), 0.0, "Open 24/7 (Shibuya Sky 10 AM - 10:30 PM)", "7:00 PM for dazzling neon lights and bustling energy", "Go to Shibuya Sky open-air rooftop observatory for looking down on the crossing.", "Shibuya Sky glass corner overlooking crossing", 2.0, "🔥 World's #1 Most Famous City Intersection", 4.8, "140K+ reviews", "Shibuya"),
            FamousPlace("tyo2", "Sensō-ji Temple & Nakamise-dori", "浅草寺", "Spiritual", "Tokyo's oldest Buddhist temple founded in 628 AD, approached through the iconic Kaminarimon gate with giant red paper lantern.", listOf("Kaminarimon Thunder Gate with giant red lantern", "Nakamise traditional shopping street", "Five-story pagoda"), 0.0, "Sanctum 6:00 AM - 5:00 PM (Grounds open 24/7)", "Morning 7:30 AM before tour crowds or night illumination", "Try traditional warm melon pan sweet bread on the approach street.", "Kaminarimon giant red lantern entrance", 2.5, "🏛️ Ancient Soul of Edo Tokyo", 4.7, "90K+ reviews", "Asakusa")
        ),
        localFoods = listOf(
            LocalFoodItem("tyof1", "Authentic Tonkotsu / Shoyu Ramen", "ラーメン", "Springy handcrafted noodles in 16-hour slow-simmered rich broth, topped with tender chashu pork, seasoned soft-boiled egg, and nori.", "Non-Veg", "Ichiran Ramen (Shibuya/Shinjuku) & Afuri Ramen (Ebisu)", 650.0, "The global standard of Japanese comfort noodle craft.", "Rich, savory, umami-packed, silky broth & springy noodles", true, "👑 World-Famous Japanese Comfort Food", 4.9, "Shibuya / Shinjuku"),
            LocalFoodItem("tyof2", "Tsukiji Fresh Nigiri Sushi & Wagyu Skewer", "築地 寿司", "Fresh melt-in-mouth bluefin tuna (otoro), salmon, and sea urchin sushi freshly prepared by master chefs right at the counter.", "Non-Veg", "Tsukiji Outer Market & Sushi Dai (Toyosu)", 1200.0, "Sourced directly from the world's freshest seafood markets every dawn.", "Delicate, sweet, buttery, fresh & sublime umami", true, "⭐ Supreme Seafood Perfection", 4.9, "Tsukiji Outer Market")
        )
    )

    private fun generateDynamicDestination(destName: String): DestinationExploreData {
        val cleanName = destName.trim().replaceFirstChar { it.uppercase() }
        return DestinationExploreData(
            destination = cleanName,
            stateOrRegion = "Popular Region",
            country = "Tourist Destination",
            tagline = "Explore the vibrant attractions, iconic sights & authentic culinary treasures of $cleanName",
            bestSeason = "October to March (Pleasant weather)",
            safetyTip = "Use ride-hailing apps or metered transit for transparent travel, and keep emergency contact numbers saved.",
            onlineTrendSummary = "Synthesized from popular travel creator guides, online vlogs, and traveler recommendations for $cleanName.",
            famousPlaces = listOf(
                FamousPlace(
                    id = "dyn_1",
                    name = "$cleanName Heritage Landmark & Old Quarter",
                    localName = "$cleanName Historic Centre",
                    category = "Historical",
                    description = "The historic beating heart of $cleanName, featuring timeless architecture, ancient monuments, and cultural exhibits.",
                    highlights = listOf("Ancient architecture", "Heritage walking trail", "Local photography vantage"),
                    entryFeeInr = 100.0,
                    timings = "8:00 AM - 6:00 PM",
                    bestTimeToVisit = "Morning 9:00 AM",
                    insiderTip = "Hire a local guide to discover hidden architectural secrets.",
                    photoSpot = "Central heritage monument plaza",
                    estimatedDurationHours = 2.5,
                    trendingTag = "🔥 Top Rated Heritage Landmark",
                    rating = 4.7,
                    reviewCount = "25K+ online reviews",
                    areaOrNeighborhood = "Central $cleanName"
                ),
                FamousPlace(
                    id = "dyn_2",
                    name = "$cleanName Botanical Gardens & Scenic Nature Park",
                    localName = "$cleanName Eco Park",
                    category = "Nature",
                    description = "Lush green oasis spread over acres with serene walking paths, floral gardens, and peaceful natural views.",
                    highlights = listOf("Tropical floral gardens", "Shaded jogging trails", "Scenic lake / fountain"),
                    entryFeeInr = 30.0,
                    timings = "6:00 AM - 7:30 PM",
                    bestTimeToVisit = "Early morning for fresh air and birdwatching",
                    insiderTip = "Visit at sunrise for peaceful uncrowded strolls.",
                    photoSpot = "Central garden flower pavilion",
                    estimatedDurationHours = 2.0,
                    trendingTag = "🌿 Lush Green Nature Spot",
                    rating = 4.6,
                    reviewCount = "18K+ online reviews",
                    areaOrNeighborhood = "Park Avenue"
                ),
                FamousPlace(
                    id = "dyn_3",
                    name = "$cleanName Sunset Viewpoint & City Panorama",
                    localName = "Sunset Point",
                    category = "Viewpoint",
                    description = "Spectacular elevated viewpoint offering breathtaking 360-degree sunset panoramas across the entire skyline of $cleanName.",
                    highlights = listOf("Panoramic skyline views", "Golden hour sunset deck", "Evening breeze"),
                    entryFeeInr = 0.0,
                    timings = "Open 24/7",
                    bestTimeToVisit = "5:00 PM for Golden Hour Sunset",
                    insiderTip = "Arrive 30 minutes before sunset to claim the best viewpoint spot.",
                    photoSpot = "Observation rail overlooking the valley/skyline",
                    estimatedDurationHours = 1.5,
                    trendingTag = "🔥 Viral Sunset Reel Spot",
                    rating = 4.8,
                    reviewCount = "30K+ online reviews",
                    areaOrNeighborhood = "Hilltop Promenade"
                ),
                FamousPlace(
                    id = "dyn_4",
                    name = "$cleanName Central Bazaar & Artisanal Market",
                    localName = "$cleanName Market",
                    category = "Market",
                    description = "Bustling retail and cultural hub packed with traditional handicraft stalls, regional souvenirs, and street artists.",
                    highlights = listOf("Handicraft & souvenir stalls", "Regional textiles & jewelry", "Local street vibe"),
                    entryFeeInr = 0.0,
                    timings = "10:30 AM - 9:30 PM",
                    bestTimeToVisit = "4:00 PM to 8:00 PM",
                    insiderTip = "Bargain respectfully with local artisans for authentic handmade souvenirs.",
                    photoSpot = "Vibrant marketplace street entrance",
                    estimatedDurationHours = 2.0,
                    trendingTag = "✨ Local Craft & Shopping Hub",
                    rating = 4.5,
                    reviewCount = "20K+ online reviews",
                    areaOrNeighborhood = "Market Square"
                )
            ),
            localFoods = listOf(
                LocalFoodItem(
                    id = "dyn_f1",
                    name = "Signature $cleanName Regional Thali / Specialty Feast",
                    regionalName = "Specialty Thali",
                    description = "Authentic multi-course regional platter showcasing traditional slow-cooked delicacies, fragrant rice, flatbreads, and signature chutneys.",
                    dietType = "Vegetarian",
                    famousAtEatery = "Iconic 50-year-old heritage restaurant in $cleanName",
                    averagePriceInr = 250.0,
                    mustTryReason = "The definitive culinary representation of $cleanName's rich heritage.",
                    flavorProfile = "Aromatic, rich, spiced, wholesome & satisfying",
                    isStreetFood = false,
                    trendingTag = "👑 #1 Signature Regional Meal",
                    rating = 4.9,
                    areaOrNeighborhood = "Central $cleanName"
                ),
                LocalFoodItem(
                    id = "dyn_f2",
                    name = "Famous $cleanName Street Food Snack & Chutney",
                    regionalName = "Street Specialty",
                    description = "Piping hot, crispy regional street snack prepared fresh at bustling evening food stalls with tangy and spicy sauces.",
                    dietType = "Vegetarian",
                    famousAtEatery = "Popular Street Food Lane in $cleanName",
                    averagePriceInr = 60.0,
                    mustTryReason = "Beloved local evening ritual with mouthwatering flavors.",
                    flavorProfile = "Crispy, tangy, spicy & flavorful",
                    isStreetFood = true,
                    trendingTag = "🔥 Viral Evening Street Food",
                    rating = 4.8,
                    areaOrNeighborhood = "Market Food Street"
                ),
                LocalFoodItem(
                    id = "dyn_f3",
                    name = "Traditional $cleanName Heritage Sweet / Dessert",
                    regionalName = "Heritage Sweet",
                    description = "Mouthwatering regional dessert made with caramelized milk, cardamom, saffron, and roasted nuts.",
                    dietType = "Sweet/Dessert",
                    famousAtEatery = "Legendary local sweet shop in $cleanName",
                    averagePriceInr = 100.0,
                    mustTryReason = "Celebrated festival sweet with decades of culinary tradition.",
                    flavorProfile = "Sweet, aromatic, creamy & nutty",
                    isStreetFood = true,
                    trendingTag = "⭐ Famous Heritage Confection",
                    rating = 4.8,
                    areaOrNeighborhood = "Old Town Sweet Corner"
                )
            )
        )
    }
}
