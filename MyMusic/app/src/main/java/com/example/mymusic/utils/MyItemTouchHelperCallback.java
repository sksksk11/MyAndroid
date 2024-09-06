package com.example.mymusic.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.adapter.MyBookmarkListAdapter;
import com.example.mymusic.data.WebInfo;

import java.util.Collections;
import java.util.List;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private Context context;

    public MyItemTouchHelperCallback(RecyclerView.Adapter adapter,RecyclerView recyclerView,Context context) {
        this.adapter = adapter;
        this.mRecyclerView = recyclerView;
        this.context = context;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 定义拖动和滑动的方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        int swipeFlags = ItemTouchHelper.LEFT;
//        int swipeFlags = 0; //禁用滑动功能

        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
        adapter.notifyItemMoved(source.getAdapterPosition(),target.getAdapterPosition());
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        MyBookmarkListAdapter bookmarkListAdapter = (MyBookmarkListAdapter) recyclerView.getAdapter();
        List<WebInfo> updatedDataList  = bookmarkListAdapter.getWebInfoList();
        Collections.swap(updatedDataList,fromPosition,toPosition);   //交换两个位置的元素
        Log.d("TAG", "source: "+fromPosition+"，target："+toPosition);
        Log.d("TAG", "排序后新列表: "+updatedDataList.toString());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//        Log.d("TAG", "menuView: 9");
        View deleteButton = viewHolder.itemView.findViewById(R.id.iv_delete);
        View btn_config = viewHolder.itemView.findViewById(R.id.iv_config);
        View btn_toTop = viewHolder.itemView.findViewById(R.id.iv_toTop);
        View btn_toBottom = viewHolder.itemView.findViewById(R.id.iv_toBottom);


        if(deleteButton!=null){
            //隐藏删除按钮
            deleteButton.setVisibility(View.GONE);
            btn_config.setVisibility(View.GONE);
            btn_toTop.setVisibility(View.GONE);
            btn_toBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //仅在侧滑时绘制
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View deleteButton = viewHolder.itemView.findViewById(R.id.iv_delete);
            View configButton = viewHolder.itemView.findViewById(R.id.iv_config);
            View toTopButton = viewHolder.itemView.findViewById(R.id.iv_toTop);
            View toBottomButton = viewHolder.itemView.findViewById(R.id.iv_toBottom);

            if(deleteButton!=null){
                //显示删除按钮
                deleteButton.setVisibility(View.VISIBLE);
                configButton.setVisibility(View.VISIBLE);
                toTopButton.setVisibility(View.VISIBLE);
                toBottomButton.setVisibility(View.VISIBLE);
                //根据dx的值调整按钮的位置
                deleteButton.setTranslationX(dX);
                configButton.setTranslationX(dX);
                toTopButton.setTranslationX(dX);
                toBottomButton.setTranslationX(dX);
            }

            // 遍历所有可见的 items 并隐藏它们的删除按钮
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);
                RecyclerView.ViewHolder viewHolderChild = recyclerView.getChildViewHolder(child);
                if (viewHolderChild != viewHolder && viewHolderChild.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    View button = child.findViewById(R.id.iv_delete);
                    View btn_config = child.findViewById(R.id.iv_config);
                    View btn_toTop = child.findViewById(R.id.iv_toTop);
                    View btn_toBottom = child.findViewById(R.id.iv_toBottom);

                    if (button != null) {
                        button.setVisibility(View.GONE);
                        btn_config.setVisibility(View.GONE);
                        btn_toTop.setVisibility(View.GONE);
                        btn_toBottom.setVisibility(View.GONE);
                    }
                }
            }


        }else {
            // 如果没有侧滑动作或者侧滑动作不活跃，隐藏所有删除按钮
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);
                RecyclerView.ViewHolder viewHolderChild = recyclerView.getChildViewHolder(child);
                if (viewHolderChild.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    View button = child.findViewById(R.id.iv_delete);
                    View btn_config = child.findViewById(R.id.iv_config);
                    View btn_toTop = child.findViewById(R.id.iv_toTop);
                    View btn_toBottom = child.findViewById(R.id.iv_toBottom);

                    if (button != null) {
                        button.setVisibility(View.GONE);
                        btn_config.setVisibility(View.GONE);
                        btn_toTop.setVisibility(View.GONE);
                        btn_toBottom.setVisibility(View.GONE);

                    }
                }
            }
        }


    }


    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState!= ItemTouchHelper.ACTION_STATE_IDLE){
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_light));
        }

        super.onSelectedChanged(viewHolder, actionState);

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(0);


        View deleteButton = viewHolder.itemView.findViewById(R.id.iv_delete);
        if(deleteButton!=null){
            //隐藏删除按钮
                deleteButton.setVisibility(View.GONE);
            }

        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
//        return super.getSwipeThreshold(viewHolder);
        return 2f;  // 设置更高的滑动阈值，例如 0.9，表示用户必须滑动超过视图宽度的 90%
    }


}
