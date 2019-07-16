package com.login.hofo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.login.hofo.R.drawable.five_stars;
import static com.login.hofo.R.drawable.four_stars;
import static com.login.hofo.R.drawable.one_star;
import static com.login.hofo.R.drawable.three_stars;
import static com.login.hofo.R.drawable.two_stars;

public class DomiciliosFormActivity extends AppCompatActivity {

    String descripcion_menu, nombre_menu, rutaImagenUser;
    int precio_menu, user_id, menu_id, currentModality, diner_id;

    String api_token;

    int roleId;

    TextView nombreMenu, descripcionMenu, precioMenu, nombreUsuario;

    ImageView user_calification;

    CircleImageView user_perfilPhoto, imageViewPerfilMenu;

    private int sYear, sMonth, sDay;

    Calendar calendar = Calendar.getInstance();

    static final int DATE_ID = 0;

    ProgressDialog dialog;

    //items del formulario

    EditText order_name, order_city, order_address, order_phone, order_date, order_hour, order_additionalComments;
    Button dateOrder_btn, btn_ordenar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domicilios_form);

        //data de activity anterior
        Intent intent = getIntent();
        nombre_menu = intent.getStringExtra("nombre_menu");
        descripcion_menu = intent.getStringExtra("descripcion_menu");
        precio_menu = intent.getIntExtra("precio_menu",0);
        user_id = intent.getIntExtra("user_id",0);
        menu_id = intent.getIntExtra("menu_id",0);
        currentModality = intent.getIntExtra("currentModality",0);

        //data de session
        SharedPreferences sp = getSharedPreferences("your_prefs", PerfilActivity.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        roleId = sp.getInt("roleId", 0);

        //fecha actual para mostrar en calendario
        sMonth = calendar.get(Calendar.MONTH);
        sYear = calendar.get(Calendar.YEAR);
        sDay = calendar.get(Calendar.DAY_OF_MONTH);

        nombreMenu = (TextView) findViewById(R.id.name_menu);
        descripcionMenu = (TextView) findViewById(R.id.descriptionMenuChef);
        precioMenu = (TextView) findViewById(R.id.priceMenuChef);
        nombreUsuario = (TextView) findViewById(R.id.user_name);

        user_calification = (ImageView) findViewById(R.id.user_calification);
        user_perfilPhoto = (CircleImageView) findViewById(R.id.profile_image);

        //elementos del formulario
        order_name = (EditText) findViewById(R.id.order_name);
        order_city = (EditText) findViewById(R.id.order_city);
        order_address = (EditText) findViewById(R.id.order_address);
        order_phone = (EditText) findViewById(R.id.order_phone);
        order_date = (EditText) findViewById(R.id.order_date);
        order_hour = (EditText) findViewById(R.id.order_hour);
        order_additionalComments = (EditText) findViewById(R.id.order_additionalComments);
        dateOrder_btn = (Button) findViewById(R.id.dateOrder_btn);
        btn_ordenar = (Button) findViewById(R.id.btn_ordenar);

        //Si hay session se cargan los datos
        chargeDataSession();

        //mostrando data de cocinero (dueño de menu)
        showUserProfile(user_id);

        //cargando datos del menu
        nombreMenu.setText(nombre_menu);
        precioMenu.setText(Integer.toString(precio_menu));
        descripcionMenu.setText(descripcion_menu);

        //al dar click en calendario
        dateOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });

        //click en ordenar
        btn_ordenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(DomiciliosFormActivity.this);
                dialog.setTitle("Registrando Pedido");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                final String orderName = order_name.getText().toString().trim();
                final String orderCity = order_city.getText().toString().trim();
                final String orderAddress = order_address.getText().toString().trim();
                final String orderPhone = order_phone.getText().toString().trim();
                final String orderDate = order_date.getText().toString().trim();
                final String orderHour = order_hour.getText().toString().trim();
                final String orderAdditionalComments = order_additionalComments.toString().trim();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("201"))
                            {
                                //pedido creado correctamente
                                dialog.dismiss();

                                goToOrderSuccess(orderName, orderCity, orderAddress, orderPhone, orderDate, orderHour);

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
                        }

                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast.makeText(getApplicationContext(), "Error en el servidor al crear pedido", Toast.LENGTH_LONG).show();
                    }
                };

                NewOrderRequest newOrder = new NewOrderRequest(orderName, orderHour, orderAddress, orderCity,
                        orderPhone, orderDate, null, null, null, orderAdditionalComments,
                        null, null, Integer.toString(menu_id), null, responseListener, errorListener);

                RequestQueue queue = Volley.newRequestQueue(DomiciliosFormActivity.this);

                queue.add(newOrder);

            }
        });
    }

    private void chargeDataSession()
    {
        //validamos de que modalidad venimos y si estamos logueados como comensales
        if (currentModality != 0 && currentModality != 1)
        {
            //pedimos login
            boolean isLoggedComensal = validateLoggedComensal();

            if (!isLoggedComensal)
            {
                Intent intentLoginComensal = new Intent(DomiciliosFormActivity.this, RegisterActivity.class);
                intentLoginComensal.putExtra("nombre_menu",nombre_menu);
                intentLoginComensal.putExtra("descripcion_menu",descripcion_menu);
                intentLoginComensal.putExtra("precio_menu",precio_menu);
                intentLoginComensal.putExtra("user_id",user_id);
                intentLoginComensal.putExtra("menu_id",menu_id);
                intentLoginComensal.putExtra("currentModality",currentModality);
                DomiciliosFormActivity.this.startActivity(intentLoginComensal);
            }
            else
            {
                //data del comensal en sesión
                SharedPreferences sp = getSharedPreferences("your_prefs", PerfilActivity.MODE_PRIVATE);
                diner_id = sp.getInt("id", 0);

                order_name.setEnabled(false);
                order_city.setEnabled(false);
                order_address.setEnabled(false);
                order_phone.setEnabled(false);

                order_name.setText(sp.getString("username",null));
                order_city.setText(sp.getString("city", null));
                order_address.setText(sp.getString("address", null));
                order_phone.setText(sp.getString("phone", null));
            }
        }
        else
        {
            diner_id = 0;
        }

    }

    public void showUserProfile (int user_id)
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
                        JSONObject data = jsonResponse.getJSONObject("data");

                        String name = data.getString("name");
                        rutaImagenUser = data.getString("photo_perfil");
                        String calification = data.getString("calification");

                        //cargando datos del usuario
                        nombreUsuario.setText(name);

                        setImageProfile();

                        setImageCalification(calification);

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DomiciliosFormActivity.this);

                        builder.setMessage("Error al registrar pregonero")
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

        ProfileRequest profilerequest = new ProfileRequest(api_token, user_id, responseListener);

        RequestQueue queue = Volley.newRequestQueue(DomiciliosFormActivity.this);

        queue.add(profilerequest);
    }

    public void setImageProfile ()
    {
        Response.Listener<Bitmap> responseListener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                user_perfilPhoto.setImageBitmap(response);
                imageViewPerfilMenu.setImageBitmap(response);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Toast.makeText(getApplicationContext(), "Error en el servidor al cargar imagen", Toast.LENGTH_LONG).show();
            }
        };


        ImageProfileRequest imageProfileRequest = new ImageProfileRequest(rutaImagenUser, responseListener, errorListener);

        RequestQueue queue = Volley.newRequestQueue(DomiciliosFormActivity.this);

        queue.add(imageProfileRequest);
    }


    public void setImageCalification (String calification)
    {
        switch (calification)
        {
            case "1":
                user_calification.setImageResource(one_star);
                break;

            case "2":
                user_calification.setImageResource(two_stars);;
                break;

            case "3":
                user_calification.setImageResource(three_stars);
                break;

            case "4":
                user_calification.setImageResource(four_stars);
                break;

            case "5":
                user_calification.setImageResource(five_stars);
                break;

            default:
                Toast.makeText(getApplicationContext(),"Error alcapturarcalificacion del usuario",Toast.LENGTH_LONG).show();
        }
    }

    public void goToProfileChef(View view) {

        Intent intent = new Intent(DomiciliosFormActivity.this, PerfilActivity.class);
        intent.putExtra("user_id",user_id);
        DomiciliosFormActivity.this.startActivity(intent);
    }

    //Funciones calendario
    @Override
    protected Dialog onCreateDialog(int id)
    {
        if (id == DATE_ID)
            return new DatePickerDialog(this, mDateSetListener, sYear, sMonth, sDay);

        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =

            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    Toast.makeText(DomiciliosFormActivity.this, year+"-"+(month+1)+"-"+dayOfMonth+"", Toast.LENGTH_LONG).show();

                    order_date.setText(year+"-"+(month+1)+"-"+dayOfMonth+"");

                }
            };

    public boolean validateLoggedComensal()
    {
        if (roleId == 0)
            return false;

        return roleId == 2;
    }


    public void goToOrderSuccess(String orderName, String orderCity, String orderAddress,
                                 String orderPhone, String orderDate, String orderHour)
    {
        Intent intent = new Intent(DomiciliosFormActivity.this, OrderSuccessActivity.class);
        intent.putExtra("orderName",orderName);
        intent.putExtra("orderCity",orderCity);
        intent.putExtra("orderAddress",orderAddress);
        intent.putExtra("orderPhone",orderPhone);
        intent.putExtra("orderDate",orderDate);
        intent.putExtra("orderHour",orderHour);
        intent.putExtra("menu_id",menu_id);
        DomiciliosFormActivity.this.startActivity(intent);
    }

}
