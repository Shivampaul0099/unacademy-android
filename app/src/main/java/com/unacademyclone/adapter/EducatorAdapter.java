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
import com.unacademyclone.model.AllTopicsItem;
import com.unacademyclone.model.Educator;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class EducatorAdapter extends RecyclerView.Adapter<EducatorAdapter.EducatorViewHolder> {
    Context context;
    List<Educator> educatorList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class EducatorViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container;
        ImageView iv_avatar;
        TextView tv_name, tv_live_minutes;

        public EducatorViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_live_minutes = itemView.findViewById(R.id.tv_live_minutes);

            tv_name.setTypeface(tfUtil.getTypefaceRegular());
            tv_live_minutes.setTypeface(tfUtil.getTypefaceLight());

            // This code is used to get the screen dimensions of the user's device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            // Set the ViewHolder width to be a third of the screen size, and height to wrap content
//            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/2.5), RecyclerView.LayoutParams.WRAP_CONTENT);
//            params.setMargins(10, 5, 0, 10);
//            ll_container.setLayoutParams(params);
        }
    }

    public EducatorAdapter(Context context, List<Educator> educatorList) {
        this.context = context;
        this.educatorList = educatorList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public EducatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_educator, parent, false);
        return new EducatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EducatorViewHolder holder, int position) {
        final Educator educator=educatorList.get(position);

        holder.tv_name.setText(educator.getFirst_name()+" "+educator.getLast_name());
        holder.tv_live_minutes.setText(((int)Math.round((educator.getLive_minutes())/60000))+"k live hours");

        Picasso.with(context).load(educator.getAvatar()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.iv_avatar);
    }

    @Override
    public int getItemCount() {
        return educatorList.size();
    }

}
