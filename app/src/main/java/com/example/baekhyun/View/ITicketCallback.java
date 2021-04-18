package com.example.baekhyun.View;

import com.example.baekhyun.base.IBaseCallback;
import com.example.baekhyun.model.domain.TicketResult;

public interface ITicketCallback extends IBaseCallback {

    void TiacketLoaded(String cover, TicketResult result);
}

