package com.astroboy.android.charts3d;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.astroboy.android.barchart3d.BarChartEntry;
import com.astroboy.android.barchart3d.BarChartView;
import com.astroboy.android.barchart3d.EntryConfig;
import com.astroboy.android.barchart3d.OnFormatLabelListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DemoActivity extends Activity {

    private BarChartView mBarChartView;
    private static final int K_YUAN = 1000;
    private static final DecimalFormat K_YUAN_FORMAT = new DecimalFormat("#0.#");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBarChartView = findViewById(R.id.test_bcv);
        mBarChartView.setConfig(new EntryConfig()
                .setGravity(Gravity.CENTER)
                .setSpaceWidth(80)
                .setAnimation(true)
                .setShowRectTopText(true)
                .setDrawLabel(true)
                .setAnimatorTime(1000)
        );
        mBarChartView.setOnFormatLabelListener(new OnFormatLabelListener() {
            @Override
            public String onFormatLabelListener(float value) {
                if (value >= K_YUAN) {
                    return K_YUAN_FORMAT.format(value / K_YUAN) + "k";
                } else if (value >= 0) {
                    return K_YUAN_FORMAT.format(value);
                } else {
                    return "";
                }
            }

            @Override
            public String onFormatValueTextListener(float value) {
                return (int) value + "k";
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBarChartData();
    }

    private void initBarChartData(){
        List<YearData> yearDatas = new ArrayList<>();
        YearData yearData1 = new YearData("2020",10000);
        YearData yearData2 = new YearData("2021",11000);
        YearData yearData3 = new YearData("2022",12000);
        yearDatas.add(yearData1);
        yearDatas.add(yearData2);
        yearDatas.add(yearData3);
        loadBarChartData(yearDatas);
    }
    private void loadBarChartData(List<YearData> yearData){
        mBarChartView.setData(getData(this,yearData));
    }

    private List<BarChartEntry> getData(Context pContext,List<YearData> yearDatas) {
        List<BarChartEntry> mBarChartEntries = new ArrayList<>();
        Collections.sort(yearDatas, new Comparator<YearData>() {
            @Override
            public int compare(YearData yearData, YearData t1) {
                return yearData.year.compareTo(t1.year);
            }
        });
        for (int i = 0, size = yearDatas.size(); i < size; i++) {
            YearData yearData = yearDatas.get(i);
            mBarChartEntries.add(new BarChartEntry(yearData.year, yearData.value, getColorByIndex(this, i)));
        }
        return mBarChartEntries;
    }

    public int getColorByIndex(Context pContext, int index) {
        int resId = 0;
        switch (index) {
            case 0:
                resId = R.color.orange_yellow;
                break;
            case 1:
                resId = R.color.sky_blue;
                break;
            case 2:
                resId = R.color.blood_red;
                break;
        }
        if (resId <= 0) {
            return 0;
        }
        return pContext.getResources().getColor(resId);
    }

    public static class YearData {
        public String year;
        public long value;

        public YearData(String pYear, long pValue) {
            year = pYear;
            value = pValue;
        }
    }
}
