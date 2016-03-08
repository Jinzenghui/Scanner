package com.example.biac.testscanner;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by BIAC on 2016/3/8.
 */
public class ScannerView extends View {

    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    private int ScreenRate;

    private static final int CORNER_WIDTH = 5;
    private static final int MIDDLE_LINE_WIDTH = 6;

    private static final int MIDDLE_LINE_PADDING = 5;

    private static final int SPEEN_DISTANCE = 5;

    private static float density;

    private static final int TEXT_SIZE = 16;
    private static final int TEXT_PADDING_TOP = 30;

    private Paint paint;

    private int slideTop;
    private int slideBottom;

    private int maskColor;
    private int resultColor;

    private boolean isFirst;

    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 480;
    private static final int MAX_FRAME_HEIGHT = 360;


    public ScannerView(Context context, AttributeSet attrs){
        super(context,attrs);

        density = context.getResources().getDisplayMetrics().density;

        ScreenRate = (int)(15*density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

    }

    @Override
    public void onDraw(Canvas canvas){
        Rect frame = getFramingRect();

        if(!isFirst){
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(maskColor);

        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        paint.setColor(Color.GREEN);

        canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate, frame.top + CORNER_WIDTH, paint);
        canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top + ScreenRate, paint);
        canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right, frame.top + CORNER_WIDTH, paint);
        canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top + ScreenRate, paint);
        canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left + ScreenRate, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - ScreenRate, frame.left + CORNER_WIDTH, frame.bottom, paint);
        canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH, frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate, frame.right, frame.bottom, paint);

        slideTop += SPEEN_DISTANCE;
        if(slideTop >= frame.bottom){
            slideTop = frame.top;
        }

        Rect lineRect = new Rect();
        lineRect.left = frame.left;
        lineRect.right = frame.right;
        lineRect.top = slideTop;
        lineRect.bottom = slideTop + 18;
        canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap(), null, lineRect, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(TEXT_SIZE * density);
        paint.setAlpha(0x40);
        paint.setTypeface(Typeface.create("System", Typeface.BOLD));
        String text = getResources().getString(R.string.scan_text);
        float textWidth = paint.measureText(text);

        canvas.drawText(text, (width - textWidth)/2, (float)(frame.bottom + (float)TEXT_PADDING_TOP*density), paint);

        postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);

    }

    public Point getScreenResolution(){

        Point screenResolution;
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        screenResolution = new Point(display.getWidth(), display.getHeight());
        return screenResolution;
    }

    public Rect getFramingRect(){
        Point screenResolution = getScreenResolution();
        Rect framingRect;

        int width = screenResolution.x*3/4;
        if(width<MIN_FRAME_WIDTH){
            width = MIN_FRAME_WIDTH;
        }else if(width > MAX_FRAME_WIDTH){
            width = MAX_FRAME_WIDTH;
        }

        int height = screenResolution.y*3/4;
        if(height < MIN_FRAME_HEIGHT){
            height = MIN_FRAME_HEIGHT;
        }else if(height > MAX_FRAME_HEIGHT){
            height = MAX_FRAME_HEIGHT;
        }

        int leftOffset = (screenResolution.x - width)/2;
        int topOffset = (screenResolution.y - height)/2;
        framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
        return framingRect;
    }

}
