package com.mzw.mydatabase.sqlites;

import android.database.sqlite.SQLiteDatabase;

import com.mzw.mydatabase.pojo.Users;

import java.util.List;

/**
 * Created by think on 2018/6/6.
 */

public interface IBaseDao<T> {

    //增 返回行id
    Long insert(T entity);
    //删
    int delete(T where);
    int deleteAll();
    //改
    int update(T entity,T where);
    //查
    List<T> queryAll();

    List<T> query(T where);

    List<T> query(T where,String orderBy,Integer startIndex,Integer limit);

}
