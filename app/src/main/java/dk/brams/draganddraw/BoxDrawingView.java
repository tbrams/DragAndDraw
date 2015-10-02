package dk.brams.draganddraw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BoxDrawingView extends View {
        public static final String TAG = "BoxDrawingView";

        private Box mCurrentBox;
        private ArrayList<Box> mBoxes = new ArrayList<Box>();
        private Paint mBoxPaint;
        private Paint mBackgroundPaint;

        private PointF centerPoint;
        private float left;
        private float right;
        private float top;
        private float bottom;
        private double origAngle1;
        private double origAngle2;
        private PointF curr1 = new PointF();
        private PointF orig1 = new PointF();

        public BoxDrawingView(android.content.Context context)
        {
            super(context, null);
        }

        public BoxDrawingView(android.content.Context context, android.util.AttributeSet attrs)
        {
            super(context, attrs);
            setId(R.id.view_id);

            mBoxPaint = new Paint();
            mBoxPaint.setColor(0x22ff0000);
            mBoxPaint.setStyle(Paint.Style.FILL);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(0xfff8efe0);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event)
        {
            curr1.set(event.getX(), event.getY());
            int pointerCount = event.getPointerCount();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    orig1.set(curr1.x, curr1.y);
                    mCurrentBox = new Box();
                    mBoxes.add(mCurrentBox);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    if (mCurrentBox != null && pointerCount == 2) {
                        centerPoint = getCenterPoint(left, top, right, bottom);
                        origAngle1 = getAngle(centerPoint, event.getX(), event.getY());
                        origAngle2 = getAngle(centerPoint, event.getX(1), event.getY(1));
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mCurrentBox != null)
                    {
                        if (pointerCount > 1) {
                            double currentAngle1 = getAngle(centerPoint, event.getX(0), event.getY(0)) - origAngle1;
                            double currentAngle2 = getAngle(centerPoint, event.getX(1), event.getY(1)) - origAngle2;
                            double currentAngle = currentAngle1 + currentAngle2;
                            mCurrentBox.setLeftTop(rotate(left, top, centerPoint.x, centerPoint.y, currentAngle));
                            mCurrentBox.setRightTop(rotate(right, top, centerPoint.x, centerPoint.y, currentAngle));
                            mCurrentBox.setRightBottom(rotate(right, bottom, centerPoint.x, centerPoint.y, currentAngle));
                            mCurrentBox.setLeftBottom(rotate(left, bottom, centerPoint.x, centerPoint.y, currentAngle));
                        } else {
                            left = Math.min(orig1.x, curr1.x);
                            right = Math.max(orig1.x, curr1.x);
                            top = Math.min(orig1.y, curr1.y);
                            bottom = Math.max(orig1.y, curr1.y);

                            mCurrentBox.setLeftTop(new PointF(left, top));
                            mCurrentBox.setRightTop(new PointF(right, top));
                            mCurrentBox.setRightBottom(new PointF(right, bottom));
                            mCurrentBox.setLeftBottom(new PointF(left, bottom));
                        }
                        invalidate();
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_UP:
                    mCurrentBox = null;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    mCurrentBox = null;
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            canvas.drawPaint(mBackgroundPaint);
            for (Box box:mBoxes){
                Path path = new Path();
                path.moveTo(box.getLeftTop().x, box.getLeftTop().y);
                path.lineTo(box.getRightTop().x, box.getRightTop().y);
                path.lineTo(box.getRightBottom().x, box.getRightBottom().y);
                path.lineTo(box.getLeftBottom().x, box.getLeftBottom().y);
                path.close();
                canvas.drawPath(path, mBoxPaint);
            }
        }

        @Override
        protected Parcelable onSaveInstanceState()
        {
            Parcelable savedState = super.onSaveInstanceState();
            Bundle bundle = new Bundle();
            bundle.putParcelable("savedState", savedState);
            bundle.putSerializable("BUNDLE_BOXES", mBoxes);
            return bundle;
        }

        @Override
        protected void onRestoreInstanceState(Parcelable state)
        {
            Parcelable savedState = ((Bundle) state).getParcelable("savedState");
            super.onRestoreInstanceState(savedState);

            mBoxes = (ArrayList<Box>) ((Bundle) state).getSerializable("BUNDLE_BOXES");
            invalidate();
        }

        PointF getCenterPoint(float left, float top, float right, float bottom){
            return new PointF((left + right) / 2, (top + bottom) / 2);
        }

        double getAngle (PointF centerPoint, float x, float y){
            return Math.atan2(x - centerPoint.x, centerPoint.y - y);
        }

        PointF rotate(double x, double y, double cx, double cy, double angle){
            double tX;
            x = x - cx;
            y = y - cy;
            tX = x;
            x = tX * Math.cos(angle) - y * Math.sin(angle);
            y = tX * Math.sin(angle) + y * Math.cos(angle);
            x = x + cx;
            y = y + cy;
            return new PointF((float)x, (float)y);
        }
}
