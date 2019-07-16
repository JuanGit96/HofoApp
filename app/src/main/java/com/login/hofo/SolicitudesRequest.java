package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class SolicitudesRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"ordersByChef/";

    SolicitudesRequest (String api_token, int chef_id, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.GET, USER_REQUEST_URL+chef_id+"?api_token="+api_token, listener, errorListener);
    }
}
