package com.mzw.mydatabase.sqlites;

import android.database.sqlite.SQLiteDatabase;

/**
 * 单例模式
 * 优点：实例只有一个，内存消耗低
 * 缺点：多线程模式下会死掉（多线程访问，访问量大会死掉）
 * Created by think on 2018/6/7.
 */

public class BaseDaoFactory {

    private static  final BaseDaoFactory mBaseDaoFactory = new BaseDaoFactory();

    public static BaseDaoFactory getInstance(){return mBaseDaoFactory;}
    SQLiteDatabase mSQLiteDatabase;

    public BaseDaoFactory() {
        //路径可以 使用代码获取
        String path = "/data/data/com.mzw.mydatabase/dataName.db";
        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path,null);
    }


    public <T> IBaseDao<T> getBaseDao(Class<T> entityClass){
        BaseDaoImpl<T> dao = null;

        try {
            dao = BaseDaoImpl.class.newInstance(); //通过反射得到实体
            dao.init(mSQLiteDatabase,entityClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return dao;
    }

}
