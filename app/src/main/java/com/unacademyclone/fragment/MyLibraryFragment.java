package com.unacademyclone.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unacademyclone.R;
import com.unacademyclone.activity.LoginRegisterActivity;
import com.unacademyclone.utility.TypefaceUtility;

public class MyLibraryFragment extends Fragment {

    Context context;
    TypefaceUtility tfUtil;

    LinearLayout ll_login_signup;
    TextView tv_search_hint, tv_lists, tv_courses, tv_lessons, tv_downloads, tv_history;
    TextView tv_title, tv_sub_title, tv_login_signup;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context=getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_library, container, false);
        tfUtil=new TypefaceUtility(context);

        tv_search_hint=rootView.findViewById(R.id.tv_search_hint);
        tv_lists=rootView.findViewById(R.id.tv_lists);
        tv_courses=rootView.findViewById(R.id.tv_courses);
        tv_lessons=rootView.findViewById(R.id.tv_lessons);
        tv_downloads=rootView.findViewById(R.id.tv_downloads);
        tv_history=rootView.findViewById(R.id.tv_history);
        tv_title=rootView.findViewById(R.id.tv_title);
        tv_sub_title=rootView.findViewById(R.id.tv_sub_title);
        tv_login_signup=rootView.findViewById(R.id.tv_login_signup);
        ll_login_signup=rootView.findViewById(R.id.ll_login_signup);


        tv_search_hint.setTypeface(tfUtil.getTypefaceRegular());
        tv_lists.setTypeface(tfUtil.getTypefaceRegular());
        tv_courses.setTypeface(tfUtil.getTypefaceRegular());
        tv_lessons.setTypeface(tfUtil.getTypefaceRegular());
        tv_downloads.setTypeface(tfUtil.getTypefaceRegular());
        tv_history.setTypeface(tfUtil.getTypefaceRegular());
        tv_title.setTypeface(tfUtil.getTypefaceBold());
        tv_sub_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_login_signup.setTypeface(tfUtil.getTypefaceRegular());

        ll_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LoginRegisterActivity.class));
            }
        });

        return rootView;
    }

}
