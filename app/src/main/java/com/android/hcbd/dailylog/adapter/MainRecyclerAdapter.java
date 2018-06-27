package com.android.hcbd.dailylog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hcbd.dailylog.R;
import com.android.hcbd.dailylog.entity.WorkLogInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guocheng on 2018/1/3.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<WorkLogInfo> list = new ArrayList<>();
    public MainRecyclerAdapter(Context context){
        this.context = context;
    }

    public void clear(){
        list.clear();
    }

    public void setData(List<WorkLogInfo> list){
        this.list = list;
    }

    public List<WorkLogInfo> getAllData(){
        return list;
    }

    public void update(WorkLogInfo info,int index){
        list.set(index,info);
        notifyItemChanged(index,info);
    }

    public void remove(int index){
        list.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(list.size() > 0)
            return new MyHolder1(LayoutInflater.from(context).inflate(R.layout.item_main_work, null));
        else
            return new MyHolder2(LayoutInflater.from(context).inflate(R.layout.view_empty, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(list.size() > 0){
            ((MyHolder1)holder).tv_title.setText("今日工作："+list.get(position).getContents().replace(";"," ; "));
            ((MyHolder1)holder).tv_content.setText("今日拜访："+list.get(position).getVisits().replace(";"," ; "));
            ((MyHolder1)holder).tv_time.setText(list.get(position).getTime());
            ((MyHolder1)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != itemClickListener) {
                        itemClickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }

    public static class MyHolder1 extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.tv_time)
        TextView tv_time;
        public MyHolder1(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public static class MyHolder2 extends RecyclerView.ViewHolder{

        public MyHolder2(View itemView){
            super(itemView);
        }
    }

    public  interface onRecyclerViewItemClickListener {
        void onItemClick(View v, int position);
    }

    private onRecyclerViewItemClickListener itemClickListener = null;

    public void setOnItemClickListener(onRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

}
