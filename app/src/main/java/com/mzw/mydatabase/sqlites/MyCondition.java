package com.mzw.mydatabase.sqlites;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *将条件对象 转换
 * Created by think on 2018/6/8.
 */

public class MyCondition {

    private String whereClause;

    private  String[] whereArgs;

    public MyCondition(ContentValues contentValues) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1=1");

        List<String> list = new ArrayList<String>();

        Set<Map.Entry<String, Object>> set =  contentValues.valueSet();
        for (Map.Entry<String, Object> item : set)
        {
            String key = item.getKey();
            Object value = item.getValue();

            if (value!=null)
            {
                /*
                拼接条件查询语句
                1=1 and name =? and password=?
                 */
                stringBuilder.append(" and "+key+" =?");
                /**
                 * ？----》value
                 */
                list.add(value.toString());
            }
        }
        this.whereClause = stringBuilder.toString();
        this.whereArgs = list.toArray(new String[list.size()]);
    }


    public String getWhereClause() {
        return whereClause;
    }

    public String[] getWhereArgs() {
        return whereArgs;
    }
}
