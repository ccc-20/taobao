package com.example.baekhyun.presenter.Impl;

import android.content.IntentFilter;

import com.example.baekhyun.View.ISearchCallback;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.Histroies;
import com.example.baekhyun.model.domain.Recommed;
import com.example.baekhyun.model.domain.SearchResult;
import com.example.baekhyun.presenter.IsearchPresenter;
import com.example.baekhyun.utils.CacheUtiles;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.RetrofitManager;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenterImpl implements IsearchPresenter {

    private final Api mApi;
    private ISearchCallback mCallback=null;
    public static int mcurrentPage=0;
    private String mkey=null;

    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getHistories() {
        CacheUtiles instance = CacheUtiles.getInstance();
        Histroies value = instance.getValue(KEY_HISTORY, Histroies.class);
        if(mCallback!=null){
            mCallback.onHistoriesLoaded(value);
        }
    }

    @Override
    public void deleteHistories() {
        CacheUtiles instance = CacheUtiles.getInstance();
        instance.delCache(KEY_HISTORY);
        if (mCallback != null) {
            mCallback.onHistoriesDel();
        }
    }

    public static final String KEY_HISTORY="key_history";

    private void saveHistory(String history){
        //this.deleteHistories();
        CacheUtiles instance = CacheUtiles.getInstance();
        Histroies value = instance.getValue(KEY_HISTORY, Histroies.class);
        List<String> list=null;
        if(value!=null&&value.getList()!=null){
             list= value.getList();
            if(list.contains(history)){
                list.remove(history);
            }
        }
        if(list==null) {
            list = new ArrayList<>();
        }
        if(value==null){
            value=new Histroies();
        }
        value.setList(list);
        if(list.size()>10){
            list=list.subList(0,10);
        }
        //TODO
        list.add(0,history);
        instance.saveCache(KEY_HISTORY,value);

    }
    @Override
    public void doSearch(String key) {
        LogUtils.d(this,key);
        if (mkey==null||!mkey.equals(key)) {
            this.saveHistory(key);
            this.mkey = key;
            //LogUtils.d(this,key);
        }
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mcurrentPage, key);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code=response.code();
                LogUtils.d(this,code+"");
                if(code==HttpURLConnection.HTTP_OK){
                    if(response.body()!=null) {
                        handleSearchResult(response.body());
                    }else {
                        onError();
                    }
                }else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    private void onError() {
        if (mCallback != null) {
            mCallback.onNetworkError();
        }
    }


    private void handleSearchResult(SearchResult body) {
        if(mCallback!=null) {
            if (isResultEmpty(body)) {
                mCallback.onEmpty();
            } else {
                mCallback.onSearchSuccess(body);
            }
        }
    }

    private boolean isResultEmpty(SearchResult body){
        try{
            return body==null||body.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size()==0;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void reSearch() {
        if(mkey!=null){
            if(mCallback!=null){
                //LogUtils.d(this,mkey);
                this.doSearch(mkey);
            }
        }else{
            mCallback.onNetworkError();
        }
    }

    @Override
    public void loadMore() {
        mcurrentPage++;
        if(mCallback!=null){
            doSearchMore();
        }else {
            mCallback.onEmpty();
        }
    }

    private void doSearchMore() {
        final Call<SearchResult> task = mApi.doSearch(mcurrentPage, mkey);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code=response.code();
                if(code==HttpURLConnection.HTTP_OK){
                    handleMoreSearchResult(response.body());
                }else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onLoadMoreError();
            }
        });
    }

    private void handleMoreSearchResult(SearchResult body) {
        if(isResultEmpty(body)){
            mCallback.onMoreEmpty();
        }else {
            mCallback.onMoreLoaded(body);
        }
    }

    private void onLoadMoreError() {
        mcurrentPage--;
        if(mCallback!=null){
            mCallback.onMoreError();
        }
    }

    @Override
    public void getRecommends() {
        Call<Recommed> task = mApi.getRecommendWords();
        task.enqueue(new Callback<Recommed>() {
            @Override
            public void onResponse(Call<Recommed> call, Response<Recommed> response) {
                int code = response.code();
                LogUtils.d(this,code+"     ");
                if(code== HttpURLConnection.HTTP_OK){
                    if(mCallback!=null) {
                        mCallback.onRecommed(response.body().getData());
                    }
                }
                
            }

            @Override
            public void onFailure(Call<Recommed> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback callback) {
        this.mCallback=callback;
    }

    @Override
    public void unregisterViewCallback(ISearchCallback callback) {
        this.mCallback=null;
    }
}
