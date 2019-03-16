package com.unacademyclone.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.unacademyclone.*;
import com.unacademyclone.adapter.CourseAdapter;
import com.unacademyclone.adapter.SearchAutocompleteItemAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.SearchAutocompleteItem;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.KeyboardUtility;
import com.unacademyclone.utility.NetworkUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchAutocompleteActivity extends AppCompatActivity {

    String currentQuery="";
    boolean explicitQuerySet = false;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm_items;
    List<SearchAutocompleteItem> searchAutocompleteItemList;
    SearchAutocompleteItemAdapter searchAutocompleteItemAdapter;

    SearchView sv_query;
    RecyclerView rv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_autocomplete);

        sp=getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(this);
        requestQueue= Volley.newRequestQueue(this);
        searchAutocompleteItemList=new ArrayList<>();
        searchAutocompleteItemAdapter=new SearchAutocompleteItemAdapter(this,searchAutocompleteItemList);

        rv_items=findViewById(R.id.rv_items);

        sv_query=findViewById(R.id.sv_query);
        TextView tv_search = sv_query.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        tv_search.setTypeface(tfUtil.getTypefaceRegular());

        llm_items=new LinearLayoutManager(this);
        rv_items.setLayoutManager(llm_items);
        rv_items.setAdapter(searchAutocompleteItemAdapter);

        rv_items.setNestedScrollingEnabled(false);
        fetchPopulars();

        sv_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query=query.trim();
                sv_query.clearFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(currentQuery)){
                    return false;
                }

                if(explicitQuerySet){
                    explicitQuerySet=false;
                    return false;
                }

                if(NetworkUtility.isAvailable(SearchAutocompleteActivity.this)){
                    if(newText.trim().length()==0){
                        fetchPopulars();
                    }
                    else{
                        fetchAutoCompleteContent(newText.trim());
                    }
                }
                currentQuery=newText;
                return false;
            }
        });
    }


    public void fetchPopulars(){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchAutocompleteItemList.clear();
                try{
                    JSONArray jaResults = new JSONArray(response);
                    for(int k=0; k<jaResults.length(); k++){
                        JSONObject joResult = jaResults.getJSONObject(k);
                        searchAutocompleteItemList.add(new SearchAutocompleteItem(
                                "popular", joResult.getString("query"), null, null, 0, 0));
                    }

                }
                catch (Exception e){
                    Toast.makeText(SearchAutocompleteActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
                searchAutocompleteItemAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchAutocompleteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest popularsRequest=new HttpsRequest(Request.Method.GET, APIS.POPULARS,resListener,errorListener,params,headers);
        requestQueue.add(popularsRequest);
    }

    public void fetchAutoCompleteContent(String query){
        Response.Listener<String> resListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchAutocompleteItemList.clear();
                try{
                    JSONArray jaResults = new JSONArray(response);
                    for(int i=0; i<jaResults.length(); i++){
                        JSONObject joResult = jaResults.getJSONObject(i);
                        switch (joResult.getString("type")){
                            case "popular":{
                                searchAutocompleteItemList.add(new SearchAutocompleteItem(
                                        "popular", joResult.getString("query"), null, null, 0, 0));
                                break;
                            }
                            case "search":{
                                searchAutocompleteItemList.add(new SearchAutocompleteItem(
                                        "search", joResult.getString("label"), null, null, 0, 0));
                                break;
                            }
                            case "user":{
                                JSONObject joDetails = joResult.getJSONObject("details");
                                String label = joResult.getString("label");
                                String user_name = joDetails.getString("username");
                                String avatar = joDetails.getString("avatar");
                                int followers = joDetails.getInt("followers");
                                int courses = joDetails.getInt("courses");
                                searchAutocompleteItemList.add(new SearchAutocompleteItem("user", label, user_name, avatar, followers, courses));
                                break;
                            }
                            case "keyword":{
                                searchAutocompleteItemList.add(new SearchAutocompleteItem(
                                        "keyword", joResult.getString("label"), null, null, 0, 0));
                                break;
                            }
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(SearchAutocompleteActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                }
                searchAutocompleteItemAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchAutocompleteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


        HashMap<String,String> params=new HashMap<String,String>();
        HashMap<String,String> headers=new HashMap<String,String>();

        HttpsRequest autocompleteSearchRequest=new HttpsRequest(Request.Method.GET, APIS.SEARCH_AUTOCOMPLETE+"?q="+query,resListener,errorListener,params,headers);
        requestQueue.add(autocompleteSearchRequest);
    }

    public void updateSearchQuery(String newQuery){
        explicitQuerySet=true;
        sv_query.setQuery(newQuery, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        KeyboardUtility.hideKeyboard(this, rv_items);
    }

    public void back_onClick(View v){
        super.onBackPressed();
    }
}
