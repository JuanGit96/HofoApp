package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class OrderSuccessRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"newOrderNotification/";

    OrderSuccessRequest (int menu_id, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.GET, USER_REQUEST_URL+menu_id, listener, errorListener);
    }

}
