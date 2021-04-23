package com.example.baekhyun.ui.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baekhyun.R;
import com.example.baekhyun.View.ISearchCallback;
import com.example.baekhyun.base.BaseFragment;
import com.example.baekhyun.model.domain.Histroies;
import com.example.baekhyun.model.domain.Recommed;
import com.example.baekhyun.model.domain.SearchResult;
import com.example.baekhyun.presenter.ITicketPresenter;
import com.example.baekhyun.presenter.IsearchPresenter;
import com.example.baekhyun.ui.MyTextView;
import com.example.baekhyun.ui.activity.TicketActivity;
import com.example.baekhyun.ui.adapter.SearchAdapter;
import com.example.baekhyun.utils.PresenterManager;
import com.example.baekhyun.utils.ToastUtil;
import com.example.baekhyun.utils.KeyBorad;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment implements ISearchCallback, SearchAdapter.OnListclickListener {

    private IsearchPresenter mSearchpresenter;
    private MyTextView mMyTextView;
    private MyTextView mRecommendTextView;
    private LinearLayout msousuo;
    private LinearLayout mtuijian;
    private ImageView mdel;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private TwinklingRefreshLayout mRefreshLayout;
    private LinearLayout mLinearLayout;
    private TextView msearch;
    private EditText mEditText;
    private ImageView mdele;
    private ImageView back;

    @Override
    public int getRootResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_search_title, container, false);
    }

    @Override
    protected void initPresenter() {
        mSearchpresenter = PresenterManager.getInstance().getSearchpresenter();
        mSearchpresenter.registerViewCallback(this);
        mSearchpresenter.getRecommends();
        mSearchpresenter.getHistories();
    }

    @Override
    protected void release() {
        mSearchpresenter.unregisterViewCallback(this);
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
        mMyTextView = view.findViewById(R.id.search_history_view);
        mRecommendTextView = view.findViewById(R.id.search_recommend_view);
        msousuo = view.findViewById(R.id.search_history_container);
        mtuijian = view.findViewById(R.id.search_recommend_container);
        mdel = view.findViewById(R.id.search_history_delete);
        mRecyclerView = view.findViewById(R.id.search_result_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchAdapter = new SearchAdapter();
        mRecyclerView.setAdapter(mSearchAdapter);
        mRefreshLayout = view.findViewById(R.id.search_result_container);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableRefresh(false);
        mLinearLayout = view.findViewById(R.id.network_error);
        msearch = view.findViewById(R.id.scan);
        mdele = view.findViewById(R.id.chacha);
        mEditText = view.findViewById(R.id.sousuo);
        back = view.findViewById(R.id.back);
    }

    @Override
    protected void initListener() {
        mdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchpresenter.deleteHistories();
            }
        });
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchpresenter != null) {
                    mSearchpresenter.loadMore();
                }
            }
        });
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchpresenter != null)
                    mSearchpresenter.reSearch();
            }
        });
        mSearchAdapter.setOnListclickListener(this);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchpresenter != null) {
                    mSearchpresenter.doSearch(mEditText.getText().toString().trim());
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    mdele.setVisibility(View.VISIBLE);
                    msearch.setText("搜索");
                } else {
                    mdele.setVisibility(View.GONE);
                    msearch.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mdele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                switchToHistory();
            }
        });
        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().length() != 0) {
                    mSearchpresenter.doSearch(mEditText.getText().toString().trim());
                    back.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    KeyBorad.hide(getContext(), v);
                } else {
                    back.setVisibility(View.GONE);
                    KeyBorad.hide(getContext(), v);
                }
            }
        });
        mMyTextView.setOnMytextListener(new MyTextView.onMytextListener() {
            @Override
            public void onMytextclick(String text) {
                mRecyclerView.scrollToPosition(0);
                mEditText.setText(text);
                mEditText.requestFocus();
                mEditText.setSelection(text.length(), text.length());
                mSearchpresenter.doSearch(text);
                back.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                KeyBorad.hide(getContext(), getView());
            }
        });
        mRecommendTextView.setOnMytextListener(new MyTextView.onMytextListener() {
            @Override
            public void onMytextclick(String text) {
                mRecyclerView.scrollToPosition(0);
                mEditText.setText(text);
                mEditText.requestFocus();
                mEditText.setSelection(text.length(), text.length());
                mSearchpresenter.doSearch(text);
                back.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                KeyBorad.hide(getContext(), getView());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                switchToHistory();
                KeyBorad.hide(getContext(), getView());
            }
        });
    }

    private void switchToHistory() {
        mSearchpresenter.getHistories();
        back.setVisibility(View.GONE);
        if (mMyTextView.getsize() != 0) {
            msousuo.setVisibility(View.VISIBLE);
            mtuijian.setVisibility(View.VISIBLE);
        } else {
            msousuo.setVisibility(View.GONE);
            mtuijian.setVisibility(View.VISIBLE);
        }
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onHistoriesLoaded(Histroies list) {
        if (list == null || list.getList().size() == 0) {
            msousuo.setVisibility(View.GONE);
        } else {
            msousuo.setVisibility(View.VISIBLE);
            mMyTextView.setList(list.getList());
        }
    }

    @Override
    public void onHistoriesDel() {
        mSearchpresenter.getHistories();
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        mtuijian.setVisibility(View.GONE);
        msousuo.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        try {
            mSearchAdapter.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            setUpState(State.EMPTY);
        }
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> map = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchAdapter.addData(result);
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreError() {
        ToastUtil.getToast("网络出错啦");
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreEmpty() {
        ToastUtil.getToast("没有更多内容啦");
        mRefreshLayout.finishLoadmore();
    }

    @Override
    public void onRecommed(List<Recommed.DataBean> list) {
        List<String> textlist = new ArrayList<>();
        for (Recommed.DataBean dataBean : list) {
            textlist.add(dataBean.getKeyword());
        }
        if (list == null || list.size() == 0) {
            mtuijian.setVisibility(View.GONE);
        } else {

            mRecommendTextView.setList(textlist);
            mtuijian.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void OnitemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item) {
        handItemClick(item);
    }

    private void handItemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item) {
        String title = item.getTitle();
        String url = item.getCoupon_share_url();
        if (TextUtils.isEmpty(url)) {
            url = item.getUrl();//这是详情界面！！！
        }
        String cover = item.getPict_url();
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}
