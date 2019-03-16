package com.unacademyclone.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unacademyclone.*;
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.adapter.QuestionAnswerAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.QuestionAnswer;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LearnMoreActivity extends AppCompatActivity {


    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm;
    List<QuestionAnswer> questionAnswerList;
    QuestionAnswerAdapter questionAnswerAdapter;

    TextView tv_title;
    RecyclerView rv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);
        questionAnswerList=new ArrayList<>();
        questionAnswerAdapter=new QuestionAnswerAdapter(this,questionAnswerList);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setTypeface(tfUtil.getTypefaceSemiBold());
        rv_items=findViewById(R.id.rv_items);

        llm=new LinearLayoutManager(this);
        rv_items.setLayoutManager(llm);
        rv_items.setAdapter(questionAnswerAdapter);
        rv_items.setNestedScrollingEnabled(false);
        fetchContent();
    }

    public void fetchContent(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                questionAnswerList.clear();
                try{
                    JSONArray jaResults = new JSONArray(response);
                    for(int k=0; k<jaResults.length(); k++){
                        JSONObject joResult = jaResults.getJSONObject(k);
                        String question = joResult.getString("question");
                        String answer = joResult.getString("answer");
                        questionAnswerList.add(new QuestionAnswer(question, answer));
                    }

                }
                catch (Exception e){
                    Toast.makeText(LearnMoreActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
                questionAnswerAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LearnMoreActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest learnMoreRequest=new HttpsRequest(Request.Method.GET, APIS.LEARN_MORE,resListener,errorListener,params,headers);
        requestQueue.add(learnMoreRequest);
    }

    public void back_onClick(View v){
        super.onBackPressed();
    }
}
