package com.example.baekhyun.model;

import com.example.baekhyun.model.domain.CateGoryies;
import com.example.baekhyun.model.domain.HomePagerContent;
import com.example.baekhyun.model.domain.Recommed;
import com.example.baekhyun.model.domain.SearchResult;
import com.example.baekhyun.model.domain.SelectCategory;
import com.example.baekhyun.model.domain.Selectid;
import com.example.baekhyun.model.domain.SpecialContent;
import com.example.baekhyun.model.domain.TicketParams;
import com.example.baekhyun.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    @GET("discovery/categories")
    Call<CateGoryies> getCategories();
    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);
    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);
    @GET("recommend/categories")
    Call<Selectid> getSelectID();
    @GET()
    Call<SelectCategory> getContentByid(@Url String url);
    @GET
    Call<SpecialContent> getSpecialContent(@Url String url);
    @GET("search/recommend")
    Call<Recommed> getRecommendWords();
    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page,@Query("keyword") String keyword);


}
