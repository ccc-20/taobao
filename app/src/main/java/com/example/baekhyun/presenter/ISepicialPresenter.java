package com.example.baekhyun.presenter;

import com.example.baekhyun.View.ISepcialCallback;
import com.example.baekhyun.base.IBasePresenter;

public interface ISepicialPresenter extends IBasePresenter<ISepcialCallback> {

    void onSellContent();

    void reLoad();

    void loadMore();
}
