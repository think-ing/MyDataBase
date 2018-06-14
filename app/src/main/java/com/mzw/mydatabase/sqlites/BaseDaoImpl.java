package com.mzw.mydatabase.sqlites;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.notification.Condition;
import android.util.Log;

import com.mzw.mydatabase.annotaions.DBField;
import com.mzw.mydatabase.annotaions.DBTable;
import com.mzw.mydatabase.pojo.Users;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by think on 2018/6/6.
 */

public class BaseDaoImpl<T> implements IBaseDao<T>{


    SQLiteDatabase mSQLiteDatabase;
    private boolean isInit = false;

    Class<T> entityClass;
    String tableName;


    // 使用init  没有使用构造 原因时 init可以反复初始化
    public void init(SQLiteDatabase mSQLiteDatabase, Class<T> entityClass) {
        //表是否存在
        boolean tabbleIsExist = false;

        if(!isInit){
            //数据库工具
            this.mSQLiteDatabase = mSQLiteDatabase;
            //操作对象
            this.entityClass = entityClass;

            //建表
            DBTable tableAnnotation = entityClass.getAnnotation(DBTable.class);

            if(tableAnnotation != null){
                tableName = tableAnnotation.value();

                //判断表是否存在
                Cursor cursor = mSQLiteDatabase.rawQuery("select count(*) from sqlite_master where type= 'table' AND name='"+tableName+"' ", null);
                if(cursor.moveToNext()){
                    int count = cursor.getInt(0);
                    if(count > 0){
                        tabbleIsExist = true;
                    }
                }
                Log.i("---mzw---","tabbleIsExist : " + tabbleIsExist);
                if(tabbleIsExist){
                    //表存在   检查表字段
                    inspectColumn();
                }else{
                    //表不存在  创建表
                    mSQLiteDatabase.execSQL(getCreateTableSql());
                }
                //避免每次都会创建表
                isInit = true;
            }
        }
    }

    //建表sql语句
    private String getCreateTableSql() {

        StringBuffer createTableSql = new StringBuffer();

        createTableSql.append("create table if not exists ");
        createTableSql.append(tableName + " ( ");

        Field[] declaredFields = entityClass.getDeclaredFields();

        for (Field f:declaredFields) {
            DBField annotation = f.getAnnotation(DBField.class);

            if(annotation == null){
                continue;
            }
            if(f.getType() == String.class){
                createTableSql.append(annotation.value() + " TEXT,");
            }else if(f.getType() == Integer.class){
                createTableSql.append(annotation.value() + " INTEGER,");
            }else if(f.getType() == Double.class){
                createTableSql.append(annotation.value() + " REAL,");
            }else{
                continue;
            }
        }
        if(createTableSql.charAt(createTableSql.length() - 1) == ','){
            createTableSql.deleteCharAt(createTableSql.length() - 1);
        }
        createTableSql.append(" ) ");

        return createTableSql.toString();
    }


    //检查表 字段
    private void inspectColumn() {
        // WHERE 0 不查询数据
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from "+tableName+" WHERE 0 ",null);
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            DBField annotation = field.getAnnotation(DBField.class);

            if (annotation == null) {
                continue;
            }
            String key = annotation.value();// 获取注解(表列名)
            String type = "";
            if(field.getType() == String.class){
                type =  " TEXT ";
            }else if(field.getType() == Integer.class){
                type =  " INTEGER ";
            }else if(field.getType() == Double.class){
                type =  " REAL ";
            }else{
                continue;
            }
            if(cursor.getColumnIndex(key) < 0){// 不存在列 返回 -1
                //ALTER TABLE 表名 ADD COLUMN 列名 数据类型
                String alterSql = " ALTER TABLE "+tableName+" ADD COLUMN "+key+" "+type+" ";
                mSQLiteDatabase.execSQL(alterSql);
            }
        }
    }



    @Override
    public Long insert(T entity) {
        Long result = -1L;
        ContentValues values = getContentValues(entity);
        result =mSQLiteDatabase.insert(tableName,null,values);

        return result;
    }

    @Override
    public int delete(T where) {
        MyCondition myCondition=new MyCondition(getContentValues(where));

        int reslut = mSQLiteDatabase.delete(tableName,myCondition.getWhereClause(),myCondition.getWhereArgs());
        return reslut;

    }

    @Override
    public int deleteAll() {
        return mSQLiteDatabase.delete(tableName,null,null);
    }

    @Override
    public int update(T entity, T where) {
        int reslut=-1;
        MyCondition myCondition=new MyCondition(getContentValues(where));
        ContentValues contentValues=getContentValues(entity);
        reslut = mSQLiteDatabase.update(tableName,contentValues,myCondition.getWhereClause(),myCondition.getWhereArgs());
        return reslut;
    }



    @Override
    public List<T> queryAll() {
        return query(null,null,null,null);
    }

    @Override
    public List<T> query(T where) {
        return query(where,null,null,null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        String limitString=null;
        if(startIndex!=null&&limit!=null){
            limitString=startIndex+" , "+limit;
        }
        MyCondition myCondition=new MyCondition(getContentValues(where));
//        Log.i("---mzw---","myCondition.getWhereClause() : " + myCondition.getWhereClause());
//        Log.i("---mzw---","myCondition.getWhereArgs() : " + myCondition.getWhereArgs());
        Cursor cursor = mSQLiteDatabase.query(tableName,null,myCondition.getWhereClause()
                ,myCondition.getWhereArgs(),null,null,orderBy,limitString);
        List<T> result=getResult(cursor,where);
        cursor.close();
        return result;
    }


    /**
     * 获取数据
     * @param cursor
     * @param where
     * @return
     */
    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list=new ArrayList();
        Object item;
        while (cursor.moveToNext()){
            try {
                if(where == null){
                    item = entityClass.newInstance();
                }else{
                    item = where.getClass().newInstance();
                }

                Field[] declaredFields = entityClass.getDeclaredFields();
                for (Field field : declaredFields) {
                    DBField annotation = field.getAnnotation(DBField.class);

                    if(annotation == null){
                        continue;
                    }
                    String key = annotation.value();// 获取注解(表列名)
                    /**
                     * 然后以列名拿到  列名在游标的位子
                     */
                    Integer colmunIndex=cursor.getColumnIndex(key);
                    Class type=field.getType();
                    if(colmunIndex!=-1)
                    {
                        if(type==String.class)
                        {
                            //反射方式赋值
                            field.set(item,cursor.getString(colmunIndex));
                        }else if(type==Double.class)
                        {
                            field.set(item,cursor.getDouble(colmunIndex));
                        }else  if(type==Integer.class)
                        {
                            field.set(item,cursor.getInt(colmunIndex));
                        }else if(type==Long.class)
                        {
                            field.set(item,cursor.getLong(colmunIndex));
                        }else  if(type==byte[].class)
                        {
                            field.set(item,cursor.getBlob(colmunIndex));
                            /*
                            不支持的类型
                             */
                        }else {
                            continue;
                        }
                    }
                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 对象 转 ContentValues
     * @param entity
     * @return
     */
    public ContentValues getContentValues(T entity){
        ContentValues cv = new ContentValues();
        //1.取得表中字段
        //2.让表的字段和entity中的注解对应，取得对应的key-value
        //3.设置进去 搞定
        try{
            Field[] declaredFields = entityClass.getDeclaredFields();
            for (Field f:declaredFields) {
                DBField annotation = f.getAnnotation(DBField.class);

                if(annotation == null){
                    continue;
                }
                String key = annotation.value();// 获取注解(表列名)
                String name = f.getName();//获取 字段名称
                Field ff = entityClass.getField(name);//获取Field
                Object obj = ff.get(entity);
                if(obj != null){
                    String value = obj.toString();
                    cv.put(key,value);//封装
                }
            }
        }catch (Exception e){
            Log.i("---mzw---","Exception ： " + e.getMessage());
            e.printStackTrace();
        }
        return cv;
    }

}
