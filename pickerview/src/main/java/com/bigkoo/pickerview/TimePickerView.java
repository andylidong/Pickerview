package com.bigkoo.pickerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 * Created by Sai on 15/11/22.
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN , YEAR_MONTH
    }// 四种选择模式，年月日时分，年月日，时分，月日时分

    WheelTime wheelTime;
    private View btnSubmit, btnCancel;
    private Button btnToday;
    private TextView tvTitle;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private static final String TAG_TODAY = "today";
    private OnTimeSelectListener timeSelectListener;
    private Type type;
    private String timeFormatter;

    public TimePickerView(Context context, Type type) {
        super(context);
        this.type = type;
        LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnToday = (Button) findViewById(R.id.btnToday);
        btnToday.setTag(TAG_TODAY);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnToday.setOnClickListener(this);
        // 顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----时间转轮
        final View timepickerview = findViewById(R.id.timepicker);
        wheelTime = new WheelTime(timepickerview, type);
        // 默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE) / 5;
        wheelTime.setPicker(year, month, day, hours, minute);
    }


    /**
     * 设置可以选择的时间范围
     * 要在setTime之前调用才有效果
     * @param startYear 开始年份
     * @param endYear 结束年份
     */
    public void setRange(int startYear, int endYear) {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);
    }

    /**
     * 设置选中时间
     * @param date 时间
     */
    public void setTime(Date date, String timeFormatter) {
        // 显示时间的格式
        this.timeFormatter = timeFormatter;
        // 显示标题信息
        this.setTitle(getTime(date, timeFormatter));
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        else {
            calendar.setTime(date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE) / 5;
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置picker的标题
     * @param title
     */
    public void setTitle(String title){
        if (TextUtils.isEmpty(title)) {
            wheelTime.setOnTimeSelectListener(new WheelTime.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(String year, String month, String day, String hour, String minutes) {
                    StringBuffer sb = new StringBuffer();
                    if (!TextUtils.isEmpty(year)) {
                        sb.append(year).append("-");
                    }
                    if (!TextUtils.isEmpty(month)) {
                        sb.append(month).append("-");
                    }
                    if (!TextUtils.isEmpty(day)) {
                        sb.append(day).append(" ");
                    }
                    if (!TextUtils.isEmpty(hour)) {
                        sb.append(hour).append(":");
                    }
                    if (!TextUtils.isEmpty(minutes)) {
                        sb.append(minutes);
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date time = null;
                    try {
                        time = formatter.parse(sb.toString());
                        setTitle(getTime(time, timeFormatter));
                    } catch (Exception e) {
                        setTitle("");
                    }
                }
            });
            return;
        }
        tvTitle.setText(title);
    }


    /**
     * 设置是否循环滚动
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        switch (tag) {
            case TAG_CANCEL:
                dismiss();
                break;
            case TAG_SUBMIT:
                if (timeSelectListener != null) {
                    try {
                        Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                        timeSelectListener.onTimeSelect(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
                break;
            case TAG_TODAY:
                if (timeSelectListener != null) {
                    timeSelectListener.onTimeSelect(new Date());
                }
                dismiss();
                break;
        }
    }

    /**
     * 监听选中的时间信息
     */
    public interface OnTimeSelectListener {
        void onTimeSelect(Date date);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    /**
     * 获取想要的时间格式
     * @param date
     * @return
     */
    public static String getTime(Date date, String timeFormatter) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormatter);
        return format.format(date);
    }
}
