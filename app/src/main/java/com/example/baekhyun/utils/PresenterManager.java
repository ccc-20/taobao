package com.example.baekhyun.utils;

import com.example.baekhyun.presenter.ISelectPagePresenter;
import com.example.baekhyun.presenter.ISepicialPresenter;
import com.example.baekhyun.presenter.ITicketPresenter;
import com.example.baekhyun.presenter.Impl.ISpecialPresenterImpl;
import com.example.baekhyun.presenter.Impl.SearchPresenterImpl;
import com.example.baekhyun.presenter.Impl.SelectPresenterImpl;
import com.example.baekhyun.presenter.Impl.TicketPresenterImpl;
import com.example.baekhyun.presenter.IsearchPresenter;

public class PresenterManager {
    private static final PresenterManager oneInstance = new PresenterManager();
    private final ISelectPagePresenter mSelectPresenter;
    private final ISepicialPresenter mSpecialPresenter;
    private final IsearchPresenter mSearchpresenter;

    public ITicketPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public ISelectPagePresenter getSelectPresenter() {
        return mSelectPresenter;
    }

    public ISepicialPresenter getSpecialPresenter() {
        return mSpecialPresenter;
    }

    public IsearchPresenter getSearchpresenter() {
        return mSearchpresenter;
    }

    private final TicketPresenterImpl mTicketPresenter;

    public static PresenterManager getInstance() {
        return oneInstance;
    }

    private PresenterManager() {
        mTicketPresenter = new TicketPresenterImpl();
        mSelectPresenter = new SelectPresenterImpl();
        mSpecialPresenter = new ISpecialPresenterImpl();
        mSearchpresenter = new SearchPresenterImpl();
    }
}
