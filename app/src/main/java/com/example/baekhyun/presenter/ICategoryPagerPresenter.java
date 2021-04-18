package com.example.baekhyun.presenter;

import com.example.baekhyun.View.ICategoryCallback;
import com.example.baekhyun.View.IHomeCallback;
import com.example.baekhyun.base.IBaseCallback;
import com.example.baekhyun.base.IBasePresenter;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryCallback> {

    void getContentCategoriesById(int id);

    void loadMore(int id);

    void reload(int id);

}
