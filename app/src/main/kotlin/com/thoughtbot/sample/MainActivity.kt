package com.thoughtbot.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.thoughtbot.sample.extensions.brusherFont
import com.thoughtbot.sample.extensions.find
import com.thoughtbot.stencil.StencilView

class MainActivity : AppCompatActivity() {

  val regular: StencilView by lazy { find<StencilView>(R.id.stencil_sample) }
  val customFont: StencilView by lazy { find<StencilView>(R.id.stencil_sample_with_custom_font) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    //set custom font
    customFont.paint.typeface = brusherFont
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_play, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.play -> {
        regular.animatePath()
        customFont.animatePath()

      }
    }
    return super.onOptionsItemSelected(item)
  }
}
