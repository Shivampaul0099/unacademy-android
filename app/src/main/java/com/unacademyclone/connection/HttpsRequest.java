package com.unacademyclone.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;

public class HttpsRequest extends StringRequest {
    HashMap<String,String> params;
    HashMap<String,String> headers;

    public HttpsRequest(int requestMethod, String serverURL, Response.Listener<String> responseListener, Response.ErrorListener errorListener, HashMap<String,String> params, HashMap<String,String> headers){
        super(requestMethod,serverURL,responseListener,errorListener);
        this.params=params;
        this.headers=headers;
    }

    @Override
    public HashMap<String,String> getParams(){
        return params;
    }

    @Override
    public HashMap<String,String> getHeaders(){
        return headers;
    }
}
