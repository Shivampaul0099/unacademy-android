package com.unacademyclone.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.adapter.ProgrammeItemAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.ProgrammeItem;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.DurationUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlusCourseActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;

    LinearLayoutManager llm;
    List<ProgrammeItem> programmeItemList;
    ProgrammeItemAdapter programmeItemAdapter;

    String programme_id="";
    String video_url="", language="", course_title="", author_name="", author_id="", start_month="", start_day="", course_start_end="",course_details="";
    String author_avatar="";

    WebView wv_player;
    TextView tv_language, tv_course_title, tv_author_name, tv_starts_title;
    TextView tv_start_month, tv_start_day, tv_course_start_end, tv_course_details;
    TextView tv_get_subscription;
    CircularImageView civ_author_avatar;
    RecyclerView rv_items;
    ImageView iv_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_course);

        programme_id = getIntent().getExtras().getString("programme_id");

        programmeItemList = new ArrayList<>();
        programmeItemAdapter = new ProgrammeItemAdapter(this, programmeItemList);

        wv_player=findViewById(R.id.wv_player);
        tv_language=findViewById(R.id.tv_language);
        tv_course_title=findViewById(R.id.tv_course_title);
        tv_author_name=findViewById(R.id.tv_author_name);
        tv_starts_title=findViewById(R.id.tv_starts_title);
        tv_start_month=findViewById(R.id.tv_start_month);
        tv_start_day=findViewById(R.id.tv_start_day);
        tv_course_start_end=findViewById(R.id.tv_course_start_end);
        tv_course_details=findViewById(R.id.tv_course_details);
        tv_get_subscription=findViewById(R.id.tv_get_subscription);
        civ_author_avatar=findViewById(R.id.civ_author_avatar);
        rv_items=findViewById(R.id.rv_items);
        iv_verified=findViewById(R.id.iv_verified);

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);

        tv_language.setTypeface(tfUtil.getTypefaceRegular());
        tv_course_title.setTypeface(tfUtil.getTypefaceSemiBold());
        tv_author_name.setTypeface(tfUtil.getTypefaceRegular());
        tv_starts_title.setTypeface(tfUtil.getTypefaceRegular());
        tv_start_month.setTypeface(tfUtil.getTypefaceRegular());
        tv_start_day.setTypeface(tfUtil.getTypefaceRegular());
        tv_course_start_end.setTypeface(tfUtil.getTypefaceRegular());
        tv_course_details.setTypeface(tfUtil.getTypefaceRegular());
        tv_get_subscription.setTypeface(tfUtil.getTypefaceRegular());

        View.OnClickListener profileOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusCourseActivity.this, UserProfileActivity.class);
                intent.putExtra("user_name", author_id);
                startActivity(intent);
            }
        };

        tv_get_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlusCourseActivity.this, LoginRegisterActivity.class));
            }
        });

        civ_author_avatar.setOnClickListener(profileOnClickListener);
        tv_author_name.setOnClickListener(profileOnClickListener);

        wv_player.getSettings().setJavaScriptEnabled(true);
        llm = new LinearLayoutManager(this);
        rv_items.setLayoutManager(llm);
        rv_items.setAdapter(programmeItemAdapter);
        rv_items.setNestedScrollingEnabled(false);

        updateCourseDetails();
        fetchContent();
        fetchProgrammeItems();
    }

    public void fetchContent(){
        if(programme_id==null || programme_id.equals("")){
            return;
        }

        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject joResponse = new JSONObject(response);
                    video_url = joResponse.getString("intro_video");
                    course_title = joResponse.getString("name");
                    language = joResponse.getString("language_display");

                    JSONObject joAuthor = joResponse.getJSONObject("author");
                    author_name = joAuthor.getString("first_name")+" "+joAuthor.getString("last_name");
                    author_avatar = joAuthor.getString("avatar");
                    author_id = joAuthor.getString("username");

                    String starts_at = DurationUtility.getMonthDayFromAZulu(joResponse.getString("starts_at"), false);
                    String ends_at = DurationUtility.getMonthDayFromAZulu(joResponse.getString("ends_at"), false);

                    if(!starts_at.equals("")){
                        String[] starts_at_splitted = starts_at.split(" ");
                        if(starts_at_splitted.length == 2){
                            start_month = starts_at_splitted[0];
                            start_day = starts_at_splitted[1];
                        }
                    }

                    course_start_end = starts_at+" - "+ends_at;
                    course_details = joResponse.getInt("sessions_count")+" lessons, 4 quizzes";
                    updateCourseDetails();
                }
                catch (Exception e){
                    Toast.makeText(PlusCourseActivity.this, "Check this: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlusCourseActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest programmeDetailsRequest=new HttpsRequest(Request.Method.GET, APIS.PROGRAMME_DETAILS+programme_id+"/details/",resListener,errorListener,params,headers);
        requestQueue.add(programmeDetailsRequest);
    }

    public void fetchProgrammeItems(){
        if(programme_id==null || programme_id.equals("")){
            return;
        }

        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject joResponse = new JSONObject(response);
                    JSONArray jaResults = joResponse.getJSONArray("results");
                    for(int i=0; i<jaResults.length(); i++){
                        JSONObject joResult = jaResults.getJSONObject(i);
                        JSONObject joProperties = joResult.getJSONObject("properties");

                        String created_at = "";
                        if(joProperties.has("created_at")){
                            created_at = joProperties.getString("created_at");
                        }
                        else if(joProperties.has("start_time")){
                            created_at = joProperties.getString("start_time").replace("Z", ".000Z");
                        }


                        String name = "";
                        if(joProperties.has("name")){
                            name = joProperties.getString("name");
                        }
                        else if(joProperties.has("title")){
                            name = joProperties.getString("title");
                        }

                        programmeItemList.add(new ProgrammeItem(
                                joResult.getInt("rank"),
                                created_at,
                                name
                        ));
                    }
                }
                catch (Exception e){
                    Log.e("ERROR", e.toString());

//                    Toast.makeText(PlusCourseActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                programmeItemAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlusCourseActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest programmeDetailsRequest=new HttpsRequest(Request.Method.GET, APIS.PROGRAMME_ITEMS+programme_id+"/items/?limit=150",resListener,errorListener,params,headers);
        requestQueue.add(programmeDetailsRequest);
    }

    public void updateCourseDetails(){
        if(!video_url.equals("") && video_url.contains("v=")){
            String[] splitted = video_url.split("v=");
            String embed_url="https://www.youtube.com/embed/"+splitted[1];
            wv_player.loadUrl(embed_url);
        }
        tv_language.setText(language.toUpperCase());
        tv_course_title.setText(course_title);
        tv_author_name.setText(author_name);
        tv_start_month.setText(start_month);
        tv_start_day.setText(start_day);
        tv_course_start_end.setText(course_start_end);
        tv_course_details.setText(course_details);

        if(!author_avatar.equals("")){
            Picasso.with(this).load(author_avatar).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(civ_author_avatar);
        }
    }
}
