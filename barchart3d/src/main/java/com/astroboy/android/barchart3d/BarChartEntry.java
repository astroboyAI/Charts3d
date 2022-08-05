package com.astroboy.android.barchart3d;


/**
 * @author 黄兴伟 (xwdz9989@gmail.com)
 * @since 2017/11/12
 */

public class BarChartEntry {

    public String lable;
    public float value;
    public int colorId;

    public BarChartEntry(String pLable, float pValue, int colorId) {
        this.lable = pLable;
        this.value = pValue;
        this.colorId = colorId;
    }
}
