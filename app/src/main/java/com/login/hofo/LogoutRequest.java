package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class LogoutRequest extends StringRequest {


    private static final String USER_REQUEST_URL =  getUrlApi()+"logout";

    LogoutRequest(String api_token, Response.Listener<String> listener)
    {
        super(Method.POST, USER_REQUEST_URL+"?api_token="+api_token, listener, null);

    }
}
