package com.yang.mytest;

import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

    private PullToRefreshListView mPullList;
    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    private String[] mStrings = {"我很善良", "我很温柔", "我是淘女郎",
            "我是阿里郎", "我是大灰狼", "我是羊羊羊"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setEventListener();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPullList = (PullToRefreshListView) findViewById(R.id.pl_refresh);
    }

    /**
     * 设置监听
     */
    private void setEventListener() {
        OnRefreshListener2 listener2 = new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // 下拉刷新触发的事件
                //获取格式化的时间
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                //	更新LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                ///开启线程模拟调接口填充数据
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // 上提加载触发的事件
                ///开启线程模拟调接口填充数据
                new GetDataTask().execute();
            }
        };

        mPullList.setOnRefreshListener(listener2);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 设置PullToRefreshListView的模式
        mPullList.setMode(Mode.BOTH);

        // 设置PullRefreshListView上提加载时的加载提示
        mPullList.setMode(Mode.BOTH);
        mPullList.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载啦...");
        mPullList.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载啦...");
        mPullList.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多啦...");

        // 设置PullRefreshListView下拉加载时的加载提示
        mPullList.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新啦...");
        mPullList.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在加载啦...");
        mPullList.getLoadingLayoutProxy(true, false).setReleaseLabel("松开加载更多啦...");

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
        mPullList.setAdapter(mAdapter);

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (mPullList.isHeaderShown()) {
                mListItems.addFirst("我是新纳入的妾——下拉刷新");
            } else if (mPullList.isFooterShown()) {
                mListItems.addLast("我是让你重回怀抱的妾——上提加载");
            }

            mAdapter.notifyDataSetChanged();

            // 调用刷新完成
            mPullList.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}
