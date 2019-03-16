package com.unacademyclone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unacademyclone.*;
import com.unacademyclone.adapter.CourseAdapter;
import com.unacademyclone.adapter.UserAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.User;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.KeyboardUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    String query="";

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm_users,llm_collections;
    List<User> userList;
    List<Course> courseList;
    UserAdapter userAdapter;
    CourseAdapter courseAdapter;

    ProgressBar pb_loading;
    RecyclerView rv_users, rv_collections;
    TextView tv_title, tv_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        query = getIntent().getExtras().getString("query");

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);

        userList=new ArrayList<>();
        courseList=new ArrayList<>();
        userAdapter=new UserAdapter(this,userList);
        courseAdapter=new CourseAdapter(this,courseList, true);

        pb_loading=findViewById(R.id.pb_loading);
        rv_users=findViewById(R.id.rv_users);
        rv_collections=findViewById(R.id.rv_collections);
        tv_title=findViewById(R.id.tv_title);
        tv_results=findViewById(R.id.tv_results);
        tv_title.setText(query);
        tv_results.setVisibility(View.GONE);

        llm_users=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        llm_collections=new LinearLayoutManager(this);
        rv_users.setLayoutManager(llm_users);
        rv_collections.setLayoutManager(llm_collections);
        rv_users.setAdapter(userAdapter);
        rv_collections.setAdapter(courseAdapter);

        rv_users.setNestedScrollingEnabled(false);
        rv_collections.setNestedScrollingEnabled(false);

        fetchSearchContent();
    }

    public void fetchSearchContent(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_loading.setVisibility(View.GONE);
                tv_results.setVisibility(View.VISIBLE);
                try{
                    JSONObject joResponse = new JSONObject(response);

                    JSONArray jaUsers = joResponse.getJSONArray("users");
                    for(int i=0; i<jaUsers.length(); i++){
                        JSONObject joUser = jaUsers.getJSONObject(i);
                        JSONObject joValue = joUser.getJSONObject("value");

                        userList.add(new User(
                                joValue.getString("username"),
                                joValue.getString("avatar"),
                                joValue.getString("first_name")+" "+joValue.getString("last_name"),
                                joValue.getString("bio"),
                                joValue.getInt("followers_count"),
                                joValue.getInt("courses_count")
                        ));
                    }


                    JSONArray jaResults = joResponse.getJSONArray("results");
                    for(int i=0; i<jaResults.length(); i++){
                        JSONObject joResult = jaResults.getJSONObject(i);
                        String type = joResult.getString("type");
                        if(type.equals("collection")){
                            JSONObject joValue = joResult.getJSONObject("value");
                            JSONObject joAuthor = joValue.getJSONObject("author");

                            String concept_topology_title="";
                            double avg_rating=0;
                            if(joValue.has("avg_rating")){
                                avg_rating = joValue.getDouble("avg_rating");
                            }

                            courseList.add(new Course(
                                    joValue.getString("uid"),
                                    joValue.getString("name"),
                                    joValue.getString("language_display"),
                                    joValue.getString("thumbnail"),
                                    concept_topology_title,
                                    (joAuthor.getString("first_name")+" "+joAuthor.getString("last_name")),
                                    joAuthor.getString("avatar"),
                                    joValue.getInt("item_count"),
                                    joValue.getInt("total_ratings"),
                                    avg_rating
                            ));
                        }
                    }
                }
                catch (Exception e){
                    Log.e("Err", e.toString());
                    Toast.makeText(SearchResultsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
                userAdapter.notifyDataSetChanged();
                courseAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchResultsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        String queryString="?limit=15&q="+query+"&filters_applied={%22context%22:{%22name%22:%22All%20of%20Unacademy%22,%22context%22:%22generic%22},%22order%22:{%22id%22:%22relevance%22,%22name%22:%22Relevance%22},%22language%22:{%22id%22:%22any%22,%22name%22:%22Any%20Language%22}}";
        HttpsRequest searchRequest=new HttpsRequest(Request.Method.GET, APIS.SEARCH+queryString,resListener,errorListener,params,headers);
        requestQueue.add(searchRequest);
    }

    public void back_onClick(View v){
        super.onBackPressed();
    }
}
