package com.example.android1.facetracker;
/**
 * Created by Android1 on 7/1/2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import java.util.List;


public class StaticCustomView extends View {

    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 25.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = 15.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;
    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;
    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;
    private Paint mFacePositionPaint;
    private Paint mIdPaint;

    public StaticCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setContent(Bitmap bitmap, SparseArray<Face> faces) {
        mBitmap = bitmap;
        mFaces = faces;


        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceRectangle(canvas, scale);
        }
    }
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int) (imageWidth * scale), (int) (imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a rectangle around each detected face
     */
    private void drawFaceRectangle(Canvas canvas, double scale) {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        for (int i = 0; i < mFaces.size(); ++i) {
            mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
            final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
            mFacePositionPaint = new Paint();
            mFacePositionPaint.setColor(selectedColor);
            paint.setColor(selectedColor);
            mIdPaint = new Paint();
            mIdPaint.setColor(selectedColor);
            mIdPaint.setTextSize(ID_TEXT_SIZE);


            Face face = mFaces.valueAt(i);

            canvas.drawRect((float) (face.getPosition().x * scale),
                    (float) (face.getPosition().y * scale),
                    (float) ((face.getPosition().x + face.getWidth()) * scale),
                    (float) ((face.getPosition().y + face.getHeight()) * scale),
                    paint);

            List<Landmark> landmarks = face.getLandmarks();
            double x = (landmarks.get(0).getPosition().x) * scale;
            double y = (landmarks.get(0).getPosition().y) * scale;

            canvas.drawText("id: " + i, (float) x + ID_X_OFFSET, (float) y - ID_Y_OFFSET, mIdPaint);
            canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), (float) x +ID_X_OFFSET, (float) y - ID_Y_OFFSET-50, mIdPaint);

        }
    }


}