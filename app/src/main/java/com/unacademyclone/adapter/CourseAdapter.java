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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.activity.PostActivity;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.Goal;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    Context context;
    List<Course> courseList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_course;
        FrameLayout fl_thumbnail;
        ImageView iv_course_thumbnail;
        TextView tv_lessons, tv_language, tv_collection_name, tv_course_title;
        TextView tv_average_star, tv_star, tv_ratings, tv_author_name;
        CircularImageView civ_author_avatar;


        public CourseViewHolder(View itemView) {
            super(itemView);
            ll_course = itemView.findViewById(R.id.ll_course);
            fl_thumbnail = itemView.findViewById(R.id.fl_thumbnail);
            iv_course_thumbnail = itemView.findViewById(R.id.iv_course_thumbnail);
            tv_lessons = itemView.findViewById(R.id.tv_lessons);
            tv_language = itemView.findViewById(R.id.tv_language);
            tv_collection_name = itemView.findViewById(R.id.tv_collection_name);
            tv_course_title = itemView.findViewById(R.id.tv_course_title);
            tv_average_star = itemView.findViewById(R.id.tv_average_star);
            tv_star = itemView.findViewById(R.id.tv_star);
            tv_ratings = itemView.findViewById(R.id.tv_ratings);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            civ_author_avatar = itemView.findViewById(R.id.civ_author_avatar);


            tv_lessons.setTypeface(tfUtil.getTypefaceRegular());
            tv_language.setTypeface(tfUtil.getTypefaceRegular());
            tv_collection_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_course_title.setTypeface(tfUtil.getTypefaceRegular());
            tv_average_star.setTypeface(tfUtil.getTypefaceRegular());
            tv_star.setTypeface(tfUtil.getTypefaceRegular());
            tv_ratings.setTypeface(tfUtil.getTypefaceRegular());
            tv_author_name.setTypeface(tfUtil.getTypefaceRegular());

            // This code is used to get the screen dimensions of the user's device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            // Set the ViewHolder width to be a third of the screen size, and height to wrap content
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/1.1), RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 5, 0, 20);
            ll_course.setLayoutParams(params);
        }
    }

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseViewHolder holder, int position) {
        final Course course=courseList.get(position);

        Picasso.with(context).load(course.getThumbnail()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.iv_course_thumbnail, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Picasso.with(context).load(course.getThumbnail()).into(holder.iv_course_thumbnail);
            }
        });

        holder.ll_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("post_id", "");
                intent.putExtra("collection_id", course.getUid());
                context.startActivity(intent);
            }
        });

        holder.tv_lessons.setText(course.getItem_count()+" Lessons");
        holder.tv_language.setText(course.getLanguage_display().toUpperCase());
        holder.tv_collection_name.setText(course.getConcept_topology_title());
        holder.tv_course_title.setText(course.getName());
        holder.tv_average_star.setText(course.getAvg_rating()+"");
        holder.tv_ratings.setText(course.getTotal_ratings()+" ratings");
        Picasso.with(context).load(course.getAuthorAvatar()).into(holder.civ_author_avatar);
        holder.tv_author_name.setText(course.getAuthorName());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

}
