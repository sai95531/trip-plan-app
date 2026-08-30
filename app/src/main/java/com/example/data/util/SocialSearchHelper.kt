package com.example.data.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object SocialSearchHelper {

    fun openYouTube(context: Context, query: String) {
        try {
            val cleanQuery = Uri.encode("$query travel vlog 4k tour")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$cleanQuery")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openGoogleSearch(context, "$query youtube travel vlog")
        }
    }

    fun openYouTubeFood(context: Context, foodName: String, eatery: String, city: String) {
        try {
            val cleanQuery = Uri.encode("$foodName $eatery $city street food review vlog")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$cleanQuery")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openGoogleSearch(context, "$foodName $eatery $city food review youtube")
        }
    }

    fun openInstagram(context: Context, query: String) {
        try {
            val cleanTag = query.replace(" ", "").replace("&", "").replace("-", "").lowercase()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/explore/tags/$cleanTag/")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openGoogleSearch(context, "$query instagram reels aesthetic photo spots")
        }
    }

    fun openGoogleMaps(context: Context, placeName: String, destinationName: String) {
        try {
            val query = Uri.encode("$placeName, $destinationName")
            val uri = Uri.parse("geo:0,0?q=$query")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode("$placeName, $destinationName")}")
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }

    fun openGoogleSearch(context: Context, query: String) {
        try {
            val uri = Uri.parse("https://www.google.com/search?q=${Uri.encode(query)}")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
        }
    }

    fun openFacebookTravel(context: Context, query: String) {
        try {
            val uri = Uri.parse("https://www.facebook.com/search/top?q=${Uri.encode("$query travel recommendations guide")}")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openGoogleSearch(context, "$query facebook traveler forum tips")
        }
    }

    fun openGoogleTranslate(context: Context, text: String, sourceLang: String = "auto", targetLang: String = "en") {
        try {
            val uri = Uri.parse("https://translate.google.com/?sl=$sourceLang&tl=$targetLang&text=${Uri.encode(text)}&op=translate")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            openGoogleSearch(context, "translate $text to $targetLang")
        }
    }
}
