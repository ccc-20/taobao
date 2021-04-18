package com.example.baekhyun.View;

import com.example.baekhyun.base.IBaseCallback;
import com.example.baekhyun.model.domain.SpecialContent;

public interface ISepcialCallback extends IBaseCallback {

    void SpecialContentLoad(SpecialContent content);

    void LoadMore(SpecialContent content);

    void onLoadMoreerror();

    void onMoreEmpty();
}
