package com.login.hofo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderSuccessActivity extends AppCompatActivity {

    TextView tvOrden, tvPrecio, tvANobreDe, tvDireccion, tvCiudad, tvTelefono;

    String orderName, orderCity, orderAddress, orderPhone, orderDate, orderHour;

    int menu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        tvOrden = (TextView) findViewById(R.id.orden);
        tvPrecio = (TextView) findViewById(R.id.precio);
        tvANobreDe = (TextView) findViewById(R.id.aNombreDe);
        tvDireccion = (TextView) findViewById(R.id.direccion);
        tvCiudad = (TextView) findViewById(R.id.ciudad);
        tvTelefono = (TextView) findViewById(R.id.telefono);

        ActividadAnterior();

        enviarNotificacionChef();

        setValuesTextView();
    }


    private void ActividadAnterior()
    {
        Intent intent = getIntent();
        orderName = intent.getStringExtra("orderName");
        orderCity = intent.getStringExtra("orderCity");
        orderAddress = intent.getStringExtra("orderAddress");
        orderPhone = intent.getStringExtra("orderPhone");
        orderDate = intent.getStringExtra("orderDate");
        orderHour = intent.getStringExtra("orderHour");
        menu_id = intent.getIntExtra("menu_id",0);
    }

    private void enviarNotificacionChef()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //en caso de exito
                Toast.makeText(OrderSuccessActivity.this, "Notificacion enviada a cocinero", Toast.LENGTH_LONG).show();

                //

            }
        };

        //en caso de error
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                Toast alertMessage = Toast.makeText(getApplicationContext(), "Error al enviar notificacion a cocinero", Toast.LENGTH_LONG);
                alertMessage.setGravity(Gravity.CENTER, 0, 0);
                alertMessage.show();

            }
        };

        OrderSuccessRequest orderSuccessRequest = new OrderSuccessRequest(menu_id, responseListener, errorListener);

        RequestQueue queue = Volley.newRequestQueue(OrderSuccessActivity.this);

        queue.add(orderSuccessRequest);
    }

    private void setValuesTextView()
    {
        tvOrden.setText(orderName);
        tvPrecio.setText(orderCity);
        tvANobreDe.setText(orderAddress);
        tvDireccion.setText(orderPhone);
        tvCiudad.setText(orderDate);
        tvTelefono.setText(orderHour);
    }

    public void goToBack(View view)
    {
        Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
        OrderSuccessActivity.this.startActivity(intent);
    }
}
