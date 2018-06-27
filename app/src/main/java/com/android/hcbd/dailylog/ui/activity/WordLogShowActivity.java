package com.android.hcbd.dailylog.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hcbd.dailylog.MyApplication;
import com.android.hcbd.dailylog.R;
import com.android.hcbd.dailylog.base.activity.BaseActivity;
import com.android.hcbd.dailylog.db.WorkLogDbTool;
import com.android.hcbd.dailylog.entity.WorkLogInfo;
import com.android.hcbd.dailylog.event.MessageEvent;
import com.android.hcbd.dailylog.utils.ProgressDialogUtils;
import com.android.hcbd.dailylog.view.SlideFromBottomPopup;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.haibin.calendarview.Calendar;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordLogShowActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_month_day)
    TextView tvMonthDay;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_lunar)
    TextView tvLunar;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_contents)
    TextView tvContents;
    @BindView(R.id.tv_visits)
    TextView tvVisits;
    @BindView(R.id.tv_phoneComms)
    TextView tvPhoneComms;
    @BindView(R.id.tv_cheats)
    TextView tvCheats;
    @BindView(R.id.tv_planss)
    TextView tvPlanss;
    @BindView(R.id.tv_conclusion)
    TextView tvConclusion;

    private Calendar mCalendar;
    private WorkLogInfo workLogInfo;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_log_show);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mCalendar = (Calendar) getIntent().getSerializableExtra("cal_info");
        workLogInfo = (WorkLogInfo) getIntent().getSerializableExtra("info");
        index = getIntent().getIntExtra("index",0);

        setData();
        tvMore.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getEventId()) {
            case MessageEvent.EVENT_EDIT:
                workLogInfo = (WorkLogInfo) event.getObj();
                tvContents.setText(getTodayStr(workLogInfo.getContents()));
                tvVisits.setText(getTodayStr(workLogInfo.getVisits()));
                tvPhoneComms.setText(TextUtils.isEmpty(workLogInfo.getPhoneComms()) ? " ":"与"+workLogInfo.getPhoneComms().replace(";","、")+"进行沟通");
                tvCheats.setText(workLogInfo.getCheats());
                tvPlanss.setText(workLogInfo.getPlanss());
                tvConclusion.setText(workLogInfo.getConclusion());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setData() {
        tvMonthDay.setText(mCalendar.getMonth() + "月" + mCalendar.getDay() + "日");
        tvYear.setText(String.valueOf(mCalendar.getYear()));
        tvLunar.setText(mCalendar.getLunar());

        String weekStr = "";
        switch (mCalendar.getWeek()){
            case 1:
                weekStr = "星期一";
                break;
            case 2:
                weekStr = "星期二";
                break;
            case 3:
                weekStr = "星期三";
                break;
            case 4:
                weekStr = "星期四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
            case 7:
                weekStr = "星期日";
                break;
        }
        tvDate.setText(mCalendar.getYear()+"年"+mCalendar.getMonth() + "月" + mCalendar.getDay() + "日"+
                "  "+weekStr);
        tvContents.setText(getTodayStr(workLogInfo.getContents()));
        tvVisits.setText(getTodayStr(workLogInfo.getVisits()));
        tvPhoneComms.setText(TextUtils.isEmpty(workLogInfo.getPhoneComms()) ? " ":"与"+workLogInfo.getPhoneComms().replace(";","、")+"进行沟通");
        tvCheats.setText(workLogInfo.getCheats());
        tvPlanss.setText(workLogInfo.getPlanss());
        tvConclusion.setText(workLogInfo.getConclusion());
    }

    private String getTodayStr(String s){
        String str = "";
        if(!TextUtils.isEmpty(s)){
            String[] arrayStr = s.split(";");
            for(int i=1;i<=arrayStr.length;i++){
                if(i == 1)
                    str += i+"、"+arrayStr[i-1];
                else
                    str += "\n"+i+"、"+arrayStr[i-1];
            }
        }
        return str;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_more:
                final SlideFromBottomPopup popup = new SlideFromBottomPopup(this);
                popup.showPopupWindow();
                popup.setOnViewClickListener(new SlideFromBottomPopup.onViewClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.tx_1:
                                Intent intent = new Intent(WordLogShowActivity.this,WorkLogEditActivity.class);
                                intent.putExtra("info",workLogInfo);
                                intent.putExtra("index",index);
                                intent.putExtra("cal_info",mCalendar);
                                startActivity(intent);
                                break;
                            case R.id.tx_2:
                                ProgressDialogUtils.showLoading(WordLogShowActivity.this);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            InputStream inputStream = getAssets().open("demo.doc");
                                            FileIOUtils.writeFileFromIS(MyApplication.getInstance().getSDPath()+"/hcbdlog/default.doc",inputStream);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        doScan();
                                    }
                                }).start();
                                break;
                            case R.id.tx_3:
                                new AlertDialog.Builder(WordLogShowActivity.this)
                                        .setTitle("提示")
                                        .setMessage("请确定是否删除？")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                WorkLogDbTool.delete(workLogInfo);
                                                MessageEvent messageEvent = new MessageEvent();
                                                messageEvent.setObj(index);
                                                messageEvent.setEventId(MessageEvent.EVENT_DELETE);
                                                EventBus.getDefault().post(messageEvent);
                                                finishActivity();
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                                break;
                            case R.id.tv_cancel:
                                break;
                        }
                        popup.dismiss();
                    }
                });
                break;
        }
    }

    private void doScan() {
        //获取模板文件
        File demoFile=new File(MyApplication.getInstance().getSDPath()+"/hcbdlog/default.doc");
        //创建生成的文件
        File newFile=new File(MyApplication.getInstance().getSDPath()+"/hcbdlog/"+workLogInfo.getDate()+" "+workLogInfo.getTime()+".doc");
        Map<String, Object> map = new HashMap<>();
        map.put("$RQSJ$", tvDate.getText());
        map.put("$JRGZ$", docToday(workLogInfo.getContents()));
        map.put("$JRBF$", docToday(workLogInfo.getVisits()));
        map.put("$DHGT$", tvPhoneComms.getText());
        map.put("$JRBW$", tvCheats.getText());
        map.put("$MRJH$", tvPlanss.getText());
        map.put("$WTTH$", tvConclusion.getText());
        if(FileUtils.isFileExists(newFile))
            FileUtils.deleteFile(newFile);
        writeDocToLocal(demoFile,newFile,map);
    }

    private void writeDocToLocal(File demoFile ,File newFile ,Map<String, Object> map) {
        try {
            FileInputStream in = new FileInputStream(demoFile);
            HWPFDocument hdt = new HWPFDocument(in);
            // Fields fields = hdt.getFields();
            // 读取word文本内容
            Range range = hdt.getRange();
            // System.out.println(range.text());
            // 替换文本内容
            for(Map.Entry<String, Object> entry : map.entrySet()){
                range.replaceText(entry.getKey(), String.valueOf(entry.getValue()));
            }
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(newFile, true);
            hdt.write(ostream);
            // 输出字节流
            out.write(ostream.toByteArray());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WordLogShowActivity.this,"word已保存至hcbdlog/文件夹下",Toast.LENGTH_LONG).show();
                    ProgressDialogUtils.dismissLoading();
                }
            });
            out.close();
            ostream.close();
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    private String docToday(String s){
        String str = "";
        if(!TextUtils.isEmpty(s)){
            String[] arrayStr = s.split(";");
            for(int i=1;i<=arrayStr.length;i++){
                if(i == 1)
                    str += "    "+i+"、"+arrayStr[i-1];
                else
                    str += "\n    "+i+"、"+arrayStr[i-1];
            }
        }
        return str;
    }



}
