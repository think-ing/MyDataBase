package com.mzw.mydatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mzw.mydatabase.pojo.Users;
import com.mzw.mydatabase.sqlites.BaseDaoFactory;
import com.mzw.mydatabase.sqlites.IBaseDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private LinearLayout log_layout;

    IBaseDao<Users> mIBaseDao;
    int id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIBaseDao = BaseDaoFactory.getInstance().getBaseDao(Users.class);

        textView = (TextView)findViewById(R.id.id_textView);
        log_layout = (LinearLayout)findViewById(R.id.id_log_layout);

        int i = mIBaseDao.deleteAll();
        logLayout("清空表共 "+i+" 条数据",0);
    }




    //增 返回行id//-------------------------------------------------
    public void insert(View v){
        id++;
        textView.setText("当前ID：" + id);
        logLayout("开始增加...",5);
        Users user = new Users(id,"ZhangSan","123","10");
        Long rowId = mIBaseDao.insert(user);
        logLayout("增加行ID为：" + rowId,0);

    }

    //删//-------------------------------------------------
    public void delete(View v){
        logLayout("开始删除...",5);
        Users where = new Users();
        where.id = id;
        int del = mIBaseDao.delete(where);
        logLayout("删除结果：" + del,0);

    }

    //改//-------------------------------------------------
    public void update(View v){
        logLayout("开始修改...",5);
        Users user = new Users();
        user.password = "12345";
        Users where = new Users();
        where.id = id;
        int up = mIBaseDao.update(user,where);
        if(up > 0){
            logLayout("修改成功...",0);
        }else {
            logLayout("修改失败...",0);
        }
    }

    //查//-------------------------------------------------
    public void queryAll(View v){
        logLayout("开始查询All...",5);
        List<Users> list = mIBaseDao.queryAll();
        if(list != null && list.size() > 0){
            logLayout("共" + list.size() + "条",0);
            for (Users user:list) {
                if(user != null){
                    logLayout(user.toString(),0);
                }
            }
        }else{
            logLayout("无数据",0);
        }
    }
    public void queryById(View v){
        logLayout("开始查询ById",5);
        Users where = new Users();
        where.id = id;
        List<Users> list = mIBaseDao.query(where);
        if(list != null && list.size() > 0){
            logLayout("共" + list.size() + "条",0);
            for (Users user:list) {
                if(user != null){
                    logLayout(user.toString(),0);
                }
            }
        }else{
            logLayout("无数据",0);
        }
    }

    private void logLayout(String str,int top) {
        TextView tv = new TextView(this);
        tv.setPadding(5,top,5,0);
        tv.setTextSize(10);
        if(top == 5){
            tv.setText("●"+str);
        }else{
            tv.setText("   "+str);
        }
        log_layout.addView(tv);
    }

}
