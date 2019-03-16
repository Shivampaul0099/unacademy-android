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
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.activity.SearchAutocompleteActivity;
import com.unacademyclone.activity.SearchResultsActivity;
import com.unacademyclone.activity.UserProfileActivity;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.SearchAutocompleteItem;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DigitsUtility;
import com.unacademyclone.utility.TypefaceUtility;

import java.util.List;

public class SearchAutocompleteItemAdapter extends RecyclerView.Adapter<SearchAutocompleteItemAdapter.SearchAutocompleteItemViewHolder> {
    Context context;
    List<SearchAutocompleteItem> searchAutocompleteItemList;
    TypefaceUtility tfUtil;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class SearchAutocompleteItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container, ll_subtitle;
        CircularImageView civ_image;
        TextView tv_title, tv_followers, tv_bullet, tv_courses;

        public SearchAutocompleteItemViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            ll_subtitle = itemView.findViewById(R.id.ll_subtitle);
            civ_image = itemView.findViewById(R.id.civ_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_followers = itemView.findViewById(R.id.tv_followers);
            tv_bullet = itemView.findViewById(R.id.tv_bullet);
            tv_courses = itemView.findViewById(R.id.tv_courses);

            tv_title.setTypeface(tfUtil.getTypefaceRegular());
            tv_followers.setTypeface(tfUtil.getTypefaceRegular());
            tv_bullet.setTypeface(tfUtil.getTypefaceRegular());
            tv_courses.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public SearchAutocompleteItemAdapter(Context context, List<SearchAutocompleteItem> searchAutocompleteItemList) {
        this.context = context;
        this.searchAutocompleteItemList = searchAutocompleteItemList;
        tfUtil = new TypefaceUtility(context);
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public SearchAutocompleteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_autocomplete_item, parent, false);
        return new SearchAutocompleteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchAutocompleteItemViewHolder holder, int position) {
        final SearchAutocompleteItem searchAutocompleteItem=searchAutocompleteItemList.get(position);
        final String title=searchAutocompleteItem.getTitle();

        holder.tv_title.setText(title);
        holder.ll_subtitle.setVisibility(View.GONE);

        String type = searchAutocompleteItem.getType();

        if(type.equals("popular") || type.equals("search") || type.equals("keyword")){
            holder.ll_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SearchAutocompleteActivity)context).updateSearchQuery(title);
                    Intent intent = new Intent(context, SearchResultsActivity.class);
                    intent.putExtra("query", title);
                    context.startActivity(intent);
                }
            });
        }

        switch (type){
            case "popular":{
                Picasso.with(context).load(R.mipmap.ic_trending).into(holder.civ_image);

                break;
            }
            case "search":{
                Picasso.with(context).load(R.mipmap.ic_search_cirlcle).into(holder.civ_image);
                break;
            }
            case "user":{
                Picasso.with(context).load(searchAutocompleteItem.getAvatar()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.civ_image);
                holder.ll_subtitle.setVisibility(View.VISIBLE);
                holder.tv_followers.setText(DigitsUtility.getCondensedNumber(searchAutocompleteItem.getFollowers())+" followers");
                holder.tv_courses.setText(searchAutocompleteItem.getCourses()+" courses");
                holder.ll_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("user_name", searchAutocompleteItem.getUser_name());
                        context.startActivity(intent);
                    }
                });
                break;
            }
            case "keyword":{
                Picasso.with(context).load(R.mipmap.ic_search_cirlcle).into(holder.civ_image);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return searchAutocompleteItemList.size();
    }

}
