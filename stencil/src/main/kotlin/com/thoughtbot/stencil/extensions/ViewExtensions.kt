package com.thoughtbot.stencil.extensions

import android.view.View
import android.view.View.MeasureSpec

fun View.reconcileSize(contentSize: Int, measureSpec: Int): Int {
  val mode = MeasureSpec.getMode(measureSpec)
  val specSize = MeasureSpec.getSize(measureSpec)

  when (mode) {
    MeasureSpec.EXACTLY -> return specSize
    MeasureSpec.AT_MOST -> return if (contentSize < specSize) contentSize else specSize
    else -> return contentSize
  }

}

