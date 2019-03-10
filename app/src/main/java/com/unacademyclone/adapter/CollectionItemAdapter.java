package com.unacademyclone.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
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
import com.unacademyclone.R;
import com.unacademyclone.activity.PostActivity;
import com.unacademyclone.model.CollectionItem;
import com.unacademyclone.model.Goal;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DurationUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.CollectionItemViewHolder> {
    Context context;
    List<CollectionItem> collectionItemList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String post_id;

    public class CollectionItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_collection_item;
        TextView tv_rank, tv_title, tv_duration;

        public CollectionItemViewHolder(View itemView) {
            super(itemView);
            ll_collection_item = itemView.findViewById(R.id.ll_collection_item);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_duration = itemView.findViewById(R.id.tv_duration);

            tv_rank.setTypeface(tfUtil.getTypefaceRegular());
            tv_title.setTypeface(tfUtil.getTypefaceRegular());
            tv_duration.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public CollectionItemAdapter(Context context, List<CollectionItem> collectionItemList, String post_id) {
        this.context = context;
        this.collectionItemList = collectionItemList;
        this.post_id = post_id;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public CollectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_collection_item, parent, false);
        return new CollectionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CollectionItemViewHolder holder, int position) {
        final CollectionItem collectionItem=collectionItemList.get(position);

        holder.ll_collection_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostActivity postActivity = (PostActivity) context;
                postActivity.updatePostID(collectionItem.getUid());
                post_id = collectionItem.getUid();
                notifyDataSetChanged();
            }
        });
        int rank = collectionItem.getRank();

        String index = rank>9? rank+"" : "0"+rank;
        holder.tv_rank.setText(index);
        holder.tv_title.setText(collectionItem.getTitle());
        holder.tv_duration.setText(DurationUtility.getTimerFromSeconds((int)collectionItem.getDuration()));

        if(post_id.equals(collectionItem.getUid())){
            holder.tv_rank.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.tv_title.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.tv_duration.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }
        else{
            holder.tv_rank.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.tv_title.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            holder.tv_duration.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return collectionItemList.size();
    }

}
