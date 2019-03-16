package com.unacademyclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.activity.LoginRegisterActivity;
import com.unacademyclone.activity.PostActivity;
import com.unacademyclone.activity.UserProfileActivity;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.Story;
import com.unacademyclone.model.StoryAuthor;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DurationUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    Context context;
    List<Story> storyList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_story, ll_user, ll_single_course, ll_multiple_courses, ll_actions;
        CircularImageView civ_user, civ_course_author_avatar;
        TextView tv_user_name, tv_verb, tv_published, tv_message, tv_language, tv_collection_name, tv_course_title;
        TextView tv_duration, tv_bullet, tv_views, tv_average_star, tv_star, tv_ratings, tv_course_author_name;
        TextView tv_combo_name, tv_likes, tv_comments, tv_share, tv_save, tv_multi_save;
        ImageView iv_verified, iv_thumbnail;
        RecyclerView rv_courses;

        public StoryViewHolder(View itemView) {
            super(itemView);
            ll_story = itemView.findViewById(R.id.ll_story);
            ll_user = itemView.findViewById(R.id.ll_user);
            ll_single_course = itemView.findViewById(R.id.ll_single_course);
            ll_multiple_courses = itemView.findViewById(R.id.ll_multiple_courses);
            ll_actions = itemView.findViewById(R.id.ll_actions);

            civ_user = itemView.findViewById(R.id.civ_user);
            civ_course_author_avatar = itemView.findViewById(R.id.civ_course_author_avatar);

            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            iv_verified = itemView.findViewById(R.id.iv_verified);
            tv_verb = itemView.findViewById(R.id.tv_verb);
            tv_published = itemView.findViewById(R.id.tv_published);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_language = itemView.findViewById(R.id.tv_language);
            tv_collection_name = itemView.findViewById(R.id.tv_collection_name);
            tv_course_title = itemView.findViewById(R.id.tv_course_title);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_bullet = itemView.findViewById(R.id.tv_bullet);
            tv_views = itemView.findViewById(R.id.tv_views);
            tv_average_star = itemView.findViewById(R.id.tv_average_star);
            tv_star = itemView.findViewById(R.id.tv_star);
            tv_ratings = itemView.findViewById(R.id.tv_ratings);
            tv_course_author_name = itemView.findViewById(R.id.tv_course_author_name);
            tv_combo_name = itemView.findViewById(R.id.tv_combo_name);
            tv_ratings = itemView.findViewById(R.id.tv_ratings);
            tv_likes = itemView.findViewById(R.id.tv_likes);
            tv_comments = itemView.findViewById(R.id.tv_comments);
            tv_share = itemView.findViewById(R.id.tv_share);
            tv_ratings = itemView.findViewById(R.id.tv_ratings);
            iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tv_save = itemView.findViewById(R.id.tv_save);
            tv_multi_save = itemView.findViewById(R.id.tv_multi_save);
            rv_courses = itemView.findViewById(R.id.rv_courses);


            tv_user_name.setTypeface(tfUtil.getTypefaceSemiBold());
            tv_verb.setTypeface(tfUtil.getTypefaceRegular());
            tv_published.setTypeface(tfUtil.getTypefaceRegular());
            tv_message.setTypeface(tfUtil.getTypefaceRegular());
            tv_language.setTypeface(tfUtil.getTypefaceRegular());
            tv_collection_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_course_title.setTypeface(tfUtil.getTypefaceRegular());
            tv_duration.setTypeface(tfUtil.getTypefaceRegular());
            tv_bullet.setTypeface(tfUtil.getTypefaceRegular());
            tv_views.setTypeface(tfUtil.getTypefaceRegular());
            tv_average_star.setTypeface(tfUtil.getTypefaceRegular());
            tv_star.setTypeface(tfUtil.getTypefaceRegular());
            tv_ratings.setTypeface(tfUtil.getTypefaceRegular());
            tv_course_author_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_combo_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_ratings.setTypeface(tfUtil.getTypefaceRegular());
            tv_likes.setTypeface(tfUtil.getTypefaceRegular());
            tv_comments.setTypeface(tfUtil.getTypefaceRegular());
            tv_share.setTypeface(tfUtil.getTypefaceRegular());
            tv_ratings.setTypeface(tfUtil.getTypefaceRegular());
            tv_save.setTypeface(tfUtil.getTypefaceSemiBold());
            tv_multi_save.setTypeface(tfUtil.getTypefaceSemiBold());
        }
    }

    public StoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StoryViewHolder holder, int position) {
        final Story story=storyList.get(position);
        final StoryAuthor storyAuthor = story.getStoryAuthor();

        Picasso.with(context).load(storyAuthor.getAvatar()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.civ_user);
        holder.tv_user_name.setText(storyAuthor.getFirst_name()+" "+storyAuthor.getLast_name());

        if(!story.getVerb_text().equals("")){
            holder.tv_verb.setVisibility(View.VISIBLE);
            holder.tv_verb.setText(story.getVerb_text());
        }
        else{
            holder.tv_verb.setVisibility(View.VISIBLE);
        }
        if(storyAuthor.isIs_verified_educator()){
            holder.iv_verified.setVisibility(View.VISIBLE);
        }
        else{
            holder.iv_verified.setVisibility(View.GONE);
        }

        holder.tv_published.setText(DurationUtility.getTimeAgo(story.getCreated_at()));
        holder.tv_message.setText(story.getMessage());
        Picasso.with(context).load(story.getVideo_thumbnail()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.iv_thumbnail);

        if(story.getLanguage()!=null){
            holder.tv_language.setText(story.getLanguage().toUpperCase());
        }

        holder.tv_collection_name.setText(story.getCollection_name());
        holder.tv_course_title.setText(story.getTitle());
        if(!story.getMessage().equals("")){
            holder.tv_message.setVisibility(View.VISIBLE);
            holder.tv_message.setText(story.getMessage());
        }
        else{
            holder.tv_message.setVisibility(View.GONE);
        }

        if(story.getObject_type() == 4){
            holder.ll_single_course.setVisibility(View.VISIBLE);
            holder.ll_multiple_courses.setVisibility(View.GONE);
            holder.tv_duration.setText(DurationUtility.getTimerFromSeconds((int) story.getVideo_duration()));
            holder.tv_views.setText(story.getPlay_count()+"");
            holder.iv_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("collection_id", story.getObject_meta());
                    context.startActivity(intent);
                }
            });
        }
        else if(story.getObject_type() ==5){
            holder.ll_single_course.setVisibility(View.VISIBLE);
            holder.ll_multiple_courses.setVisibility(View.GONE);
            holder.tv_average_star.setText(story.getAverage_rating_star()+"");
            holder.tv_ratings.setText(story.getTotal_ratings()+"");
            Picasso.with(context).load(story.getCourse_author_avatar()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.civ_course_author_avatar);
            holder.tv_course_author_name.setText(story.getCourse_author_name()+"");
            holder.iv_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("post_id", story.getObject_meta());
                    context.startActivity(intent);
                }
            });
        }
        else{
            holder.ll_single_course.setVisibility(View.GONE);
            holder.ll_multiple_courses.setVisibility(View.VISIBLE);
            holder.tv_combo_name.setText(story.getTitle());

            CourseAdapter courseAdapter=new CourseAdapter(context,story.getCourseList(), false);

            LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.rv_courses.setLayoutManager(llm);
            holder.rv_courses.setAdapter(courseAdapter);
            holder.rv_courses.setNestedScrollingEnabled(false);
        }

        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("user_name", story.getStoryAuthor().getUsername());
                context.startActivity(intent);
            }
        });

        holder.tv_likes.setText(story.getReactions_count()+" likes");
        holder.tv_comments.setText(story.getComments_count()+" comments");

        holder.ll_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LoginRegisterActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

}
