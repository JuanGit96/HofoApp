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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListMenusAdapter extends ArrayAdapter<Menu> {

    //the list values in the List of type hero
    List<Menu> menuList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    Bitmap imageMenu;

    //constructor initializing the values
    public ListMenusAdapter(Context context, int resource, List<Menu> menuList) {
        super(context, resource, menuList);
        this.context = context;
        this.resource = resource;
        this.menuList = menuList;
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
        ImageView tvPhotoMenuChef = (ImageView) view.findViewById(R.id.viewMenuChef);
        final TextView tvNameMenuChef = view.findViewById(R.id.nameMenuChef);
        final TextView tvDescriptionMenu = view.findViewById(R.id.descriptionMenuChef);
        final TextView tvPriceMenu = view.findViewById(R.id.price_menu);
        final TextView tv_identMenu = view.findViewById(R.id.ident_menu);
        final TextView tv_identUser = view.findViewById(R.id.ident_user);

        //botones
        final Button btn_editar = (Button) view.findViewById(R.id.btn_editar);
        final Button btn_eliminar = (Button) view.findViewById(R.id.btn_eliminar);


        LinearLayout lClick = view.findViewById(R.id.menuClick);

        //getting the hero of the specified position
        Menu menu = menuList.get(position);

        //adding values to the list item
        //imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
        Log.d("IMAGEN","");
        Picasso.get()
                .load(UrlApi.getUrlServer()+"storage/"+menu.getPhoto())
                .placeholder(R.drawable.imagen_no_disponible)
                .error(R.drawable.logo)
                .into(tvPhotoMenuChef);
        //tvPhotoMenuChef.setImageBitmap(getBitmapForUrl(menu.getphoto()));
        tvNameMenuChef.setText(menu.getName());
        tvDescriptionMenu.setText(menu.getDescription());
        tvPriceMenu.setText(menu.getPrice());
        tv_identMenu.setText(Integer.toString(menu.getId()));
        tv_identUser.setText(Integer.toString(menu.getUser_id()));

        //adding a click listener to the button to remove item from the list
        lClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                //removePregon(position);
                //PregonesActivosActivity p = new PregonesActivosActivity();
                //al dar click en el elemento de la lista
                goToFormMenu(
                        Integer.parseInt(tv_identMenu.getText().toString()),
                        tvNameMenuChef.getText().toString(),
                        tvDescriptionMenu.getText().toString(),
                        Integer.parseInt(tvPriceMenu.getText().toString()),
                        Integer.parseInt(tv_identUser.getText().toString()),
                        Menu.getCurrentModality()
                );

            }
        });


        //accion de los botones de accion
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditmenu(tv_identMenu.getText().toString());
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"eliminar",Toast.LENGTH_LONG).show();
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
                menuList.remove(position);

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

    private void goToFormMenu(int menu_id,String nombreMenu, String descripcionMenu, int precioMenu, int idUser, int currentModality)
    {
        // Redirigiendo formulario de pedido
        Intent intentOrder = new Intent(getContext(), DomiciliosFormActivity.class);
        intentOrder.putExtra("menu_id", menu_id);
        intentOrder.putExtra("nombre_menu", nombreMenu);
        intentOrder.putExtra("descripcion_menu", descripcionMenu);
        intentOrder.putExtra("precio_menu", precioMenu);
        intentOrder.putExtra("user_id", idUser);
        intentOrder.putExtra("currentModality", currentModality);
        getContext().startActivity(intentOrder);

        Toast.makeText(getContext(), "Mostrando menu",Toast.LENGTH_LONG).show();
    }

    private void goToEditmenu(String id_menu)
    {
        // Redirigiendo a Bienvenida a usuario por su registro
        Intent intentEdit = new Intent(getContext(), EditMenyActivity.class);
        intentEdit.putExtra("id_menu", id_menu);
        getContext().startActivity(intentEdit);
    }

}
