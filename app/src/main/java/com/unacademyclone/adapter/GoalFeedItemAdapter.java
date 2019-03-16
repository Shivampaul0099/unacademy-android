package com.unacademyclone.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.GoalFeedItem;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class GoalFeedItemAdapter extends RecyclerView.Adapter<GoalFeedItemAdapter.GoalFeedItemViewHolder> {
    Context context;
    List<GoalFeedItem> goalFeedItemList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class GoalFeedItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container;
        TextView tv_title, tv_see_all;
        RecyclerView rv_items;

        public GoalFeedItemViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_see_all = itemView.findViewById(R.id.tv_see_all);
            rv_items = itemView.findViewById(R.id.rv_items);

            tv_title.setTypeface(tfUtil.getTypefaceSemiBold());
            tv_see_all.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public GoalFeedItemAdapter(Context context, List<GoalFeedItem> goalFeedItemList) {
        this.context = context;
        this.goalFeedItemList = goalFeedItemList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public GoalFeedItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_goal_feed_item, parent, false);
        return new GoalFeedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoalFeedItemViewHolder holder, int position) {
        final GoalFeedItem goalFeedItem=goalFeedItemList.get(position);

        holder.tv_title.setText(goalFeedItem.getTitle());
        holder.rv_items.setNestedScrollingEnabled(true);


        if(goalFeedItem.getType().equals("all_topic_groups")){

            holder.tv_see_all.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.rv_items.setLayoutManager(llm);

            AllTopicsItemAdapter allTopicsItemAdapter = new AllTopicsItemAdapter(context, goalFeedItem.getAllTopicsItemList());
            holder.rv_items.setAdapter(allTopicsItemAdapter);

        }
        else if(goalFeedItem.getType().equals("educators")){
            holder.tv_see_all.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            holder.rv_items.setLayoutManager(llm);

            EducatorAdapter educatorAdapter = new EducatorAdapter(context, goalFeedItem.getEducatorList());
            holder.rv_items.setAdapter(educatorAdapter);

        }
        else if(goalFeedItem.getType().equals("topic_group")){
            holder.tv_see_all.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.rv_items.setLayoutManager(llm);

            TopicGroupItemAdapter topicGroupItemAdapter = new TopicGroupItemAdapter(context, goalFeedItem.getTopicGroupItemList(), true);
            holder.rv_items.setAdapter(topicGroupItemAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return goalFeedItemList.size();
    }

}
