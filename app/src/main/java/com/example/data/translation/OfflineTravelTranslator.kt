package com.example.data.translation

import com.example.data.model.TranslationResult
import com.example.data.model.TravelerPhrase

object OfflineTravelTranslator {

    data class PhraseEntry(
        val english: String,
        val keywords: List<String>,
        val category: String,
        val translations: Map<String, LanguageTranslation>
    )

    data class LanguageTranslation(
        val translatedText: String,
        val pronunciation: String,
        val etiquetteTip: String
    )

    private val travelPhrasebook: List<PhraseEntry> = listOf(
        // Greetings & Politeness
        PhraseEntry(
            english = "Hello / Greetings",
            keywords = listOf("hello", "hi", "hey", "greetings", "namaste", "morning", "good morning"),
            category = "Greetings",
            translations = mapOf(
                "hi" to LanguageTranslation("नमस्ते", "Namaste", "Join palms together in front of the chest with a polite nod."),
                "kn" to LanguageTranslation("ನಮಸ್ಕಾರ", "Namaskara", "Slight bow with folded hands is customary across Karnataka."),
                "ta" to LanguageTranslation("வணக்கம்", "Vanakkam", "Fold hands together with a warm smile."),
                "te" to LanguageTranslation("నమస్కారం", "Namaskaram", "Traditional respectful greeting across Andhra and Telangana."),
                "ml" to LanguageTranslation("നമസ്കാരം", "Namaskaram", "Respectful greeting with gentle smile in Kerala."),
                "mr" to LanguageTranslation("नमस्कार", "Namaskar", "Standard polite greeting across Maharashtra."),
                "bn" to LanguageTranslation("নমস্কার", "Nomoshkar", "Warm greeting in West Bengal."),
                "gu" to LanguageTranslation("નમસ્તે", "Namaste / Jai Shri Krishna", "Friendly respectful greeting in Gujarat."),
                "pa" to LanguageTranslation("ਸਤਿ ਸ਼੍ਰੀ ਅਕਾਲ", "Sat Sri Akal", "Traditional Sikh greeting with folded hands."),
                "fr" to LanguageTranslation("Bonjour", "Bon-zhoor", "Make eye contact and say it politely before speaking."),
                "es" to LanguageTranslation("¡Hola! Buenos días", "Oh-la, bway-nos dee-as", "Warm and friendly greeting in Spanish."),
                "de" to LanguageTranslation("Guten Tag / Hallo", "Goo-ten tahk / Hah-loh", "A firm handshake and eye contact is common."),
                "ja" to LanguageTranslation("こんにちは", "Konnichiwa", "Make a slight bow of 15 degrees."),
                "th" to LanguageTranslation("สวัสดีครับ/ค่ะ", "Sawatdee khrap/kha", "Press palms together at chest level (Wai gesture)."),
                "it" to LanguageTranslation("Ciao / Buongiorno", "Chow / Bwon-jorn-oh", "Friendly and animated greeting."),
                "ar" to LanguageTranslation("مرحباً / السلام عليكم", "Marhaban / As-salamu alaykum", "Right hand over heart is a sign of warmth.")
            )
        ),
        PhraseEntry(
            english = "Thank you very much",
            keywords = listOf("thank", "thanks", "thank you", "dhanyawad", "shukriya", "grateful"),
            category = "Greetings",
            translations = mapOf(
                "hi" to LanguageTranslation("बहुत-बहुत धन्यवाद / शुक्रिया", "Bahut bahut dhanyavaad / Shukriya", "A genuine smile is the best way to say thanks."),
                "kn" to LanguageTranslation("ತುಂಬಾ ಧನ್ಯವಾದಗಳು", "Tumba dhanyavadagalu", "Polite and highly appreciated by locals."),
                "ta" to LanguageTranslation("மிக்க நன்றி", "Mikka nandri", "Expresses heartfelt gratitude in Tamil Nadu."),
                "te" to LanguageTranslation("చాలా ధన్యవాదాలు", "Chala dhanyavadalu", "Standard polite thanks in Telugu."),
                "ml" to LanguageTranslation("വളരെ നന്ദി", "Valare nandi", "Warm and friendly gratitude."),
                "mr" to LanguageTranslation("खूप खूप धन्यवाद", "Khoop khoop dhanyavaad", "Appreciative response across Maharashtra."),
                "bn" to LanguageTranslation("অনেক ধন্যবাদ", "Onek dhonnobad", "Sweet and gentle thank you in Bengali."),
                "gu" to LanguageTranslation("ખૂબ ખૂબ આભાર", "Khoob khoob aabhar", "Polite gratitude in Gujarat."),
                "pa" to LanguageTranslation("ਬਹੁਤ ਬਹੁਤ ਧੰਨਵਾਦ", "Bahut bahut dhanvaad", "Warm energetic thank you."),
                "fr" to LanguageTranslation("Merci beaucoup", "Mair-see boh-koo", "Say it warmly when receiving food, tickets or change."),
                "es" to LanguageTranslation("Muchas gracias", "Moo-chas grah-see-as", "Universally polite across Spanish-speaking countries."),
                "de" to LanguageTranslation("Vielen Dank", "Fee-len dank", "Respectful and standard in Germany and Austria."),
                "ja" to LanguageTranslation("どうもありがとうございます", "Dōmo arigatō gozaimasu", "Bow slightly to express deep appreciation."),
                "th" to LanguageTranslation("ขอบคุณมากครับ/ค่ะ", "Khop khun mak khrap/kha", "Add the polite particle khrap (male) / kha (female)."),
                "it" to LanguageTranslation("Grazie mille", "Graht-see-eh meel-leh", "Lively and cheerful expression of thanks."),
                "ar" to LanguageTranslation("شكراً جزيلاً", "Shukran jazeelan", "Right hand on chest shows sincerity.")
            )
        ),
        // Shopping & Bargaining
        PhraseEntry(
            english = "How much does this cost?",
            keywords = listOf("cost", "how much", "price", "kitna", "rate", "rupees", "kitne ka"),
            category = "Shopping",
            translations = mapOf(
                "hi" to LanguageTranslation("यह कितने का है?", "Yeh kitne ka hai?", "Point to the item with an open palm."),
                "kn" to LanguageTranslation("ಇದು ಎಷ್ಟು ಬೆಲೆ?", "Idu eshtu bele?", "Ask with a friendly smile at local markets."),
                "ta" to LanguageTranslation("இது எவ்வளவு?", "Ithu evvalavu?", "Point to the item and ask clearly."),
                "te" to LanguageTranslation("దీని ధర ఎంత?", "Deeni dhara entha?", "Common question at street stalls in Hyderabad/Vizag."),
                "ml" to LanguageTranslation("ഇതിന് എത്രയാണ് വില?", "Ithinu ethrayanu vila?", "Ask shopkeeper with friendly tone in Kerala."),
                "mr" to LanguageTranslation("हे कितीला आहे?", "He kitila aahe?", "Standard shopping phrase in Mumbai and Pune."),
                "bn" to LanguageTranslation("এটার দাম কত?", "Etar daam koto?", "Everyday shopping question in Kolkata."),
                "gu" to LanguageTranslation("આ કેટલાનું છે?", "Aa ketlaanu chhe?", "Friendly market inquiry across Gujarat."),
                "pa" to LanguageTranslation("ਇਹ ਕਿੰਨੇ ਦਾ ਹੈ?", "Eh kinne da hai?", "Clear price question in Punjab."),
                "fr" to LanguageTranslation("Combien ça coûte ?", "Kohm-byan sah koot?", "Polite standard price inquiry in France."),
                "es" to LanguageTranslation("¿Cuánto cuesta esto?", "Kwan-to kwes-tah es-toh?", "Clear and polite price question in Spain & Latin America."),
                "de" to LanguageTranslation("Wie viel kostet das?", "Vee feel kos-tet dahs?", "Standard price question in Germany."),
                "ja" to LanguageTranslation("これはいくらですか？", "Kore wa ikura desu ka?", "Point politely to the product."),
                "th" to LanguageTranslation("อันนี้ราคาเท่าไหร่ครับ/ค่ะ?", "An nee raka tao rai khrap/kha?", "Ask before buying at night markets."),
                "it" to LanguageTranslation("Quanto costa questo?", "Kwan-toh kos-tah kwes-toh?", "Friendly inquiry at local boutiques and cafes."),
                "ar" to LanguageTranslation("بكم هذا؟", "Bikam hatha?", "Standard bazaar inquiry in Arab souks.")
            )
        ),
        PhraseEntry(
            english = "Can you give a discount?",
            keywords = listOf("discount", "bargain", "kam", "reduce", "cheaper", "less", "lower"),
            category = "Shopping",
            translations = mapOf(
                "hi" to LanguageTranslation("कुछ कम कीजिए न? सही दाम लगाइए।", "Kuch kam kijiye na? Sahi daam lagaiye.", "Keep it lighthearted and conversational when bargaining."),
                "kn" to LanguageTranslation("ಸ್ವಲ್ಪ ಕಡಿಮೆ ಮಾಡಿ ಕೊಡಿ", "Swalpa kadime maadi kodi", "Friendly, polite negotiation at local shops."),
                "ta" to LanguageTranslation("கொஞ்சம் குறைத்து கொடுங்கள்", "Konjam kuraithu kodungal", "Smiling bargaining phrase in Tamil bazaars."),
                "te" to LanguageTranslation("కొంచెం తగ్గించండి ప్లీజ్", "Konchem tagginchandi please", "Soft, polite bargaining query."),
                "ml" to LanguageTranslation("കുറച്ചു കുറയ്ക്കാമോ?", "Kurachu kuraykkamo?", "Casual negotiation at street stalls."),
                "mr" to LanguageTranslation("थोडं कमी करा ना!", "Thoda kami kara na!", "Colloquial friendly bargaining in Maharashtra."),
                "bn" to LanguageTranslation("একটু কম রাখবেন?", "Ektu kom rakhben?", "Polite negotiation at Kolkata New Market."),
                "gu" to LanguageTranslation("થોડું ઓછું કરો ને!", "Thodu ochhu karo ne!", "Humorous and friendly bargaining."),
                "pa" to LanguageTranslation("ਕੁਝ ਘੱਟ ਕਰੋ ਜੀ!", "Kujh ghatt karo ji!", "Good-natured bargaining in Amritsar bazaars."),
                "fr" to LanguageTranslation("Pouvez-vous faire un geste / une remise ?", "Poo-vay voo fair oon zhest?", "Best used in flea markets, not in retail stores."),
                "es" to LanguageTranslation("¿Me puede hacer una rebaja / descuento?", "Meh pweh-day ah-sair oo-na reh-bah-hah?", "Common in street markets (Mercados)."),
                "de" to LanguageTranslation("Können Sie einen Rabatt geben?", "Kur-nen zee eye-nen rah-baht gay-ben?", "Use mainly at flea markets (Flohmarkt)."),
                "ja" to LanguageTranslation("少し安くなりませんか？", "Sukoshi yasuku narimasen ka?", "Only acceptable at flea markets, never in department stores."),
                "th" to LanguageTranslation("ลดราคาหน่อยได้ไหมครับ/ค่ะ?", "Lod raka noi dai mai khrap/kha?", "Standard friendly phrase at Thai night bazaars."),
                "it" to LanguageTranslation("Può farmi un po' di sconto?", "Pwoh fahr-mee oon poh dee skon-toh?", "Friendly smile works well at street stalls."),
                "ar" to LanguageTranslation("هل يمكنك تخفيض السعر قليلاً؟", "Hal yumkinuka takhfeedh as-si'r qaleelan?", "Expected and customary in traditional souks.")
            )
        ),
        PhraseEntry(
            english = "Can I pay with UPI / Google Pay / QR Code?",
            keywords = listOf("upi", "gpay", "google pay", "phonepe", "paytm", "qr", "qr code", "online payment", "card"),
            category = "Shopping",
            translations = mapOf(
                "hi" to LanguageTranslation("क्या UPI / Google Pay / PhonePe चलेगा?", "Kya UPI / PhonePe chalega?", "Show your phone camera pointing at the QR standee."),
                "kn" to LanguageTranslation("UPI / Google Pay ನಡೀತಾ?", "UPI / Google Pay nadeetha?", "Show phone screen towards the QR code."),
                "ta" to LanguageTranslation("UPI / GPay ஏற்றுக்கொள்ளப்படுமா?", "UPI / GPay yetrukkollappaduma?", "Point phone at the QR scanner sticker."),
                "te" to LanguageTranslation("UPI / PhonePe తీసుకుంటారా?", "UPI / PhonePe theesukuntara?", "Very common across shops and autos."),
                "ml" to LanguageTranslation("UPI / Google Pay സ്വീകരിക്കുമോ?", "UPI / Google Pay sweekarikumo?", "UPI is accepted in nearly 100% of Kerala shops."),
                "mr" to LanguageTranslation("UPI / GPay चालेल का?", "UPI / GPay chaalel ka?", "Show UPI app on your screen."),
                "bn" to LanguageTranslation("UPI / Google Pay চলবে কি?", "UPI / Google Pay cholbe ki?", "Look for the blue/green QR stand on the counter."),
                "gu" to LanguageTranslation("UPI / PhonePe ચાલશે?", "UPI / PhonePe chaalshe?", "Show phone payment scanner."),
                "pa" to LanguageTranslation("ਕੀ UPI / Google Pay ਚੱਲੇਗਾ?", "Ki UPI / Google Pay challega?", "Accepted in dhabas and city shops alike."),
                "fr" to LanguageTranslation("Puis-je payer par carte sans contact ou QR code ?", "Pweezh pay-ay par kart sahn kohn-takt?", "Contactless card (Sans contact) is standard."),
                "es" to LanguageTranslation("¿Puedo pagar con tarjeta / contactless?", "Pweh-doh pah-gahr kon tar-heh-tah?", "Look for the contactless symbol on the terminal."),
                "de" to LanguageTranslation("Kann ich mit Karte / kontaktlos bezahlen?", "Kahn ikh mit kar-teh bay-tsah-len?", "Some traditional bakeries in Germany remain cash-only."),
                "ja" to LanguageTranslation("QRコード決済やカードは使えますか？", "Kyū-āru kōdo kessai ya kādo wa tsukaemasu ka?", "PayPay, Suica/Pasmo or credit card."),
                "th" to LanguageTranslation("จ่ายด้วย QR code หรือบัตรได้ไหมครับ/ค่ะ?", "Jye duay QR code reu bat dai mai khrap/kha?", "PromptPay or credit cards in Bangkok."),
                "it" to LanguageTranslation("Posso pagare con carta contactless?", "Pos-soh pah-gah-reh kon kahr-tah?", "Standard across restaurants and shops."),
                "ar" to LanguageTranslation("هل يمكنني الدفع بالبطاقة؟", "Hal yumkinuni ad-daf' bil-bitaqah?", "Apple Pay and cards are widely used in UAE.")
            )
        ),
        // Food & Dining
        PhraseEntry(
            english = "Is this vegetarian / without egg?",
            keywords = listOf("veg", "vegetarian", "pure veg", "egg", "eggless", "non veg", "shuddh", "jain", "halal", "meat"),
            category = "Food & Dining",
            translations = mapOf(
                "hi" to LanguageTranslation("क्या यह शुद्ध शाकाहारी है? इसमें अंडा तो नहीं है?", "Kya yeh shuddh shakahari hai? Isme anda toh nahi hai?", "Look for the green dot inside green square symbol on packaging."),
                "kn" to LanguageTranslation("ಇದು ಶುದ್ಧ ಸಸ್ಯಾಹಾರವೇ? ಮೊಟ್ಟೆ ಇಲ್ವಾ?", "Idu shuddha sasyahaarave? Motte ilva?", "Ask waiter for 'Pure Veg' options in Karnataka."),
                "ta" to LanguageTranslation("இது சைவ உணவா? முட்டை இல்லையா?", "Ithu saiva unava? Muttai illaiya?", "Look for the 'Pure Vegetarian' green signboards."),
                "te" to LanguageTranslation("ఇది శాకాహార భోజనమేనా? గుడ్డు లేదా?", "Idi shakahara bhojanamena? Guddu leda?", "Look for pure veg symbols on menus."),
                "ml" to LanguageTranslation("ഇത് പ്യുവർ വെജിറ്റേറിയൻ ആണോ? മുട്ട ഉണ്ടോ?", "Ithu pure vegetarian aano? Mutta undo?", "Ask for 'Pure Vegetarian Sadya/meals'."),
                "mr" to LanguageTranslation("हे शुद्ध शाकाहारी आहे का? यात अंडं नाही ना?", "He shuddh shakahari aahe ka? Yaat anda nahi na?", "Look for the green square veg symbol."),
                "bn" to LanguageTranslation("এটা কি নিরামিষ? ডিম আছে কি?", "Eta ki niramish? Dim aache ki?", "'Niramish' means 100% vegetarian without egg or onion/garlic."),
                "gu" to LanguageTranslation("આ શુદ્ધ શાકાહારી છે? ઈંડું નથી ને?", "Aa shuddh shakahari chhe? Indu nathi ne?", "Most restaurants in Gujarat are pure veg."),
                "pa" to LanguageTranslation("ਕੀ ਇਹ ਸ਼ੁੱਧ ਸ਼ਾਕਾਹਾਰੀ ਹੈ? ਅੰਡਾ ਤਾਂ ਨਹੀਂ?", "Ki eh shuddh shakahari hai? Anda taan nahi?", "Ask for 'Vaishno Dhaba' for guaranteed 100% pure veg."),
                "fr" to LanguageTranslation("Est-ce végétarien ? Sans viande ni œuf ?", "Es-suh vay-zhay-tahr-yan? Sahn vyand nee erf?", "Explain 'sans viande ni poisson' (no meat or fish)."),
                "es" to LanguageTranslation("¿Es esto vegetariano? ¿Sin carne ni huevo?", "Es es-toh veh-heh-tah-ree-ah-noh? Seen kar-nay nee weh-voh?", "Clarify that chicken and ham (jamón) are also meat."),
                "de" to LanguageTranslation("Ist das vegetarisch? Ohne Fleisch und Ei?", "Ist dahs veh-gay-tah-rish? Oh-neh fly-sh oont eye?", "Look for the green 'V' vegetarian mark."),
                "ja" to LanguageTranslation("これはベジタリアン料理ですか？（肉・魚・卵なし）", "Kore wa bejitarian ryōri desu ka? (Niku, sakana, tamago nashi)", "Many Japanese soups contain dashi fish broth; ask clearly."),
                "th" to LanguageTranslation("อันนี้เจ/มังสวิรัติไหมครับ/ค่ะ?", "An nee jay/mangsawirat mai khrap/kha?", "'A-hahn Jay' (อาหารเจ) means strict vegetarian / vegan."),
                "it" to LanguageTranslation("È vegetariano? Senza carne e uova?", "Eh veh-jeh-tah-ryah-noh? Sen-tsah kar-neh eh woh-vah?", "Italian cuisine has abundant natural vegetarian options."),
                "ar" to LanguageTranslation("هل هذا نباتي خالي من اللحم والبيض؟", "Hal hatha nabati khalin min al-lahm wal-baydh?", "Vegetarian dishes are labeled 'Nabaati'.")
            )
        ),
        PhraseEntry(
            english = "Please make it less spicy / mild",
            keywords = listOf("spicy", "less spicy", "tikha", "mirchi", "mild", "chilli", "hot", "kam teekha"),
            category = "Food & Dining",
            translations = mapOf(
                "hi" to LanguageTranslation("कृपया कम तीखा / बिना मिर्च के बनाइए।", "Kripya kam teekha / bina mirch ke banaiye.", "Say 'kam mirchi' clearly when placing order."),
                "kn" to LanguageTranslation("ಖಾರ ಕಡಿಮೆ ಮಾಡಿ ಕೊಡಿ ಪ್ಲೀಸ್", "Khara kadime maadi kodi please", "Specify 'Less spicy' for rasam or sambar."),
                "ta" to LanguageTranslation("காரத்தை கொஞ்சம் குறைத்து செய்யுங்கள்", "Kaarathai konjam kuraithu seiyungal", "South Indian curries can be fiery; 'Kam kaaram' is useful."),
                "te" to LanguageTranslation("కారం తక్కువగా వేయండి ప్లీజ్", "Kaaram thakkuvaga veyandi please", "Andhra food is famous for spice; specify low spice."),
                "ml" to LanguageTranslation("എരിവ് കുറച്ചു തരുമോ?", "Erivu kurachu tharumo?", "Ask to reduce black pepper and green chillies."),
                "mr" to LanguageTranslation("कृपया कमी तिखट करा!", "Kripya kami tikhat kara!", "Say 'Kami tikhat' for street snacks like Misal."),
                "bn" to LanguageTranslation("একটু কম ঝাল দেবেন প্লিজ।", "Ektu kom jhal deben please.", "'Kom jhal' means less chilli in Kolkata."),
                "gu" to LanguageTranslation("જરા ઓછું તીખું બનાવજો!", "Zara ochhu teekhu banavjo!", "Gujarati food is generally mild and sweet-savory."),
                "pa" to LanguageTranslation("ਕਿਰਪਾ ਕਰਕੇ ਘੱਟ ਤਿੱਖਾ ਬਣਾਉਣਾ ਜੀ।", "Kripa karke ghatt tikkha banauna ji.", "Say 'Less spicy masala' for dhabas."),
                "fr" to LanguageTranslation("Pas trop épicé, s'il vous plaît.", "Pah troh ay-pee-say, seel voo play.", "French cuisine is generally mild."),
                "es" to LanguageTranslation("Poco picante, por favor. / Sin picante.", "Poh-koh pee-kahn-tay, por fah-vor.", "Say 'Sin picante' for zero chilli."),
                "de" to LanguageTranslation("Bitte nicht zu scharf!", "Bit-teh nikht tsoo sharf!", "Standard request for low spice."),
                "ja" to LanguageTranslation("辛くしないでください（甘口でお願いします）", "Karaku shinaide kudasai (Amakuchi de onegaishimasu)", "'Amakuchi' means mild sweet curry style."),
                "th" to LanguageTranslation("ไม่เผ็ดเลยครับ/ค่ะ / เผ็ดน้อย", "Mai phet loei khrap/kha / Phet noi", "Thai food can be extremely fiery; 'Mai phet' means not spicy."),
                "it" to LanguageTranslation("Non piccante, per favore.", "Nohn peek-kahn-teh, pair fah-voh-reh.", "Standard request in Italian trattorias."),
                "ar" to LanguageTranslation("من فضلك، بدون فلفل حار أو بهارات قوية.", "Min fadlik, bidun filfil harr.", "Request mild seasoning in Arab restaurants.")
            )
        ),
        PhraseEntry(
            english = "One bottle of mineral drinking water please",
            keywords = listOf("water", "drinking water", "mineral water", "paani", "bottle", "thanda paani"),
            category = "Food & Dining",
            translations = mapOf(
                "hi" to LanguageTranslation("एक सीलबंद पीने के पानी की बोतल दीजिए।", "Ek sealed peene ke paani ki bottle dijiye.", "Always check that the bottle cap seal is unbroken."),
                "kn" to LanguageTranslation("ಒಂದು ಕುಡಿಯುವ ನೀರಿನ ಬಾಟಲಿ ಕೊಡಿ", "Ondu kudiyuva neerina bottle kodi", "Verify the ISI seal on the packaged water bottle."),
                "ta" to LanguageTranslation("ஒரு குடிநீர் பாட்டில் கொடுங்கள்", "Oru kudineer bottle kodungal", "Check bottle cap intactness before accepting."),
                "te" to LanguageTranslation("ఒక మంచి నీళ్ల బాటిల్ ఇవ్వండి", "Oka manchi neella bottle ivvandi", "Ask for packaged mineral water."),
                "ml" to LanguageTranslation("ഒരു കുപ്പിവെള്ളം തരുമോ?", "Oru kuppivellam tharumo?", "Ask for bottled mineral water in Kerala."),
                "mr" to LanguageTranslation("एक पिण्याच्या पाण्याची बाटली द्या।", "Ek pinyachya panyachi baatli dya.", "Check cap seal."),
                "bn" to LanguageTranslation("একটি মিনারেল ওয়াটার বোতল দিন।", "Ekti mineral water bottle din.", "Standard request at food counters."),
                "gu" to LanguageTranslation("એક પીવાના પાણીની બોટલ આપો.", "Ek peevana paanini bottle aapo.", "Ensure fresh sealed bottle."),
                "pa" to LanguageTranslation("ਇੱਕ ਪਾਣੀ ਦੀ ਬੋਤਲ ਦਿਓ ਜੀ।", "Ikk paani di bottle deo ji.", "Ask for chilled (thanda) or normal water."),
                "fr" to LanguageTranslation("Une bouteille d'eau minérale, s'il vous plaît.", "Oon boo-tay duh meen-ay-rahl seel voo play.", "Specify 'eau plate' (still) or 'eau gazeuse' (sparkling)."),
                "es" to LanguageTranslation("Una botella de agua mineral, por favor.", "Oo-nah boh-tay-yah deh ah-gwah mee-neh-rahl, por fah-vor.", "Specify 'sin gas' (still water) or 'con gas' (sparkling)."),
                "de" to LanguageTranslation("Eine Flasche Mineralwasser (still), bitte.", "Eye-neh flah-sheh mee-neh-rahl-vas-ser shtill, bit-teh.", "In Germany, default water is sparkling unless you say 'Still'."),
                "ja" to LanguageTranslation("ミネラルウォーターを1本お願いします。", "Mineraru wōtā o ippon onegaishimasu.", "Very easily bought in convenience stores (Konbini)."),
                "th" to LanguageTranslation("ขอน้ำดื่ม 1 ขวดครับ/ค่ะ", "Kho nam deum neung khuat khrap/kha.", "Bottled water is cheap and safe in 7-Eleven."),
                "it" to LanguageTranslation("Una bottiglia d'acqua naturale, per favore.", "Oo-nah boht-teel-yah dahk-wah nah-too-rah-leh, pair fah-voh-reh.", "'Naturale' = still water; 'Frizzante' = sparkling."),
                "ar" to LanguageTranslation("زجاجة مياه معدنية من فضلك.", "Zujajat miyah ma'daniyyah min fadlik.", "Standard request across the Middle East.")
            )
        ),
        PhraseEntry(
            english = "Bill / Check please",
            keywords = listOf("bill", "check", "hisab", "cheque", "how much to pay"),
            category = "Food & Dining",
            translations = mapOf(
                "hi" to LanguageTranslation("बिल ले आइए, कृपया।", "Bill le aaiye, kripya.", "Signaling handwriting gesture in air is universally understood."),
                "kn" to LanguageTranslation("ಬಿಲ್ ತನ್ನಿ ಪ್ಲೀಸ್", "Bill tanni please", "Standard request at end of meal."),
                "ta" to LanguageTranslation("பில் கொண்டு வாருங்கள்", "Bill kondu vaarungal", "Friendly call to server."),
                "te" to LanguageTranslation("బిల్లు తీసుకురండి ప్లీజ్", "Billu theesukurandi please", "Standard dining conclusion."),
                "ml" to LanguageTranslation("ബിൽ തരുമോ?", "Bill tharumo?", "Ask server for bill with hand wave."),
                "mr" to LanguageTranslation("बिल आणा कृपया!", "Bill aana kripya!", "Standard polite phrase in restaurants."),
                "bn" to LanguageTranslation("বিলটা দিন প্লিজ।", "Bill-ta din please.", "Everyday dining phrase."),
                "gu" to LanguageTranslation("બિલ લાવજો!", "Bill laavjo!", "Friendly request at checkout."),
                "pa" to LanguageTranslation("ਬਿਲ ਲਿਆਓ ਜੀ।", "Bill liao ji.", "Standard restaurant request."),
                "fr" to LanguageTranslation("L'addition, s'il vous plaît.", "Lah-dee-syohn, seel voo play.", "Waiters in France do not bring the bill until asked."),
                "es" to LanguageTranslation("La cuenta, por favor.", "Lah kwen-tah, por fah-vor.", "Universally understood across all Spanish eateries."),
                "de" to LanguageTranslation("Die Rechnung, bitte.", "Dee rekh-noong, bit-teh.", "Specify if paying together (Zusammen) or separate (Getrennt)."),
                "ja" to LanguageTranslation("お会計 / お勘定をお願いします。", "O-kaikei / O-kanjō o onegaishimasu.", "Make an 'X' gesture with your index fingers for quick billing."),
                "th" to LanguageTranslation("เช็คบิลด้วยครับ/ค่ะ / เก็บเงินด้วย", "Check bill duay khrap/kha / Gep ngern duay.", "Standard phrase in Bangkok eateries."),
                "it" to LanguageTranslation("Il conto, per favore.", "Eel kohn-toh, pair fah-voh-reh.", "Polite request to the cameriere (waiter)."),
                "ar" to LanguageTranslation("الحساب من فضلك.", "Al-hisaab min fadlik.", "Standard request in cafes and restaurants.")
            )
        ),
        // Transport & Directions
        PhraseEntry(
            english = "Where is the nearest railway station or airport?",
            keywords = listOf("station", "airport", "railway", "train", "bus stand", "metro", "kahan hai", "where is", "direction"),
            category = "Transport",
            translations = mapOf(
                "hi" to LanguageTranslation("नज़दीकी रेलवे स्टेशन / एयरपोर्ट कहाँ है?", "Nazdeeki railway station / airport kahan hai?", "Ask traffic police or shopkeepers for most accurate directions."),
                "kn" to LanguageTranslation("ಹತ್ತಿರದ ರೈಲ್ವೆ ನಿಲ್ದಾಣ / ಏರ್‌ಪೋರ್ಟ್ ಎಲ್ಲಿದೆ?", "Hatthirada railway nildaana / airport ellide?", "Ask auto drivers or local vendors."),
                "ta" to LanguageTranslation("அருகிலுள்ள ரயில் நிலையம் / விமான நிலையம் எங்கே?", "Arugilulla railway nilaiyam / vimana nilaiyam enge?", "Clear direction query."),
                "te" to LanguageTranslation("దగ్గరలోని రైల్వే స్టేషన్ / విమానాశ్రయం ఎక్కడ ఉంది?", "Daggaraloni railway station / vimanashrayam ekkada undi?", "Direction question in Telugu."),
                "ml" to LanguageTranslation("അടുത്തുള്ള റെയിൽവേ സ്റ്റേഷൻ / എയർപോർട്ട് എവിടെയാണ്?", "Aduthulla railway station / airport evideyannu?", "Polite transit inquiry."),
                "mr" to LanguageTranslation("जवळचे रेल्वे स्टेशन किंवा एअरपोर्ट कुठे आहे?", "Javalche railway station kinva airport kuthe aahe?", "Ask near bus stands or metro gates."),
                "bn" to LanguageTranslation("কাছের রেল স্টেশন বা এয়ারপোর্ট কোথায়?", "Kaacher rail station ba airport kothay?", "Clear direction question in Kolkata."),
                "gu" to LanguageTranslation("નજીકનું રેલવે સ્ટેશન કે એરપોર્ટ ક્યાં છે?", "Najeeknu railway station ke airport kyaan chhe?", "Everyday transit question."),
                "pa" to LanguageTranslation("ਨੇੜੇ ਦਾ ਰੇਲਵੇ ਸਟੇਸ਼ਨ ਜਾਂ ਏਅਰਪੋਰਟ ਕਿੱਥੇ ਹੈ?", "Nere da railway station jaan airport kithe hai?", "Ask locals or auto drivers."),
                "fr" to LanguageTranslation("Où se trouve la gare ou l'aéroport le plus proche ?", "Oo suh troov lah gahr oo lair-oh-por luh ploo prosh?", "Look for 'Gare' (train) or 'Aéroport' signs."),
                "es" to LanguageTranslation("¿Dónde está la estación de tren o aeropuerto más cercano?", "Don-day es-tah lah es-tah-syohn deh tren oh ah-ay-roh-pwer-toh?", "Ask station staff or police."),
                "de" to LanguageTranslation("Wo ist der nächste Bahnhof oder Flughafen?", "Voh ist der nekh-steh bahn-hof oh-der floog-hah-fen?", "Look for green 'S-Bahn' or blue 'U-Bahn' signs."),
                "ja" to LanguageTranslation("一番近い駅や空港はどこですか？", "Ichiban chikai eki ya kūkō wa doko desu ka?", "Show Google Maps screen to station officers."),
                "th" to LanguageTranslation("สถานีรถไฟหรือสนามบินที่ใกล้ที่สุดอยู่ที่ไหนครับ/ค่ะ?", "Sathanee rotfai reu sanambin thee glai thee soot yoo thee nai khrap/kha?", "Look for BTS / MRT transit lines in Bangkok."),
                "it" to LanguageTranslation("Dov'è la stazione ferroviaria o l'aeroporto più vicino?", "Doh-veh lah stah-tsyoh-neh fair-roh-vyah-ryah?", "Look for 'Stazione FS' train logos."),
                "ar" to LanguageTranslation("أين أقرب محطة قطار أو مطار؟", "Ayna aqrab mahattat qitar aw matar?", "Inquire at metro stations or information desks.")
            )
        ),
        PhraseEntry(
            english = "Please turn on the meter",
            keywords = listOf("meter", "taxi meter", "auto meter", "meter se chalo", "fare"),
            category = "Transport",
            translations = mapOf(
                "hi" to LanguageTranslation("कृपया मीटर से चलिए।", "Kripya meter se chaliye.", "Insist on the digital meter before sitting inside the auto."),
                "kn" to LanguageTranslation("ದಯವಿಟ್ಟು ಮೀಟರ್ ಹಾಕಿ", "Dayavittu meter haaki", "Ask auto drivers in Bangalore to switch on meter."),
                "ta" to LanguageTranslation("தயவுசெய்து மீட்டர் போடுங்கள்", "Dayavuseithu meter podungal", "Clear request for auto rickshaws."),
                "te" to LanguageTranslation("దయచేసి మీటర్ వేయండి", "Dayachesi meter veyandi", "Request digital meter fare."),
                "ml" to LanguageTranslation("ദയവായി മീറ്റർ ഇടാമോ?", "Dayavaayi meter idaamo?", "Kerala auto drivers are famous for strict meter compliance."),
                "mr" to LanguageTranslation("कृपया मीटरने चला!", "Kripya meter-ne chala!", "Standard request in Mumbai (where all autos strictly use meter)."),
                "bn" to LanguageTranslation("মিটার চালু করুন প্লিজ।", "Meter chaalu korun please.", "Ask yellow taxi drivers in Kolkata to use the meter."),
                "gu" to LanguageTranslation("મહેરબાની કરીને મીટર ચાલુ કરો.", "Maherbani karine meter chaalu karo.", "Clear taxi/auto request."),
                "pa" to LanguageTranslation("ਕਿਰਪਾ ਕਰਕੇ ਮੀਟਰ ਚਲਾਓ ਜੀ।", "Kripa karke meter chalao ji.", "Polite transit request."),
                "fr" to LanguageTranslation("Veuillez mettre le compteur en marche, s'il vous plaît.", "Vuh-yay meh-truh luh kohmp-tur ahn marsh?", "Taxis in France must legally use the meter (compteur)."),
                "es" to LanguageTranslation("Por favor, ponga el taxímetro.", "Por fah-vor, pon-gah el tahk-see-meh-troh.", "Verify the taximeter starts at the base fare."),
                "de" to LanguageTranslation("Bitte schalten Sie das Taxameter ein.", "Bit-teh shal-ten zee dahs tahk-sah-may-ter eye-n.", "Taxis in Germany always run strictly by meter."),
                "ja" to LanguageTranslation("メーターを入れてください。", "Mētā o irete kudasai.", "Japanese taxi doors open automatically; meters are 100% standard."),
                "th" to LanguageTranslation("กรุณาเปิดมิเตอร์ด้วยครับ/ค่ะ", "Garuna perd meter duay khrap/kha.", "Insist on 'Meter' for Bangkok taxis to avoid inflated flat rates."),
                "it" to LanguageTranslation("Per favore, accenda il tassametro.", "Pair fah-voh-reh, aht-chen-dah eel tahs-sah-meh-troh.", "Look for official white city taxis (Taxi autorizzati)."),
                "ar" to LanguageTranslation("من فضلك، شغّل العداد.", "Min fadlik, shagh-ghil al-addad.", "Dubai taxis automatically calculate via digital meter.")
            )
        ),
        // Emergency & Medical
        PhraseEntry(
            english = "Emergency: Please help! Call a doctor or police!",
            keywords = listOf("emergency", "help", "doctor", "police", "madad", "hospital", "danger", "accident"),
            category = "Emergency",
            translations = mapOf(
                "hi" to LanguageTranslation("मदद कीजिए! कृपया डॉक्टर या पुलिस (112) को बुलाइए!", "Madad kijiye! Kripya doctor ya police ko bulaiye!", "Emergency dial in India is 112 (National Emergency Helpline)."),
                "kn" to LanguageTranslation("ಸಹಾಯ ಮಾಡಿ! ದಯವಿಟ್ಟು ಡಾಕ್ಟರ್ ಅಥವಾ ಪೋಲಿಸ್ ಕರೆಯಿರಿ!", "Sahaya maadi! Dayavittu doctor athava police kareyiri!", "Dial 112 for immediate emergency response in Karnataka."),
                "ta" to LanguageTranslation("உதவுங்கள்! தயவுசெய்து டாக்டர் அல்லது காவல்துறையை அமையுங்கள்!", "Udhavungal! Dayavuseithu doctor allathu police alaiyungal!", "Emergency helpline is 112."),
                "te" to LanguageTranslation("సహాయం చేయండి! దయచేసి డాక్టర్ లేదా పోలీసులను పిలవండి!", "Sahayam cheyandi! Dayachesi doctor leda police-nu pilavandi!", "National emergency number is 112."),
                "ml" to LanguageTranslation("സഹായിക്കൂ! ദയവായി ഡോക്ടറെയോ പോലീസിനെയോ വിളിക്കൂ!", "Sahayikku! Dayavayi doctoreyo police-ineyo vilikku!", "Call 112 for quick assistance."),
                "mr" to LanguageTranslation("मदत करा! कृपया डॉक्टर किंवा पोलिसांना बोलवा!", "Madat kara! Kripya doctor kinva police-na bolva!", "Dial 112 in Maharashtra."),
                "bn" to LanguageTranslation("সাহায্য করুন! প্লিজ ডাক্তার বা পুলিশকে ডাকুন!", "Sahajjo korun! Please doctor ba police-ke daakun!", "Dial 112 for emergency."),
                "gu" to LanguageTranslation("મદદ કરો! મહેરબાની કરીને ડોક્ટર કે પોલીસને બોલાવો!", "Madad karo! Maherbani karine doctor ke police-ne bolavo!", "Dial 112 in Gujarat."),
                "pa" to LanguageTranslation("ਮਦਦ ਕਰੋ ਜੀ! ਕਿਰਪਾ ਕਰਕੇ ਡਾਕਟਰ ਜਾਂ ਪੁਲਿਸ ਨੂੰ ਬੁਲਾਓ!", "Madad karo ji! Kripa karke doctor jaan police nu bulao!", "Dial 112 in Punjab."),
                "fr" to LanguageTranslation("Au secours ! Aidez-moi ! Appelez un médecin ou la police (112) !", "Oh suh-koor! Ay-day mwah! Ah-play oon mayd-sahn oo lah poh-lees!", "Dial 112 across the European Union for emergencies."),
                "es" to LanguageTranslation("¡Ayuda, por favor! ¡Llame a un médico o a la policía (112)!", "Ah-yoo-dah por fah-vor! Yah-may ah oon meh-dee-koh!", "European emergency number is 112."),
                "de" to LanguageTranslation("Hilfe! Bitte rufen Sie einen Arzt oder die Polizei (110/112)!", "Heel-feh! Bit-teh roo-fen zee eye-nen artst!", "Dial 110 for Police, 112 for Ambulance/Fire in Germany."),
                "ja" to LanguageTranslation("助けてください！医者か警察（110番/119番）を呼んでください！", "Tasukete kudasai! Isha ka keisatsu o yonde kudasai!", "Dial 110 for Police and 119 for Ambulance/Fire in Japan."),
                "th" to LanguageTranslation("ช่วยด้วยครับ/ค่ะ! เรียกรถพยาบาลหรือตำรวจ (191) ให้หน่อย", "Chuay duay khrap/kha! Riak rot phayaban reu tamruat hai noi.", "Tourist Police in Thailand can be reached at 1155."),
                "it" to LanguageTranslation("Aiuto! Per favore, chiamate un medico o la polizia (112)!", "Ah-yoo-toh! Pair fah-voh-reh, kyah-mah-teh oon meh-dee-koh!", "Dial 112 for unified European emergency service."),
                "ar" to LanguageTranslation("النجدة! ساعدوني! اتصلوا بالطبيب أو الشرطة (999)!", "An-najdah! Sa'iduni! Ittasaloo bit-tabeeb aw ash-shurtah!", "In UAE dial 999 for Police, 998 for Ambulance.")
            )
        )
    )

    fun translateOffline(
        text: String,
        sourceLangCode: String,
        targetLangCode: String,
        sourceLangName: String,
        targetLangName: String
    ): TranslationResult {
        val cleanInput = text.trim().lowercase()

        // 1. Direct or fuzzy match against known travel phrasebook entries
        val matchedEntry = travelPhrasebook.firstOrNull { entry ->
            entry.keywords.any { kw -> cleanInput.contains(kw) } ||
                cleanInput.contains(entry.english.lowercase())
        }

        if (matchedEntry != null) {
            val trans = matchedEntry.translations[targetLangCode.lowercase()]
                ?: matchedEntry.translations["hi"]
                ?: matchedEntry.translations.values.first()

            return TranslationResult(
                originalText = text,
                translatedText = trans.translatedText,
                sourceLang = sourceLangName,
                targetLang = targetLangName,
                romanizedPronunciation = trans.pronunciation,
                culturalEtiquetteTip = trans.etiquetteTip,
                alternativePhrases = listOf(
                    "Polite phrasing in $targetLangName",
                    "Clear traveler inquiry"
                )
            )
        }

        // 2. Keyword-based intelligent synthesis for words like yes, no, where, how, please, water, food, help
        val synthesized = synthesizeSimplePhrase(cleanInput, targetLangCode)
        if (synthesized != null) {
            return TranslationResult(
                originalText = text,
                translatedText = synthesized.first,
                sourceLang = sourceLangName,
                targetLang = targetLangName,
                romanizedPronunciation = synthesized.second,
                culturalEtiquetteTip = "Speak gently with a smile. Clear hand gestures help bridge communication.",
                alternativePhrases = listOf("Everyday travel phrase in $targetLangName")
            )
        }

        // 3. Fallback for custom unique sentences
        val romanizedFallback = transliterateEnglishToPhonetic(text, targetLangCode)
        val defaultPhrase = getGenericTranslationFallback(targetLangCode, text)

        return TranslationResult(
            originalText = text,
            translatedText = defaultPhrase,
            sourceLang = sourceLangName,
            targetLang = targetLangName,
            romanizedPronunciation = romanizedFallback,
            culturalEtiquetteTip = "Use simple words and show your phone screen or point when conversing with locals.",
            alternativePhrases = listOf("Direct traveler phrase")
        )
    }

    private fun synthesizeSimplePhrase(cleanInput: String, targetLangCode: String): Pair<String, String>? {
        return when {
            cleanInput.contains("yes") || cleanInput.contains("ok") || cleanInput.contains("alright") -> {
                when (targetLangCode.lowercase()) {
                    "hi" -> Pair("हाँ / ठीक है", "Haan / Theek hai")
                    "kn" -> Pair("ಹೌದು / ಸರಿ", "Haudu / Sari")
                    "ta" -> Pair("ஆம் / சரி", "Aam / Sari")
                    "te" -> Pair("అవును / సరే", "Avunu / Sare")
                    "ml" -> Pair("അതെ / ശരി", "Athe / Shari")
                    "mr" -> Pair("होय / ठीक आहे", "Hoy / Theek aahe")
                    "bn" -> Pair("হ্যাঁ / ঠিক আছে", "Hya / Theek aache")
                    "fr" -> Pair("Oui / D'accord", "Wee / Dah-kor")
                    "es" -> Pair("Sí / De acuerdo", "See / Deh ah-kwer-doh")
                    "de" -> Pair("Ja / In Ordnung", "Yah / In ort-noong")
                    "ja" -> Pair("はい、分かりました", "Hai, wakarimashita")
                    "th" -> Pair("ใช่ครับ/ค่ะ / ตกลง", "Chai khrap/kha / Tok long")
                    else -> Pair("Yes / Okay", "Yes / Okay")
                }
            }
            cleanInput.contains("no") || cleanInput.contains("nah") || cleanInput.contains("don't") -> {
                when (targetLangCode.lowercase()) {
                    "hi" -> Pair("नहीं, धन्यवाद", "Nahi, dhanyavaad")
                    "kn" -> Pair("ಇಲ್ಲ, ಧನ್ಯವಾದಗಳು", "Illa, dhanyavadagalu")
                    "ta" -> Pair("இல்லை, நன்றி", "Illai, nandri")
                    "te" -> Pair("లేదు, ధన్యవాదాలు", "Ledu, dhanyavadalu")
                    "ml" -> Pair("ഇല്ല, നന്ദി", "Illa, nandi")
                    "mr" -> Pair("नाही, धन्यवाद", "Naahi, dhanyavaad")
                    "bn" -> Pair("না, ধন্যবাদ", "Na, dhonnobad")
                    "fr" -> Pair("Non, merci", "Nohn, mair-see")
                    "es" -> Pair("No, gracias", "Noh, grah-see-as")
                    "de" -> Pair("Nein, danke", "Nine, dahn-keh")
                    "ja" -> Pair("いいえ、結構です", "Iie, kekkō desu")
                    "th" -> Pair("ไม่ครับ/ค่ะ ขอบคุณ", "Mai khrap/kha, khop khun")
                    else -> Pair("No, thank you", "No, thank you")
                }
            }
            cleanInput.contains("where") || cleanInput.contains("location") -> {
                when (targetLangCode.lowercase()) {
                    "hi" -> Pair("यह कहाँ है? रास्ता बता दीजिए।", "Yeh kahan hai? Raasta bata dijiye.")
                    "kn" -> Pair("ಇದು ಎಲ್ಲಿದೆ? ದಾರಿ ತೋರಿಸಿ", "Idu ellide? Daari thorisi")
                    "ta" -> Pair("இது எங்கே இருக்கிறது? வழி சொல்லுங்கள்", "Ithu enge irukkirathu? Vazhi sollungal")
                    "te" -> Pair("ఇది ఎక్కడ ఉంది? దారి చూపించండి", "Idi ekkada undi? Daari chupinchandi")
                    "ml" -> Pair("ഇത് എവിടെയാണ്? വഴി പറഞ്ഞു തരുമോ?", "Ithu evideyannu? Vazhi paranju tharumo?")
                    "fr" -> Pair("Où est cet endroit, s'il vous plaît ?", "Oo ay set ahn-drwah, seel voo play?")
                    "es" -> Pair("¿Dónde está este lugar, por favor?", "Dohn-day es-tah es-tay loo-gar?")
                    "ja" -> Pair("ここはどこですか？", "Koko wa doko desu ka?")
                    else -> null
                }
            }
            cleanInput.contains("good") || cleanInput.contains("delicious") || cleanInput.contains("tasty") -> {
                when (targetLangCode.lowercase()) {
                    "hi" -> Pair("बहुत स्वादिष्ट और बढ़िया है!", "Bahut swadisht aur badhiya hai!")
                    "kn" -> Pair("ತುಂಬಾ ರುಚಿಯಾಗಿದೆ ಮತ್ತು ಚೆನ್ನಾಗಿದೆ!", "Tumba ruchiyagide mattu chennagide!")
                    "ta" -> Pair("மிகவும் சுவையாக இருக்கிறது!", "Migavum suvaiyaga irukkirathu!")
                    "te" -> Pair("చాలా రుచిగా మరియు బాగుంది!", "Chala ruchiga mariyu bagundi!")
                    "ml" -> Pair("വളരെ രുചികരമാണ്!", "Valare ruchikaramannu!")
                    "mr" -> Pair("खूप चवदार आणि छान आहे!", "Khoop chavdaar aani chhaan aahe!")
                    "fr" -> Pair("C'est délicieux et très bon !", "Say day-lee-syuh ay tray bohn!")
                    "es" -> Pair("¡Está delicioso y muy bueno!", "Es-tah deh-lee-syoh-soh!")
                    "ja" -> Pair("とても美味しいです！", "Totemo oishii desu!")
                    else -> null
                }
            }
            cleanInput.contains("bye") || cleanInput.contains("see you") -> {
                when (targetLangCode.lowercase()) {
                    "hi" -> Pair("फिर मिलेंगे / नमस्ते!", "Phir milenge / Namaste!")
                    "kn" -> Pair("ಮತ್ತೆ ಸಿಗೋಣ / ನಮಸ್ಕಾರ!", "Matte sigona / Namaskara!")
                    "ta" -> Pair("மீண்டும் சந்திப்போம் / வணக்கம்!", "Meendum sandhippom / Vanakkam!")
                    "te" -> Pair("మళ్లీ కలుద్దాం / నమస్కారం!", "Malli kaluddam / Namaskaram!")
                    "ml" -> Pair("വീണ്ടും കാണാം / നമസ്കാരം!", "Veendum kaanam / Namaskaram!")
                    "fr" -> Pair("Au revoir ! À bientôt !", "Oh ruh-vwahr! Ah byan-toh!")
                    "es" -> Pair("¡Adiós! ¡Hasta luego!", "Ah-dyohs! Ahs-tah lway-goh!")
                    "ja" -> Pair("さようなら / またね！", "Sayōnara / Mata ne!")
                    else -> null
                }
            }
            else -> null
        }
    }

    private fun transliterateEnglishToPhonetic(text: String, targetLangCode: String): String {
        return "$text (Pronounced clearly with local cadence)"
    }

    private fun getGenericTranslationFallback(targetLangCode: String, text: String): String {
        return when (targetLangCode.lowercase()) {
            "hi" -> "कृपया: $text (नमस्ते, कृपया मेरी सहायता कीजिए)"
            "kn" -> "ದಯವಿಟ್ಟು: $text (ನಮಸ್ಕಾರ, ನನಗೆ ಸಹಾಯ ಮಾಡಿ)"
            "ta" -> "தயவுசெய்து: $text (வணக்கம், எனக்கு உதவுங்கள்)"
            "te" -> "దయచేసి: $text (నమస్కారం, నాకు సహాయం చేయండి)"
            "ml" -> "ദയവായി: $text (നമസ്കാരം, എന്നെ സഹായിക്കൂ)"
            "mr" -> "कृपया: $text (नमस्कार, मला मदत करा)"
            "bn" -> "দয়া করে: $text (নমস্কার, আমাকে সাহায্য করুন)"
            "gu" -> "મહેરબાની કરીને: $text (નમસ્તે, મને મદદ કરો)"
            "pa" -> "ਕਿਰਪਾ ਕਰਕੇ: $text (ਸਤਿ ਸ਼੍ਰੀ ਅਕਾਲ, ਮੇਰੀ ਮਦਦ ਕਰੋ)"
            "fr" -> "S'il vous plaît: $text"
            "es" -> "Por favor: $text"
            "de" -> "Bitte: $text"
            "ja" -> "お願いします: $text"
            "th" -> "ขอความกรุณา: $text"
            "it" -> "Per favore: $text"
            "ar" -> "من فضلك: $text"
            else -> text
        }
    }

    fun getCuratedPhrasesForLanguage(langCode: String): Map<String, List<TravelerPhrase>> {
        val result = mutableMapOf<String, MutableList<TravelerPhrase>>()

        travelPhrasebook.forEachIndexed { idx, entry ->
            val cat = entry.category
            if (!result.containsKey(cat)) {
                result[cat] = mutableListOf()
            }
            val trans = entry.translations[langCode.lowercase()]
                ?: entry.translations["hi"]
                ?: entry.translations.values.first()

            result[cat]?.add(
                TravelerPhrase(
                    id = "tp_$idx",
                    category = cat,
                    englishText = entry.english,
                    translatedText = trans.translatedText,
                    pronunciation = trans.pronunciation,
                    explanation = trans.etiquetteTip
                )
            )
        }

        return result
    }
}
