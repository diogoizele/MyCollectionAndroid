package com.diogo.mycollection.core.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.diogo.mycollection.R
import kotlin.math.roundToInt
import kotlin.math.max
import kotlin.math.min

class StarRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var starDrawableEmpty: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_star_solid_full)!!
    private var starDrawableFilled: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_star_solid_full)!!

    private var starSizePx = dpToPx(24f).toInt()
    private var starSpacingPx = dpToPx(8f).toInt()
    private var maxRating = 5
    private var step = 0.1f
    private var rating = 1.0f

    private val starBounds = Rect()

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.StarRatingView, 0, 0)
            starSizePx = a.getDimensionPixelSize(R.styleable.StarRatingView_starSize, starSizePx)
            starSpacingPx = a.getDimensionPixelSize(R.styleable.StarRatingView_starSpacing, starSpacingPx)
            maxRating = a.getInt(R.styleable.StarRatingView_maxRating, maxRating)
            step = a.getFloat(R.styleable.StarRatingView_step, step)
            rating = a.getFloat(R.styleable.StarRatingView_initialRating, rating)
            a.recycle()
        }
        // tint: empty gray, filled yellow
        starDrawableEmpty = starDrawableEmpty.mutate()
        starDrawableEmpty.setTint(0xFFB0B0B0.toInt()) // cinza claro

        starDrawableFilled = starDrawableFilled.mutate()
        starDrawableFilled.setTint(0xFFFFD700.toInt()) // amarelo/dourado

        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = starSizePx * maxRating + starSpacingPx * (maxRating - 1)
        val h = starSizePx
        setMeasuredDimension(resolveSize(w, widthMeasureSpec), resolveSize(h, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var x = 0
        for (i in 0 until maxRating) {
            val left = x
            val top = 0
            val right = left + starSizePx
            val bottom = top + starSizePx
            starBounds.set(left, top, right, bottom)

            // draw empty star
            starDrawableEmpty.bounds = starBounds
            starDrawableEmpty.draw(canvas)

            // compute portion to fill for this star
            val starIndex = i + 1
            val portion = computePortionForStar(starIndex.toFloat(), rating)

            if (portion > 0f) {
                canvas.save()
                // clip horizontal porção dentro do bounds da estrela
                val clipRight = left + (portion * starSizePx)
                canvas.clipRect(left.toFloat(), top.toFloat(), clipRight.toFloat(), bottom.toFloat())
                starDrawableFilled.bounds = starBounds
                starDrawableFilled.draw(canvas)
                canvas.restore()
            }
            x += starSizePx + starSpacingPx
        }
    }

    private fun computePortionForStar(starIndex: Float, rating: Float): Float {
        // starIndex é 1..maxRating
        val lower = starIndex - 1f
        val diff = rating - lower
        return when {
            rating >= starIndex -> 1f
            diff <= 0f -> 0f
            else -> diff.coerceIn(0f, 1f)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val x = event.x.coerceIn(0f, width.toFloat())
                val rawValue = (x / width.toFloat()) * maxRating
                // quantize ao passo (ex.: 0.1)
                val steps = (rawValue / step).roundToInt()
                var newRating = steps * step
                // garantir intervalo 1..maxRating
                newRating = max(1.0f, min(maxRating.toFloat(), newRating))
                if (newRating != rating) {
                    rating = newRating
                    invalidate()
                    listener?.onRatingChange(rating)
                }
            }
            MotionEvent.ACTION_UP -> performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    // API pública
    fun setRating(value: Float) {
        val v = value.coerceIn(1f, maxRating.toFloat())
        rating = ((v / step).roundToInt() * step).coerceIn(1f, maxRating.toFloat())
        invalidate()
    }

    fun getRating(): Float = rating

    interface RatingChangeListener { fun onRatingChange(newRating: Float) }
    private var listener: RatingChangeListener? = null
    fun setOnRatingChangeListener(l: RatingChangeListener) { listener = l }

    private fun dpToPx(dp: Float): Float = dp * context.resources.displayMetrics.density
}
