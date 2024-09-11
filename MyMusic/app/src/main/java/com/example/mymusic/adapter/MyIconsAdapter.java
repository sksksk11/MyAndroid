package com.example.mymusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MyIconsAdapter extends BaseAdapter {

    private Context mContext;
    private int[] iconIds;

    public MyIconsAdapter(Context context, int[] iconIds) {
        this.mContext = context;
        this.iconIds = iconIds;
    }

    @Override
    public int getCount() {
        return iconIds.length;
    }

    @Override
    public Object getItem(int position) {
        return iconIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);

            int iconWidth = 80;    //设置图片宽度
            int iconHeight = 80;   //设置图片高度
            imageView.setLayoutParams(new ViewGroup.LayoutParams(iconWidth,iconHeight));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else{
            imageView = (ImageView) convertView;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),iconIds[position]);
        imageView.setImageBitmap(bitmap);

        return imageView;
    }
}
