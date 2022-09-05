package com.vahitkeskin.jetpackcomposegithubuserinfo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

fun String.goToLink(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(this)
    intent.setPackage("com.android.chrome")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        intent.setPackage(null)
        context.startActivity(intent)
    }
}

fun String.updatedOn(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    val date = sdf.parse(this.replace("Z",".000Z").replace("Z".toRegex(), "+0000"))
    return SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date)
}