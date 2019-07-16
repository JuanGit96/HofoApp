package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import static com.login.hofo.UrlApi.getUrlApi;

public class ShowMenuRequest extends StringRequest {

    private static final String USER_REQUEST_URL =  getUrlApi()+"menus/";

    ShowMenuRequest (String api_token, String menu_id, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.GET, USER_REQUEST_URL+menu_id+"?api_token="+api_token, listener, errorListener);
    }

}
