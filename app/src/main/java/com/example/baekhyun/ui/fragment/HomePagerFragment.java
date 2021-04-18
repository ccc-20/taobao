package com.example.baekhyun.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baekhyun.R;
import com.example.baekhyun.View.ICategoryCallback;
import com.example.baekhyun.base.BaseFragment;
import com.example.baekhyun.model.domain.CateGoryies;
import com.example.baekhyun.model.domain.HomePagerContent;
import com.example.baekhyun.presenter.ICategoryPagerPresenter;
import com.example.baekhyun.presenter.Impl.CategoryPagePresenterImpl;
import com.example.baekhyun.presenter.ITicketPresenter;
import com.example.baekhyun.ui.activity.TicketActivity;
import com.example.baekhyun.ui.adapter.HomePagerContentAdapter;
import com.example.baekhyun.utils.Constant;
import com.example.baekhyun.utils.LogUtils;
import com.example.baekhyun.utils.PresenterManager;
import com.example.baekhyun.utils.ToastUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

public class HomePagerFragment extends BaseFragment implements ICategoryCallback,HomePagerContentAdapter.OnListclickListener{
    private RecyclerView mRecyclerView;
    private TwinklingRefreshLayout mTwinklingRefreshLayout;
    private ICategoryPagerPresenter mcategoryPagePresenter;
    private int id;
    private HomePagerContentAdapter mHomePagerContentAdapter;

    public static HomePagerFragment newInstance(CateGoryies.DataBean category){
        HomePagerFragment homePagerFragment=new HomePagerFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_TITLE,category.getTitle());
        bundle.putInt(Constant.KEY_ID,category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }
    @Override
    public int getRootResId() {
        return R.layout.home_pager;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView=view.findViewById(R.id.home_pager_content_lists);
        mTwinklingRefreshLayout=view.findViewById(R.id.home_pager_refresh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=8;
                outRect.bottom=8;
            }
        });
        mHomePagerContentAdapter = new HomePagerContentAdapter();
        mRecyclerView.setAdapter(mHomePagerContentAdapter);
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {
        mHomePagerContentAdapter.setOnListclickListener(this);


        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
                @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mcategoryPagePresenter.loadMore(id);
            }
        });

    }

    @Override
    protected void initPresenter() {
        mcategoryPagePresenter=CategoryPagePresenterImpl.getInstance();
        mcategoryPagePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadDate() {
        Bundle bundle=getArguments();
        String title=bundle.getString(Constant.KEY_TITLE);
        id=bundle.getInt(Constant.KEY_ID);
        if (mcategoryPagePresenter!=null) {
            mcategoryPagePresenter.getContentCategoriesById(id);
        }
        LogUtils.d(this,"title"+title+id);
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        mHomePagerContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public int getCategoryId() {
        return id;
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoadMoreError() {
        ToastUtil.getToast("哎呀，网络出现了一点问题呀");
        if (mTwinklingRefreshLayout!=null) {
            mTwinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtil.getToast("没有更多数据啦");
        if (mTwinklingRefreshLayout!=null) {
            mTwinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents ) {
        mHomePagerContentAdapter.addDate(contents);
        if (mTwinklingRefreshLayout!=null) {
            mTwinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLooperLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    protected void release() {
        if (mcategoryPagePresenter!=null) {
            mcategoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void OnitemClick(HomePagerContent.DataBean item) {
        LogUtils.d(this,"item click-->"+item.getTitle());
        handItemClick(item);
    }

    private void handItemClick(HomePagerContent.DataBean item) {
        String title=item.getTitle();
        //这个才是领券的链接！！！！
        String url=item.getCoupon_click_url();
        LogUtils.d(this,url);
        if(TextUtils.isEmpty(url)){
            url=item.getClick_url();//这是详情界面！！！
        }
        String cover=item.getPict_url();
       // LogUtils.d(HomePagerFragment.class,title+url+cover);
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}
