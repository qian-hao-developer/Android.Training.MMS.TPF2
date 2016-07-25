package com.example.hqian.kadai001_paintingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hqian on 16/06/22.
 */

public class GraphicsView extends View {

    private int paintColor = Color.BLACK;
    private float paintSize = 5;
    private float locate_x;
    private float locate_y;
    private boolean allClearFlag = false;

    private Paint paint = new Paint();
    private Path path;
    private Bitmap bitmap;

    public GraphicsView(Context context) {
        super(context);

        /////////////////////////////
        // onTouch vs onTouchEvent //
        /////////////////////////////
//        this.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                locate_x = motionEvent.getX();
//                locate_y = motionEvent.getY();
//
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                    path = new Path();
//                    path.moveTo(locate_x, locate_y);
//                }else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
//                    path.lineTo(locate_x, locate_y);
//                    invalidate();
//                }else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//                    path.lineTo(locate_x, locate_y);
//                    invalidate();
//
////            setDrawingCacheEnabled(true);
//
//                }else{
//                    // do nothing
//                }
//
//                return true;
//            }
//        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);
        if (allClearFlag){
            canvas.drawColor(Color.WHITE);
            allClearFlag = false;
            bitmap = null;
        }else {

            //attention to the turn of draw(bitmap first, then path)
            //if not, eraser will not effect
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, paint);
            }

            if (path != null) {
                canvas.drawPath(path, paint);
            }
        }
        super.onDraw(canvas);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        locate_x = event.getX();
        locate_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            path = new Path();
            path.moveTo(locate_x, locate_y);
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            path.lineTo(locate_x, locate_y);
            invalidate();
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            path.lineTo(locate_x, locate_y);

            //save trace of path into bitmap, and paint
            setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(getDrawingCache());
            setDrawingCacheEnabled(false);
            invalidate();
        }else{
            // do nothing
        }
//        return super.onTouchEvent(event);    // default return false
        return true;
    }

    public void translateData_color(int color){
        paintColor = color;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
    }
    public void translateData_size(float size){
        paintSize = size;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(paintSize);
        paint.setAntiAlias(true);
    }
    public void setAllClearFlag(boolean state){
        allClearFlag = state;
        invalidate();
    }

    // how to use it???
    public void setEraser(){
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setARGB(0, 0, 0, 0);
    }
}
