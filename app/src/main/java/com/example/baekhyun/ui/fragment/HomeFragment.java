package com.example.baekhyun.ui.fragment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.baekhyun.R;
import com.example.baekhyun.View.IHomeCallback;
import com.example.baekhyun.View.ISearchCallback;
import com.example.baekhyun.base.BaseFragment;
import com.example.baekhyun.model.domain.CateGoryies;
import com.example.baekhyun.presenter.Impl.HomePresenterImpl;
import com.example.baekhyun.ui.activity.MainActivity;
import com.example.baekhyun.ui.adapter.HomePagerAdapter;
import com.example.baekhyun.utils.LogUtils;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends BaseFragment implements IHomeCallback {
    private HomePresenterImpl mHomePresenter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomePagerAdapter mHomePagerAdapter;
    private LinearLayout mLinearLayout;
    private EditText mEditText;
    @Override
    public int getRootResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initPresenter() {
        mHomePresenter=new HomePresenterImpl();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void initView(View view) {
        mTabLayout=view.findViewById(R.id.home_indicator);
        mViewPager=view.findViewById(R.id.home_pager);
        mLinearLayout=view.findViewById(R.id.network_error);
        mEditText=view.findViewById(R.id.home_search);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHomePresenter!=null)
                mHomePresenter.getCategory();
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mHomePagerAdapter=new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mHomePagerAdapter);
        //String ss=mEditText.getText().toString();
        //LogUtils.d(HomeFragment.this,ss);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                if(activity instanceof MainActivity){
                    ((MainActivity) activity).switchSearch();
                }
            }
        });
    }


    @Override
    protected void loadDate() {
        mHomePresenter.getCategory();
    }

    @Override
    public void OnCategoryLoad(CateGoryies cateGoryies) {
        setUpState(State.SUCCESS);
        if (mHomePagerAdapter!=null) {
            mHomePagerAdapter.setCategories(cateGoryies);
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
    setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
    setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        if (mHomePresenter!=null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }

}
