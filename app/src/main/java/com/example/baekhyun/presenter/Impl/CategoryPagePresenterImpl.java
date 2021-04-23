package com.example.baekhyun.presenter.Impl;

import android.database.Cursor;

import com.example.baekhyun.View.ICategoryCallback;
import com.example.baekhyun.model.Api;
import com.example.baekhyun.model.domain.HomePagerContent;
import com.example.baekhyun.presenter.ICategoryPagerPresenter;
import com.example.baekhyun.utils.RetrofitManager;
import com.example.baekhyun.utils.UrlUtiles;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> hashMap = new HashMap<>();
    public static final int DEFAULT_PAGE = 1;
    private Integer mCurrentPage;

    private CategoryPagePresenterImpl() {

    }

    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }

    @Override
    public void getContentCategoriesById(int id) {
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == id) {
                callback.onLoading();
            }
        }
        Integer page = hashMap.get(id);
        if (page == null) {
            page = DEFAULT_PAGE;
            hashMap.put(id, DEFAULT_PAGE);
        }
        Call<HomePagerContent> task = createTask(id, page);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    handlePageContentResult(pagerContent, id);
                } else {
                    handleNetworkError(id);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handleNetworkError(id);
            }
        });
    }

    private Call<HomePagerContent> createTask(int id, Integer page) {
        String url = UrlUtiles.createHomePagerUrl(id, page);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePagerContent(url);
    }

    private void handleNetworkError(int id) {
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == id) {
                callback.onNetworkError();
            }
        }
    }

    private void handlePageContentResult(HomePagerContent pagerContent, int id) {
        for (ICategoryCallback calback : callbacks) {
            if (calback.getCategoryId() == id) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    calback.onEmpty();
                } else {
                    calback.onContentLoaded(pagerContent.getData());
                }
            }
        }
    }

    @Override
    public void loadMore(int id) {
        mCurrentPage = hashMap.get(id);
        if (mCurrentPage == null)
            mCurrentPage = 1;
        mCurrentPage++;
        hashMap.put(id, mCurrentPage);
        Call<HomePagerContent> task = createTask(id, mCurrentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent body = response.body();
                    handleLoadmoreResult(body, id);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                handleLoadMoreError(id);
            }
        });

    }

    private void handleLoadmoreResult(HomePagerContent body, int id) {
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == id) {
                if (body == null || body.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreLoaded(body.getData());
                }
            }
        }
    }

    private void handleLoadMoreError(int id) {
        mCurrentPage--;
        hashMap.put(id, mCurrentPage);
        for (ICategoryCallback callback : callbacks) {
            if (callback.getCategoryId() == id) {
                callback.onLoadMoreError();
            }
        }
    }

    private List<ICategoryCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryCallback callback) {
        callbacks.remove(callback);
    }
}
