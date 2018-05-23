package com.example.asus.qmt5.BasicView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.qmt5.R;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TabFragment extends Fragment {
    private String lMain = "Default";  //主视图显示文本
    View view;
    public static final String MAIN = "title";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (getArguments() != null)  //如果已经传值
        {
            lMain = getArguments().getString(MAIN);  //将获取到的值，赋值给主视图显示文本
        }
        if( lMain=="搜索") {
            view = inflater.inflate(R.layout.activity_searchhome, container, false);                         //设置文本显示位置居中
        }
        if( lMain=="地图")
        {
            view = inflater.inflate(R.layout.activity_map, container, false);
        }
        else
        {
            view = inflater.inflate(R.layout.activity_personalcenter, container, false);
        }
        return view;
    }

}
