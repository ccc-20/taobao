package com.example.baekhyun.presenter.Impl;

import com.example.baekhyun.View.IHomeCallback;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.CateGoryies;
import com.example.baekhyun.presenter.IHomePresenter;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.RetrofitManager;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mIHomeCallback;
    @Override
    public void getCategory() {
        if(mIHomeCallback!=null){
            mIHomeCallback.onLoading();
        }
        Retrofit retrofit= RetrofitManager.getInstance().getRetrofit();
        Api api=retrofit.create(Api.class);
        Call<CateGoryies> task=api.getCategories();
        task.enqueue(new Callback<CateGoryies>() {
            @Override
            public void onResponse(Call<CateGoryies> call, Response<CateGoryies> response) {
                int code=response.code();
                LogUtils.d(HomePresenterImpl.this,"request-->"+code);
                if (code== HttpURLConnection.HTTP_OK) {
                    CateGoryies cateGoryies=response.body();
                    if (mIHomeCallback!=null) {
                        mIHomeCallback.OnCategoryLoad(cateGoryies);
                    }
                }else{
                    if(mIHomeCallback!=null)
                        mIHomeCallback.onNetworkError();
                }
            }
            @Override
            public void onFailure(Call<CateGoryies> call, Throwable t) {
                //LogUtils.e(HomePresenterImpl.this,"请求错误-->");
                if(mIHomeCallback!=null)
                    mIHomeCallback.onNetworkError();
            }
        });
    }


    @Override
    public void registerViewCallback(IHomeCallback callback) {
        mIHomeCallback=callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mIHomeCallback=null;
    }
}
