package com.login.hofo;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.login.hofo.UrlApi.getUrlApi;


public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL =  getUrlApi()+"register";

    private Map<String,String> params;

    RegisterRequest (String nameUser, String emailUser, String birthdateUser,String phoneUser, String addressUser, String descriptionUser, String cityUser,
                            String ageExperienceUser, String photoHUser, String photoPUser, String roleUser,
                     String passUser, String passConfirmUser, String FMCToken, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();

        params.put("name",nameUser);
        params.put("email",emailUser);
        params.put("birth_date",birthdateUser);
        params.put("password",passUser);
        params.put("password_confirmation",passConfirmUser);
        params.put("address",addressUser);
        params.put("description",descriptionUser);
        params.put("city",cityUser);
        params.put("age_experience",ageExperienceUser);
        params.put("photo_home",photoHUser);
        params.put("photo_perfil",photoPUser);
        params.put("role_id",roleUser);
        params.put("phone",phoneUser);
        params.put("FMCToken",FMCToken);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
