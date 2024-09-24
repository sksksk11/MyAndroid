package com.example.mymusic.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/***
 *
 * 要在 RecyclerView 的瀑布流布局中根据图片大小自动调整 item 的大小，可以通过以下步骤实现：
 * 首先，创建一个自定义的 LayoutParams 类来管理 item 的布局参数
 * 然后，在 Adapter 中根据图片的尺寸来设置 LayoutParams：
 */

public class DynamicLayoutParams extends RecyclerView.LayoutParams {

    public DynamicLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public DynamicLayoutParams(int width, int height) {
        super(width, height);
    }

    public DynamicLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }

    public DynamicLayoutParams(ViewGroup.MarginLayoutParams source) {
        super(source);
    }

    @Override
    public void setMargins(int left, int top, int right, int bottom) {
        super.setMargins(left, top, right, bottom);
    }
}

//
//public class DynamicLayoutParams extends RecyclerView.LayoutParams {
//    public DynamicLayoutParams(Context c, AttributeSet attrs) {
//        super(c, attrs);
//    }
//
//    public DynamicLayoutParams(int width, int height) {
//        super(width, height);
//    }
//
//    public DynamicLayoutParams(ViewGroup.MarginLayoutParams source) {
//        super(source);
//    }
//
//    public DynamicLayoutParams(ViewGroup.LayoutParams source) {
//        super(source);
//    }
//
//    public DynamicLayoutParams(RecyclerView.LayoutParams source) {
//        super(source);
//    }
//
//    @Override
//    public void setMargins(int left, int top, int right, int bottom) {
//        super.setMargins(left, top, right, bottom);
//    }
//}
