package com.android.hcbd.dailylog.event;

/**
 * Created by guocheng on 2016/12/3.
 */
public class MessageEvent {

    /*发出的广播类型*/
    public static final int EVENT_ADD = 100;
    public static final int EVENT_EDIT = 101;
    public static final int EVENT_DELETE = 102;

    private int eventId;
    private Object obj;
    private Object obj2;
    public MessageEvent() {
    }

    public MessageEvent(int eventId) {
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj2() {
        return obj2;
    }

    public void setObj2(Object obj2) {
        this.obj2 = obj2;
    }
}
