package com.example.module_commonview.wheelview.yuri;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.module_commonview.R;
import com.example.module_commonview.wheelview.yuri.wheelview.OnWheelChangedListener;
import com.example.module_commonview.wheelview.yuri.wheelview.OnWheelScrollListener;
import com.example.module_commonview.wheelview.yuri.wheelview.WheelView;
import com.example.module_commonview.wheelview.yuri.wheelview.adapter.AbstractWheelTextAdapter1;

import java.util.ArrayList;


/**
 *
 */
public class DatePickerView extends LinearLayout {


    private static final int YEAR_MIN = 1950;
    private static final int YEAR_MAX = 2020;

    private int year = YEAR_MIN;
    private int month = 1;
    private int day = 1;


    private ArrayList<Integer> yearList = new ArrayList<>(YEAR_MAX - YEAR_MIN + 1);
    private ArrayList<Integer> monthList = new ArrayList<>(12);
    private ArrayList<Integer> dayList = new ArrayList<>(31);

    private DateTextAdapter yearAdapter;
    private DateTextAdapter monthAdapter;
    private DateTextAdapter dayAdapter;

    private Context context;
    private WheelView wheelViewYear;
    private WheelView wheelViewMonth;
    private WheelView wheelViewDay;

    public DatePickerView(Context context) {
        this(context, null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        for (int i = YEAR_MIN; i < YEAR_MAX + 1; i++) {
            yearList.add(i);
        }
        for (int i = 1; i < 13; i++) {
            monthList.add(i);
        }
        for (int i = 1; i < 32; i++) {
            dayList.add(i);
        }

        LayoutInflater.from(context).inflate(R.layout.wheel_widget_date_picker, this);

        wheelViewYear = (WheelView) findViewById(R.id.wheelViewYear);
        wheelViewMonth = (WheelView) findViewById(R.id.wheelViewMonth);
        wheelViewDay = (WheelView) findViewById(R.id.wheelViewDay);

        wheelViewYear.setCyclic(true);// 可循环滚动
        wheelViewMonth.setCyclic(true);// 可循环滚动
        wheelViewDay.setCyclic(true);// 可循环滚动

        yearAdapter = new DateTextAdapter(context);
        monthAdapter = new DateTextAdapter(context);
        dayAdapter = new DateTextAdapter(context);

        yearAdapter.setList(yearList);
        monthAdapter.setList(monthList);
        dayAdapter.setList(dayList);

        wheelViewYear.setViewAdapter(yearAdapter);
        wheelViewMonth.setViewAdapter(monthAdapter);
        wheelViewDay.setViewAdapter(dayAdapter);

        wheelViewYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                year = YEAR_MIN + newValue;
                int days = calcDay(year, month); // days=28
                dayList = getDayList(days);
                dayAdapter.setList(dayList);
                if (day > days) {
                    dayAdapter.currentIndex = days - 1;
                    wheelViewDay.setCurrentItem(dayAdapter.currentIndex);
                } else {
                    dayAdapter.currentIndex = day - 1; // day = 30
                }

                if (onSelectedChangedListener != null) {
                    onSelectedChangedListener.OnSelectedChanged(parseInt2Array(YEAR_MIN + oldValue, month, day), getSelectDate());
                }
            }
        });

        wheelViewMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                month = 1 + newValue;
                int days = calcDay(year, month); // days=28
                dayList = getDayList(days);
                dayAdapter.setList(dayList);
                if (day > days) {
                    dayAdapter.currentIndex = days - 1;
                    wheelViewDay.setCurrentItem(dayAdapter.currentIndex);
                } else {
                    dayAdapter.currentIndex = day - 1; // day = 30
                }

                if (onSelectedChangedListener != null) {
                    onSelectedChangedListener.OnSelectedChanged(parseInt2Array(year, 1 + oldValue, day), getSelectDate());
                }
            }
        });

        wheelViewDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                day = 1 + newValue;
                if (onSelectedChangedListener != null) {
                    onSelectedChangedListener.OnSelectedChanged(parseInt2Array(year, month, oldValue + 1), getSelectDate());
                }
            }
        });

        wheelViewYear.addScrollingListener(onWheelScrollListener);
        wheelViewMonth.addScrollingListener(onWheelScrollListener);
        wheelViewDay.addScrollingListener(onWheelScrollListener);
    }

    OnWheelScrollListener onWheelScrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            setTextViewStyle();
        }
    };

    private void setTextViewStyle() {
        setTextViewSize(year + "", yearAdapter);
        setTextViewSize(month + "", monthAdapter);
        setTextViewSize(day + "", dayAdapter);
    }

    private void setTextViewSize(String currentItemText, AbstractWheelTextAdapter1 adapter) {
        ArrayList<View> arrayList = adapter.getTextViews();
        int size = arrayList.size();
        String currentText;
        TextView textView;
        boolean current;
        for (int i = 0; i < size; i++) {
            textView = (TextView) arrayList.get(i);
            currentText = textView.getText().toString();
            current = currentItemText.equals(currentText);
            textView.setTextColor(current ? adapter.selected_text_color : adapter.un_selected_text_color);
            textView.setTextSize(current ? adapter.selected_text_size : adapter.un_selected_text_size);
        }
    }

    /**
     * 设置控件的初始值
     *
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {
        //验证合法性
        if (year > YEAR_MAX || year < YEAR_MIN) {
            year = YEAR_MIN;
        }
        if (month > 12 || month < 1) {
            month = 1;
        }
        final int days = calcDay(year, month);
        if (day > days || day < 1) {
            day = 1;
        }

        this.year = year;
        this.month = month;
        this.day = day;

        yearAdapter.currentIndex = DatePickerView.this.year - YEAR_MIN;
        monthAdapter.currentIndex = DatePickerView.this.month - 1;
        dayAdapter.currentIndex = DatePickerView.this.day - 1;

        wheelViewYear.setCurrentItem(yearAdapter.currentIndex);
        wheelViewMonth.setCurrentItem(monthAdapter.currentIndex);
        wheelViewDay.setCurrentItem(dayAdapter.currentIndex);
    }

    public void addOnSelectedChangingListener(OnSelectedChangedListener onSelectedChangedListener) {
        this.onSelectedChangedListener = onSelectedChangedListener;
    }

    private OnSelectedChangedListener onSelectedChangedListener;

    public interface OnSelectedChangedListener {
        void OnSelectedChanged(int[] oldValue, int[] newValue);
    }

    private int[] parseInt2Array(int year, int month, int day) {
        return new int[]{year, month, day};
    }

    private int[] getSelectDate() {
        return new int[]{year, month, day};
    }

    private ArrayList<Integer> getDayList(int days) {
        if (days <= 0) {
            return null;
        }
        ArrayList<Integer> list = new ArrayList(days);
        for (int i = 1; i < days + 1; i++) {
            list.add(i);
        }
        return list;
    }

    private int calcDay(int year, int month) {
        int days = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) ? 29 : 28;
                break;
        }
        return days;
    }

    private static class DateTextAdapter extends AbstractWheelTextAdapter1 {
        ArrayList<Integer> list;

        public DateTextAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        public void setList(ArrayList<Integer> list) {
            this.list = list;
            notifyDataChangedEvent();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list == null ? "" : String.valueOf(list.get(index));
        }

        @Override
        public int getItemsCount() {
            return list == null ? 0 : list.size();
        }
    }
}
