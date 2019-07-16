package com.login.hofo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {


    //a List of type hero for holding list items
    List<Menu> menuList;

    //the listview
    ListView listViewMenu;

    String api_token;

    int idUser;

    Bitmap ImageMenu;

    final int SIN_MODALIDAD = 0;
    final int DOMICILIOS = 1;
    final int CHEF_EN_CASA = 2;
    final int EN_CASA_CHEF = 3;

    int idModality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Cargar datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", MenuActivity.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        idUser = sp.getInt("id", -1);

        //cargando datos de intent
        Intent intent = getIntent();
        idModality = intent.getIntExtra("idModality",0);

        if (idModality == SIN_MODALIDAD)
            listMenusByChef();
        else
            listMenyByModality(idModality);
    }

    public void goToAction(View view) {

        Intent intentMenuAction = new Intent(MenuActivity.this, CreateMenuActivity.class);
        MenuActivity.this.startActivity(intentMenuAction);
    }

    public void listMenusByChef ()
    {
        menuList = new ArrayList<>();
        listViewMenu = (ListView) findViewById(R.id.listViewMenu);

        //asignando modalidad actual en el objeto menu
        Menu.setCurrentModality(0);

        //consumiento servicio
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {

                    JSONObject jsonResponse = new JSONObject(response);


                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");


                        if (jsonArray.length() == 0)
                        {
                            Toast.makeText(MenuActivity.this, "No hay menús disponibles en este momento", Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectPregon = jsonArray.getJSONObject(i);

                                int id = jsonObjectPregon.getInt("id");
                                String nombre = jsonObjectPregon.getString("nombre");
                                String descripcion = jsonObjectPregon.getString("descripcion");
                                String precio = jsonObjectPregon.getString("precio");
                                String foto = jsonObjectPregon.getString("foto");
                                String ingredientes = jsonObjectPregon.getString("ingredientes");
                                String utencilios = jsonObjectPregon.getString("utencilios");
                                int user_id = jsonObjectPregon.getInt("user_id");

                                //adding some values to our list
                                menuList.add(new Menu(id, nombre, descripcion, precio,foto,ingredientes,utencilios,user_id));

                            } catch (JSONException e) {

                            }
                        }

                        //creating the adapter
                        ListMenusAdapter adapter = new ListMenusAdapter(MenuActivity.this, R.layout.custom_list_menus_cocinero, menuList);

                        //attaching adapter to the listview
                        listViewMenu.setAdapter(adapter);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

                        builder.setMessage("Error al listar menus...")
                                .setNegativeButton("Retry", null)
                                .create().show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Toast.makeText(getApplicationContext(), "Error en el servidor al listar menus por chef", Toast.LENGTH_LONG).show();
            }
        };

        MenusByChefRequest menusByChefRequest = new MenusByChefRequest(api_token, Integer.toString(idUser),responseListener,errorListener);

        RequestQueue queue = Volley.newRequestQueue(MenuActivity.this);

        queue.add(menusByChefRequest);
    }



    public void listMenyByModality(int modality)
    {
        menuList = new ArrayList<>();
        listViewMenu = (ListView) findViewById(R.id.listViewMenu);

        //asignando modalidad actual en el objeto menu
        Menu.setCurrentModality(modality);

        //consumiento servicio
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {

                    JSONObject jsonResponse = new JSONObject(response);


                    String code = jsonResponse.getString("code");

                    if (code.equals("200"))
                    {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");


                        if (jsonArray.length() == 0)
                        {
                            Toast.makeText(MenuActivity.this, "No hay menús disponibles en este momento", Toast.LENGTH_LONG).show();
                        }

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            try {
                                JSONObject jsonObjectPregon = jsonArray.getJSONObject(i);

                                int id = jsonObjectPregon.getInt("id");
                                String nombre = jsonObjectPregon.getString("nombre");
                                String descripcion = jsonObjectPregon.getString("descripcion");
                                String precio = jsonObjectPregon.getString("precio");
                                String foto = jsonObjectPregon.getString("foto");
                                String ingredientes = jsonObjectPregon.getString("ingredientes");
                                String utencilios = jsonObjectPregon.getString("utencilios");
                                int user_id = jsonObjectPregon.getInt("user_id");

                                //adding some values to our list
                                menuList.add(new Menu(id, nombre, descripcion, precio,foto,ingredientes,utencilios,user_id));

                            } catch (JSONException e) {

                            }
                        }

                        //creating the adapter
                        ListMenusAdapter adapter = new ListMenusAdapter(MenuActivity.this, R.layout.custom_list_menus_cocinero, menuList);

                        //attaching adapter to the listview
                        listViewMenu.setAdapter(adapter);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

                        builder.setMessage("Error al listar menus........")
                                .setNegativeButton("Retry", null)
                                .create().show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Toast.makeText(getApplicationContext(), "Error en el servidor al listar menus por modalidad", Toast.LENGTH_LONG).show();
            }
        };

        MenuByModalityRequest menusByChefRequest = new MenuByModalityRequest(Integer.toString(modality),responseListener,errorListener);

        RequestQueue queue = Volley.newRequestQueue(MenuActivity.this);

        queue.add(menusByChefRequest);
    }


}
