package com.thoughtbot.stencil

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.thoughtbot.stencil.extensions.reconcileSize

class StencilView : View {

  companion object {
    val DEFAULT_COLOR = Color.BLACK
    val DEFAULT_TEXT_SIZE = 64f
    val DEFAULT_STROKE_WIDTH = 10f
  }

  val paint = Paint()
  var text: String? = null
    set(value) {
      field = value
      invalidate()
    }
  val bounds: RectF by lazy { RectF(0f, 0f, width.toFloat(), height.toFloat()) }

  var strokeColor = DEFAULT_COLOR
  var strokeWidth = DEFAULT_STROKE_WIDTH
  var textSize = DEFAULT_TEXT_SIZE
    set(value) {
      field = value * resources.displayMetrics.scaledDensity
    }

  private var pathLength = 0f
  private var path: Path = Path()
    set(value) {
      field = value
      setPathLength()
    }

  private var progress = 0f
    set(value) {
      field = value
      invalidate()
    }

  constructor(context: Context) : super(context) {
    init(context)
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init(context, attrs)
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr) {
    init(context, attrs)
  }

  fun init(context: Context, attrs: AttributeSet? = null) {
    if (attrs != null) {
      val ta = context.obtainStyledAttributes(attrs, R.styleable.StencilView)
      strokeColor = ta.getColor(R.styleable.StencilView_sv_strokeColor, strokeColor)
      strokeWidth = ta.getFloat(R.styleable.StencilView_sv_strokeWidth, strokeWidth)
      textSize = ta.getDimension(R.styleable.StencilView_sv_textSize, textSize)
      text = ta.getText(R.styleable.StencilView_sv_text)?.toString()
      ta.recycle()
    }

    //configurable
    paint.color = strokeColor
    paint.strokeWidth = strokeWidth
    paint.textSize = textSize

    //non configurable
    paint.isAntiAlias = true
    paint.flags = Paint.LINEAR_TEXT_FLAG
    paint.style = Paint.Style.STROKE
    paint.textAlign = Paint.Align.CENTER

  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    path = makeTextPath()

    val pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength),
        (pathLength - pathLength * progress))
    paint.pathEffect = pathEffect

    canvas?.save()
    canvas?.translate(paddingLeft.toFloat(), paddingTop.toFloat())
    canvas?.drawPath(path, paint)
    canvas?.restore()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    val fontMetric = paint.fontMetrics

    val maxTextWidth = if (text != null) paint.measureText(text) else 0f
    val maxTextHeight = -fontMetric.top + fontMetric.bottom

    val desiredWidth = Math.round(maxTextWidth + paddingLeft + paddingRight)
    val desiredHeight = Math.round(maxTextHeight + paddingTop + paddingBottom)

    val measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec)
    val measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec)

    setMeasuredDimension(measuredWidth, measuredHeight)
  }

  private fun makeTextPath(): Path {
    text?.let {
      val textHeight = paint.descent() - paint.ascent()
      val textOffset = (textHeight / 2) - paint.descent()
      paint.getTextPath(it, 0, it.length, bounds.centerX(), bounds.centerY() + textOffset, path)
    }
    return path
  }

  private fun setPathLength() {
    val measure = PathMeasure(path, true)
    while (true) {
      pathLength = Math.max(pathLength, measure.length)
      if (!measure.nextContour()) {
        break
      }
    }
  }

  fun animatePath() {
    val animator = ObjectAnimator.ofFloat(this, "progress", 0f, 1f)
    animator.duration = 2500
    animator.start()
  }
}

