package com.example.baekhyun.presenter;

import com.example.baekhyun.View.IHomeCallback;
import com.example.baekhyun.base.IBasePresenter;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    void getCategory();

}
