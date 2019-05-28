package com.nablanet.aula31.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.entity.CourseExt;
import com.nablanet.aula31.utils.Util;

import java.util.Calendar;
import java.util.List;

public class DayScheduleView extends CardView {

    private static int startMinute, endMinute;
    private static int startMarkMinute, intervalMinute, intervalsCount;
    private static List<CourseExt> courses;

    // Unidades en dp
    int leftMarginDp, rightMarginDp;
    final int topMarginDp = 20;
    final int bottomMarginDp = 10;

    String dayName;
    int weekDay;
    float[] pointsMarks;
    int startPx, startMarkPx, endPx, currentPx;
    float intervalPx, bottomMarginPx, slopePx;

    Paint hourPaint, minutePaint, baseSubject, currentTimePaint;
    TextPaint textHourPaint, textDayPaint, textSubject;

    public DayScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (DayScheduleView.startMinute == 0 && DayScheduleView.endMinute == 0)
            setMarginTime(8 * 60, 20 * 60);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startPx = (int) (topMarginDp * getResources().getDisplayMetrics().density);
        endPx = (int) (h - bottomMarginDp * getResources().getDisplayMetrics().density);
        slopePx = (endPx - startPx) / (float) (endMinute - startMinute);
        intervalPx =  slopePx * intervalMinute;
        startMarkPx = (int) (startPx + slopePx * (startMarkMinute - startMinute));
        bottomMarginPx = bottomMarginDp * getResources().getDisplayMetrics().density;
        updateTime();
        loadPointMarks();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackGround(canvas);
        if (dayName != null)
            drawTimes(canvas);
        drawCurrentTime(canvas);
    }

    public static void setMarginTime(int startTime, int endTime) {
        DayScheduleView.startMinute = (startTime > 0 && startTime < 24 * 60) ? startTime : 0;
        DayScheduleView.endMinute = (endTime > DayScheduleView.startMinute && endTime <= 24 * 60) ?
                endTime : DayScheduleView.startMinute + 1;
        DayScheduleView.intervalMinute = getIntervalMinutes();
        DayScheduleView.startMarkMinute = Util.getStartMarkMinute(
                DayScheduleView.startMinute, DayScheduleView.intervalMinute
        );
        DayScheduleView.intervalsCount = (
                DayScheduleView.endMinute - DayScheduleView.startMinute
        ) / DayScheduleView.intervalMinute + 1;
    }

    public static void setCourses(List<CourseExt> courses) {
        DayScheduleView.courses = courses;
    }

    private static int getIntervalMinutes() {
        if (DayScheduleView.endMinute - DayScheduleView.startMinute > 12 * 60)
            return  60;
        else if (DayScheduleView.endMinute - DayScheduleView.startMinute > 8 * 60)
            return  30;
        else if (DayScheduleView.endMinute - DayScheduleView.startMinute > 4 * 60)
            return  20;
        else
            return  15;
    }

    private void init() {

        hourPaint = new Paint();
        hourPaint.setStrokeWidth(2);
        hourPaint.setColor(Color.BLACK);
        hourPaint.setStyle(Paint.Style.STROKE);

        currentTimePaint = new Paint();
        currentTimePaint.setStrokeWidth(4);
        currentTimePaint.setColor(Color.BLUE);
        currentTimePaint.setStyle(Paint.Style.STROKE);

        minutePaint = new Paint();
        minutePaint.setStrokeWidth(1);
        minutePaint.setColor(Color.GRAY);

        textHourPaint = new TextPaint();
        textHourPaint.setTextSize(18);
        textHourPaint.setTextAlign(Paint.Align.RIGHT);

        textDayPaint = new TextPaint();
        textDayPaint.setTextAlign(Paint.Align.CENTER);
        textDayPaint.setStyle(Paint.Style.STROKE);
        textDayPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.week_day_font_size));

        baseSubject = new Paint();
        baseSubject.setStrokeWidth(2);
        baseSubject.setColor(Color.GREEN);

        textSubject = new TextPaint();
        textSubject.setTextSize(getResources().getDimensionPixelSize(R.dimen.subject_font_size));
        textSubject.setColor(Color.BLACK);

        switch (getId()){
            case R.id.schedule_monday:
                dayName = getContext().getString(R.string.monday);
                weekDay = Calendar.MONDAY;
                break;
            case R.id.schedule_tuesday:
                dayName = getContext().getString(R.string.tuesday);
                weekDay = Calendar.TUESDAY;
                break;
            case R.id.schedule_wednesday:
                dayName = getContext().getString(R.string.wednesday);
                weekDay = Calendar.WEDNESDAY;
                break;
            case R.id.schedule_thursday:
                dayName = getContext().getString(R.string.thursday);
                weekDay = Calendar.THURSDAY;
            break;
            case R.id.schedule_friday:
                dayName = getContext().getString(R.string.friday);
                weekDay = Calendar.FRIDAY;
            break;
        }

        updateTime();

    }

    private void loadPointMarks() {
        float yPx = startMarkPx;
        pointsMarks = new float[4 * intervalsCount];
        int point = 0;
        int count = 0;
        while (yPx < endPx && point < 4 * intervalsCount) {
            pointsMarks[point] = leftMarginDp * getResources().getDisplayMetrics().density; point++;
            pointsMarks[point] = yPx; point++;
            pointsMarks[point] = getWidth() - rightMarginDp * getResources().getDisplayMetrics().density; point++;
            pointsMarks[point] = yPx; point++;
            count++;
            yPx = startMarkPx + count * intervalPx;
        }
    }

    private void drawBackGround(Canvas canvas) {

        float px = startMarkPx;
        int minute = startMarkMinute;

        if (getId() == R.id.schedule_hour){

            while (minute <= endMinute) {
                if (minute % 60 == 0)
                    canvas.drawText(Util.getTimeFromMinutes(minute), getWidth(), px, textHourPaint);
                minute += intervalMinute;
                px += intervalPx;
            }

        } else {
            canvas.drawLines(pointsMarks, 0, 4 * intervalsCount, hourPaint);
            if (dayName != null){
                canvas.drawText(
                        dayName,
                        getWidth() / 2.0f,
                        startPx / 2.0f - textDayPaint.getFontMetrics().ascent / 2,
                        textDayPaint
                );
            }

        }

    }

    private void drawTimes(Canvas canvas) {

        int startY = 150;
        int endY = 300;

        canvas.drawRect(0,startY, getWidth(), endY, baseSubject);

        Rect b = new Rect(0, startY, getWidth(), endY);
        int width = b.width() - 1; //10 to keep some space on the right for the "..."
        CharSequence txt = TextUtils.ellipsize("Matemática", textSubject, width, TextUtils.TruncateAt.END);
        canvas.drawText(txt, 0, txt.length(), 5, (startY + endY) / 2.0f - textSubject.getFontMetrics().ascent / 2.0f, textSubject);



    }

    private void drawCurrentTime(Canvas canvas) {
        if (weekDay == 0 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == weekDay)
            canvas.drawLine(0, currentPx, getWidth(), currentPx, currentTimePaint);
    }

    public void update() {
        updateTime();
        updateCourses();
    }

    public void updateTime() {
        int currentMinute = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 +
                Calendar.getInstance().get(Calendar.MINUTE);
        currentPx = (int) (slopePx * (currentMinute - startMinute) + startPx);
        loadPointMarks();
    }

    private void updateCourses() {
        /*List<CourseExt.Schedule> schedules = new ArrayList<>();
        for (CourseExt course : courses) {
            if (course.schedules.containsKey(weekDay))
                schedules.add(course.schedules.get(weekDay));

        }*/
    }

}
