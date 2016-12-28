package com.bigkoo.pickerview.adapter;


import android.text.TextUtils;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {

	/**
	 * The default min value
	 */
	public static final int DEFAULT_MAX_VALUE = 9;

	/**
	 * The default max value
	 */
	private static final int DEFAULT_MIN_VALUE = 0;

	private Format initNumFormat = new DecimalFormat("00");

	// Values
	private int minValue;
	private int maxValue;
	private String unit;

	/**
	 * Default constructor
	 */
	public NumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 *
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.unit = "";
	}

	/**
	 * Constructor
	 * @param minValue
	 * @param maxValue
	 * @param unit
     */
	public NumericWheelAdapter(int minValue, int maxValue,String unit) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.unit = unit;
	}

	@Override
	public Object getItem(int index) {
		if (!TextUtils.isEmpty(unit)) {
			if (index >= 0 && index < getItemsCount()) {
				int value = minValue + index;
				// 返回5的倍数
				return initNumFormat.format(value * 5);
			}
		} else {
			if (index >= 0 && index < getItemsCount()) {
				int value = minValue + index;
				return initNumFormat.format(value);
			}
		}
		return 0;
	}

	@Override
	public int getItemsCount() {
		if (!TextUtils.isEmpty(unit)) {
			return (maxValue - minValue + 1) / 5;
		}
		return maxValue - minValue + 1;
	}

	@Override
	public int indexOf(Object o) {
		try {
//			if (!TextUtils.isEmpty(unit)) {
//				return (Integer.valueOf(o.toString()).intValue() - minValue) * 5;
//			}
			return Integer.valueOf(o.toString()).intValue() - minValue;
		} catch (Exception e) {
			return 0;
		}
	}
}
