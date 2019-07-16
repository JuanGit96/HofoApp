package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class ProfileRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"users/";

    ProfileRequest (String api_token, int id, Response.Listener<String> listener)
    {
        super(Method.GET, USER_REQUEST_URL+id+"?api_token="+api_token, listener, null);
    }

}
