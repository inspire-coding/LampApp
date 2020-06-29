package com.inspirecoding.lampapp

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class LampView(context: Context, attrs: AttributeSet): View(context, attrs)
{
    companion object {
        const val turnedOff = 0L
        const val turnedOn = 1L
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var borderColor = Color.BLACK

    private var size = 0
    
    private var center_x: Float = 0f
    private var center_y: Float = 0f

    private var startY = 0f
    private var endY = 0f
    private var startX_leftVeticalLine = 0f
    private var endX_leftVeticalLine = 0f
    private var startX_rightVeticalLine = 0f
    private var endX_rightVeticalLine = 0f
    
    private var radius: Float = 0f
    val border: Float = 10f

    //BORDER
    val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG)
    //FILL
    val paintFill = Paint()

    var state = turnedOff
        set(state) {
            field = state
            invalidate()
        }

    init {
        paint.isAntiAlias = true
        setXmlAttributes(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGlassBulb(canvas)
        initStartAndEndPoints()
        drawSocketFill(canvas)
        drawSocket(canvas)
    }
    private fun setXmlAttributes(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.LampView, 0, 0)

        state = typedArray.getInt(R.styleable.LampView_state, turnedOn.toInt()).toLong()

        typedArray.recycle()
    }
    private fun initStartAndEndPoints() {
        startY = (sin(Math.toRadians(125.0)) * radius + center_y - border/2f).toFloat()
        endY = (size*0.9).toFloat()

        startX_leftVeticalLine = (cos(Math.toRadians(125.0)) * radius + center_x).toFloat()
        endX_leftVeticalLine = (cos(Math.toRadians((125.0))) * radius + center_x).toFloat()

        startX_rightVeticalLine = (cos(Math.toRadians(305.0)) * radius + center_x).toFloat()
        endX_rightVeticalLine = (cos(Math.toRadians((305.0))) * radius + center_x).toFloat()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
    private fun drawGlassBulb(canvas: Canvas) {
        paintBorder.color = borderColor
        paintBorder.strokeWidth = border
        paintBorder.style = Paint.Style.STROKE

        if(state == turnedOff)
        {
            paintFill.color = Color.WHITE
            paintFill.strokeWidth = border
            paintFill.style = Paint.Style.FILL
        }
        else
        {
            paintFill.color = Color.YELLOW
            paintFill.strokeWidth = border
            paintFill.style = Paint.Style.FILL
        }

        radius = size / 3f

        val oval = RectF()

        center_x = size / 2f
        center_y = size / 3f

        oval.set(
            center_x - radius + border/2f,
            center_y - radius + border/2f,
            center_x + radius - border/2f,
            center_y + radius - border/2f
        )

        //FILL
        canvas.drawCircle(center_x, center_y, radius, paintFill)

        // Circle Arc Line
        canvas.drawArc(oval, 125f, 290f, false, paintBorder)
    }
    private fun drawSocket(canvas: Canvas) {
        paintBorder.color = borderColor
        paintBorder.strokeWidth = border
        paintBorder.style = Paint.Style.STROKE

        //BORDER
        //Left vertical line
        canvas.drawLine(
            startX_leftVeticalLine,
            startY,
            endX_leftVeticalLine,
            endY, paintBorder)
        //Right vertical line
        canvas.drawLine(
            startX_rightVeticalLine,
            startY,
            endX_rightVeticalLine,
            endY, paintBorder)

        //Diagonal line left
        canvas.drawLine(
            endX_leftVeticalLine,
            endY,
            endX_leftVeticalLine*1.2f,
            size.toFloat(), paintBorder)
        //Diagonal line right
        canvas.drawLine(
            endX_rightVeticalLine,
            endY,
            (endX_rightVeticalLine - endX_leftVeticalLine*0.2f),
            size.toFloat(), paintBorder)

        //Horizontal top line
        canvas.drawLine(
            (startX_leftVeticalLine),
            (endY - ((endY - startY)/2)), //The half of the left vertical line
            (startX_rightVeticalLine),
            (endY - ((endY - startY)/2)), paintBorder) //The half of the right vertical line
        //Horizontal middle line - 1
        canvas.drawLine(
            startX_leftVeticalLine,
            (endY - ((endY - startY)/4)), //The half of the left vertical line
            startX_rightVeticalLine,
            (endY - ((endY - startY)/4)), paintBorder) //The half of the right vertical line
        //Horizontal middle line - 2
        canvas.drawLine(
            startX_leftVeticalLine,
            endY,
            startX_rightVeticalLine,
            endY, paintBorder)
        //Horizontal bottom line
        canvas.drawLine(
            endX_leftVeticalLine*1.2f,
            size.toFloat(),
            (endX_rightVeticalLine - endX_leftVeticalLine*0.2f),
            size.toFloat(), paintBorder)
    }
    private fun drawSocketFill(canvas: Canvas) {
        paintFill.color = Color.GRAY
        paintFill.strokeWidth = border
        paintFill.style = Paint.Style.FILL

        val path = Path()
        path.moveTo(startX_leftVeticalLine, startY)
        path.lineTo(startX_rightVeticalLine, startY)
        path.lineTo((endX_rightVeticalLine), endY)
        path.lineTo(endX_leftVeticalLine, endY)
        path.lineTo(startX_leftVeticalLine, startY)
        canvas.drawPath(path, paintFill)

        path.reset()
        path.moveTo(endX_leftVeticalLine, endY)
        path.lineTo(startX_rightVeticalLine, endY)
        path.lineTo((endX_rightVeticalLine - endX_leftVeticalLine*0.2f), size.toFloat())
        path.lineTo(endX_leftVeticalLine*1.2f, size.toFloat())
        path.lineTo(endX_leftVeticalLine, endY)
        canvas.drawPath(path, paintFill)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()

        bundle.putLong("state", state)
        bundle.putParcelable("superState", super.onSaveInstanceState())

        return bundle
    }
    override fun onRestoreInstanceState(_state: Parcelable) {
        var viewState = _state

        if (viewState is Bundle) {
            state = viewState.getLong("state", turnedOff)
            viewState = viewState.getParcelable("superState")!!
        }

        super.onRestoreInstanceState(viewState)
    }
}