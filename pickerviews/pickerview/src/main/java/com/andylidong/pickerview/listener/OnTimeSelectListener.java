package com.andylidong.pickerview.listener;

import java.util.Date;

/**
 * Created by Andy.Li on 2017/1/4.
 */

public interface OnTimeSelectListener {
    void onTimeSelect(Date date);
    void onTimeSelect(String year, String month, String day, String hour, String minutes);
}
