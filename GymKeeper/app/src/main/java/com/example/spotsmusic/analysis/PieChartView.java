package com.example.spotsmusic.analysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class PieChartView extends View {
    private int screenW, screenH;

    private Paint textPaint, piePaint, linePaint;

    private int pieCenterX, pieCenterY, pieRadius;
 
    private RectF pieOval;

    private float smallMargin;

    private int[] mPieColors = new int[]{ Color.argb(255,64, 196, 0), Color.YELLOW, Color.argb(255,235,200,38), Color.MAGENTA,Color.CYAN, Color.GREEN};

    private PieItemBean[] mPieItems;
    private float totalValue;

    public PieChartView(Context context) {
        super(context);

        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        //init screen
        screenW = ScreenUtils.getScreenW(context);
        screenH = ScreenUtils.getScreenH(context);

        pieCenterX = screenW / 2;
        pieCenterY = screenH / 3;
        pieRadius = screenW / 4;
        smallMargin = ScreenUtils.dp2px(context, 5);

        pieOval = new RectF();
        pieOval.left = pieCenterX - pieRadius;
        pieOval.top = pieCenterY - pieRadius;
        pieOval.right = pieCenterX + pieRadius;
        pieOval.bottom = pieCenterY + pieRadius;

        //The paint to draw text.
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(ScreenUtils.dp2px(context, 16));

        //The paint to draw circle.
        piePaint = new Paint();
        piePaint.setAntiAlias(true);
        piePaint.setStyle(Paint.Style.FILL);

        //The paint to draw line to show the concrete text
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(ScreenUtils.dp2px(context, 1));

    }

    //The degree position of the last item arc's center.
    private float lastDegree = 0;
    //The count of the continues 'small' item.
    private int addTimes = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPieItems != null && mPieItems.length > 0) {
            float start = 0.0f;
            for (int i = 0; i < mPieItems.length; i++) {
                //draw pie
            	if(mPieItems.length > 1 && (mPieItems.length % mPieColors.length == 1) && i == mPieItems.length - 1) {
            		piePaint.setColor(mPieColors[3]);
            	} else
            		piePaint.setColor(mPieColors[i % mPieColors.length]);
                float sweep = mPieItems[i].getItemValue() / totalValue * 360;
                canvas.drawArc(pieOval, start, sweep, true, piePaint);

                //draw line away from the pie
                float radians = (float) ((start + sweep / 2) / 180 * Math.PI);
                float lineStartX = pieCenterX + pieRadius * 0.7f * (float) (Math.cos(radians));
                float lineStartY = pieCenterY + pieRadius * 0.7f * (float) (Math.sin(radians));

                float lineStopX, lineStopY;
                float rate;
                if (getOffset(start + sweep / 2) > 60) {
                    rate = 1.3f;
                } else if (getOffset(start + sweep / 2) > 30) {
                    rate = 1.2f;
                } else {
                    rate = 1.1f;
                }
                //If the item is very small, make the text further away from the pie to avoid being hided by other text.
                if (start + sweep / 2 - lastDegree < 30) {
                    addTimes++;
                    rate += 0.2f * addTimes;
                } else {
                    addTimes = 0;
                }

                lineStopX = pieCenterX + pieRadius * rate * (float) (Math.cos(radians));
                lineStopY = pieCenterY + pieRadius * rate * (float) (Math.sin(radians));
                canvas.drawLine(lineStartX, lineStartY, lineStopX, lineStopY, linePaint);

                //write text
                String itemTypeText = mPieItems[i].getItemType();
                String itemPercentText = Utility.formatFloat(mPieItems[i].getItemValue() / totalValue * 100) + "%";

                float itemTypeTextLen = textPaint.measureText(itemTypeText);
                float itemPercentTextLen = textPaint.measureText(itemPercentText);
                float lineTextWidth = Math.max(itemTypeTextLen, itemPercentTextLen);

                float textStartX = lineStopX;
                float textStartY = lineStopY - smallMargin;
                float percentStartX = lineStopX;
                float percentStartY = lineStopY + textPaint.getTextSize();
                if (lineStartX > pieCenterX) {
                    textStartX += (smallMargin + Math.abs(itemTypeTextLen - lineTextWidth) / 2);
                    percentStartX += (smallMargin + Math.abs(itemPercentTextLen - lineTextWidth) / 2);
                } else {
                    textStartX -= (smallMargin + lineTextWidth - Math.abs(itemTypeTextLen - lineTextWidth) / 2);
                    percentStartX -= (smallMargin + lineTextWidth - Math.abs(itemPercentTextLen - lineTextWidth) / 2);
                }
                canvas.drawText(itemTypeText, textStartX, textStartY, textPaint);
                //draw percent text
                canvas.drawText(itemPercentText, percentStartX, percentStartY, textPaint);

                //draw text underline
                float textLineStopX = lineStopX;
                if (lineStartX > pieCenterX) {
                    textLineStopX += (lineTextWidth + smallMargin * 2);
                } else {
                    textLineStopX -= (lineTextWidth + smallMargin * 2);
                }
                canvas.drawLine(lineStopX, lineStopY, textLineStopX, lineStopY, linePaint);

                lastDegree = start + sweep / 2;
                start += sweep;
            }
        }
    }

    public PieItemBean[] getPieItems() {
        return mPieItems;
    }

    public void setPieItems(PieItemBean[] pieItems) {
        this.mPieItems = pieItems;

        totalValue = 0;
        for (PieItemBean item : mPieItems) {
            totalValue += item.getItemValue();
        }
        invalidate();//使无效后重绘
    }

    private float getOffset(float radius) {
        int a = (int) (radius % 360 / 90);
        switch (a) {
            case 0:
                return radius;
            case 1:
                return 180 - radius;
            case 2:
                return radius - 180;
            case 3:
                return 360 - radius;
        }
        return radius;
    }

    public static class PieItemBean {
        private String itemType;
        private float itemValue;

        public PieItemBean(String itemType, float itemValue) {
            this.itemType = itemType;
            this.itemValue = itemValue;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public float getItemValue() {
            return itemValue;
        }

        public void setItemValue(float itemValue) {
            this.itemValue = itemValue;
        }
    }

}


