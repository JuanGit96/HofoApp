package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class UpdateFMCTockenRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"updateFMCTocken/";

    UpdateFMCTockenRequest (int user_id, String FMCTocken, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.GET, USER_REQUEST_URL+user_id+"/"+FMCTocken, listener, errorListener);
    }

}
