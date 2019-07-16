package com.login.hofo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.hofo.UrlApi.getUrlApi;

public class MenusByChefRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"menusByChef/";


    MenusByChefRequest (String api_token, String user_id, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.GET, REGISTER_REQUEST_URL+user_id+"?api_token="+api_token, listener, errorListener);
    }


}
