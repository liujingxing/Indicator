package com.ljx.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import com.ljx.indicator.R

/**
 * User: ljx
 * Date: 2023/12/24
 * Time: 14:57
 */
class IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val WORM = 0
        const val SMOOTH = 1
        const val SCALE = 2

        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    @IntDef(HORIZONTAL, VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
    annotation class Orientation

    @IntDef(WORM, SMOOTH, SCALE)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
    annotation class SlideMode


    private var gravity: Int

    @Orientation
    private var orientation: Int

    @SlideMode
    private var sliderMode: Int
    private var sliderCount: Int
    private var normalColor: Int
    private var checkedColor: Int
    private var sliderGap: Float
    private var roundRadius: Float
    private var primarySize: Float
    private var secondarySize: Float
    private var primaryCheckedSize: Float
    private var secondaryCheckedSize: Float
    private var reverseLayout = false

    private var shouldReverseLayout = false
    private var shouldUpdateLayoutState = false

    private var position = 0
    private var positionOffset = 0.0f
    private val rectF = RectF()
    private var paint = Paint()
    private val layoutState = LayoutState()
    private val argbEvaluator: ArgbEvaluator by lazy { ArgbEvaluator() }

    init {
        paint.isAntiAlias = true
        val dp5 = 5f * context.resources.displayMetrics.density
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView).also {
            gravity = it.getInt(R.styleable.IndicatorView_android_gravity, 0)
            orientation = it.getInt(R.styleable.IndicatorView_android_orientation, HORIZONTAL)
            reverseLayout = it.getBoolean(R.styleable.IndicatorView_slider_reverseLayout, false)
            sliderGap = it.getDimension(R.styleable.IndicatorView_slider_gap, dp5)
            primarySize = it.getDimension(R.styleable.IndicatorView_slider_primary_size, dp5)
            secondarySize = it.getDimension(R.styleable.IndicatorView_slider_secondary_size, dp5)
            primaryCheckedSize = it.getDimension(R.styleable.IndicatorView_slider_primary_checked_size, primarySize)
            secondaryCheckedSize = it.getDimension(R.styleable.IndicatorView_slider_secondary_checked_size, secondarySize)
            val radius = it.getDimension(R.styleable.IndicatorView_slider_round_radius, -1.0f)
            roundRadius = if (radius >= 0) radius else calculateSecondaryUsedSize() / 2f
            sliderMode = it.getInt(R.styleable.IndicatorView_slide_mode, WORM)
            sliderCount = it.getInt(R.styleable.IndicatorView_slider_count, 0)
            normalColor = it.getColor(R.styleable.IndicatorView_slider_normal_color, Color.DKGRAY)
            checkedColor = it.getColor(R.styleable.IndicatorView_slider_checked_color, Color.WHITE)
        }
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)

        val specHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        val minWidth: Int
        val minHeight: Int
        if (orientation == HORIZONTAL) {
            minWidth = paddingLeft + paddingRight + calculatePrimaryUsedSize().toInt()
            minHeight = paddingTop + paddingBottom + calculateSecondaryUsedSize().toInt()
        } else {
            minWidth = paddingLeft + paddingRight + calculateSecondaryUsedSize().toInt()
            minHeight = paddingTop + paddingBottom + calculatePrimaryUsedSize().toInt()
        }
        val width = when (specWidthMode) {
            MeasureSpec.EXACTLY -> specWidthSize
            else -> minWidth.coerceAtMost(specWidthSize).coerceAtLeast(suggestedMinimumWidth)
        }
        val height = when (specHeightMode) {
            MeasureSpec.EXACTLY -> specHeightSize
            else -> minHeight.coerceAtMost(specHeightSize).coerceAtLeast(suggestedMinimumHeight)
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        shouldUpdateLayoutState = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sliderCount <= 0) return
        if (shouldUpdateLayoutState) {
            resolveShouldLayoutReverse()
            updateLayoutState()
            shouldUpdateLayoutState = false
        }
        val saveCount = canvas.save()
        canvas.clipRect(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
        if (sliderMode == SCALE) {
            drawScaleSlide(canvas)
        } else {
            drawNormal(canvas)
            if (sliderMode == WORM) {
                drawWormSlider(canvas)
            } else if (sliderMode == SMOOTH) {
                drawSmoothSlider(canvas)
            }
        }
        canvas.restoreToCount(saveCount)
    }

    fun setReverseLayout(reverseLayout: Boolean) {
        if (reverseLayout == this.reverseLayout) return
        this.reverseLayout = reverseLayout
        requestLayout()
        invalidate()
    }

    fun setOrientation(@Orientation orientation: Int) {
        if (this.orientation == orientation) return
        this.orientation = orientation
        requestLayout()
        invalidate()
    }

    fun setSliderCount(@IntRange(from = 0) slideCount: Int) {
        if (this.sliderCount == slideCount) return
        this.sliderCount = slideCount
        requestLayout()
        invalidate()
    }

    fun setPrimarySize(@Px @FloatRange(from = 0.0) size: Float) {
        if (primarySize == size) return
        primarySize = size
        requestLayout()
        invalidate()
    }

    fun setPrimaryCheckedSize(@Px @FloatRange(from = 0.0) size: Float) {
        if (primaryCheckedSize == size) return
        primaryCheckedSize = size
        requestLayout()
        invalidate()
    }

    fun setSecondarySize(@Px @FloatRange(from = 0.0) size: Float) {
        if (secondarySize == size) return
        secondarySize = size
        requestLayout()
        invalidate()
    }

    fun setSecondaryCheckedSize(@Px @FloatRange(from = 0.0) size: Float) {
        if (secondaryCheckedSize == size) return
        secondaryCheckedSize = size
        requestLayout()
        invalidate()
    }

    fun setSliderGap(@Px size: Float) {
        if (sliderGap == size) return
        sliderGap = size
        requestLayout()
        invalidate()
    }

    fun setSliderMode(@SlideMode sliderMode: Int) {
        if (this.sliderMode == sliderMode) return
        this.sliderMode = sliderMode
        invalidate()
    }

    fun setGravity(newGravity: Int) {
        if (gravity == newGravity) return
        gravity = newGravity
        shouldUpdateLayoutState = true
        invalidate()
    }

    fun setRoundRadius(@FloatRange(from = 0.0) radius: Float) {
        if (roundRadius == radius) return
        roundRadius = radius
        invalidate()
    }

    fun scrollSlider(
        @IntRange(from = 0) position: Int,
        @FloatRange(from = 0.0, to = 1.0, toInclusive = false) positionOffset: Float
    ) {
        if (this.position == position && this.positionOffset == positionOffset) return
        this.position = position
        this.positionOffset = positionOffset
        invalidate()
    }

    fun setSliderNormalColor(@ColorInt color: Int) {
        if (normalColor == color) return
        normalColor = color
        invalidate()
    }

    fun setSliderCheckedColor(@ColorInt color: Int) {
        if (checkedColor == color) return
        checkedColor = color
        invalidate()
    }

    private fun resolveShouldLayoutReverse() {
        // A == B is the same result, but we rather keep it readable
        shouldReverseLayout =
            if (orientation == VERTICAL || !isLayoutRTL()) reverseLayout else !reverseLayout
    }

    private fun isLayoutRTL(): Boolean {
        return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
    }

    private fun calculatePrimaryUsedSize(): Float {
        if (sliderCount == 0) return 0f
        return primarySize * (sliderCount - 1) + primarySize.coerceAtLeast(primaryCheckedSize) + sliderGap * (sliderCount - 1)
    }

    private fun calculateSecondaryUsedSize(): Float {
        return secondarySize.coerceAtLeast(secondaryCheckedSize)
    }

    private fun drawScaleSlide(canvas: Canvas) {
        var start = layoutState.primaryStart
        val itemDirection = layoutState.itemDirection
        val color = argbEvaluator.evaluate(positionOffset, checkedColor, normalColor) as Int
        val secondaryDiffSize = secondaryCheckedSize - secondarySize
        val secondaryCenterLine = layoutState.secondaryCenterLine
        for (i in 0 until sliderCount) {
            var offset = primarySize
            var secondarySize = this.secondarySize
            if (i == position) {
                paint.color = color
                offset += (primaryCheckedSize - primarySize) * (1 - positionOffset)
                secondarySize += secondaryDiffSize * (1 - positionOffset)
            } else if (i == position + 1) {
                paint.color = normalColor + checkedColor - color
                offset += (primaryCheckedSize - primarySize) * positionOffset
                secondarySize += secondaryDiffSize * positionOffset
            } else {
                paint.color = normalColor
            }
            val end = start + offset * itemDirection
            val secondaryStart = secondaryCenterLine - secondarySize / 2f
            val secondaryEnd = secondaryStart + secondarySize
            setRectF(start, end, secondaryStart, secondaryEnd)
            canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint)
            start = end + sliderGap * itemDirection
        }
    }

    private fun drawNormal(canvas: Canvas) {
        paint.color = normalColor
        var start = layoutState.primaryStart
        val itemDirection = layoutState.itemDirection
        val secondaryCenterLine = layoutState.secondaryCenterLine
        val secondaryStart = secondaryCenterLine - secondarySize / 2f
        val secondaryEnd = secondaryStart + secondarySize
        for (i in 0 until sliderCount) {
            val end = start + primarySize * itemDirection
            setRectF(start, end, secondaryStart, secondaryEnd)
            canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint)
            val diff = if (i == position) (primaryCheckedSize - primarySize).coerceAtLeast(0f) else 0f
            val offset = sliderGap + diff
            start = end + offset * itemDirection
        }
    }

    private fun drawWormSlider(canvas: Canvas) {
        paint.color = checkedColor
        var start = layoutState.primaryStart
        val itemDirection = layoutState.itemDirection
        val secondaryCheckedSize = secondaryCheckedSize
        val slideProgress = positionOffset
        val currentPosition = position
        val distance = sliderGap + primarySize
        val offset = distance * currentPosition +
                (distance * (slideProgress - 0.5f) * 2.0f).coerceAtLeast(0f) +
                ((primarySize - primaryCheckedSize) / 2f).coerceAtLeast(0f)
        start += offset * itemDirection
        val sliderSize = primaryCheckedSize +
                distance * 2f * (if (slideProgress > 0.5) 1 - slideProgress else slideProgress)
        val end = start + sliderSize * itemDirection
        val secondaryCenterLine = layoutState.secondaryCenterLine
        val secondaryStart = secondaryCenterLine - secondaryCheckedSize / 2f
        val secondaryEnd = secondaryStart + secondaryCheckedSize
        setRectF(start, end, secondaryStart, secondaryEnd)
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint)
    }

    private fun drawSmoothSlider(canvas: Canvas) {
        paint.color = checkedColor
        var start = layoutState.primaryStart
        val itemDirection = layoutState.itemDirection
        val currentPosition = position
        val primaryCheckedSize = primaryCheckedSize
        val secondaryCheckedSize = secondaryCheckedSize
        val distance = sliderGap + primarySize
        val offset = distance * currentPosition + distance * positionOffset +
                ((primarySize - primaryCheckedSize) / 2f).coerceAtLeast(0f)
        start += offset * itemDirection
        val end = start + primaryCheckedSize * itemDirection
        val secondaryCenterLine = layoutState.secondaryCenterLine
        val secondaryStart = secondaryCenterLine - secondaryCheckedSize / 2f
        val secondaryEnd = secondaryStart + secondaryCheckedSize
        setRectF(start, end, secondaryStart, secondaryEnd)
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint)
    }

    private fun setRectF(start: Float, end: Float, secondaryStart: Float, secondaryEnd: Float) {
        if (orientation == HORIZONTAL) {
            if (shouldReverseLayout) {
                rectF.set(end, secondaryStart, start, secondaryEnd)
            } else {
                rectF.set(start, secondaryStart, end, secondaryEnd)
            }
        } else {
            if (shouldReverseLayout) {
                rectF.set(secondaryStart, end, secondaryEnd, start)
            } else {
                rectF.set(secondaryStart, start, secondaryEnd, end)
            }
        }
    }

    private fun updateLayoutState() {
        val layoutDirection = ViewCompat.getLayoutDirection(this)
        val absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection)
        val horizontalGravity = absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK
        val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
        val primaryUsedSize = calculatePrimaryUsedSize()
        val secondaryUsedSize = calculateSecondaryUsedSize()

        val left = paddingLeft.toFloat()
        val top = paddingTop.toFloat()
        val right = width - paddingRight.toFloat()
        val bottom = height - paddingBottom.toFloat()

        val primaryStart: Float
        val secondaryStart: Float
        if (orientation == HORIZONTAL) {
            primaryStart = if (horizontalGravity == Gravity.CENTER_HORIZONTAL) {
                val offset = (right - left - primaryUsedSize) / 2f
                if (shouldReverseLayout) right - offset else left + offset
            } else if (horizontalGravity == Gravity.LEFT) {
                if (shouldReverseLayout) left + primaryUsedSize else left
            } else if (horizontalGravity == Gravity.RIGHT) {
                if (shouldReverseLayout) right else right - primaryUsedSize
            } else {
                if (shouldReverseLayout) right else left
            }

            secondaryStart = if (verticalGravity == Gravity.CENTER_VERTICAL) {
                top + (bottom - top - secondaryUsedSize) / 2f
            } else if (verticalGravity == Gravity.BOTTOM) {
                bottom - secondaryUsedSize
            } else {
                top
            }
        } else {
            primaryStart = if (verticalGravity == Gravity.CENTER_VERTICAL) {
                val offset = (bottom - top - primaryUsedSize) / 2f
                if (shouldReverseLayout) bottom - offset else top + offset
            } else if (verticalGravity == Gravity.TOP) {
                if (shouldReverseLayout) top + primaryUsedSize else top
            } else if (verticalGravity == Gravity.BOTTOM) {
                if (shouldReverseLayout) bottom else bottom - primaryUsedSize
            } else {
                if (shouldReverseLayout) bottom else top
            }

            secondaryStart = if (horizontalGravity == Gravity.CENTER_HORIZONTAL) {
                left + (right - left - secondaryUsedSize) / 2f
            } else if (horizontalGravity == Gravity.LEFT) {
                left
            } else if (horizontalGravity == Gravity.RIGHT) {
                right - secondaryUsedSize
            } else {
                if (isLayoutRTL()) right - secondaryUsedSize else left
            }
        }
        layoutState.primaryStart = primaryStart
        layoutState.secondaryCenterLine = secondaryStart +
                (secondarySize.coerceAtLeast(secondaryCheckedSize)) / 2f
        layoutState.itemDirection =
            if (shouldReverseLayout) LayoutState.ITEM_DIRECTION_HEAD else LayoutState.ITEM_DIRECTION_TAIL
    }

    internal class LayoutState {
        var primaryStart = 0f
        var secondaryCenterLine = 0f
        var itemDirection = 1

        companion object {
            const val ITEM_DIRECTION_HEAD = -1
            const val ITEM_DIRECTION_TAIL = 1
        }
    }
}