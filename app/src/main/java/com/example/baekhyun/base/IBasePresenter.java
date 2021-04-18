package com.example.baekhyun.base;

import com.example.baekhyun.View.ICategoryCallback;

public interface IBasePresenter<T> {

    void registerViewCallback(T callback);

    void unregisterViewCallback(T  callback);
}
