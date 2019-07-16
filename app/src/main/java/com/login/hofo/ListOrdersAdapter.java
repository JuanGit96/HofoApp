package com.login.hofo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListOrdersAdapter extends ArrayAdapter<Solicitud> {

    //the list values in the List of type hero
    List<Solicitud> solicitudList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;


    //constructor initializing the values
    public ListOrdersAdapter(Context context, int resource, List<Solicitud> solicitudList) {
        super(context, resource, solicitudList);
        this.context = context;
        this.resource = resource;
        this.solicitudList = solicitudList;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view

//        final TextView nombre_plato = view.findViewById(R.id.nombre_plato);
        final TextView nombre_comensal = view.findViewById(R.id.nombre_comensal);
        final TextView fecha_orden = view.findViewById(R.id.fecha_orden);
        final TextView hora_orden = view.findViewById(R.id.hora_orden);

        //botones
        final Button btn_aceptar = (Button) view.findViewById(R.id.btn_aceptar);
        final Button btn_rechazar = (Button) view.findViewById(R.id.btn_rechazar);


        LinearLayout lClick = view.findViewById(R.id.orderClick);

        //getting the hero of the specified position
        final Solicitud solicitud = solicitudList.get(position);

        //LLenando dinamicamente cada elemento
        nombre_comensal.setText(solicitud.getDiner_name());
        fecha_orden.setText(solicitud.getDate());
        hora_orden.setText(solicitud.getHour());


        //adding a click listener to the button to remove item from the list
        lClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //removePregon(position);

                //al dar click en el elemento de la lista
                goToOrderDetail(
                    solicitud.getId()
                );

            }
        });


        //accion de los botones de accion
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(),"aceptando pedido",Toast.LENGTH_LONG).show();
            }
        });

        btn_rechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(),"descartando pedido",Toast.LENGTH_LONG).show();
            }
        });

        //finally returning the view
        return view;
    }

    //this method will remove the item from the list
    private void removePregon(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                solicitudList.remove(position);

                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goToOrderDetail(int order_id)
    {
        // Redirigiendo formulario de pedido
        Intent intentOrderDetail = new Intent(getContext(), DomiciliosFormActivity.class);
        intentOrderDetail.putExtra("order_id", order_id);
        getContext().startActivity(intentOrderDetail);

    }

    private void checkOrder(String id_menu)
    {
        // Redirigiendo a Bienvenida a usuario por su registro
        Intent intentEdit = new Intent(getContext(), EditMenyActivity.class);
        intentEdit.putExtra("id_menu", id_menu);
        getContext().startActivity(intentEdit);
    }


}
