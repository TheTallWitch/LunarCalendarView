package com.gearback.zt.lunarcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gearback.zt.calendarcore.core.interfaces.OnDayClickedListener;
import com.gearback.zt.calendarcore.core.interfaces.OnDayLongClickedListener;
import com.gearback.zt.calendarcore.core.interfaces.OnMonthChangedListener;
import com.gearback.zt.calendarcore.core.models.IslamicDate;
import com.gearback.zt.lunarcalendar.core.LunarCalendarHandler;
import com.gearback.zt.lunarcalendar.core.fragments.CalendarFragment;

public class LunarCalendarView extends FrameLayout {
    private LunarCalendarHandler mCalendarHandler;
    CalendarFragment mCalendarFragment = null;

    public LunarCalendarView(Context context) {
        super(context);
        makeView(context,null);
    }

    public LunarCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        makeView(context,attrs);
    }

    public LunarCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        makeView(context,attrs);
    }

    private void makeView(Context context, AttributeSet attrs){
        mCalendarHandler = LunarCalendarHandler.getInstance(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LunarCalendarView, 0, 0);

        if(typedArray.hasValue(R.styleable.LunarCalendarView_pcv_typefacePath)) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    typedArray.getString(R.styleable.LunarCalendarView_pcv_typefacePath));
            if (typeface != null)
                mCalendarHandler.setTypeface(typeface);
        }
        if(typedArray.hasValue(R.styleable.LunarCalendarView_pcv_headersTypefacePath)) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    typedArray.getString(R.styleable.LunarCalendarView_pcv_headersTypefacePath));
            if (typeface != null)
                mCalendarHandler.setHeadersTypeface(typeface);
        }
        mCalendarHandler.setDaysFontSize(typedArray.getDimensionPixelSize(
                R.styleable.LunarCalendarView_pcv_fontSize,
                25));
        mCalendarHandler.setHeadersFontSize(typedArray.getDimensionPixelSize(
                R.styleable.LunarCalendarView_pcv_headersFontSize,
                20));
        mCalendarHandler.setTodayBackground(typedArray.getResourceId(
                R.styleable.LunarCalendarView_pcv_todayBackground,
                mCalendarHandler.getTodayBackground()));
        mCalendarHandler.setSelectedDayBackground(typedArray.getResourceId(
                R.styleable.LunarCalendarView_pcv_selectedDayBackground,
                mCalendarHandler.getSelectedDayBackground()));
        mCalendarHandler.setColorDayName(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_colorDayName,
                mCalendarHandler.getColorDayName()));
        mCalendarHandler.setColorBackground(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_colorBackground,
                mCalendarHandler.getColorHolidaySelected()));
        mCalendarHandler.setColorHolidaySelected(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_colorHolidaySelected,
                mCalendarHandler.getColorHolidaySelected()));
        mCalendarHandler.setColorHoliday(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_colorHoliday,
                mCalendarHandler.getColorHoliday()));
        mCalendarHandler.setColorNormalDaySelected(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_colorNormalDaySelected,
                mCalendarHandler.getColorNormalDaySelected()));
        mCalendarHandler.setColorNormalDay(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_colorNormalDay,
                mCalendarHandler.getColorNormalDay()));
        mCalendarHandler.setColorEventUnderline(typedArray.getColor(
                R.styleable.LunarCalendarView_pcv_eventUnderlineColor,
                mCalendarHandler.getColorEventUnderline()));
        try {
            mCalendarFragment = CalendarFragment.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setBackgroundColor(mCalendarHandler.getColorBackground());
        FragmentManager m = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        m.beginTransaction()
                .replace(R.id.fragment_holder,
                        mCalendarFragment,
                        CalendarFragment.class.getName())
                .commit();

        typedArray.recycle();
    }

    public void update(){
        this.invalidate();
        setBackgroundColor(mCalendarHandler.getColorBackground());

        //LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.BROADCAST_UPDATE_EVENTS));

        if(mCalendarHandler.getOnEventUpdateListener() != null)
            mCalendarHandler.getOnEventUpdateListener().update();
    }

    public void goToDate(IslamicDate date){
        mCalendarFragment.bringDate(date);
    }

    public void goToToday(){
        mCalendarFragment.bringDate(mCalendarHandler.getToday());
    }
    public void goToNextMonth(){
        goToMonthFromNow(1);
    }

    public void goToPreviousMonth(){
        goToMonthFromNow(-1);
    }

    public void goToMonthFromNow(int offset){
        mCalendarFragment.changeMonth(-offset);
    }

    public LunarCalendarHandler getCalendar() {
        return mCalendarHandler;
    }

    public void setOnDayClickedListener(OnDayClickedListener listener){
        mCalendarHandler.setOnDayClickedListener(listener);
    }

    public void setOnDayLongClickedListener(OnDayLongClickedListener listener){
        mCalendarHandler.setOnDayLongClickedListener(listener);
    }

    public void setOnMonthChangedListener(OnMonthChangedListener listener){
        mCalendarHandler.setOnMonthChangedListener(listener);
    }
}
