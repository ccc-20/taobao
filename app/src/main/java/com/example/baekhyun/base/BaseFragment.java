package com.example.baekhyun.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baekhyun.R;

public abstract class BaseFragment extends Fragment{
    private FrameLayout mFrameLayout;
    private View msuccessView;
    private View mloadingView;
    private View memptyView;
    private View merrorView;


    private State currentstate=State.NONE;
    public enum State{
        NONE,LOADING,SUCCESS,EMPTY,ERROR
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=loadRootView(inflater,container);
        mFrameLayout=rootview.findViewById(R.id.base_container);
        loadStateView(inflater,container);
        initView(rootview);
        initListener();
        initPresenter();
        loadDate();

        return rootview;
    }

    protected void initListener() {
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout,container,false);
    }

    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        msuccessView=loadSuccessView(inflater,container);
        mFrameLayout.addView(msuccessView);
        mloadingView=loadLoadingView(inflater,container);
        mFrameLayout.addView(mloadingView);
        merrorView=loadErrorView(inflater,container);
        mFrameLayout.addView(merrorView);
        memptyView=loadEmptyView(inflater,container);
        mFrameLayout.addView(memptyView);
        setUpState(State.NONE);
    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.empty,container,false);
    }

    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.error,container,false);
    }

    public void setUpState(State state){
        currentstate=state;
        msuccessView.setVisibility(currentstate==State.SUCCESS?View.VISIBLE:View.GONE);
        mloadingView.setVisibility(currentstate==State.LOADING?View.VISIBLE:View.GONE);
        merrorView.setVisibility(currentstate==State.ERROR?View.VISIBLE:View.GONE);
        memptyView.setVisibility(currentstate==State.EMPTY?View.VISIBLE:View.GONE);

    }
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.loading,container,false);
    }

    protected void initView(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    protected  void release(){

    }

    protected void initPresenter() {

    }

    protected void loadDate() {
    }

    private View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resId=getRootResId();
        return inflater.inflate(resId,container,false);
    }

    public abstract int getRootResId();
}
