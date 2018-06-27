package com.android.hcbd.dailylog.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hcbd.dailylog.R;
import com.android.hcbd.dailylog.adapter.InputRecyclerAdapter;
import com.android.hcbd.dailylog.base.activity.BaseActivity;
import com.android.hcbd.dailylog.db.WorkLogDbTool;
import com.android.hcbd.dailylog.entity.WorkLogInfo;
import com.android.hcbd.dailylog.event.MessageEvent;
import com.haibin.calendarview.Calendar;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkLogEditActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_month_day)
    TextView tvMonthDay;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_lunar)
    TextView tvLunar;
    @BindView(R.id.rl_tool)
    RelativeLayout rlTool;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.btn_add1)
    Button btnAdd1;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @BindView(R.id.btn_add2)
    Button btnAdd2;
    @BindView(R.id.recyclerView3)
    RecyclerView recyclerView3;
    @BindView(R.id.btn_add3)
    Button btnAdd3;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_cheat)
    MaterialEditText etCheat;
    @BindView(R.id.et_plans)
    MaterialEditText etPlans;
    @BindView(R.id.et_conclusion)
    MaterialEditText etConclusion;

    private Calendar mCalendar;
    private InputRecyclerAdapter adapter1;
    private InputRecyclerAdapter adapter2;
    private InputRecyclerAdapter adapter3;

    private WorkLogInfo workLogInfo;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_log_edit);
        ButterKnife.bind(this);

        mCalendar = (Calendar) getIntent().getSerializableExtra("cal_info");
        tvMonthDay.setText(mCalendar.getMonth() + "月" + mCalendar.getDay() + "日");
        tvYear.setText(String.valueOf(mCalendar.getYear()));
        tvLunar.setText(mCalendar.getLunar());

        workLogInfo = (WorkLogInfo) getIntent().getSerializableExtra("info");
        index = getIntent().getIntExtra("index",0);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());

        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView3.setNestedScrollingEnabled(false);

        if(workLogInfo == null){
            adapter1 = new InputRecyclerAdapter(this,new ArrayList<String>());
            adapter2 = new InputRecyclerAdapter(this,new ArrayList<String>());
            adapter3 = new InputRecyclerAdapter(this,new ArrayList<String>());
        }else{
            if(TextUtils.isEmpty(workLogInfo.getContents())){
                adapter1 = new InputRecyclerAdapter(this,new ArrayList<String>());
            }else{
                List<String> list1 = new ArrayList<>();
                String[] s1 = workLogInfo.getContents().split(";");
                for(int i=0;i<s1.length;i++){
                    list1.add(s1[i]);
                }
                adapter1 = new InputRecyclerAdapter(this,list1);
            }
            if(TextUtils.isEmpty(workLogInfo.getVisits())){
                adapter2 = new InputRecyclerAdapter(this,new ArrayList<String>());
            }else{
                List<String> list2 = new ArrayList<>();
                String[] s2 = workLogInfo.getVisits().split(";");
                for(int i=0;i<s2.length;i++){
                    list2.add(s2[i]);
                }
                adapter2 = new InputRecyclerAdapter(this,list2);
            }
            if(TextUtils.isEmpty(workLogInfo.getPhoneComms())){
                adapter3 = new InputRecyclerAdapter(this,new ArrayList<String>());
            }else{
                List<String> list3 = new ArrayList<>();
                String[] s3 = workLogInfo.getPhoneComms().split(";");
                for(int i=0;i<s3.length;i++){
                    list3.add(s3[i]);
                }
                adapter3 = new InputRecyclerAdapter(this,list3);
            }
            etCheat.setText(workLogInfo.getCheats());
            etPlans.setText(workLogInfo.getPlanss());
            etConclusion.setText(workLogInfo.getConclusion());
        }
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);

        btnAdd1.setOnClickListener(this);
        btnAdd2.setOnClickListener(this);
        btnAdd3.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add1:
                if (adapter1.isEndInput()) {
                    adapter1.add();
                    adapter1.inserted();
                }
                break;
            case R.id.btn_add2:
                if (adapter2.isEndInput()) {
                    adapter2.add();
                    adapter2.inserted();
                }
                break;
            case R.id.btn_add3:
                if (adapter3.isEndInput()) {
                    adapter3.add();
                    adapter3.inserted();
                }
                break;
            case R.id.tv_save:
                MessageEvent messageEvent = new MessageEvent();
                if(workLogInfo == null){
                    workLogInfo = new WorkLogInfo();
                    workLogInfo.setDate(mCalendar.getYear()+"-"+mCalendar.getMonth()+"-"+mCalendar.getDay());
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String time = sdf.format(new java.util.Date());
                    workLogInfo.setTime(time);
                    workLogInfo.setWeek("星期"+mCalendar.getWeek());
                    workLogInfo.setContents(adapter1.getAllStr());
                    workLogInfo.setVisits(adapter2.getAllStr());
                    workLogInfo.setPhoneComms(adapter3.getAllStr());
                    workLogInfo.setCheats(etCheat.getText().toString());
                    workLogInfo.setPlanss(etPlans.getText().toString());
                    workLogInfo.setConclusion(etConclusion.getText().toString());
                    WorkLogDbTool.add(workLogInfo);
                    messageEvent.setEventId(MessageEvent.EVENT_ADD);
                }else{
                    workLogInfo.setContents(adapter1.getAllStr());
                    workLogInfo.setVisits(adapter2.getAllStr());
                    workLogInfo.setPhoneComms(adapter3.getAllStr());
                    workLogInfo.setCheats(etCheat.getText().toString());
                    workLogInfo.setPlanss(etPlans.getText().toString());
                    workLogInfo.setConclusion(etConclusion.getText().toString());
                    WorkLogDbTool.update(workLogInfo);
                    messageEvent.setEventId(MessageEvent.EVENT_EDIT);
                    messageEvent.setObj(workLogInfo);
                    messageEvent.setObj2(index);
                }
                EventBus.getDefault().post(messageEvent);
                finishActivity();
                break;
        }
    }

}
