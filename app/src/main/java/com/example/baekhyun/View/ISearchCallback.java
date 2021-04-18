package com.example.baekhyun.View;

import android.widget.LinearLayout;

import com.example.baekhyun.base.IBaseCallback;
import com.example.baekhyun.model.domain.Histroies;
import com.example.baekhyun.model.domain.Recommed;
import com.example.baekhyun.model.domain.SearchResult;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {

    void onHistoriesLoaded(Histroies list);

    void onHistoriesDel();

    void onSearchSuccess(SearchResult result);

    void onMoreLoaded(SearchResult result);

    void onMoreError();

    void onMoreEmpty();

    void onRecommed(List<Recommed.DataBean> list);
}
