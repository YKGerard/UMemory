package com.example.umemory;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ${HYK} on 2017/3/26.
 */

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder>{
    private Context mContext;
    private List<Memory> mMemory;

    //定义一个内部类ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView mTitle;
        TextView mContent;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView;
            mTitle=(TextView)itemView.findViewById(R.id.mtitle);
            mContent=(TextView)itemView.findViewById(R.id.mcontent);
        }
    }

    public MemoryAdapter(List<Memory> memoryList){
        mMemory = memoryList;
    }

    //创建ViewHolder实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.memory_item,parent,false);
        return new ViewHolder(view);
    }

    //对RecycleView子项的数据进行赋值，会在每个子项被滚动到屏幕内时执行
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Memory memory=mMemory.get(position);
        holder.mTitle.setText(memory.getTitle());
        holder.mContent.setText(memory.getContent());
    }

    //高数RecycleView子项的总数
    @Override
    public int getItemCount() {
        return mMemory.size();
    }
}
