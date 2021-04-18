package com.example.baekhyun.presenter;

import com.example.baekhyun.View.ITicketCallback;
import com.example.baekhyun.base.IBasePresenter;

public interface ITicketPresenter extends IBasePresenter<ITicketCallback> {

    void getTicket(String title,String url,String cover);
}
