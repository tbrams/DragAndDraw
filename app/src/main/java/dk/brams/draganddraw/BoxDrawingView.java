package dk.brams.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private ArrayList<Box> mBoxes = new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    // Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    // Used when creating the view from XML
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setId(R.id.view_id);

        // Use a semitransparent red for boxes
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // And off-white for the background
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);


    }

    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());
        Log.i(TAG, "Received event at ("+curr.x + ","+curr.y+")");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, " ACTION DOWN");
                // Reset drawing state
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG," ACTION_MOVE");
                if (mCurrentBox!=null) {
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG," ACTION_UP");
                mCurrentBox=null;
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG," ACTION_CANCEL");
                mCurrentBox=null;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box: mBoxes) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();

        if (mBoxes.size()!=0) {
            bundle.putParcelable("instanceState", super.onSaveInstanceState());
            bundle.putSerializable("arrayListToSave", this.mBoxes);
        }

        return bundle;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = null;
        if (state instanceof Bundle) {
            bundle = (Bundle) state;
            this.mBoxes = (ArrayList<Box>)bundle.getSerializable("arrayListToSave");
            state=bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
