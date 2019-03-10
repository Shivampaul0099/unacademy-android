package com.unacademyclone.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unacademyclone.R;
import com.unacademyclone.activity.GoalsActivity;
import com.unacademyclone.adapter.GoalAdapter;
import com.unacademyclone.adapter.StoryAdapter;
import com.unacademyclone.connection.HttpsRequest;
import com.unacademyclone.model.Course;
import com.unacademyclone.model.Goal;
import com.unacademyclone.model.Story;
import com.unacademyclone.model.StoryAuthor;
import com.unacademyclone.utility.APIS;
import com.unacademyclone.utility.Constant;
import com.unacademyclone.utility.NetworkUtility;
import com.unacademyclone.utility.TypefaceUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    final int ITEMS_LIMIT = 7;
    int currentPage = 0;

    boolean isScrolling=false, isResponsePending=false;
    int currentItems,totalItems,scrollOutItems;

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TypefaceUtility tfUtil;
    RequestQueue requestQueue;
    LinearLayoutManager llm;
    List<Story> storyList;
    StoryAdapter storyAdapter;
    FrameLayout fl_progress;
    TextView tv_search_hint;
    RecyclerView rv_stories;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home,container,false);

        sp=context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
        tfUtil=new TypefaceUtility(context);
        requestQueue= Volley.newRequestQueue(context);
        storyList=new ArrayList<>();

        storyAdapter=new StoryAdapter(context,storyList);

        fl_progress=rootView.findViewById(R.id.fl_progress);
        tv_search_hint=rootView.findViewById(R.id.tv_search_hint);
        rv_stories=rootView.findViewById(R.id.rv_stories);

        tv_search_hint.setTypeface(tfUtil.getTypefaceRegular());

        llm=new LinearLayoutManager(context);
        rv_stories.setLayoutManager(llm);
        rv_stories.setAdapter(storyAdapter);
        rv_stories.setNestedScrollingEnabled(false);

        rv_stories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=llm.getChildCount();
                totalItems=llm.getItemCount();
                scrollOutItems=llm.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrollOutItems)==totalItems){
                    if(NetworkUtility.isAvailable(context)){
                        fetchContent();
                        isScrolling=false;
                    }
                }
            }
        });

        if(NetworkUtility.isAvailable(context)){
            fetchContent();
        }

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
//        Toast.makeText(context, "Networkcall made", Toast.LENGTH_SHORT).show();

        HashMap<String,String> headers=new HashMap<String,String>();


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
                        JSONObject joFeedItem = jaResults.getJSONObject(i);

                        if(joFeedItem.getString("type").equals("story")){

                            JSONObject joData = joFeedItem.getJSONObject("data");

                            Story story = new Story();
                            story.setObject_meta(joData.getString("object_meta"));
                            story.setMessage(joData.getString("message"));
                            story.setObject_type(joData.getInt("object_type"));
                            story.setUid(joData.getString("uid"));
                            story.setCreated_at(joData.getString("created_at"));
                            story.setVerb_text(joData.getString("verb_text"));
                            story.setId(joData.getInt("id"));
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
                                story.setCollection_name(joObject.getJSONObject("concept_topology").getString("name"));
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
                                    courseList.add(new Course(
                                            joCourse.getString("uid"),
                                            joCourse.getString("name"),
                                            joCourse.getString("language_display"),
                                            joCourse.getString("thumbnail"),
                                            joCourse.getJSONObject("concept_topology").getString("title"),
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

                }
                catch (Exception e){
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("UA_ERROR", e.getMessage());

                }
                storyAdapter.notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isResponsePending = false;
                fl_progress.setVisibility(View.GONE);
                Log.d("GOPALAKRISHNAN", error.toString());
            }
        };

        Log.d("GDBUG", APIS.FEED);
        HttpsRequest goalsRequest=new HttpsRequest(Request.Method.GET, APIS.FEED+"?version=1&exp_variation=2&goal=KSCGY&limit=7&offset="+offset,resListener,errorListener,params,headers);
        requestQueue.add(goalsRequest);
        isResponsePending=true;
        fl_progress.setVisibility(View.VISIBLE);
    }

}
