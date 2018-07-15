package com.example.asus.qmt5.Personalcenter.activity;

import android.support.v4.app.FragmentTransaction;

import com.example.asus.qmt5.Map.map;
import com.example.asus.qmt5.Personalcenter.personalcenter;
import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Personalcenter.activity.basic.BaseActivity;
import com.example.asus.qmt5.Personalcenter.fragment.MainFragment;
import com.example.asus.qmt5.Personalcenter.fragment.basic.BaseFragment;


/**
 * 版权所有：XXX有限公司
 *
 * MainActivity
 *
 * @author zhou.wenkai ,Created on 2016-5-5 10:24:35
 * 		   Major Function：<b>主界面</b>
 *
 *         注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        initMainFragment();
    }

    /**
     * 初始化内容Fragment
     *
     * @return void
     */
    public void initMainFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BaseFragment mFragment = MainFragment.newInstance();
        transaction.replace(R.id.main_act_container, mFragment, mFragment.getFragmentName());
        transaction.commit();
    }

    @Override
    protected void initEvents() {

    }
}
