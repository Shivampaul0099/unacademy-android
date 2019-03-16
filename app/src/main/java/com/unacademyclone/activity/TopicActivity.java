package com.unacademyclone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.unacademyclone.*;
import com.unacademyclone.adapter.TopicGroupItemAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.TopicGroupItem;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DigitsUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicActivity extends AppCompatActivity {

    String title ="", topic_group_id="";

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm_ongoing, llm_upcoming;
    List<TopicGroupItem> onGoingList, upcomingList;
    TopicGroupItemAdapter onGoingAdapter, upcomingAdapter;

    NestedScrollView nsv_items;
    TextView tv_title, tv_ongoing_courses, tv_upcoming;
    RecyclerView rv_ongoing_courses, rv_upcoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        title=getIntent().getExtras().getString("title");
        topic_group_id=getIntent().getExtras().getString("topic_group_id");

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);
        onGoingList=new ArrayList<>();
        upcomingList=new ArrayList<>();
        onGoingAdapter=new TopicGroupItemAdapter(this,onGoingList, true);
        upcomingAdapter=new TopicGroupItemAdapter(this,upcomingList, false);

        nsv_items = findViewById(R.id.nsv_items);
        tv_title = findViewById(R.id.tv_title);
        tv_ongoing_courses = findViewById(R.id.tv_ongoing_courses);
        tv_upcoming = findViewById(R.id.tv_upcoming);
        rv_ongoing_courses = findViewById(R.id.rv_ongoing_courses);
        rv_upcoming = findViewById(R.id.rv_upcoming);

        tv_title.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_ongoing_courses.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_upcoming.setTypeface(tfUtil.getTypefaceSemiBold());

        tv_title.setText(title);

        llm_ongoing=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        llm_upcoming=new LinearLayoutManager(this);
        rv_ongoing_courses.setLayoutManager(llm_ongoing);
        rv_upcoming.setLayoutManager(llm_upcoming);
        rv_ongoing_courses.setAdapter(onGoingAdapter);
        rv_upcoming.setAdapter(upcomingAdapter);
        rv_ongoing_courses.setNestedScrollingEnabled(false);
        rv_upcoming.setNestedScrollingEnabled(false);
        fetchOngoingCourses();
        fetchUpcomingCourses();
    }

    public void fetchOngoingCourses(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONObject joResponse = new JSONObject(response);
                    JSONArray jaData = joResponse.getJSONArray("results");
                    if(jaData.length()>0){
                        tv_ongoing_courses.setVisibility(View.VISIBLE);
                        rv_ongoing_courses.setVisibility(View.VISIBLE);
                    }

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
                                "",
                                joDataItem.getString("cover_photo"),
                                joDataItem.getString("starts_at"),
                                topic_group_name,
                                joDataItem.getInt("item_count")
                        );
                        onGoingList.add(topicGroupItem);
                    }
                }
                catch (Exception e){
                    Toast.makeText(TopicActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                onGoingAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TopicActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest liveStatsRequest=new HttpsRequest(Request.Method.GET, APIS.TOPIC_ONGOING+"?limit=30&topic_group_uid="+topic_group_id,resListener,errorListener,params,headers);
        requestQueue.add(liveStatsRequest);
    }

    public void fetchUpcomingCourses(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                    JSONObject joResponse = new JSONObject(response);
                    JSONArray jaData = joResponse.getJSONArray("results");
                    if(jaData.length()>0){
                        tv_upcoming.setVisibility(View.VISIBLE);
                        rv_upcoming.setVisibility(View.VISIBLE);
                    }

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
                                "",
                                joDataItem.getString("cover_photo"),
                                joDataItem.getString("starts_at"),
                                topic_group_name,
                                joDataItem.getInt("item_count")
                        );
                        upcomingList.add(topicGroupItem);
                    }
                }
                catch (Exception e){
                    Toast.makeText(TopicActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                upcomingAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TopicActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest liveStatsRequest=new HttpsRequest(Request.Method.GET, APIS.TOPIC_UPCOMING+"?limit=30&topic_group_uid="+topic_group_id,resListener,errorListener,params,headers);
        requestQueue.add(liveStatsRequest);
    }

    public void back_onClick(View v){
        super.onBackPressed();
    }
}
