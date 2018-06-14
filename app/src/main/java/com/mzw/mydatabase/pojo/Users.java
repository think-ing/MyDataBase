package com.mzw.mydatabase.pojo;

import com.mzw.mydatabase.annotaions.DBField;
import com.mzw.mydatabase.annotaions.DBTable;

/**
 * Created by think on 2018/6/6.
 */

@DBTable("tb_user")
public class Users {

    @DBField("_id")
    public Integer id;
    @DBField("_username")
    public String usernme;
    @DBField("_password")
    public String password;
    @DBField("_age")
    public String age;

//    @DBField("_ageA")
//    public String ageA;
//
//    @DBField("_ageB")
//    public String ageB;
//
//    @DBField("_ageC")
//    public String ageC;
//
//    @DBField("_ageD")
//    public String ageD;

    public Users() {
    }

    public Users(Integer id, String usernme, String password,String age) {
        this.id = id;
        this.usernme = usernme;
        this.password = password;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", usernme='" + usernme + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
