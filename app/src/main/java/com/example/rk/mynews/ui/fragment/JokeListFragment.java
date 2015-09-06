package com.example.rk.mynews.ui.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.rk.mynews.R;
import com.example.rk.mynews.core.JokeBiz;
import com.example.rk.mynews.model.TYPE;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.adapter.FeedsAdapter;
import com.example.rk.mynews.ui.adapter.MyArrayAdapter;
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
public class JokeListFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener ,LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;

    /**
     * 界面组件
     */
    SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    /**
     * listview适配器
     */
    private MyArrayAdapter arrayAdapter;
    public MyArrayAdapter getArrayAdapter(){
        return arrayAdapter;
    }
    /**
     * fragment类型
     */
    private int FRAGMENT_TYPE;
    //是否为第一次加载
    private boolean mIsFirstLoad = true;
    private int page=0;//分页
    private int size=8;//每页条数
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * * @return A new instance of fragment JokeListFragment.
     */
    public static JokeListFragment newInstance(int type) {
        Log.i("newInstance", "");
        JokeListFragment fragment = new JokeListFragment();
        Bundle args = new Bundle();
        args.putInt("FRAGMENT_TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }

    public JokeListFragment() {
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
        //getLoaderManager().initLoader(0,null,this);
        return swipeRefreshLayout;
    }

    /**
     * 对view初始化
     *
     * @param view
     */
    FeedsAdapter feedsAdapter;
    public void initview(View view) {

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN);

        listView = (ListView) swipeRefreshLayout.findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        arrayAdapter = new MyArrayAdapter(getActivity(), R.layout.joke_item_list);

        LayoutInflater l=getActivity().getLayoutInflater();
        LinearLayout t= (LinearLayout) l.inflate(R.layout.loadingtextview, null);
        TitanicTextView tv= (TitanicTextView) t.findViewById(R.id.tv_titanic);
        Titanic ti=new Titanic();
        ti.start(tv);
        listView.addFooterView(t);
        listView.setAdapter(arrayAdapter);
        listView.setOnScrollListener(new JokeListScrollListion());

        JokeBiz.getJoke(arrayAdapter, TYPE.FROM_DB_REPLACE, size, page);

        mListener.onFragmentInteraction(swipeRefreshLayout);

    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.i("qq","onCreateLoader");
        return new CursorLoader(getActivity(), Uri.parse("content://com.example.rk.mynews.provider/joke"),null,null,null,null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        Log.i("qq","onLoadFinished");
        feedsAdapter.changeCursor(cursor);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


    /**
     * JokeList 滚动监听类
     */
    private class  JokeListScrollListion implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState==0){
                /*当list到最后一个人时，自动加载*/
                if (view.getLastVisiblePosition()==view.getCount()-1){
                    Random random = new Random();
                    int num = random.nextInt(26000);
                    JokeBiz.getJoke(arrayAdapter,TYPE.FROM_NET_ADD,5,num);
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
        JokeBiz.getJoke(arrayAdapter, TYPE.FROM_NET_REPLACE, 10,0);
    }

    /**
     * listview点击事件回调
     *
     * @param view

     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        swipeRefreshLayout.setRefreshing(false);
        Log.d(""+id,""+position);
    }

    /**
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(View view);
    }

}
