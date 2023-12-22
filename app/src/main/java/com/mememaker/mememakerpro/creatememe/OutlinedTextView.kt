package com.mememaker.mememakerpro.creatememe
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class OutlinedTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private val strokePaint = TextPaint()

    init {
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 10f // Adjust the outline width as needed
        strokePaint.color = Color.GREEN // Adjust the outline color as needed
        strokePaint.typeface = Typeface.DEFAULT_BOLD // Adjust the text typeface as needed
    }

    override fun onDraw(canvas: Canvas) {
        val text = text.toString()
        canvas.drawText(text, 0f, baseline.toFloat(), strokePaint)
        super.onDraw(canvas) // Draw the text over the outline
    }
}
