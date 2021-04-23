package com.example.baekhyun.presenter;

import com.example.baekhyun.View.ISelelctCallback;
import com.example.baekhyun.base.IBasePresenter;
import com.example.baekhyun.model.domain.Selectid;

public interface ISelectPagePresenter extends IBasePresenter<ISelelctCallback> {

    //获取分类
    void getCategories();

    //根据分类id获取内容
    void getContentByid(Selectid.DataBean item);

    void reloadContent();
}
