package com.nablanet.aula31.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.User;

public class PupilView extends LinearLayout {

    Drawable drawable;

    public interface OnToggledListener {
        void OnToggled(PupilView v, boolean touchOn);
    }

    boolean touchOn = false;
    boolean mDownTouch = false;
    private OnToggledListener toggledListener;
    int idX;
    int idY;
    int textY;
    int imageW;
    User user;
    Paint paint;

    public PupilView(Context context, User user, int x, int y) {
        super(context);
        this.user = user;
        idX = x;
        idY = y;
        drawable = (this.user == null) ?
                getResources().getDrawable(R.drawable.ic_subject_color) :
                getResources().getDrawable(R.drawable.ic_subject_color);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);

    }

    public PupilView(Context context) {
        super(context);
    }

    public PupilView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PupilView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int m = (int) Math.min(h * 0.75, w);
        drawable.setBounds((w - m) / 2, 0, (w + m) / 2, m);
        textY = h;
        imageW = w;
        //hourPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (touchOn) {
            drawable.draw(canvas);
            if (user != null){
                canvas.drawText(user.getNames(), 0, textY, paint);
            }
        } else {
            canvas.drawColor(Color.GRAY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                touchOn = !touchOn;
                invalidate();

                if(toggledListener != null)
                    toggledListener.OnToggled(this, touchOn);

                mDownTouch = true;
                return true;

            case MotionEvent.ACTION_UP:

                if (mDownTouch) {
                    mDownTouch = false;
                    performClick();
                    return true;
                }
        }
        return false;
    }

    public void setOnToggledListener(OnToggledListener listener){
        toggledListener = listener;
    }

    public int getIdX(){
        return idX;
    }

    public int getIdY(){
        return idY;
    }

    public void setUser(User user) {
        this.user = user;
        invalidate();
    }

}
