package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class SolicitudRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"orders/";

    SolicitudRequest (int order_id, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.GET, USER_REQUEST_URL+order_id, listener, errorListener);
    }
}
