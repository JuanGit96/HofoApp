package com.login.hofo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.hofo.UrlApi.getUrlApi;

public class NewOrderRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"orders";

    private Map<String,String> params;

    NewOrderRequest(String diner_name, String hour, String address, String city, String phone, String date, String amount_people,
                    String ingredients, String utensils, String additional_comments, String qualification,
                    String final_comment, String menu_id, String diner_id, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);

        params = new HashMap<>();

        params.put("diner_name",diner_name);
        params.put("hour",hour);
        params.put("address",address);
        params.put("city",city);
        params.put("phone",phone);
        params.put("date",date);
        if (amount_people != null)
            params.put("amount_people",amount_people);

        if (ingredients != null)
            params.put("ingredients",ingredients);

        if (utensils != null)
            params.put("utensils",utensils);

        if (additional_comments != null)
            params.put("additional_comments",additional_comments);

        if (qualification != null)
            params.put("qualification",qualification);

        if (final_comment != null)
            params.put("final_comment",final_comment);

        params.put("menu_id",menu_id);

        if (diner_id != null)
            params.put("diner_id",diner_id);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
