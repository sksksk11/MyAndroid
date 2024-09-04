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
        View deleteButton = viewHolder.itemView.findViewById(R.id.btn_delete);
        if(deleteButton!=null){
            //隐藏删除按钮
            deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        //仅在侧滑时绘制
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            View deleteButton = viewHolder.itemView.findViewById(R.id.btn_delete);
            if(deleteButton!=null){
                //显示删除按钮
                deleteButton.setVisibility(View.VISIBLE);
                //根据dx的值调整按钮的位置
                deleteButton.setTranslationX(dX);
            }
        }
        Log.d("TAG", "menuView: 8");
    }


    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState!= ItemTouchHelper.ACTION_STATE_IDLE){
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_light));
        }
        Log.d("TAG", "menuView: 3");
        super.onSelectedChanged(viewHolder, actionState);

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(0);
        super.clearView(recyclerView, viewHolder);

        View deleteButton = viewHolder.itemView.findViewById(R.id.btn_delete);
        if(deleteButton!=null){
            //隐藏删除按钮
                deleteButton.setVisibility(View.GONE);
            }
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
//        return super.getSwipeThreshold(viewHolder);
        return 0.9f;  // 设置更高的滑动阈值，例如 0.9，表示用户必须滑动超过视图宽度的 90%
    }
}
