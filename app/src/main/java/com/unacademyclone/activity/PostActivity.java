package com.unacademyclone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.unacademyclone.*;
import com.unacademyclone.adapter.CollectionItemAdapter;
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.CollectionItem;
import com.unacademyclone.model.Goal;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm;
    List<CollectionItem> collectionItemList;
    CollectionItemAdapter collectionItemAdapter;

    String post_id="";
    String video_url="", language="", collection_name="",title="";
    String author_name="", author_avatar="", user_name="", followers_count="", collection_id="";
    int recommend_count=0, comments_count=0;

    WebView wv_player;
    TextView tv_likes, tv_comments,tv_share, tv_language, tv_collection_name;
    TextView tv_title, tv_user_name, tv_followers, tv_follow;
    TextView tv_average_star, tv_star, tv_ratings, tv_write_review;
    CircularImageView civ_user;
    LinearLayout ll_actions;
    View v_seperator;
    RecyclerView rv_collection_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            post_id = b.getString("post_id", "");
            collection_id = b.getString("collection_id", "");

        }

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);
        collectionItemList=new ArrayList<>();
        collectionItemAdapter=new CollectionItemAdapter(this,collectionItemList, post_id);

        wv_player = findViewById(R.id.wv_player);
        tv_likes = findViewById(R.id.tv_likes);
        tv_comments = findViewById(R.id.tv_comments);
        tv_share = findViewById(R.id.tv_share);
        tv_language = findViewById(R.id.tv_language);
        tv_collection_name = findViewById(R.id.tv_collection_name);
        tv_title = findViewById(R.id.tv_title);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_followers = findViewById(R.id.tv_followers);
        tv_follow = findViewById(R.id.tv_follow);
        tv_average_star = findViewById(R.id.tv_average_star);
        tv_star = findViewById(R.id.tv_star);
        tv_ratings = findViewById(R.id.tv_ratings);
        tv_write_review = findViewById(R.id.tv_write_review);
        civ_user = findViewById(R.id.civ_user);
        ll_actions = findViewById(R.id.ll_actions);
        v_seperator = findViewById(R.id.v_seperator);
        rv_collection_items = findViewById(R.id.rv_collection_items);

        tv_likes.setTypeface(tfUtil.getTypefaceRegular());
        tv_comments.setTypeface(tfUtil.getTypefaceRegular());
        tv_share.setTypeface(tfUtil.getTypefaceRegular());
        tv_language.setTypeface(tfUtil.getTypefaceRegular());
        tv_collection_name.setTypeface(tfUtil.getTypefaceRegular());
        tv_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_user_name.setTypeface(tfUtil.getTypefaceRegular());
        tv_followers.setTypeface(tfUtil.getTypefaceRegular());
        tv_follow.setTypeface(tfUtil.getTypefaceRegular());
        tv_average_star.setTypeface(tfUtil.getTypefaceRegular());
        tv_star.setTypeface(tfUtil.getTypefaceRegular());
        tv_ratings.setTypeface(tfUtil.getTypefaceRegular());
        tv_write_review.setTypeface(tfUtil.getTypefaceRegular());

        llm=new LinearLayoutManager(this);
        rv_collection_items.setLayoutManager(llm);
        rv_collection_items.setAdapter(collectionItemAdapter);
//        rv_collection_items.setNestedScrollingEnabled(false);

        tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, LoginRegisterActivity.class));
            }
        });

        wv_player.getSettings().setJavaScriptEnabled(true);
        if(!collection_id.equals("")){
            fetchPostIdByCollectionId();
        }

        ll_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, LoginRegisterActivity.class));
            }
        });

        View.OnClickListener profileOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, UserProfileActivity.class);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
            }
        };

        civ_user.setOnClickListener(profileOnClickListener);
        tv_user_name.setOnClickListener(profileOnClickListener);
        tv_followers.setOnClickListener(profileOnClickListener);

        fetchPostData();
        updateUI();
    }

    public void fetchPostData(){
        if(post_id == null || post_id.equals("")){
            return;
        }

        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject joResponse = new JSONObject(response);
                    JSONObject joAuthor = joResponse.getJSONObject("author");
                    JSONObject joACollection = joResponse.getJSONObject("collection");

                    video_url=joResponse.getJSONObject("video").getString("url");
                    language=joResponse.getString("language");
                    collection_name=joACollection.getString("name");
                    title=joResponse.getString("title");

                    author_name=joAuthor.getString("first_name")+" "+joAuthor.getString("last_name");
                    author_avatar=joAuthor.getString("avatar");
                    user_name=joAuthor.getString("username");
                    followers_count=joAuthor.getString("followers_count");
                    collection_id=joACollection.getString("uid");

                    recommend_count=joResponse.getInt("recommend_count");
                    comments_count=joResponse.getInt("comments_count");
                    updateUI();
                    fetchCollectionItems();
                }
                catch (Exception e){
                    Toast.makeText(PostActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PostActivity.this, "Here: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest postRequest=new HttpsRequest(Request.Method.GET, APIS.POST+post_id+"/",resListener,errorListener,params,headers);
        requestQueue.add(postRequest);
    }

    public void fetchPostIdByCollectionId(){
        if(collection_id == null || collection_id.equals("")){
            return;
        }

        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject joResponse = new JSONObject(response);
                    JSONObject joProgress = joResponse.getJSONObject("progress");
                    String next_lesson_uid = joProgress.getString("next_lesson_uid");
                    post_id = next_lesson_uid;
                    updatePostID(post_id);
                }
                catch (Exception e){
                    Toast.makeText(PostActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(PostActivity.this, "Here: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest postRequest=new HttpsRequest(Request.Method.GET, "https://unacademy.com/api/v1/collection/"+collection_id+"/",resListener,errorListener,params,headers);
        requestQueue.add(postRequest);
    }

    public void updatePostID(String newPostId){
        post_id = newPostId;
        fetchPostData();
    }

    public void fetchCollectionItems(){
        if(collection_id == null || collection_id.equals("")){
            return;
        }

        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                collectionItemList.clear();
                try{
                    JSONObject joResponse = new JSONObject(response);
                    JSONArray jaResults = joResponse.getJSONArray("results");
                    for(int i=0; i<jaResults.length(); i++){
                        JSONObject joResult = jaResults.getJSONObject(i);
                        JSONObject joValue = joResult.getJSONObject("value");

                        collectionItemList.add(new CollectionItem(
                                joValue.getString("uid"),
                                joValue.getString("title"),
                                joValue.getJSONObject("video").getString("url"),
                                joResult.getInt("rank"),
                                joValue.getJSONObject("video").getDouble("duration")
                        ));
                    }
                }
                catch (Exception e){
                    Toast.makeText(PostActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                collectionItemAdapter.notifyDataSetChanged();
                if(collectionItemList.size()>0){
                    v_seperator.setVisibility(View.VISIBLE);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostActivity.this, "Reaching here"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest postRequest=new HttpsRequest(Request.Method.GET, APIS.COLLECTION+collection_id
                +"/items/?limit=75&offset=0",resListener,errorListener,params,headers);
        requestQueue.add(postRequest);
    }

    public void updateUI(){
        wv_player.loadUrl(video_url);
        tv_likes.setText(recommend_count+" likes");
        tv_comments.setText(comments_count+" comments");
        if(language!=null){
            tv_language.setText(language.toUpperCase());
        }

        tv_collection_name.setText(collection_name);
        tv_title.setText(title);
        tv_user_name.setText(author_name);;
        tv_followers.setText(followers_count+" followers");
        if(author_avatar!=null && author_avatar.trim().length()>0){
            Picasso.with(this).load(author_avatar).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(civ_user);
        }
    }

    public void back_onClick(View v){
        super.onBackPressed();
    }
}
