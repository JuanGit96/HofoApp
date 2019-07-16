package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.hofo.UrlApi.getUrlApi;

public class EditMenuRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"menus/";

    private Map<String,String> params;

    EditMenuRequest (String api_token,String menu_id,String user_id,String nameMenu, String description, String price,String photo, String ingredientes, String utencilios, String modalities,
                       Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.PUT, REGISTER_REQUEST_URL+menu_id+"?api_token="+api_token, listener, errorListener);

        params = new HashMap<>();

        params.put("nombre",nameMenu);
        params.put("descripcion",description);
        params.put("precio",price);
        params.put("foto",photo);
        params.put("ingredientes",ingredientes);
        params.put("utencilios",utencilios);
        params.put("modalidades",modalities);
        params.put("user_id",user_id);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
