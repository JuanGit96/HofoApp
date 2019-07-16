package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.hofo.UrlApi.getUrlApi;

public class LoginRequest extends  StringRequest{

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"login";

    private Map<String,String> params;

    LoginRequest (String emailUser, String passUser, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();

        params.put("email",emailUser);
        params.put("password",passUser);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }



}
