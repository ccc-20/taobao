package com.example.baekhyun.View;

import com.example.baekhyun.base.IBaseCallback;
import com.example.baekhyun.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryCallback extends IBaseCallback {

    void onContentLoaded(List<HomePagerContent.DataBean> contents);


    int getCategoryId();


    void onLoadMoreError();

    void onLoadMoreEmpty();

    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    void onLooperLoaded(List<HomePagerContent.DataBean> contents);
}
