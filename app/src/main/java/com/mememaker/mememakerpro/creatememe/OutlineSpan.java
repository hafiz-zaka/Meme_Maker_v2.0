package com.mememaker.mememakerpro.creatememe;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class OutlineSpan extends CharacterStyle implements UpdateAppearance {
    private final float strokeWidth;
    private final int strokeColor;

    public OutlineSpan(float strokeWidth, int strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setStyle(Paint.Style.STROKE);
        tp.setStrokeWidth(strokeWidth);
        tp.setColor(strokeColor);
    }
}
