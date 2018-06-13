package com.mzw.mydatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.mzw.mydatabase.pojo.Users;
import com.mzw.mydatabase.sqlites.BaseDaoFactory;
import com.mzw.mydatabase.sqlites.IBaseDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    IBaseDao<Users> mIBaseDao;
    int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIBaseDao = BaseDaoFactory.getInstance().getBaseDao(Users.class);

        int i = mIBaseDao.deleteAll();
        Log.i("---mzw---","deleteAll : " + i);
    }



    //增 返回行id//-------------------------------------------------
    void insert(View v){
        Log.i("---mzw---","开始增加...");
        Users user = new Users(id,"ZhangSan","123");
        Long rowId = mIBaseDao.insert(user);
        Log.i("---mzw---","rowId : " + rowId);
        id++;
    }

    //删//-------------------------------------------------
    void delete(View v){
        Log.i("---mzw---","开始删除...");
        Users where = new Users();
        where.id = id;
        int del = mIBaseDao.delete(where);
        Log.i("---mzw---","删除结果：" + del);
    }

    //改//-------------------------------------------------
    void update(View v){
        Log.i("---mzw---","开始修改...");
        Users user = new Users();
        user.password = "12345";
        Users where = new Users();
        where.id = id;
        int up = mIBaseDao.update(user,where);
        if(up > 0){
            Log.i("---mzw---","修改成功...");
        }else {
            Log.i("---mzw---","修改失败...");
        }
    }

    //查//-------------------------------------------------
    void queryAll(View v){
        Log.i("---mzw---","开始查询All...");
        List<Users> list =  mIBaseDao.queryAll();
        if(list != null && list.size() > 0){
            Log.i("---mzw---","共" + list.size() + "条");
            for (Users user:list) {
                if(user != null){
                    Log.i("---mzw---",user.toString());
                }
            }
        }else{
            Log.i("---mzw---","查询All错误...");
        }
    }
}
