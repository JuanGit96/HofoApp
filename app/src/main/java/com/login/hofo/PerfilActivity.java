package com.login.hofo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.login.hofo.R.drawable.five_stars;
import static com.login.hofo.R.drawable.four_stars;
import static com.login.hofo.R.drawable.one_star;
import static com.login.hofo.R.drawable.three_stars;
import static com.login.hofo.R.drawable.two_stars;

public class PerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView user_name, user_description;

    ImageView user_calification;

    CircleImageView user_perfilPhoto, imageViewPerfilMenu;

    int idUser;

    String api_token, rutaImagenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        //Inicializando elementos de activity
        user_name = findViewById(R.id.user_name);
        user_description = findViewById(R.id.user_description);
        user_calification = (ImageView) findViewById(R.id.user_calification);
        user_perfilPhoto = (CircleImageView) findViewById(R.id.profile_image);

        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Icono de correo
        //FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //cambiando imagen de menu derecho
        View hView =  navigationView.getHeaderView(0);
        imageViewPerfilMenu = (CircleImageView)hView.findViewById(R.id.imageViewPerfilMenu);



        //data de session
        SharedPreferences sp = getSharedPreferences("your_prefs", PerfilActivity.MODE_PRIVATE);
        api_token = sp.getString("api_token", null);
        idUser = sp.getInt("id", -1);

        //datat de activity anterior
        Intent intent = getIntent();
        int user_id = intent.getIntExtra("user_id",0);

        showUserProfile(user_id);

    }

    @Override
    public void onBackPressed() {


        Intent intentBack = new Intent(PerfilActivity.this, MainActivity.class);
        PerfilActivity.this.startActivity(intentBack);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_menu) {

            goToMenuCRUD();

        } else if (id == R.id.nav_solicitud) {

            goToSolicitudes();

        } else if (id == R.id.nav_logout) {

            logout();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                        String description = data.getString("description");
                        rutaImagenUser = data.getString("photo_perfil");
                        String calification = data.getString("calification");

                        //cargando datos del usuario
                        user_name.setText(name);
                        user_description.setText(description);

                        setImageProfile();

                        setImageCalification(calification);

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);

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

        if (user_id != 0)
            idUser = user_id;

        ProfileRequest profilerequest = new ProfileRequest(api_token, idUser, responseListener);

        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);

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

        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);

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

    public void logout()
    {
        final ProgressDialog dialog = new ProgressDialog(PerfilActivity.this);
        dialog.setTitle("Cerrando sesión");
        dialog.setMessage("Cargando...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        dialog.setCancelable(false);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                    JSONObject jsonResponse = new JSONObject(response);

                    String msg = jsonResponse.getString("data");

                    if (msg.equals("User logged out."))
                    {
                        //Borrando datos en session

                        SharedPreferences sp = getSharedPreferences("your_prefs", MainActivity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove("username");
                        editor.remove("email");
                        editor.remove("api_token");
                        editor.remove("id");
                        editor.remove("city");
                        editor.remove("address");
                        editor.remove("phone");
                        editor.remove("roleId");
                        editor.remove("photo_perfil");
                        editor.commit();

                        dialog.dismiss();


                        // Redirigiendo a pagina inicial de login
                        Intent intentReturns = new Intent(PerfilActivity.this, MainActivity.class);
                        PerfilActivity.this.startActivity(intentReturns);
                    }
                    else
                    {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PerfilActivity.this);

                        builder.setMessage("Error al hacer logout del pregonero")
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

        LogoutRequest logoutrequest = new LogoutRequest(api_token, responseListener);

        RequestQueue queue = Volley.newRequestQueue(PerfilActivity.this);

        queue.add(logoutrequest);
    }

    public void goToMenuCRUD ()
    {
        Intent intentMenuCRUD = new Intent(PerfilActivity.this, MenuActivity.class);
        PerfilActivity.this.startActivity(intentMenuCRUD);
    }


    private void goToSolicitudes()
    {
        Intent intentSolicitudes = new Intent(PerfilActivity.this, SolicitudesActivity.class);
        PerfilActivity.this.startActivity(intentSolicitudes);
    }

}
