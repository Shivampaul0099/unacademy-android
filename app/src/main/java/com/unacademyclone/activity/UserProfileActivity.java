package com.unacademyclone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unacademyclone.*;
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.adapter.HatAdapter;
import com.unacademyclone.adapter.StoryAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.Hat;
import com.unacademyclone.model.Story;
import com.unacademyclone.model.StoryAuthor;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DigitsUtility;
import com.unacademyclone.utility.NetworkUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    final int ITEMS_LIMIT = 4;
    int currentPage = 0;

    boolean isResponsePending=false;

    String user_name="", bio="", profile_since= "", first_name="", last_name="", channel_uid="";
    int followers_count, follows_count;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm_hats, llm_stories;
    List<Hat> hatList;
    List<Story> storyList;
    HatAdapter hatAdapter;
    StoryAdapter storyAdapter;

    FrameLayout fl_progress;
    NestedScrollView nsv_items;
    TextView tv_name, tv_educator_since, tv_bio, tv_followers_count, tv_followers_title;
    TextView tv_following_count, tv_following_title, tv_follow, tv_feed_title;
    RecyclerView rv_hats, rv_stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user_name = getIntent().getExtras().getString("user_name");

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);
        hatList=new ArrayList<>();
        storyList=new ArrayList<>();
        hatAdapter=new HatAdapter(this,hatList);
        storyAdapter=new StoryAdapter(this,storyList);

        fl_progress = findViewById(R.id.fl_progress);
        nsv_items = findViewById(R.id.nsv_items);
        tv_name = findViewById(R.id.tv_name);
        tv_educator_since = findViewById(R.id.tv_educator_since);
        tv_bio = findViewById(R.id.tv_bio);
        tv_followers_count = findViewById(R.id.tv_followers_count);
        tv_followers_title = findViewById(R.id.tv_followers_title);
        tv_following_count = findViewById(R.id.tv_following_count);
        tv_following_title = findViewById(R.id.tv_following_title);
        tv_follow = findViewById(R.id.tv_follow);
        tv_feed_title = findViewById(R.id.tv_feed_title);
        rv_hats = findViewById(R.id.rv_hats);
        rv_stories = findViewById(R.id.rv_stories);

        tv_name.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_educator_since.setTypeface(tfUtil.getTypefaceRegular());
        tv_bio.setTypeface(tfUtil.getTypefaceRegular());
        tv_followers_count.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_followers_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_following_count.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_following_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_follow.setTypeface(tfUtil.getTypefaceRegular());
        tv_feed_title.setTypeface(tfUtil.getTypefaceSemiBold());

        tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, LoginRegisterActivity.class));
            }
        });

        llm_hats=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        llm_stories=new LinearLayoutManager(this);
        rv_hats.setLayoutManager(llm_hats);
        rv_stories.setLayoutManager(llm_stories);
        rv_hats.setAdapter(hatAdapter);
        rv_stories.setAdapter(storyAdapter);
        rv_hats.setNestedScrollingEnabled(false);
        rv_stories.setNestedScrollingEnabled(false);

        nsv_items.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                        if(NetworkUtility.isAvailable(UserProfileActivity.this)){
                            fetchStoriesContent();
                        }
                    }
                }
            }
        });

        fetchProfileContent();
        fetchHatsContent();
    }

    public void fetchHatsContent(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jaResults = new JSONArray(response);
                    for(int i=0; i<jaResults.length(); i++){
                        JSONObject joResult = jaResults.getJSONObject(i);
                        int count = joResult.getInt("count");
                        String hat_icon = joResult.getJSONObject("hat").getString("hat_icon");
                        hatList.add(new Hat(hat_icon, count, true));
                    }
                }
                catch (Exception e){
                    Toast.makeText(UserProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
                hatAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest hatsRequest=new HttpsRequest(Request.Method.GET, APIS.HAT_ACHIEVED +"?username="+user_name,resListener,errorListener,params,headers);
        requestQueue.add(hatsRequest);
    }

    public void fetchProfileContent(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject joResponse = new JSONObject(response);
                    String avatar = joResponse.getString("avatar");
                    first_name=joResponse.getString("first_name");
                    last_name=joResponse.getString("last_name");
                    bio=joResponse.getString("bio");
                    profile_since=joResponse.getString("profile_since");
                    followers_count = joResponse.getInt("followers_count");
                    follows_count = joResponse.getInt("follows_count");

                    JSONArray jaChannels =joResponse.getJSONArray("channels");
                    for(int m=0; m<jaChannels.length(); m++){
                        if(m==0){
                            channel_uid=jaChannels.getJSONObject(m).getString("uid");
                        }
                    }
                    updateProfile();

                    hatList.add(0, new Hat(avatar,0, false));
                }
                catch (Exception e){
                    Toast.makeText(UserProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
                fetchStoriesContent();
                hatAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(UserProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        Log.e("APIS.USER+user_name", APIS.USER+user_name);
        HttpsRequest userProfileRequest=new HttpsRequest(Request.Method.GET, APIS.USER+user_name+"/",resListener,errorListener,params,headers);
        requestQueue.add(userProfileRequest);
    }


    public void fetchStoriesContent(){
        if(isResponsePending){
            /*Another request is already waiting for response, so don't send any request now.
             It may lead to collapse of last_post_time and entire funtionality*/
            return;
        }

        HashMap<String,String> params=new HashMap<String,String>();
        final int currentPageBeforeRequest = currentPage;
        final int offset = currentPage*ITEMS_LIMIT;
//        Toast.makeText(context, "Networkcall made", Toast.LENGTH_SHORT).show();

        HashMap<String,String> headers=new HashMap<String,String>();
        final int previousSize = storyList.size();


        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isResponsePending = false;
                fl_progress.setVisibility(View.GONE);
//                if(offset == 0){
//                    storyList.clear();
//                }
                currentPage = currentPageBeforeRequest+1;
                try{
                    JSONObject joResponse = new JSONObject(response);
                    JSONArray jaResults = joResponse.getJSONArray("results");
                    for(int i=0; i<jaResults.length(); i++){
                        JSONObject joData = jaResults.getJSONObject(i);

                        Story story = new Story();
                        story.setObject_meta(joData.getString("object_meta"));
                        story.setMessage(joData.getString("message"));
                        story.setObject_type(joData.getInt("object_type"));
                        story.setUid(joData.getString("uid"));
                        story.setCreated_at(joData.getString("created_at"));
                        story.setVerb_text(joData.getString("verb_text"));
                        story.setId(joData.has("id")? joData.getInt("id"): 0);
                        story.setReactions_count(joData.getInt("reactions_count"));
                        story.setComments_count(joData.getInt("comments_count"));
                        story.setShare_count(joData.getInt("share_count"));
                        story.setIs_liked(joData.getBoolean("is_liked"));

                        JSONObject joAuthor = joData.getJSONObject("author");
                        StoryAuthor storyAuthor = new StoryAuthor(
                                joAuthor.getString("username"),
                                joAuthor.getString("first_name"),
                                joAuthor.getString("last_name"),
                                joAuthor.getString("bio"),
                                joAuthor.getString("uid"),
                                joAuthor.getString("avatar"),
                                joAuthor.getString("followers_count"),
                                joAuthor.getString("follows_count"),
                                joAuthor.getString("profile_since"),
                                joAuthor.getBoolean("is_verified_educator")
                        );
                        story.setStoryAuthor(storyAuthor);


                        int object_type = joData.getInt("object_type");
                        JSONObject joObject = joData.getJSONObject("object");

                        if(object_type == 4){
//                            if(joObject.has("concept_topology")){
//                                story.setCollection_name(joObject.getJSONObject("concept_topology").getString("name"));
//                            }
                            story.setMessage("");
                            story.setTotal_ratings(joObject.getInt("total_ratings"));
                            try {
                                story.setAverage_rating_star(joObject.getDouble("avg_rating"));
                            }
                            catch (Exception e){
                            }
                            story.setLanguage(joObject.getString("language_display"));
                            story.setTitle(joObject.getString("name"));

                            JSONObject joCourseAuthor = joObject.getJSONObject("author");
                            story.setCourse_author_name(joCourseAuthor.getString("first_name")+" "+joCourseAuthor.getString("last_name"));
                            story.setCourse_author_avatar(joCourseAuthor.getString("avatar"));
                            story.setVideo_thumbnail(joObject.getString("thumbnail"));
                        }
                        else if(object_type == 5){
                            story.setCollection_name(joObject.getString("collection_name"));
                            story.setTitle(joObject.getString("title"));
                            JSONObject joVideo = joObject.getJSONObject("video");
                            try{
                                story.setVideo_duration(joVideo.getDouble("duration"));
                            }
                            catch (Exception e){
                            }

                            story.setTitle(joObject.getString("title"));
                            story.setPlay_count(joVideo.getInt("play_count"));
                            story.setLanguage(joObject.getString("language"));
                            story.setVideo_thumbnail(joObject.getJSONObject("video").getString("thumbnail"));
                        }
                        else if(object_type == 8){
                            story.setCollection_name(joObject.getString("name"));
                            story.setTitle(joObject.getString("name"));
                            JSONArray jaPeekCourses = joObject.getJSONArray("peek_courses");
                            List<Course> courseList = new ArrayList<>();
                            for(int p =0; p<jaPeekCourses.length(); p++){
                                JSONObject joPeekCourse = jaPeekCourses.getJSONObject(p);
                                JSONObject joCourse = joPeekCourse.getJSONObject("course");
                                JSONObject joCourseAuthor = joCourse.getJSONObject("author");
                                String topoloy_title=joCourse.getJSONObject("concept_topology").getString("title");

                                courseList.add(new Course(
                                        joCourse.getString("uid"),
                                        joCourse.getString("name"),
                                        joCourse.getString("language_display"),
                                        joCourse.getString("thumbnail"),
                                        topoloy_title,
                                        joCourseAuthor.getString("first_name")+" "+joCourseAuthor.getString("last_name"),
                                        joCourseAuthor.getString("avatar"),
                                        joCourse.getInt("item_count"),
                                        joCourse.getInt("total_ratings"),
                                        joCourse.getDouble("avg_rating")
                                ));
                            }
                            story.setCourseList(courseList);
                        }
                        if(object_type ==4 || object_type==5 || object_type==8){
                            storyList.add(story);
                        }
                    }

                }
                catch (Exception e){
                    Toast.makeText(UserProfileActivity.this, "Catch: "+e.toString(), Toast.LENGTH_SHORT).show();

                }
//                storyAdapter.notifyDataSetChanged();
                storyAdapter.notifyItemRangeInserted(previousSize,storyList.size());
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isResponsePending = false;
                fl_progress.setVisibility(View.GONE);
            }
        };

        HttpsRequest storiesRequest=new HttpsRequest(Request.Method.GET, APIS.CHANNEL_STORIES+channel_uid+"/stories/?limit=10&offset="+offset+"&version=1",resListener,errorListener,params,headers);
        requestQueue.add(storiesRequest);
        isResponsePending=true;
        fl_progress.setVisibility(View.VISIBLE);
    }


    public void updateProfile(){
        tv_name.setText(first_name+" "+last_name);
        tv_educator_since.setText("Educator since June 2018");
        tv_bio.setText(bio);
        tv_followers_count.setText(DigitsUtility.getCondensedNumber(followers_count));
        tv_following_count.setText(DigitsUtility.getCondensedNumber(follows_count));
        tv_feed_title.setText(first_name+"\'s Feed");
    }

    public void back_onClick(View v){
        super.onBackPressed();
    }
}
