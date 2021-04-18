package com.example.baekhyun.presenter.Impl;

import com.example.baekhyun.View.ISepcialCallback;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.SpecialContent;
import com.example.baekhyun.presenter.ISepicialPresenter;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.RetrofitManager;
import com.example.baekhyun.utils.ToastUtil;
import com.example.baekhyun.utils.UrlUtiles;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ISpecialPresenterImpl implements ISepicialPresenter {
    private  int mpage =1;
    private ISepcialCallback mCallback=null;
    private final Api mApi;

    public ISpecialPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }


    @Override
    public void onSellContent() {
        if(mCallback!=null){
            mCallback.onLoading();
        }
        //LogUtils.d(this,specialUrl);
        String specialUrl = UrlUtiles.getSpecialUrl(mpage);
        Call<SpecialContent> task = mApi.getSpecialContent(specialUrl);
        //LogUtils.d(this,task.toString());
        task.enqueue(new Callback<SpecialContent>() {
            @Override
            public void onResponse(Call<SpecialContent> call, Response<SpecialContent> response) {
                int code=response.code();
                LogUtils.d(this,code+"");
                if(code== HttpURLConnection.HTTP_OK){
                    SpecialContent content = response.body();
                    onSuccess(content);
                }else {
                    onerror();
                }
            }
            @Override
            public void onFailure(Call<SpecialContent> call, Throwable t) {
                onerror();
            }
        });
        

    }

    private void onSuccess(SpecialContent content) {
        if (mCallback != null) {
            try {
                int size=content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if(size==0){
                    onEmpty();
                }else {
                    mCallback.SpecialContentLoad(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private void onEmpty() {
        if(mCallback!=null)
            mCallback.onEmpty();
    }

    private void onerror() {
        if (mCallback != null) {
            mCallback.onNetworkError();
        }
    }

    @Override
    public void reLoad() {
        this.onSellContent();
    }

    @Override
    public void loadMore() {
        mpage++;
        String specialUrl = UrlUtiles.getSpecialUrl(mpage);
        Call<SpecialContent> task = mApi.getSpecialContent(specialUrl);
        task.enqueue(new Callback<SpecialContent>() {
            @Override
            public void onResponse(Call<SpecialContent> call, Response<SpecialContent> response) {
                int code=response.code();
                if(code== HttpURLConnection.HTTP_OK){
                    SpecialContent content = response.body();
                    onMoreLoaded(content);
                }else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SpecialContent> call, Throwable t) {
                onLoadMoreError();
            }
        });

    }

    private void onLoadMoreError() {
        mpage--;
         mCallback.onLoadMoreerror();
    }

    private void onMoreLoaded(SpecialContent content) {
        if (mCallback != null) {
            try {
                int size=content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
                if(size==0){
                    mCallback.onLoadMoreerror();
                }else {
                    //mCallback.SpecialContentLoad(content);
                    mCallback.LoadMore(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mCallback.onMoreEmpty();
            }
        }
    }

    @Override
    public void registerViewCallback(ISepcialCallback callback) {
        this.mCallback=callback;
    }

    @Override
    public void unregisterViewCallback(ISepcialCallback callback) {
        this.mCallback=null;
    }
}
