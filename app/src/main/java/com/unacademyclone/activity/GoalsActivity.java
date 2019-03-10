package com.unacademyclone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unacademyclone.*;
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Goal;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm;
    List<Goal> goalList;
    GoalAdapter goalAdapter;

    TextView tv_title;
    RecyclerView rv_goals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);


        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);
        goalList=new ArrayList<>();
        goalAdapter=new GoalAdapter(this,goalList);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setTypeface(tfUtil.getTypefaceSemiBold());
        rv_goals=findViewById(R.id.rv_goals);

        llm=new LinearLayoutManager(this);
        rv_goals.setLayoutManager(llm);
        rv_goals.setAdapter(goalAdapter);
        rv_goals.setNestedScrollingEnabled(false);
        //rv_posts.addItemDecoration(new SimpleDividerItemDecoration(this));
        fetchContent();
    }

    public void fetchContent(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                goalList.clear();
                Log.d("GOPALAKRISHNAN", response+"CHECK_THIS");
                try{
                    JSONObject joResponse = new JSONObject(response);
                    Log.i("json", joResponse.toString());
                    JSONArray jaGoals = joResponse.getJSONArray("goals");
                    for(int i=0; i<jaGoals.length(); i++){
                        JSONObject joGoal = jaGoals.getJSONObject(i);
                        String uid = joGoal.getString("uid");
                        String name = joGoal.getString("name");
                        String icon_url = joGoal.getString("icon_url");

                        goalList.add(new Goal(uid, name, icon_url));
                    }

                }
                catch (Exception e){
                    Log.e("UA_ERROR", e.getMessage());

                }
                goalAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GoalsActivity.this, "Reaching here"+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UA_ERROR", error.getMessage());

            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest goalsRequest=new HttpsRequest(Request.Method.GET, APIS.GOALS,resListener,errorListener,params,headers);
        requestQueue.add(goalsRequest);
    }

    public void cancel(View v){

    }
}
