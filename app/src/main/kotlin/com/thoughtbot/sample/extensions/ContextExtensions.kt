package com.thoughtbot.sample.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.View
import uk.co.chrisjenx.calligraphy.TypefaceUtils

inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id) as T

val Context.brusherFont: Typeface
  get() = getFont("Brusher-Regular.otf")

fun Context.getFont(filePath: String): Typeface {
  return TypefaceUtils.load(this.assets, filePath)
}



