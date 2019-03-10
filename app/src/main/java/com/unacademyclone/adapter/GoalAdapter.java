package com.unacademyclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.model.Goal;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;
import com.unacademyclone.*;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    Context context;
    List<Goal> goalList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_goal;
        ImageView iv_icon;
        TextView tv_name;

        public GoalViewHolder(View itemView) {
            super(itemView);
            ll_goal = itemView.findViewById(R.id.ll_goal);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);

            tv_name.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public GoalAdapter(Context context, List<Goal> goalList) {
        this.context = context;
        this.goalList = goalList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        final Goal goal=goalList.get(position);

        holder.tv_name.setText(goal.getName());
        holder.ll_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bIntent = new Intent();
                bIntent.putExtra("type","updateGoal");
                bIntent.putExtra("goal_uid",goal.getUid());
                bIntent.putExtra("goal_title",goal.getName());
                bIntent.setAction("PlusFragment");
                context.sendBroadcast(bIntent);
                ((Activity)context).finish();
            }
        });

        Picasso.with(context).load(goal.getIcon_url()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.iv_icon, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                Picasso.with(context).load(goal.getIcon_url()).into(holder.iv_icon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
