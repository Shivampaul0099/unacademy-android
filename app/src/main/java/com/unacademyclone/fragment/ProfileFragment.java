package com.unacademyclone.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unacademyclone.R;
import com.unacademyclone.utility.TypefaceUtility;


public class ProfileFragment extends Fragment {

    Context context;
    TypefaceUtility tfUtil;

    TextView tv_title, tv_create_profile, tv_desc, tv_follow;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        tfUtil=new TypefaceUtility(context);

        tv_title=rootView.findViewById(R.id.tv_title);
        tv_create_profile=rootView.findViewById(R.id.tv_create_profile);
        tv_desc=rootView.findViewById(R.id.tv_desc);
        tv_follow=rootView.findViewById(R.id.tv_follow);

        tv_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_create_profile.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_desc.setTypeface(tfUtil.getTypefaceRegular());
        tv_follow.setTypeface(tfUtil.getTypefaceRegular());

        return rootView;
    }

}
