package com.android.hcbd.dailylog.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hcbd.dailylog.R;
import com.android.hcbd.dailylog.adapter.MainRecyclerAdapter;
import com.android.hcbd.dailylog.base.activity.BaseActivity;
import com.android.hcbd.dailylog.db.WorkLogDbTool;
import com.android.hcbd.dailylog.entity.WorkLogInfo;
import com.android.hcbd.dailylog.event.MessageEvent;
import com.android.hcbd.dailylog.utils.LogUtils;
import com.android.hcbd.dailylog.utils.ToastUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.melnykov.fab.FloatingActionButton;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        View.OnClickListener,
        /*CalendarView.OnDateSelectedListener,*/
        CalendarView.OnDateChangeListener/*,
        CalendarView.OnYearChangeListener */ {


    @BindView(R.id.tv_month_day)
    TextView mTextMonthDay;
    @BindView(R.id.tv_year)
    TextView mTextYear;
    @BindView(R.id.tv_lunar)
    TextView mTextLunar;
    @BindView(R.id.ib_calendar)
    ImageView ibCalendar;
    @BindView(R.id.tv_current_day)
    TextView mTextCurrentDay;
    @BindView(R.id.rl_tool)
    RelativeLayout mRelativeTool;
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fl_current)
    FrameLayout flCurrent;

    private int mYear;
    private MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        setSchemesData();
        initListener();
        checkUpdate();
    }

    private void initListener() {
        fab.setOnClickListener(this);
        mTextMonthDay.setOnClickListener(this);
        flCurrent.setOnClickListener(this);
        mCalendarView.setOnDateChangeListener(this);
        adapter.setOnItemClickListener(new MainRecyclerAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, WordLogShowActivity.class);
                intent.putExtra("info", adapter.getAllData().get(position));
                intent.putExtra("index", position);
                intent.putExtra("cal_info", mCalendarView.getSelectedCalendar());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_ADD:
                setRelcycleView();
                setSchemesData();
                break;
            case MessageEvent.EVENT_EDIT:
                WorkLogInfo info = (WorkLogInfo) event.getObj();
                int index = (int) event.getObj2();
                adapter.update(info, index);
                break;
            case MessageEvent.EVENT_DELETE:
                int i = (int) event.getObj();
                adapter.remove(i);
                setSchemesData();
                break;
        }
    }

    private void initView() {
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //setRelcycleView();
    }

    private void setRelcycleView() {
        List<WorkLogInfo> list = WorkLogDbTool.queryByDate(mCalendarView.getSelectedCalendar().getYear() + "-" + mCalendarView.getSelectedCalendar().getMonth() + "-" + mCalendarView.getSelectedCalendar().getDay());
        if(adapter == null )
            adapter = new MainRecyclerAdapter(this);
        adapter.setData(list);
        mRecyclerView.setAdapter(adapter);
    }

    protected void setSchemesData() {
        List<Calendar> schemes = new ArrayList<>();
        List<WorkLogInfo> list = WorkLogDbTool.queryAll();
        for (WorkLogInfo info : list) {
            String[] s = info.getDate().split("-");
            if (s.length == 3) {
                int year = Integer.parseInt(s[0]);
                int month = Integer.parseInt(s[1]);
                int day = Integer.parseInt(s[2]);
                schemes.add(getSchemeCalendar(year, month, day, 0xFFbbbbbb, "记"));
            }
        }
        mCalendarView.setSchemeDate(schemes);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month_day:
                mCalendarView.showSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
                break;
            case R.id.fl_current:
                mCalendarView.scrollToCurrent();
                break;
            case R.id.fab:
                Intent intent = new Intent(this, WorkLogEditActivity.class);
                intent.putExtra("cal_info", mCalendarView.getSelectedCalendar());
                startActivity(intent);
                break;
        }
    }


   /* @Override
    public void onDateSelected(Calendar calendar) {
        System.out.println("sssss = "+calendar.toString());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }*/

    @Override
    public void onDateChange(Calendar calendar) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        setRelcycleView();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    private void checkUpdate() {
        //PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
        PgyUpdateManager.register(this, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
                LogUtils.LogShow("无更新版本");
            }

            @Override
            public void onUpdateAvailable(String result) {
                // 将新版本信息封装到AppBean中
                final AppBean appBean = getAppBeanFromString(result);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("发现新版本，v"+appBean.getVersionName());
                builder.setMessage(appBean.getReleaseNote());
                builder.setCancelable(false);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startDownloadTask(MainActivity.this,appBean.getDownloadURL());
                    }
                });
                builder.create().show();
            }
        });
    }

    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            ToastUtils.showShortToast(MainActivity.this,"再按一次退出程序");
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        }else{//退出程序
            this.finish();
            System.exit(0);
        }
    }

}
