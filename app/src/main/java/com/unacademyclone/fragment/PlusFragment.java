package com.unacademyclone.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.unacademyclone.R;
import com.unacademyclone.activity.GoalsActivity;
import com.unacademyclone.activity.LearnMoreActivity;
import com.unacademyclone.activity.LoginRegisterActivity;
import com.unacademyclone.activity.PlusCourseActivity;
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.adapter.GoalFeedItemAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.AllTopicsItem;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.Educator;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.GoalFeedItem;
import com.unacademyclone.model.Story;
import com.unacademyclone.model.StoryAuthor;
import com.unacademyclone.model.TopicGroupItem;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.NetworkUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlusFragment extends Fragment {

    Context context;
    TypefaceUtility tfUtil;
    LinearLayoutManager llm;
    RequestQueue requestQueue;
    List<GoalFeedItem> goalFeedItemList;
    GoalFeedItemAdapter goalFeedItemAdapter;

    NestedScrollView nsv_items;
    LinearLayout ll_goal_type;
    ImageView iv_banner;
    TextView tv_goal_type, tv_sessions_count, tv_languages, tv_educators, tv_star, tv_learn_more, tv_subscribe ;
    RecyclerView rv_items;

    String goal_uid = "KSCGY", goal_title = "UPSC CSE";

    String educators_count="",live_hours ="", languages="";

    final int ITEMS_LIMIT = 3;
    int currentPage = 0;
    boolean isResponsePending=false;

    BroadcastReceiver brPlusFragment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context=getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plus, container, false);

        requestQueue= Volley.newRequestQueue(context);
        tfUtil=new TypefaceUtility(context);
        goalFeedItemList = new ArrayList<>();
        goalFeedItemAdapter = new GoalFeedItemAdapter(context, goalFeedItemList);

        nsv_items=rootView.findViewById(R.id.nsv_items);
        ll_goal_type=rootView.findViewById(R.id.ll_goal_type);
        iv_banner=rootView.findViewById(R.id.iv_banner);
        tv_goal_type=rootView.findViewById(R.id.tv_goal_type);
        tv_sessions_count=rootView.findViewById(R.id.tv_sessions_count);
        tv_languages=rootView.findViewById(R.id.tv_languages);
        tv_educators=rootView.findViewById(R.id.tv_educators);
        tv_star=rootView.findViewById(R.id.tv_star);
        tv_learn_more=rootView.findViewById(R.id.tv_learn_more);
        tv_subscribe=rootView.findViewById(R.id.tv_subscribe);
        rv_items=rootView.findViewById(R.id.rv_items);

        tv_goal_type.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_sessions_count.setTypeface(tfUtil.getTypefaceRegular());
        tv_languages.setTypeface(tfUtil.getTypefaceRegular());
        tv_educators.setTypeface(tfUtil.getTypefaceRegular());
        tv_star.setTypeface(tfUtil.getTypefaceRegular());
        tv_learn_more.setTypeface(tfUtil.getTypefaceRegular());
        tv_subscribe.setTypeface(tfUtil.getTypefaceRegular());

        ll_goal_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoalsActivity.class);
                context.startActivity(intent);
            }
        });

        tv_learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LearnMoreActivity.class));
            }
        });

        tv_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, LoginRegisterActivity.class));
            }
        });


        llm=new LinearLayoutManager(context);
        rv_items.setLayoutManager(llm);
        rv_items.setAdapter(goalFeedItemAdapter);
        rv_items.setNestedScrollingEnabled(true);

        nsv_items.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                        if(NetworkUtility.isAvailable(context)){
                            fetchContent();
                        }
                    }
                }
            }
        });

        Picasso.with(context).load(R.drawable.subscription_banner).into(iv_banner);

        if(NetworkUtility.isAvailable(context)){
            fetchContent();
            fetchStaticCardContent();
        }

        initBroadcastReceiver();
        return rootView;
    }

    public void fetchContent(){
        if(isResponsePending){
            /*Another request is already waiting for response, so don't send any request now.
             It may lead to collapse of last_post_time and entire funtionality*/
            return;
        }

        HashMap<String,String> params=new HashMap<String,String>();
        final int currentPageBeforeRequest = currentPage;
        final int offset = currentPage*ITEMS_LIMIT;

        HashMap<String,String> headers=new HashMap<String,String>();

        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(currentPage == 0){
                    goalFeedItemList.clear();
                }

                isResponsePending = false;
                currentPage = currentPageBeforeRequest+1;
               try {
                   JSONObject joResponse = new JSONObject(response);
                   JSONArray jaResults = joResponse.getJSONArray("results");
                   for(int i=0; i<jaResults.length(); i++){
                       JSONObject joResult = jaResults.getJSONObject(i);
                       String type = joResult.getString("type");

                       if(type.equals("upcoming")){
                           List<TopicGroupItem> topicGroupItemList = new ArrayList<>();
                           JSONArray jaData = joResult.getJSONArray("data");
                           for(int d=0; d<jaData.length(); d++){
                               JSONObject joDataItem = jaData.getJSONObject(d);
                               JSONObject joAuthor = joDataItem.getJSONObject("author");

                               String topic_group_name="";
                               JSONArray joTopicGroups = joDataItem.getJSONArray("topic_groups");
                               for(int t=0; t<joTopicGroups.length(); t++){
                                   JSONObject joTopicGroup = joTopicGroups.getJSONObject(t);
                                   topic_group_name+=joTopicGroup.getString("name")+" ";
                               }

                               TopicGroupItem topicGroupItem = new TopicGroupItem(
                                       joDataItem.getString("uid"),
                                       joDataItem.getString("name"),
                                       joAuthor.getString("first_name")+" "+joAuthor.getString("last_name"),
                                       joAuthor.getString("username"),
                                       goal_uid,
                                       joDataItem.getString("cover_photo"),
                                       joDataItem.getString("starts_at"),
                                       topic_group_name,
                                       joDataItem.getInt("item_count")
                               );
                               topicGroupItemList.add(topicGroupItem);
                           }
                           GoalFeedItem goalFeedItem = new GoalFeedItem("topic_group", "Upcoming courses");
                           goalFeedItem.setTopicGroupItemList(topicGroupItemList);
                           goalFeedItemList.add(goalFeedItem);
                       }
                       else if(type.equals("all_topic_groups")){
                           List<AllTopicsItem> allTopicsItemList = new ArrayList<>();
                           JSONArray jaData = joResult.getJSONArray("data");
                           for(int d=0; d<jaData.length(); d++){
                               JSONObject joDataItem = jaData.getJSONObject(d);

                               AllTopicsItem allTopicsItem = new AllTopicsItem(
                                       joDataItem.getString("uid"),
                                       joDataItem.getString("name"),
                                       joDataItem.getInt("count")
                               );
                               allTopicsItemList.add(allTopicsItem);
                           }
                           GoalFeedItem goalFeedItem = new GoalFeedItem(type, "Topics");
                           goalFeedItem.setAllTopicsItemList(allTopicsItemList);
                           goalFeedItemList.add(goalFeedItem);
                       }
                       else if(type.equals("educators")){
                           List<Educator> educatorList = new ArrayList<>();
                           JSONArray jaData = joResult.getJSONArray("data");
                           for(int d=0; d<jaData.length(); d++){
                               JSONObject joDataItem = jaData.getJSONObject(d);

                               Educator educator = new Educator(
                                       joDataItem.getString("uid"),
                                       joDataItem.getString("username"),
                                       joDataItem.getString("first_name"),
                                       joDataItem.getString("last_name"),
                                       joDataItem.getString("avatar"),
                                       joDataItem.getDouble("live_minutes")
                               );
                               educatorList.add(educator);
                           }
                           GoalFeedItem goalFeedItem = new GoalFeedItem(type, "Educators");
                           goalFeedItem.setEducatorList(educatorList);
                           goalFeedItemList.add(goalFeedItem);
                       }
                       else if(type.equals("topic_group")){
                           List<TopicGroupItem> topicGroupItemList = new ArrayList<>();
                           JSONArray jaData = joResult.getJSONArray("data");
                           for(int d=0; d<jaData.length(); d++){
                               JSONObject joDataItem = jaData.getJSONObject(d);
                               JSONObject joAuthor = joDataItem.getJSONObject("author");

                               String topic_group_name="";
                               JSONArray joTopicGroups = joDataItem.getJSONArray("topic_groups");
                               for(int t=0; t<joTopicGroups.length(); t++){
                                   JSONObject joTopicGroup = joTopicGroups.getJSONObject(t);
                                   topic_group_name+=joTopicGroup.getString("name")+" ";
                               }

                               TopicGroupItem topicGroupItem = new TopicGroupItem(
                                       joDataItem.getString("uid"),
                                       joDataItem.getString("name"),
                                       joAuthor.getString("first_name")+" "+joAuthor.getString("last_name"),
                                       joAuthor.getString("username"),
                                       goal_uid,
                                       joDataItem.getString("cover_photo"),
                                       joDataItem.getString("starts_at"),
                                       topic_group_name,
                                       joDataItem.getInt("item_count")
                               );
                               topicGroupItemList.add(topicGroupItem);
                           }
                           String groupTitle = joResult.getJSONObject("extra_block_info").getString("name");
                           GoalFeedItem goalFeedItem = new GoalFeedItem(type, groupTitle);
                           goalFeedItem.setTopicGroupItemList(topicGroupItemList);
                           goalFeedItemList.add(goalFeedItem);
                       }
                   }

               }
               catch (Exception e){
                   Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
               }
                goalFeedItemAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isResponsePending = false;
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        Log.d("GDBUG", APIS.FEED);
        HttpsRequest goalsRequest=new HttpsRequest(Request.Method.GET, APIS.GOAL_FEED+"?feed_type=1&goal_uid="+goal_uid+"&limit="+ITEMS_LIMIT+"&offset="+offset,resListener,errorListener,params,headers);
        requestQueue.add(goalsRequest);
        isResponsePending=true;
    }

//
    public void fetchStaticCardContent(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject joResponse = new JSONObject(response);
                    educators_count=joResponse.getString("educators_count");
                    goal_title=joResponse.getString("goal_name");
                    live_hours = joResponse.getString("live_hours");
                    languages="";
                    JSONArray jaLanguages = joResponse.getJSONArray("languages");
                    for(int i=0; i<jaLanguages.length(); i++){
                        String lang = jaLanguages.getString(i);
                        if(i==0){
                            languages=lang;
                        }
                        else{
                            lang+=", "+lang;
                        }
                    }

                    updateCard();
                }
                catch (Exception e){
                    Log.e("UA_ERROR", e.getMessage());

                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Reaching here"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest staticCardRequest=new HttpsRequest(Request.Method.GET, APIS.GOAL_STATIC_CARD+"?goal_uid="+goal_uid,resListener,errorListener,params,headers);
        requestQueue.add(staticCardRequest);
    }

    public void updateCard(){
        tv_sessions_count.setText(live_hours+" hours of live sessions every day");
        tv_languages.setText("Structured courses in "+languages);
        tv_educators.setText(educators_count+" Top educators");
    }

    public void initBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter("PlusFragment");
        brPlusFragment = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    Bundle b=intent.getExtras();

                    if(b!=null){
                        String type=b.getString("type","");
                        if(type.equals("updateGoal")){
                            goal_uid = b.getString("goal_uid", "");
                            goal_title = b.getString("goal_title", "");
                            tv_goal_type.setText(goal_title);

                            currentPage=0;
                            fetchStaticCardContent();
                            goalFeedItemList.clear();
                            goalFeedItemAdapter.notifyDataSetChanged();
                            fetchContent();
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        context.registerReceiver(brPlusFragment, intentFilter);
    }

}
