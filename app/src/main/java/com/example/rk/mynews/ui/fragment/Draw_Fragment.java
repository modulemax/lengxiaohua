package com.example.rk.mynews.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rk.mynews.R;
import com.example.rk.mynews.ui.activity.MainActivity;
import com.example.rk.mynews.ui.activity.SettingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Draw_Fragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private ImageButton setteing_but;


    public Draw_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.draw_fragment, container, false);
        setteing_but= (ImageButton) view.findViewById(R.id.img_setting);
        setteing_but.setOnClickListener(this);
        TextView fragment_1= (TextView) view.findViewById(R.id.fragment_1);
        TextView fragment_2= (TextView) view.findViewById(R.id.fragment_2);
        TextView fragment_3= (TextView) view.findViewById(R.id.fragment_3);
        fragment_1.setOnClickListener(this);
        fragment_3.setOnClickListener(this);
        fragment_2.setOnClickListener(this);
        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        MainActivity activity= (MainActivity) getActivity();
        switch (id){
            case R.id.img_setting:Intent intent=new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_1:activity.getViewPager().setCurrentItem(0);activity.getDrawerLayout().closeDrawers();
                break;
            case R.id.fragment_2:activity.getViewPager().setCurrentItem(1);activity.getDrawerLayout().closeDrawers();
                break;
            case R.id.fragment_3:activity.getViewPager().setCurrentItem(2);activity.getDrawerLayout().closeDrawers();
                break;
            default:break;
        }

    }

    /**
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
