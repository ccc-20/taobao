package com.example.baekhyun.View;

import com.example.baekhyun.base.IBaseCallback;
import com.example.baekhyun.model.domain.SelectCategory;
import com.example.baekhyun.model.domain.Selectid;

public interface ISelelctCallback extends IBaseCallback {

    void onCategoriesLoaded(Selectid selectid);

    void onContentByidLoaded(SelectCategory content);
}
