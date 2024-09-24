package com.example.mymusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.mymusic.R;
import com.example.mymusic.data.WebImage;

import java.util.List;


public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<WebImage> mWebImageList;
    private OnItemClickedListener onItemClickedListener;

    public MyImageAdapter(Context context, List<WebImage> webImageList) {
        mContext = context;
        mWebImageList = webImageList;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imgUrl = mWebImageList.get(position).getImgUrl();

        //使用Glide 库加载网络图片
        Glide.with(holder.itemView.getContext())
//                .asBitmap()
                .load(imgUrl)
//                .override(500, 500)  // 这里设置只加载宽和高都超过 500 像素的图片
                .into(holder.iv_image);



    }

    @Override
    public int getItemCount() {
        return mWebImageList == null ?  0 : mWebImageList.size() ;

    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_image,iv_bigImage;
        private CheckBox cb_isImageSelected;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_image = itemView.findViewById(R.id.iv_image);
            cb_isImageSelected = itemView.findViewById(R.id.cb_isImageSelected);
            iv_bigImage = itemView.findViewById(R.id.iv_bigImage);

            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickedListener.OnItemClicked(getAdapterPosition());
                }
            });

        }
    }


    public interface OnItemClickedListener{

        public void OnItemClicked(int position);

    }

    public void setOnItemClickedListener(OnItemClickedListener onItemclickedListener){
        this.onItemClickedListener = onItemclickedListener;
    }


}
