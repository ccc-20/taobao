package com.example.baekhyun.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.baekhyun.R;
import com.example.baekhyun.base.BaseFragment;
import com.example.baekhyun.ui.fragment.HomeFragment;
import com.example.baekhyun.ui.fragment.RedFragment;
import com.example.baekhyun.ui.fragment.SearchFragment;
import com.example.baekhyun.ui.fragment.SelectFragment;
import com.example.baekhyun.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private SelectFragment mSelectFragment;
    private RedFragment mRedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mfm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        initListener();
    }

    public void switchSearch(){
        mNavigationView.setSelectedItemId(R.id.search);
    }
    private void initFragment() {
        mNavigationView=findViewById(R.id.main_navagtion_bar);
        mHomeFragment=new HomeFragment();
        mRedFragment=new RedFragment();
        mSelectFragment=new SelectFragment();
        mSearchFragment=new SearchFragment();
        mfm=getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }
    private BaseFragment lastOneFragment=null;
    private void switchFragment(BaseFragment baseFragment) {
        FragmentTransaction transaction=mfm.beginTransaction();
        if (!baseFragment.isAdded()) {
            transaction.add(R.id.main_page,baseFragment);
        }else {
            transaction.show(baseFragment);
        }
        if(lastOneFragment!=null&&lastOneFragment!=baseFragment) transaction.hide(lastOneFragment);
        lastOneFragment=baseFragment;
        transaction.commit();
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home) {
                    switchFragment(mHomeFragment);
                }
                else if(item.getItemId()==R.id.select) {
                    switchFragment(mSelectFragment);
                }
                else if(item.getItemId()==R.id.red) {
                    switchFragment(mRedFragment);
                }
                else if(item.getItemId()==R.id.search) {
                    switchFragment(mSearchFragment);
                }
                return true;
            }
        });

    }
}
