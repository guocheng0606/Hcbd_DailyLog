package com.android.hcbd.dailylog.db;

import com.android.hcbd.dailylog.entity.WorkLogInfo;
import com.android.hcbd.dailylog.entity.WorkLogInfoDao;
import com.android.hcbd.dailylog.manager.GreenDaoManager;

import java.util.List;

/**
 * Created by guocheng on 2017/12/19.
 */

public class WorkLogDbTool {

    //添加一个
    public static void add(WorkLogInfo info){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.insert(info);
    }

    //批量添加
    public static void addList(List<WorkLogInfo> list){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.insertInTx(list);
    }

    //根据实体删除
    public static void delete(WorkLogInfo info){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.delete(info);
    }

    //根据实体批量删除
    public static void deleteList(List<WorkLogInfo> list){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.deleteInTx(list);
    }

    //根据id删除
    public static void deleteById(Long id){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.deleteByKey(id);
    }

    //根据id批量删除
    public static void deleteListById(List<Long> list){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.deleteByKeyInTx(list);
    }

    //删除全部
    public static void deleteAll(){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.deleteAll();
    }

    //修改一个
    public static void update(WorkLogInfo info){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.update(info);
    }

    //批量修改
    public static void updateList(List<WorkLogInfo> list){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        dao.updateInTx(list);
    }


    //查询全部
    public static List<WorkLogInfo> queryAll(){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        List<WorkLogInfo> list = dao.loadAll();
        return list;
    }

    public static List<WorkLogInfo> queryByDate(String s){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        List<WorkLogInfo> list = dao.queryBuilder().where(WorkLogInfoDao.Properties.Date.eq(s)).orderDesc(WorkLogInfoDao.Properties.Id).build().list();
        return list;
    }


    public static WorkLogInfo queryById(Long id){
        WorkLogInfoDao dao = GreenDaoManager.getInstance().getSession().getWorkLogInfoDao();
        return dao.load(id);
    }
}
