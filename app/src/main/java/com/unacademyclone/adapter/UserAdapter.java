package com.unacademyclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.activity.LoginRegisterActivity;
import com.unacademyclone.activity.SearchAutocompleteActivity;
import com.unacademyclone.activity.UserProfileActivity;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.User;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DigitsUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    List<User> userList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container, ll_header, ll_follow;
        CircularImageView civ_avatar;
        TextView tv_name, tv_followers, tv_bio, tv_courses_value,tv_courses_title, tv_follow;

        public UserViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            ll_header = itemView.findViewById(R.id.ll_header);
            ll_follow = itemView.findViewById(R.id.ll_follow);
            civ_avatar = itemView.findViewById(R.id.civ_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_followers = itemView.findViewById(R.id.tv_followers);
            tv_bio = itemView.findViewById(R.id.tv_bio);
            tv_courses_value = itemView.findViewById(R.id.tv_courses_value);
            tv_courses_title = itemView.findViewById(R.id.tv_courses_title);
            tv_follow = itemView.findViewById(R.id.tv_follow);

            tv_name.setTypeface(tfUtil.getTypefaceSemiBold());
            tv_followers.setTypeface(tfUtil.getTypefaceRegular());
            tv_bio.setTypeface(tfUtil.getTypefaceRegular());
            tv_courses_value.setTypeface(tfUtil.getTypefaceRegular());
            tv_courses_title.setTypeface(tfUtil.getTypefaceRegular());
            tv_follow.setTypeface(tfUtil.getTypefaceRegular());

            // This code is used to get the screen dimensions of the user's device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            // Set the ViewHolder width to be a third of the screen size, and height to wrap content
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/1.2), RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 5, 5);
            ll_container.setLayoutParams(params);
        }
    }

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        final User user=userList.get(position);

        Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.civ_avatar);
        holder.tv_name.setText(user.getName());
        holder.tv_followers.setText(DigitsUtility.getCondensedNumber(user.getFollowers_count())+" Followers");
        holder.tv_bio.setText(user.getBio());
        holder.tv_courses_value.setText(user.getCourses_count()+"");

        holder.ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("user_name", user.getUser_name());
                context.startActivity(intent);
            }
        });

        holder.ll_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginRegisterActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
