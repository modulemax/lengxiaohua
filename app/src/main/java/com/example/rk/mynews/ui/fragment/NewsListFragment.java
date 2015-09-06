package com.example.rk.mynews.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.NewsBiz;
import com.example.rk.mynews.model.TYPE;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.activity.NewsContentActivity;
import com.example.rk.mynews.ui.adapter.NewsListAdapter;
import com.example.rk.mynews.ui.view.titanic.Titanic;
import com.example.rk.mynews.ui.view.titanic.TitanicTextView;

import java.util.Random;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JokeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * 界面组件
     */
    SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    /**
     * listview适配器
     */
    private NewsListAdapter arrayAdapter;
    /**
     * fragment类型
     */
    private int FRAGMENT_TYPE;
    //是否为第一次加载
    private boolean mIsFirstLoad = true;
    private int page=0;//分页
    private int size=8;//每页条数
    Date date=new Date();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * * @return A new instance of fragment JokeListFragment.
     */
    public static NewsListFragment newInstance(int type) {
        Log.i("newInstance", "");
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt("FRAGMENT_TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "");
        if (getArguments() != null) {
            FRAGMENT_TYPE = getArguments().getInt("FRAGMENT_TYPE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("onCreateView", "");
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.swipe_refresh, container, false);
        initview(swipeRefreshLayout);

        return swipeRefreshLayout;
    }

    /**
     * 对view初始化
     *
     * @param view
     */
    public void initview(View view) {

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN);

        listView = (ListView) swipeRefreshLayout.findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        arrayAdapter = new NewsListAdapter(getActivity(), R.layout.news_item_list);
        listView.setAdapter(arrayAdapter);
        LayoutInflater l=getActivity().getLayoutInflater();
        LinearLayout t= (LinearLayout) l.inflate(R.layout.loadingtextview, null);
        TitanicTextView tv= (TitanicTextView) t.findViewById(R.id.tv_titanic);
        Titanic ti=new Titanic();
        ti.start(tv);
        listView.addFooterView(t);

        NewsBiz.getNews(arrayAdapter, TYPE.FROM_DB_REPLACE, 15, date.getTime());
        listView.setOnScrollListener(new NewsListListener());
        mListener.onFragmentInteraction2(swipeRefreshLayout);

    }

    /**
     * JokeList 滚动监听类
     */
    private class  NewsListListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (scrollState==0){
                /*当list到最后一个人时，自动加载*/
                if (view.getLastVisiblePosition()==view.getCount()-1){
                    Random random = new Random();
                    int num = random.nextInt(26000);
                    NewsBiz.getNews(arrayAdapter, TYPE.FROM_NET_ADD,
                            15,
                            arrayAdapter.list.get(arrayAdapter.list.size()-1).behot_time);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i("onAttach", "");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 下拉刷新回调
     */
    @Override
    public void onRefresh() {
        NewsBiz.getNews(arrayAdapter, TYPE.FROM_NET_REPLACE, 15, date.getTime());
    }

    /**
     * listview点击事件回调
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        swipeRefreshLayout.setRefreshing(false);
        Intent intent=new Intent(getActivity(),NewsContentActivity.class);
        intent.setPackage(getActivity().getPackageName());
        intent.putExtra("contentURL", arrayAdapter.list.get(position).article_url);
        intent.putExtra("source",arrayAdapter.list.get(position).source);
        startActivity(intent);
    }

    /**
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction2(View view);
    }

}
