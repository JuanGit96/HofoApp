package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class MenuByModalityRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"menusByModality/";


    public MenuByModalityRequest(String modality,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, REGISTER_REQUEST_URL+modality, listener, errorListener);
    }


}
