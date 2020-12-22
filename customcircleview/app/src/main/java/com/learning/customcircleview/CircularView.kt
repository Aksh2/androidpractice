package com.learning.customcircleview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircularView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var circleColor: Int = 0
        set(value) {
            field = value
            this.invalidate()
            this.requestLayout()
        }
    var labelColor: Int = 0
        set(value) {
            field = value
            this.invalidate()
            this.requestLayout()
        }
    var circleText: String = ""
        set(value) {
            field = value
            this.invalidate()
            this.requestLayout()
        }
    private var circlePaint: Paint = Paint()



    init {
        val attributes = context?.theme?.obtainStyledAttributes(attrs,
        R.styleable.CircularView,0,0)

        try{
            circleColor = attributes?.getInt(R.styleable.CircularView_circleColor,0)!!
            labelColor = attributes.getInt(R.styleable.CircularView_labelColor,0)
            circleText = attributes.getString(R.styleable.CircularView_circleLabel)!!

        } finally{
            attributes?.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val viewWidthHalf: Float = (measuredWidth/2).toFloat()
        val viewHeightHalf: Float = (measuredHeight/2).toFloat()
        var radius = 0F
        if(viewWidthHalf > viewHeightHalf){
            radius = viewHeightHalf-10
        } else {
            radius = viewWidthHalf - 10F
        }
        circlePaint.style = Paint.Style.FILL
        circlePaint.isAntiAlias = true
        circlePaint.color = circleColor
        canvas?.drawCircle(viewWidthHalf as Float, viewHeightHalf as Float, radius as Float, circlePaint)

        // drawing the text
        circlePaint.setColor(labelColor)
        circlePaint.textAlign = Paint.Align.CENTER
        circlePaint.textSize = 50F
        canvas?.drawText(circleText, viewWidthHalf as Float, viewHeightHalf as Float, circlePaint)
    }



}