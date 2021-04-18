package com.example.baekhyun.presenter.Impl;

import com.example.baekhyun.View.ITicketCallback;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.TicketParams;
import com.example.baekhyun.model.domain.TicketResult;
import com.example.baekhyun.presenter.ITicketPresenter;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.RetrofitManager;
import com.example.baekhyun.utils.UrlUtiles;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {
    private ITicketCallback mViewCallback=null;
    private String mcover=null;
    private TicketResult mBody;

    enum LoadState{
        LOADING,SUCCESS,ERROR,NONE
    }

    private LoadState currentState=LoadState.NONE;
    @Override
    public void getTicket(String title, String url, String cover) {
        //currentState=LoadState.LOADING;
        this.onTicketLoading();
        this.mcover=UrlUtiles.getTicketUrl(cover);
        //LogUtils.d(this,mcover);
        String ticketUrl = UrlUtiles.getTicketUrl(url);
        Retrofit retrofit= RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams=new TicketParams(ticketUrl,title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code=response.code();
                if (code== HttpURLConnection.HTTP_OK) {
                    //成功
                     mBody = response.body();
                    //currentState=LoadState.SUCCESS;
                    LogUtils.d(TicketPresenterImpl.this,""+mBody);
                    onTicketSuccess();
                }else{
                    onLoadTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadTicketError();
            }
        });


    }

    private void onTicketSuccess() {
        if (mViewCallback != null) {
            mViewCallback.TiacketLoaded(mcover,mBody);
        }else {
            currentState=LoadState.SUCCESS;
        }
    }

    private void onLoadTicketError() {
        if (mViewCallback!=null) {
            mViewCallback.onNetworkError();
        }else {
            currentState=LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketCallback callback) {
        if (currentState!=LoadState.NONE) {
            if (currentState== LoadState.SUCCESS) {
                onTicketSuccess();
            }else if(currentState==LoadState.ERROR){
                onLoadTicketError();
            }else if(currentState==LoadState.LOADING){
                onTicketLoading();
            }
        }
        this.mViewCallback=callback;
    }

    private void onTicketLoading() {
        if (mViewCallback!=null) {
            mViewCallback.onLoading();
        }else {
            currentState=LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketCallback callback) {
        mViewCallback=null;
    }
}
