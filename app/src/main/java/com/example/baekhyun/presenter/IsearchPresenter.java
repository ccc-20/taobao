package com.example.baekhyun.presenter;

import com.example.baekhyun.View.ISearchCallback;
import com.example.baekhyun.base.IBasePresenter;

public interface IsearchPresenter extends IBasePresenter<ISearchCallback> {
    void getHistories();

    void deleteHistories();

    void doSearch(String key);

    void reSearch();

    void loadMore();

    void getRecommends();
}
