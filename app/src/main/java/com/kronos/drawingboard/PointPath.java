package com.kronos.drawingboard;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class PointPath {
    private Path path = null;
    private PointF prePoint = null;
    private Paint paint = null;

    private static volatile PointPath pointPath = null;
    private static PorterDuffXfermode sClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private float currentWidth;

    public void setCurrentWidth(float currentWidth) {
        this.currentWidth = currentWidth;
    }

    PointPath(){
        path = new Path();
        paint = new Paint();
    }

    public synchronized  static PointPath newPointPathInstance(PointF pointF){
        pointPath = new PointPath();
        pointPath.path.moveTo(pointF.x,pointF.y);
        pointPath.prePoint = pointF;
        return pointPath;
    }

    public void savePointPath(PointF currentPoint){
        path.quadTo(prePoint.x,prePoint.y,currentPoint.x,currentPoint.y);
        prePoint = currentPoint;
    }

    public void disPlay(Canvas canvas){
        if (paint == null){
            paint = new Paint();
        }

        int color = Color.argb(128,0,0,0);
        paint.setColor(color);
        paint.setXfermode(sClearMode);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(20);
    }
}
