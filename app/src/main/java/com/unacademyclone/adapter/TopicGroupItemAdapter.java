package com.unacademyclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.model.Educator;
import com.unacademyclone.model.TopicGroupItem;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DurationUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.Duration;

public class TopicGroupItemAdapter extends RecyclerView.Adapter<TopicGroupItemAdapter.TopicGroupItemViewHolder> {
    Context context;
    List<TopicGroupItem> topicGroupItemList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class TopicGroupItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container;
        ImageView iv_cover_photo;
        TextView tv_topic_group_name, tv_name, tv_starts_on, tv_bullet, tv_lessons, tv_author_name;

        public TopicGroupItemViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            iv_cover_photo = itemView.findViewById(R.id.iv_cover_photo);
            tv_topic_group_name = itemView.findViewById(R.id.tv_topic_group_name);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_starts_on = itemView.findViewById(R.id.tv_starts_on);
            tv_bullet = itemView.findViewById(R.id.tv_bullet);
            tv_lessons = itemView.findViewById(R.id.tv_lessons);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);

            tv_topic_group_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_name.setTypeface(tfUtil.getTypefaceSemiBold());
            tv_starts_on.setTypeface(tfUtil.getTypefaceRegular());
            tv_bullet.setTypeface(tfUtil.getTypefaceRegular());
            tv_lessons.setTypeface(tfUtil.getTypefaceRegular());
            tv_author_name.setTypeface(tfUtil.getTypefaceRegular());

            // This code is used to get the screen dimensions of the user's device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            // Set the ViewHolder width to be a third of the screen size, and height to wrap content
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/1.25), RecyclerView.LayoutParams.MATCH_PARENT);
            params.setMargins(30, 5, 20, 5);
            ll_container.setLayoutParams(params);
        }
    }

    public TopicGroupItemAdapter(Context context, List<TopicGroupItem> topicGroupItemList) {
        this.context = context;
        this.topicGroupItemList = topicGroupItemList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public TopicGroupItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topic_group_item, parent, false);
        return new TopicGroupItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopicGroupItemViewHolder holder, int position) {
        final TopicGroupItem topicGroupItem=topicGroupItemList.get(position);

        Picasso.with(context).load(topicGroupItem.getCover_photo()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.iv_cover_photo);
        holder.tv_topic_group_name.setText(topicGroupItem.getTopic_group_name().toUpperCase());
        holder.tv_name.setText(topicGroupItem.getName());
        holder.tv_starts_on.setText("Starts on "+DurationUtility.getDateFromZulu(topicGroupItem.getStarts_at()));
        holder.tv_lessons.setText(topicGroupItem.getItem_count()+"lessons");
        holder.tv_author_name.setText(topicGroupItem.getAuthor_name());
    }

    @Override
    public int getItemCount() {
        return topicGroupItemList.size();
    }

}
