package com.login.hofo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    TextView tv_goTo_registerPage, etEmailUser, etPasswordUser;

    Button btnLogin;

    ProgressDialog dialog;

    Context thiscontext = this;

    //variables para capturar data de actividad anterior
    String nombre_menu, descripcion_menu;
    int precio_menu, user_id, menu_id, currentModality;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //validando si es un login de cocinero o de comensal
        //data de activity anterior
        Intent intent = getIntent();
        nombre_menu = intent.getStringExtra("nombre_menu");
        descripcion_menu = intent.getStringExtra("descripcion_menu");
        precio_menu = intent.getIntExtra("precio_menu",0);
        user_id = intent.getIntExtra("user_id",0);
        menu_id = intent.getIntExtra("menu_id",0);
        currentModality = intent.getIntExtra("currentModality",0);

        if(existSession())
        {
            if (currentModality == 0) // si NO venimos de un menu
            {
                // Redirigiendo a Perfil de usuario
                Intent intentMenu = new Intent(LoginActivity.this, PerfilActivity.class);
                LoginActivity.this.startActivity(intentMenu);
            }

        }

        btnLogin = (Button) findViewById(R.id.btn_login);

        etEmailUser = (EditText) findViewById(R.id.login_user);
        etPasswordUser = (EditText) findViewById(R.id.login_password);

        tv_goTo_registerPage = (TextView) findViewById(R.id.tv_goTo_registerPage);

        tv_goTo_registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Redirigiendo a perfil de usuario
                Intent intentLogin = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intentLogin);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Iniciando sesión");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                final String emailUser = etEmailUser.getText().toString();
                final String passUser = etPasswordUser.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("200"))
                            {
                                //Guardando datos en session

                                JSONObject resultData = jsonResponse.getJSONObject("data");

                                String username = resultData.optString("name");
                                String email = resultData.getString("email");
                                String api_token = resultData.getString("api_token");
                                String img_perfil = resultData.getString("photo_perfil");
                                int id = resultData.getInt("id");
                                int roleId = resultData.getInt("role_id");
                                String city = resultData.getString("city");
                                String address = resultData.getString("address");
                                String phone = resultData.getString("phone");

                                SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username", username);
                                editor.putString("email", email);
                                editor.putString("api_token", api_token);
                                editor.putString("img_perfil", img_perfil);
                                editor.putInt("id", id);
                                editor.putInt("roleId", roleId);
                                editor.putString("city", city);
                                editor.putString("address", address);
                                editor.putString("phone", phone);
                                editor.commit();


                                actualizarFMCToken(id, FirebaseInstanceId.getInstance().getToken());

                                dialog.dismiss();

                                if (currentModality != 0) //si llegamos al registro por  un menu
                                {
                                    goToFormularioDePedido();
                                }
                                else
                                {
                                    Intent loginIntent = new Intent(LoginActivity.this, PerfilActivity.class);
                                    LoginActivity.this.startActivity(loginIntent);
                                }



                            }

                            if (code.equals("400"))
                            {

                                //Mostrando errores en pantalla

                                String error = jsonResponse.getString("error");

                                dialog.dismiss();

                                Toast alertMessage = Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG);
                                alertMessage.setGravity(Gravity.CENTER, 0, 0);
                                alertMessage.show();

                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();

                            Toast alertMessage = Toast.makeText(getApplicationContext(),"Error en la comunicacion con el servidor", Toast.LENGTH_LONG);
                            alertMessage.setGravity(Gravity.CENTER, 0, 0);
                            alertMessage.show();
                        }

                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast alertMessage = Toast.makeText(getApplicationContext(), "Error al iniciar sesión: "+volleyError, Toast.LENGTH_LONG);
                        alertMessage.setGravity(Gravity.CENTER, 0, 0);
                        alertMessage.show();

                        dialog.dismiss();
                    }
                };

                LoginRequest loginrequest = new LoginRequest(emailUser, passUser, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                queue.add(loginrequest);


            }
        });




    }

    public boolean existSession ()
    {
        //datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", LoginActivity.MODE_PRIVATE);
        String nameSession = sp.getString("username",null);

        if (nameSession != null)
            return true;
        else
            return false;
    }

    public boolean sessionChef()
    {
        //datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", LoginActivity.MODE_PRIVATE);
        int roleId = sp.getInt("roleId",0);

        return roleId == 1;
    }

    private void goToFormularioDePedido()
    {
        // Redirigiendo a formulario de menu
        Intent intentFormMenu = new Intent(LoginActivity.this, DomiciliosFormActivity.class);
        intentFormMenu.putExtra("nombre_menu", nombre_menu);
        intentFormMenu.putExtra("descripcion_menu", descripcion_menu);
        intentFormMenu.putExtra("precio_menu", precio_menu);
        intentFormMenu.putExtra("user_id", user_id);
        intentFormMenu.putExtra("menu_id", menu_id);
        intentFormMenu.putExtra("currentModality", currentModality);
        LoginActivity.this.startActivity(intentFormMenu);
    }

    public void actualizarFMCToken(int id, String FMCTocken)
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonResponse = new JSONObject(response);

                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {
                        //Todo Bien
                    }

                    if (code.equals("400"))
                    {
                        dialog.dismiss();

                        Toast alertMessage = Toast.makeText(getApplicationContext(),"error al actualizar codigo de telefono", Toast.LENGTH_LONG);
                        alertMessage.setGravity(Gravity.CENTER, 0, 0);
                        alertMessage.show();

                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();

                    Toast alertMessage = Toast.makeText(getApplicationContext(),"Error al actualizar codigo de telefono", Toast.LENGTH_LONG);
                    alertMessage.setGravity(Gravity.CENTER, 0, 0);
                    alertMessage.show();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Toast alertMessage = Toast.makeText(getApplicationContext(), "Error al iniciar sesión: "+volleyError, Toast.LENGTH_LONG);
                alertMessage.setGravity(Gravity.CENTER, 0, 0);
                alertMessage.show();

                dialog.dismiss();
            }
        };

        UpdateFMCTockenRequest updateFMCTockenRequest = new UpdateFMCTockenRequest(id, FMCTocken,responseListener, errorListener);

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        queue.add(updateFMCTockenRequest);
    }


}
