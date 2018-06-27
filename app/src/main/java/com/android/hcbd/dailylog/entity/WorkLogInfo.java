package com.android.hcbd.dailylog.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by guocheng on 2017/12/15.
 */
@Entity
public class WorkLogInfo implements Serializable {

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true)
    private Long id;
    private String date;  //日期
    private String time;  //时间
    private String week;  //星期几
    private String weather; //天气
    private String contents; //今日工作
    private String visits;  //今日拜访
    private String phoneComms;  //今日电话沟通
    private String cheats;  //今日备忘
    private String planss;  //明日计划
    private String conclusion; //问题与体会
    private String template;  //模板类型
    private String data1;  //备用字段1
    private String data2;  //备用字段2

    @Generated(hash = 1173835821)
    public WorkLogInfo(Long id, String date, String time, String week,
            String weather, String contents, String visits, String phoneComms,
            String cheats, String planss, String conclusion, String template,
            String data1, String data2) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.week = week;
        this.weather = weather;
        this.contents = contents;
        this.visits = visits;
        this.phoneComms = phoneComms;
        this.cheats = cheats;
        this.planss = planss;
        this.conclusion = conclusion;
        this.template = template;
        this.data1 = data1;
        this.data2 = data2;
    }
    @Generated(hash = 1238775740)
    public WorkLogInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getWeek() {
        return this.week;
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public String getWeather() {
        return this.weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public String getContents() {
        return this.contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getVisits() {
        return this.visits;
    }
    public void setVisits(String visits) {
        this.visits = visits;
    }
    public String getPhoneComms() {
        return this.phoneComms;
    }
    public void setPhoneComms(String phoneComms) {
        this.phoneComms = phoneComms;
    }
    public String getCheats() {
        return this.cheats;
    }
    public void setCheats(String cheats) {
        this.cheats = cheats;
    }
    public String getPlanss() {
        return this.planss;
    }
    public void setPlanss(String planss) {
        this.planss = planss;
    }
    public String getConclusion() {
        return this.conclusion;
    }
    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTemplate() {
        return this.template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public String getData1() {
        return this.data1;
    }
    public void setData1(String data1) {
        this.data1 = data1;
    }
    public String getData2() {
        return this.data2;
    }
    public void setData2(String data2) {
        this.data2 = data2;
    }

}
