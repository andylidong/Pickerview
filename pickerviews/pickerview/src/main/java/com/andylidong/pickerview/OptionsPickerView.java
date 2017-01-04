package com.andylidong.pickerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andylidong.pickerview.listener.OnOptionsSelectListener;
import com.andylidong.pickerview.view.BasePickerView;
import com.andylidong.pickerview.view.WheelOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 选项选择器
 * Created by Andy.Li on 15/11/22.
 */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
    private WheelOptions<T> wheelOptions;
    private Button btnSubmit, btnCancel;
    private TextView tvTitle;
    private OnOptionsSelectListener optionsSelectListener;
    private List<T> optionsItems1 = new ArrayList<>();
    private ArrayList<ArrayList<T>> optionsItems2 = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<T>>> optionsItems3 = new ArrayList<>();
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    public OptionsPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer);
        // -----确定和取消按钮
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----转轮
        final View optionspicker = findViewById(R.id.optionspicker);
        wheelOptions = new WheelOptions(optionspicker);
    }

    public void setPicker(ArrayList<T> optionsItems) {
        this.optionsItems1 = optionsItems;
        wheelOptions.setPicker(optionsItems, null, null, false);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items, boolean linkage) {
        this.optionsItems1 = options1Items;
        this.optionsItems2 = options2Items;
        wheelOptions.setPicker(options1Items, options2Items, null, linkage);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items,
                          ArrayList<ArrayList<ArrayList<T>>> options3Items,
                          boolean linkage) {
        this.optionsItems1 = options1Items;
        this.optionsItems2 = options2Items;
        this.optionsItems3 = options3Items;
        wheelOptions.setPicker(options1Items, options2Items, options3Items,
                linkage);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1 位置
     */
    public void setSelectOptions(int option1) {
        // 设置标题
        this.setTitle(optionsItems1.get(option1).toString());
        // 设置当前的选项
        wheelOptions.setCurrentItems(option1, 0, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1 位置
     * @param option2 位置
     */
    public void setSelectOptions(int option1, int option2) {
        wheelOptions.setCurrentItems(option1, option2, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1 位置
     * @param option2 位置
     * @param option3 位置
     */
    public void setSelectOptions(int option1, int option2, int option3) {
        wheelOptions.setCurrentItems(option1, option2, option3);
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     */
    public void setLabels(String label1) {
        wheelOptions.setLabels(label1, null, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     * @param label2 单位
     */
    public void setLabels(String label1, String label2) {
        wheelOptions.setLabels(label1, label2, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1 单位
     * @param label2 单位
     * @param label3 单位
     */
    public void setLabels(String label1, String label2, String label3) {
        wheelOptions.setLabels(label1, label2, label3);
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic 是否循环
     */
    public void setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
    }


    /**
     * 设置picker标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            wheelOptions.setOnoptionsSelectListener(new OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    tvTitle.setText(optionsItems1.get(options1).toString() + "-"
                            + optionsItems2.get(option2).toString() + "-"
                            + optionsItems3.get(options3).toString() );
                }

                @Override
                public void onOptionsSelect(int options1) {
                    tvTitle.setText(optionsItems1.get(options1).toString());
                }
            });
            return;
        }
        tvTitle.setText(title);
        wheelOptions.setOnoptionsSelectListener(null);
    }

    /**
     * 设置确定的按钮信息
     *
     * @param submitText
     */
    public void setSubmit(String submitText) {
        if (TextUtils.isEmpty(submitText)) {
            return;
        }
        btnSubmit.setText(submitText);
    }

    /**
     * 设置取消的按钮信息
     *
     * @param cancelText
     */
    public void setCancel(String cancelText) {
        if (TextUtils.isEmpty(cancelText)) {
            return;
        }
        btnCancel.setText(cancelText);
    }

    /**
     * 判断数据是否重复显示
     *
     * @param cyclic1
     * @param cyclic2
     * @param cyclic3
     */
    public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (optionsSelectListener != null) {
                if (wheelOptions.getCurrentItems().length > 1) {
                    int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                    optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
                } else {
                    optionsSelectListener.onOptionsSelect(wheelOptions.getCurrentItems()[0]);
                }
            }
            dismiss();
            return;
        }
    }

    /**
     * 监听选中的时间信息
     */
    public void setOnoptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }
}