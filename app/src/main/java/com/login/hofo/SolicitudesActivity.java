package com.login.hofo;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SolicitudesActivity extends AppCompatActivity {


    //a List of type hero for holding list items
    List<Solicitud> solicitudList;

    //the listview
    ListView listViewOrder;

    String api_token;

    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        //Cargar datos de session
        SharedPreferences sp = getSharedPreferences("your_prefs", MenuActivity.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        idUser = sp.getInt("id", -1);

        listOrdersByChef();
    }

    private void listOrdersByChef()
    {
        solicitudList = new ArrayList<>();
        listViewOrder = (ListView) findViewById(R.id.listViewOrder);

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
                        JSONObject jsonObject = jsonResponse.getJSONObject("data");

                        Iterator<String> keys = jsonObject.keys();

                        while(keys.hasNext()) {

                            String key = keys.next(); // se guarda el nombre de plato

                            JSONArray jsonArray = jsonObject.getJSONArray(key); // obtenemos las solicitudes de los platos

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                try {
                                    JSONObject jsonObjectOrder = jsonArray.getJSONObject(i);

                                    // Elementos del objeto Json
                                    int id = jsonObjectOrder.getInt("id");
                                    String diner_name = jsonObjectOrder.getString("diner_name");
                                    String hour = jsonObjectOrder.getString("hour");
                                    String address = jsonObjectOrder.getString("address");
                                    String city = jsonObjectOrder.getString("city");
                                    String phone = jsonObjectOrder.getString("phone");
                                    String date = jsonObjectOrder.getString("date");

                                    int amount_people = 1;
                                    if (!jsonObjectOrder.getString("amount_people").equals("null"))
                                    {
                                        amount_people = jsonObjectOrder.getInt("amount_people");
                                    }

                                    String ingredients = jsonObjectOrder.getString("ingredients");
                                    String utensils = jsonObjectOrder.getString("utensils");
                                    String additional_comments = jsonObjectOrder.getString("additional_comments");
                                    String final_comment = jsonObjectOrder.getString("final_comment");

                                    int qualification = 0;

                                    if (!jsonObjectOrder.getString("qualification").equals("null"))
                                    {
                                        qualification = jsonObjectOrder.getInt("qualification");
                                    }

                                    int menu_id = jsonObjectOrder.getInt("menu_id");


                                    int diner_id = 0;
                                    if (!jsonObjectOrder.getString("diner_id").equals("null"))
                                    {
                                        diner_id = jsonObjectOrder.getInt("diner_id");
                                    }



                                    Solicitud solicitud = new Solicitud(diner_name, hour,
                                            address, city,phone,date,ingredients,final_comment,utensils,
                                            additional_comments, amount_people,qualification,menu_id,
                                            diner_id,id);

                                    //Agregando valores al objeto y a la lista
                                     solicitudList.add(solicitud);

                                } catch (JSONException e) {
                                    Toast.makeText(SolicitudesActivity.this, e.toString(),Toast.LENGTH_LONG).show();
                                }
                            }


                        }

                        //creating the adapter
                        ListOrdersAdapter adapter = new ListOrdersAdapter(SolicitudesActivity.this, R.layout.custom_list_solicitudes, solicitudList);

                        //attaching adapter to the listview
                        listViewOrder.setAdapter(adapter);
                        //Toast.makeText(SolicitudesActivity.this,solicitudList.toString(),Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SolicitudesActivity.this);

                        builder.setMessage("Error al listar solicitudes...")
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

        SolicitudesRequest solicitudesRequest = new SolicitudesRequest(api_token, idUser,responseListener,errorListener);

        RequestQueue queue = Volley.newRequestQueue(SolicitudesActivity.this);

        queue.add(solicitudesRequest);
    }
}
