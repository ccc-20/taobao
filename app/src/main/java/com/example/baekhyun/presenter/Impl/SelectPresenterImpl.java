package com.example.baekhyun.presenter.Impl;

import android.util.Log;

import com.example.baekhyun.View.ISelelctCallback;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.SelectCategory;
import com.example.baekhyun.model.domain.Selectid;
import com.example.baekhyun.presenter.ISelectPagePresenter;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.RetrofitManager;
import com.example.baekhyun.utils.UrlUtiles;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectPresenterImpl implements ISelectPagePresenter {
    private ISelelctCallback mViewCallback = null;
    private final Api mApi;
    private Selectid.DataBean mCurrentItem = null;

    public SelectPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        Call<Selectid> task = mApi.getSelectID();
        task.enqueue(new Callback<Selectid>() {
            @Override
            public void onResponse(Call<Selectid> call, Response<Selectid> response) {
                int code = response.code();
                LogUtils.d(SelectPresenterImpl.this, code + "");
                if (code == HttpURLConnection.HTTP_OK) {
                    Selectid body = response.body();
                    mViewCallback.onCategoriesLoaded(body);
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<Selectid> call, Throwable t) {
                onLoadError();
            }
        });
    }

    private void onLoadError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }

    @Override
    public void getContentByid(Selectid.DataBean item) {
        this.mCurrentItem = item;
        int targetId = item.getFavorites_id();
        String contentUrl = UrlUtiles.getContentUrl(targetId);
        Call<SelectCategory> task = mApi.getContentByid(contentUrl);
        task.enqueue(new Callback<SelectCategory>() {
            @Override
            public void onResponse(Call<SelectCategory> call, Response<SelectCategory> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    SelectCategory body = response.body();
                    mViewCallback.onContentByidLoaded(body);
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectCategory> call, Throwable t) {
                onLoadError();
            }
        });
    }

    @Override
    public void reloadContent() {

    }

    @Override
    public void registerViewCallback(ISelelctCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelelctCallback callback) {
        this.mViewCallback = null;
    }
}
