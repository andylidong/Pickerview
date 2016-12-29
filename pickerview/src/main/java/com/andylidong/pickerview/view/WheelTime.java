package com.andylidong.pickerview.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.andylidong.pickerview.TimePickerView.Type;
import com.andylidong.pickerview.adapter.NumericWheelAdapter;
import com.andylidong.pickerview.lib.WheelView;
import com.andylidong.pickerview.listener.OnItemSelectedListener;
import com.andylidong.pickerview.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


public class WheelTime {
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;

	private TextView tvYear, tvMonth, tvDay, tvHour, tvMinute;

	private Type type;
	public static final int DEFULT_START_YEAR = 1990;
	public static final int DEFULT_END_YEAR = 2100;
	private int startYear = DEFULT_START_YEAR;
	private int endYear = DEFULT_END_YEAR;

	// 记录当前的日期
	private OnTimeSelectListener timeSelectListener;
	private static String currentYear = "";
	private static String currentMonth = "";
	private static String currentDay = "";
	private static String currentHour = "";
	private static String currentMinute = "";

	public WheelTime(View view) {
		super();
		this.view = view;
		type = Type.ALL;
		setView(view);
	}
	public WheelTime(View view,Type type) {
		super();
		this.view = view;
		this.type = type;
		setView(view);
	}
	public void setPicker(int year ,int month,int day){
		this.setPicker(year, month, day, 0, 0);
	}
	
	public void setPicker(int year ,int month ,int day,int h,int m) {
		currentYear = setTime(year);
		currentMonth = setTime(month + 1);
		currentDay = setTime(day);
		currentHour = setTime(h);
		currentMinute = setTime(m * 5);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
		String[] months_little = {"4", "6", "9", "11"};

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		Context context = view.getContext();

		// 获取时间的单位信息
		tvYear = (TextView) view.findViewById(R.id.tv_year);
		tvYear.setText(context.getString(R.string.pickerview_year));
		tvMonth = (TextView) view.findViewById(R.id.tv_month);
		tvMonth.setText(context.getString(R.string.pickerview_month));
		tvDay = (TextView) view.findViewById(R.id.tv_day);
		tvDay.setText(context.getString(R.string.pickerview_day));
		tvHour = (TextView) view.findViewById(R.id.tv_hour);
		tvHour.setText(context.getString(R.string.pickerview_hours));
		tvMinute = (TextView) view.findViewById(R.id.tv_minute);
		tvMinute.setText(context.getString(R.string.pickerview_minutes));

		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(startYear, endYear));// 设置"年"的显示数据
		wv_year.setCurrentItem(year - startYear);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setCurrentItem(day - 1);


		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCurrentItem(h);

		wv_mins = (WheelView) view.findViewById(R.id.min);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "分钟"));
		wv_mins.setCurrentItem(m);

		// 添加"年"监听
		OnItemSelectedListener wheelListener_year = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index) {
				int year_num = index + startYear;
				// 判断大小月及是否闰年,用来确定"日"的数据
				int maxItem = 30;
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					maxItem = 31;
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					maxItem = 30;
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						maxItem = 29;
					} else {
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
						maxItem = 28;
					}
				}
				if (wv_day.getCurrentItem() > maxItem - 1) {
					wv_day.setCurrentItem(maxItem - 1);
					currentDay = setTime(maxItem);
				}
				// 得到年份的信息
				if (timeSelectListener != null) {
					currentYear = setTime(wv_year.getCurrentItem() + startYear);
					timeSelectListener.onTimeSelect(currentYear, currentMonth, currentDay, currentHour, currentMinute);
				}
			}
		};
		// 添加"月"监听
		OnItemSelectedListener wheelListener_month = new OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index) {
				int month_num = index + 1;
				int maxItem = 30;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
					maxItem = 31;
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
					maxItem = 30;
				} else {
					if (((wv_year.getCurrentItem() + startYear) % 4 == 0 && (wv_year
							.getCurrentItem() + startYear) % 100 != 0)
							|| (wv_year.getCurrentItem() + startYear) % 400 == 0) {
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
						maxItem = 29;
					} else {
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
						maxItem = 28;
					}
				}
				if (wv_day.getCurrentItem() > maxItem - 1) {
					wv_day.setCurrentItem(maxItem - 1);
					currentDay = setTime(maxItem);
				}
				// 得到月份的信息
				if (timeSelectListener != null) {
					currentMonth = setTime(wv_month.getCurrentItem() + 1 );
					timeSelectListener.onTimeSelect(currentYear, currentMonth, currentDay, currentHour, currentMinute);
				}
			}
		};
		// 日的监听者
		OnItemSelectedListener wheelListener_day = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				// 得到日期的信息
				if (timeSelectListener != null) {
					currentDay = setTime(wv_day.getCurrentItem() + 1);
					timeSelectListener.onTimeSelect(currentYear, currentMonth, currentDay, currentHour, currentMinute);
				}
			}
		};
		// 小时的监听者
		OnItemSelectedListener wheelListener_hour = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				// 得到小时的信息
				if (timeSelectListener != null) {
					currentHour = setTime(wv_hours.getCurrentItem());
					timeSelectListener.onTimeSelect(currentYear, currentMonth, currentDay, currentHour, currentMinute);
				}
			}
		};
		// 分钟的监听者
		OnItemSelectedListener wheelListener_minute = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				// 得到分钟的信息
				if (timeSelectListener != null) {
					currentMinute = setTime(wv_mins.getCurrentItem());
					timeSelectListener.onTimeSelect(currentYear, currentMonth, currentDay, currentHour, currentMinute);
				}
			}
		};
		// 注册监听者
		wv_year.setOnItemSelectedListener(wheelListener_year);
		wv_month.setOnItemSelectedListener(wheelListener_month);
		wv_day.setOnItemSelectedListener(wheelListener_day);
		wv_hours.setOnItemSelectedListener(wheelListener_hour);
		wv_mins.setOnItemSelectedListener(wheelListener_minute);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 6;
		switch (type) {
			case ALL:
				textSize = textSize * 3;
				break;
			case YEAR_MONTH_DAY:
				textSize = textSize * 4;
				wv_hours.setVisibility(View.GONE);
				wv_mins.setVisibility(View.GONE);

				tvHour.setVisibility(View.GONE);
				tvMinute.setVisibility(View.GONE);
				break;
			case HOURS_MINS:
				textSize = textSize * 4;
				wv_year.setVisibility(View.GONE);
				wv_month.setVisibility(View.GONE);
				wv_day.setVisibility(View.GONE);

				tvYear.setVisibility(View.GONE);
				tvMonth.setVisibility(View.GONE);
				tvDay.setVisibility(View.GONE);
				break;
			case MONTH_DAY_HOUR_MIN:
				textSize = textSize * 3;
				wv_year.setVisibility(View.GONE);
				tvYear.setVisibility(View.GONE);
				break;
			case YEAR_MONTH:
				textSize = textSize * 4;
				wv_day.setVisibility(View.GONE);
				wv_hours.setVisibility(View.GONE);
				wv_mins.setVisibility(View.GONE);

				tvDay.setVisibility(View.GONE);
				tvHour.setVisibility(View.GONE);
				tvMinute.setVisibility(View.GONE);
		}
		wv_day.setTextSize(textSize);
		wv_month.setTextSize(textSize);
		wv_year.setTextSize(textSize);
		wv_hours.setTextSize(textSize);
		wv_mins.setTextSize(textSize);
	}

	/**
	 * 设置是否循环滚动
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic){
		wv_year.setCyclic(cyclic);
		wv_month.setCyclic(cyclic);
		wv_day.setCyclic(cyclic);
		wv_hours.setCyclic(cyclic);
		wv_mins.setCyclic(cyclic);
	}


	/**
	 * 设置年份的信息
	 * @param yearText
	 */
	public void setYearText(String yearText) {
		if(TextUtils.isEmpty(yearText)) {
			return;
		}
		tvYear.setText(yearText);
	}

	/**
	 * 设置月份的信息
	 * @param monthText
	 */
	public void setMonthText(String monthText) {
		if(TextUtils.isEmpty(monthText)) {
			return;
		}
		tvMonth.setText(monthText);
	}


	/**
	 * 设置日期的信息
	 * @param dayText
	 */
	public void setDayText(String dayText) {
		if(TextUtils.isEmpty(dayText)) {
			return;
		}
		tvDay.setText(dayText);
	}


	/**
	 * 设置小时的信息
	 * @param hourText
	 */
	public void setHourText(String hourText) {
		if(TextUtils.isEmpty(hourText)) {
			return;
		}
		tvHour.setText(hourText);
	}


	/**
	 * 设置分钟的信息
	 * @param minuteText
	 */
	public void setMinuteText(String minuteText) {
		if(TextUtils.isEmpty(minuteText)) {
			return;
		}
		tvMinute.setText(minuteText);
	}

	/**
	 * 设置显示item的数目
	 * @param itemsVisible
     */
	public void setItemsVisible(int itemsVisible) {
		wv_year.setItemsVisible(itemsVisible);
		wv_month.setItemsVisible(itemsVisible);
		wv_day.setItemsVisible(itemsVisible);
		wv_hours.setItemsVisible(itemsVisible);
		wv_mins.setItemsVisible(itemsVisible);
	}


	/**
	 * 获取选中的时间
	 * @return
     */
	public String getTime() {
		StringBuffer sb = new StringBuffer();
			sb.append((wv_year.getCurrentItem() + startYear)).append("-")
			.append((wv_month.getCurrentItem() + 1)).append("-")
			.append((wv_day.getCurrentItem() + 1)).append(" ")
			.append(wv_hours.getCurrentItem()).append(":")
			.append(wv_mins.getCurrentItem());
		return sb.toString();
	}

	public String setTime(int time) {
		if (time < 10) {
			return "0" + time;
		}
		return time + "";
	}


	/**
	 * 监听选中的时间信息
	 */
	public interface OnTimeSelectListener {
		void onTimeSelect(String year, String month, String day, String hour, String minutes);
	}

	public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
		this.timeSelectListener = timeSelectListener;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
}
