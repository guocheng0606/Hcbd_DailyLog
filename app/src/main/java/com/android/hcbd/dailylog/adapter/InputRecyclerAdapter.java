package com.android.hcbd.dailylog.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hcbd.dailylog.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guocheng on 2017/12/18.
 */

public class InputRecyclerAdapter extends RecyclerView.Adapter<InputRecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<String> list;
    private boolean flag;
    public InputRecyclerAdapter(Context mContext,List<String> list){
        this.mContext = mContext;
        this.list = list;
        if(this.list.size() == 0)
            this.list.add("");
    }

    public void add(){
        this.list.add("");
    }

    public void inserted(){
        flag = true;
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler, null);
        MyHolder myholder = new MyHolder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.et.setText(list.get(position));
        holder.et.addTextChangedListener(new TextSwitcher(holder));
        holder.et.setTag(position);

        if(flag && position == getItemCount() -1){
            holder.et.setFocusable(true);
            holder.et.setFocusableInTouchMode(true);
            holder.et.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean isEndInput(){
        if(TextUtils.isEmpty(list.get(getItemCount()-1)))
            return false;
        else
            return true;
    }

    public String getAllStr(){
        String str = "";
        for(String s : list){
            if(!TextUtils.isEmpty(s)){
                str += ";"+s;
            }
        }
        if(!TextUtils.isEmpty(str)){
            str = str.substring(1,str.length());
        }
        return str;
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.et)
        MaterialEditText et;
        public MyHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class TextSwitcher implements TextWatcher {

        private MyHolder myViewHolder;

        public TextSwitcher(MyHolder myViewHolder) {
            this.myViewHolder = myViewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable!=null){
                list.set(Integer.parseInt(myViewHolder.et.getTag().toString()),editable.toString());
            }
        }
    }



}
