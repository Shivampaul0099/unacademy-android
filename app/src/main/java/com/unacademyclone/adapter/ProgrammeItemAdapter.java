package com.unacademyclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.ProgrammeItem;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DurationUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

import javax.xml.datatype.Duration;

public class ProgrammeItemAdapter extends RecyclerView.Adapter<ProgrammeItemAdapter.ProgrammeItemViewHolder> {
    Context context;
    List<ProgrammeItem> programmeItemList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class ProgrammeItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_programme_item;
        TextView tv_month_day, tv_name, tv_rank, tv_bullet, tv_time;

        public ProgrammeItemViewHolder(View itemView) {
            super(itemView);
            ll_programme_item = itemView.findViewById(R.id.ll_programme_item);
            tv_month_day = itemView.findViewById(R.id.tv_month_day);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_bullet = itemView.findViewById(R.id.tv_bullet);
            tv_time = itemView.findViewById(R.id.tv_time);

            tv_month_day.setTypeface(tfUtil.getTypefaceRegular());
            tv_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_rank.setTypeface(tfUtil.getTypefaceRegular());
            tv_bullet.setTypeface(tfUtil.getTypefaceRegular());
            tv_time.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public ProgrammeItemAdapter(Context context, List<ProgrammeItem> programmeItemList) {
        this.context = context;
        this.programmeItemList = programmeItemList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public ProgrammeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_programme_item, parent, false);
        return new ProgrammeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgrammeItemViewHolder holder, int position) {
        final ProgrammeItem programmeItem=programmeItemList.get(position);
        Log.e("created_at", programmeItem.getRank()+" : "+programmeItem.getCreated_at());

        holder.tv_month_day.setText(DurationUtility.getMonthDayFromAZulu(programmeItem.getCreated_at(), true).replaceAll(" ", "\n"));
        holder.tv_name.setText(programmeItem.getName());
        holder.tv_rank.setText("Lesson "+programmeItem.getRank());
        holder.tv_time.setText(DurationUtility.getTimeFromZulu(programmeItem.getCreated_at(), true));
    }

    @Override
    public int getItemCount() {
        return programmeItemList.size();
    }

}
