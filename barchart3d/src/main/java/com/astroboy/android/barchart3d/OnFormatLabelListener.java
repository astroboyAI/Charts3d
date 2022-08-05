package com.astroboy.android.barchart3d;

/**
 * 格式化x y 轴
 *
 * @author 黄兴伟 (xwdz9989@gmail.com)
 * @since 2017/11/12
 */
public interface OnFormatLabelListener {

    String onFormatLabelListener(float label);

    String onFormatValueTextListener(float value);
}
