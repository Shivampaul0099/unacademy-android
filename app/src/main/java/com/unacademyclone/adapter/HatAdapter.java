package com.unacademyclone.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.model.Hat;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DigitsUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class HatAdapter extends RecyclerView.Adapter<HatAdapter.HatViewHolder> {
    Context context;
    List<Hat> hatList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class HatViewHolder extends RecyclerView.ViewHolder {
        FrameLayout fl_container;
        LinearLayout ll_count;
        CircularImageView civ_hat;
        TextView tv_count;

        public HatViewHolder(View itemView) {
            super(itemView);
            fl_container = itemView.findViewById(R.id.fl_container);
            ll_count = itemView.findViewById(R.id.ll_count);
            civ_hat = itemView.findViewById(R.id.civ_hat);
            tv_count = itemView.findViewById(R.id.tv_count);

            tv_count.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public HatAdapter(Context context, List<Hat> hatList) {
        this.context = context;
        this.hatList = hatList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public HatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hat, parent, false);
        return new HatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HatViewHolder holder, int position) {
        final Hat hat=hatList.get(position);

        Picasso.with(context).load(hat.getHat_icon()).error(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.civ_hat);
        if(hat.isShowCount()){
            holder.ll_count.setVisibility(View.VISIBLE);
            holder.tv_count.setText(DigitsUtility.getCondensedNumber(hat.getCount()));
        }
        else{
            holder.ll_count.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return hatList.size();
    }

}
