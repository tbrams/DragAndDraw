package dk.brams.draganddraw;


import android.graphics.PointF;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Box implements Serializable {
    private static final long serialVersionUID = 0L;

    private PointF mLeftTop;
    private PointF mRightTop;
    private PointF mRightBottom;
    private PointF mLeftBottom;

    public void setLeftTop(PointF lt)
    {
        this.mLeftTop = lt;
    }

    public PointF getLeftTop()
    {
        return mLeftTop;
    }

    public void setRightTop(PointF rt)
    {
        this.mRightTop = rt;
    }

    public PointF getRightTop()
    {
        return mRightTop;
    }

    public void setRightBottom(PointF rb)
    {
        this.mRightBottom = rb;
    }

    public PointF getRightBottom()
    {
        return mRightBottom;
    }

    public void setLeftBottom(PointF lb)
    {
        this.mLeftBottom = lb;
    }

    public PointF getLeftBottom()
    {
        return mLeftBottom;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeFloat(mLeftTop.x);
        out.writeFloat(mLeftTop.y);

        out.writeFloat(mRightTop.x);
        out.writeFloat(mRightTop.x);

        out.writeFloat(mRightTop.x);
        out.writeFloat(mRightTop.x);

        out.writeFloat(mLeftBottom.x);
        out.writeFloat(mLeftBottom.x);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        mLeftTop.x = in.readFloat();
        mLeftTop.y = in.readFloat();

        mRightTop.x = in.readFloat();
        mRightTop.y = in.readFloat();

        mRightBottom.x = in.readFloat();
        mRightBottom.y = in.readFloat();

        mLeftBottom.x = in.readFloat();
        mLeftBottom.y = in.readFloat();
    }
}
