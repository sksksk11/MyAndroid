package com.example.mymusic.utils;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.adapter.MyBookmarkListAdapter;
import com.example.mymusic.data.WebInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final RecyclerView.Adapter adapter;


    public MyItemTouchHelperCallback(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 定义拖动和滑动的方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

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
        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
        super.clearView(recyclerView, viewHolder);
    }

    

}
