package com.login.hofo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegisterActivity extends AppCompatActivity {

    private int sYear, sMonth, sDay;

    Calendar calendar = Calendar.getInstance();

    static final int DATE_ID = 0;

    TextView tv_loginPage;

    EditText etNameUser, etCiudad,  etAniosExperiencia, etBirthUser,  etEmailUser, etPhonelUser, etAddressetPhonelUser, etDescription, etPasswordUser, etConfirmPasswordUser;

    Button btnRegister, etBirthUserBtn, botonImagenPerfil, botonImagenHome;

    ImageView imageVProfile;
    ImageView imageVHome;

    ProgressDialog dialog;

    Context thiscontext = this;

    //captura de fotos
    String photoHUserString = "";
    String photoPUserString = "";
    //Ruta para guardar imagen desde celular
    private final String CARPETA_RAIZ = "misImagenesPregoneros/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ+"misFotos";
    //Guardar ruta de imagen (almacenamiento)
    String path;


    //codigos para diferenciar si se carga una imagen desde galeria o si es tomada desde camara
    private final int COD_GALERIA = 10;
    private final int COD_CAMARA = 20;

    private String slot = "";

    //variables para capturar data de actividad anterior
    String nombre_menu, descripcion_menu;
    int precio_menu, user_id, menu_id, currentModality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //fecha actual para mostrar en calendario
        sMonth = calendar.get(Calendar.MONTH);
        sYear = calendar.get(Calendar.YEAR);
        sDay = calendar.get(Calendar.DAY_OF_MONTH);

        //inicializacion de campos activity
        tv_loginPage = (TextView) findViewById(R.id.tv_goTo_loginPage);

        btnRegister = (Button) findViewById(R.id.user_register);
        botonImagenPerfil = (Button) findViewById(R.id.addImagePerfil);
        botonImagenHome = (Button) findViewById(R.id.addImageHome);

        etNameUser = (EditText) findViewById(R.id.user_name);
        etCiudad = (EditText) findViewById(R.id.user_city);
        etAniosExperiencia = (EditText) findViewById(R.id.user_age_experience);
        etBirthUser = (EditText) findViewById(R.id.user_birthdate);
        etBirthUserBtn = (Button) findViewById(R.id.user_birthdate_btn);
        etEmailUser = (EditText) findViewById(R.id.user_email);
        etAddressetPhonelUser = (EditText) findViewById(R.id.user_address);
        etDescription = (EditText) findViewById(R.id.user_description);
        etPhonelUser = (EditText) findViewById(R.id.user_phone);
        etPasswordUser = (EditText) findViewById(R.id.user_password);
        etConfirmPasswordUser = (EditText) findViewById(R.id.user_confirmPassword);

        imageVProfile = (ImageView) findViewById(R.id.viewPerfil);
        imageVHome = (ImageView) findViewById(R.id.viewHogar);

        //deshabilitando texto de fecha
        etBirthUser.setEnabled(false);

        //validando si es un registro de cocinero o de comensal
        //data de activity anterior
        Intent intent = getIntent();
        nombre_menu = intent.getStringExtra("nombre_menu");
        descripcion_menu = intent.getStringExtra("descripcion_menu");
        precio_menu = intent.getIntExtra("precio_menu",0);
        user_id = intent.getIntExtra("user_id",0);
        menu_id = intent.getIntExtra("menu_id",0);
        currentModality = intent.getIntExtra("currentModality",0);

        if (currentModality != 0) //si llega de una actividad con modalidad (como menuActivity)
        {
            ocultandoCamposParaRegistroComensal();
        }

        //validando permisos
        if (validaPermisos())
        {
            botonImagenPerfil.setEnabled(true);
            botonImagenHome.setEnabled(true);
            btnRegister.setEnabled(true);
        }
        else
        {
            Toast errorPermisionMessage = Toast.makeText(getApplicationContext(),"Para que la aplicacion funcione necesitamos que aceptes todos los permisos", Toast.LENGTH_LONG);
            errorPermisionMessage.show();

            botonImagenPerfil.setEnabled(true);
            botonImagenHome.setEnabled(true);
            btnRegister.setEnabled(false);
        }

        //al dar click en calendario
        etBirthUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_ID);
            }
        });


        //Click en ir a login
        tv_loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentModality != 0) //si llegamos al registro por  un menu
                {
                    // Redirigiendo a LoginActivity
                    Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    intentLogin.putExtra("nombre_menu", nombre_menu);
                    intentLogin.putExtra("descripcion_menu", descripcion_menu);
                    intentLogin.putExtra("precio_menu", precio_menu);
                    intentLogin.putExtra("user_id", user_id);
                    intentLogin.putExtra("menu_id", menu_id);
                    intentLogin.putExtra("currentModality", currentModality);
                    RegisterActivity.this.startActivity(intentLogin);
                }
                else
                {
                    // Redirigiendo a LoginActivity
                    Intent intentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(intentLogin);
                }

            }
        });

        //click en registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setTitle("Registrando Cocinero");
                dialog.setMessage("Cargando...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
                dialog.setCancelable(false);

                final String nameUser = etNameUser.getText().toString().trim();
                final String birthdateUser = etBirthUser.getText().toString().trim();
                final String emailUser = etEmailUser.getText().toString().trim();
                final String phoneUser = etPhonelUser.getText().toString().trim();
                final String addressUser = etAddressetPhonelUser.getText().toString().trim();
                final String descriptionUser = etDescription.getText().toString().trim();
                final String cityUser = etCiudad.getText().toString().trim();
                final String ageExperienceUser = etAniosExperiencia.getText().toString().trim();
                final String photoHUser = photoHUserString;
                final String photoPUser = photoPUserString;
                final String FMCToken = FirebaseInstanceId.getInstance().getToken();

                String role_id = "";

                if (currentModality != 0)
                {
                    role_id = "2";
                }
                else
                {
                    role_id = "1";
                }

                final String roleUser = role_id;

                final String passUser = etPasswordUser.getText().toString().trim();
                final String passConfirmUser = etConfirmPasswordUser.getText().toString().trim();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);

                            String code = jsonResponse.getString("code");

                            if (code.equals("201"))
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

                                dialog.dismiss();

                                if (currentModality != 0) //si llegamos al registro por  un menu
                                {
                                    goToFormularioDePedido();
                                }
                                else
                                {
                                    // Redirigiendo a perfil de usuario
                                    Intent intentRegister = new Intent(RegisterActivity.this, PerfilActivity.class);
                                    intentRegister.putExtra("username", username);
                                    RegisterActivity.this.startActivity(intentRegister);
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
                        }

                    }
                };

                RegisterRequest registerrequest = new RegisterRequest(nameUser, emailUser, birthdateUser, phoneUser, addressUser, descriptionUser, cityUser, ageExperienceUser, photoHUser, photoPUser, roleUser,passUser, passConfirmUser, FMCToken, responseListener);

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

                queue.add(registerrequest);

            }
        });

    }

    private void ocultandoCamposParaRegistroComensal()
    {
        botonImagenHome.setVisibility(View.GONE);
        etAniosExperiencia.setVisibility(View.GONE);
        etDescription.setVisibility(View.GONE);
        imageVHome.setVisibility(View.GONE);
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

                    Toast.makeText(RegisterActivity.this, year+"-"+(month+1)+"-"+dayOfMonth+"", Toast.LENGTH_LONG).show();

                    etBirthUser.setText(year+"-"+(month+1)+"-"+dayOfMonth+"");

                }
            };


    public boolean validaPermisos ()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)  && (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED))
            return true;


        if ((shouldShowRequestPermissionRationale(CAMERA))  ||  (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))  )
            cargarDialogoRecomendacion();
        else
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);


        return false;

    }

    //Se le dice al usuario que tiene que habilitar permisos para poder usar la aplicacion
    public void cargarDialogoRecomendacion ()
    {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(RegisterActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la aplicacion");
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });

        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100)  //pregunta nuevamente por los permisos
        {
            if (grantResults.length == 2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED)
            {
                botonImagenPerfil.setEnabled(true);
                botonImagenHome.setEnabled(true);
                btnRegister.setEnabled(true);
            }
            else
            {
                solicitarPermisosManual();
            }
        }

    }

    public void solicitarPermisosManual ()
    {
        final CharSequence[] opciones = {"SI","NO"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegisterActivity.this);
        alertOpciones.setTitle("¿Prefiere dar permisos de forma manual?");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("SI"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                else
                {
                    Toast alertPermisos = Toast.makeText(getApplication(),"Necesitamos dichos permisos para el funcionamiento de la aplicacion",Toast.LENGTH_SHORT);
                    alertPermisos.setGravity(Gravity.CENTER, 0, 0);
                    alertPermisos.show();
                    dialog.dismiss();

                    goToMainActivity();
                }
            }
        });

        alertOpciones.show();
    }

    public  void goToMainActivity ()
    {
        Intent intentMain = new Intent(RegisterActivity.this, MainActivity.class);
        RegisterActivity.this.startActivity(intentMain);
    }

    //Tomando foto
    public void uploadImagePerfil (View view)
    {
        slot = "perfil";
        cargarImagen();
    }
    public void uploadImageHogar (View view)
    {
        slot = "hogar";
        cargarImagen();
    }

    @SuppressLint("IntentReset")
    public void cargarImagen()
    {

        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen","Cancelar"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegisterActivity.this);
        alertOpciones.setTitle("seleccione una opcion");

        //Seteando las opciones
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Tomar foto"))
                {
                    tomarFotografia();
                }
                else if (opciones[which].equals("Cargar imagen"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //ACTION_PICK O ACTION_GET_CONTENT

                    intent.setType("image/");

                    startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"),COD_GALERIA); // va al metodo onActivityResult
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        alertOpciones.show();

    }

    public void tomarFotografia()
    {
        //Abre la imagen
        File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);

        String nombre_imagen = "";

        //valida si la imagen se crecorrectamente
        boolean isCreada = fileImagen.exists();


        if (!isCreada)
        {
            isCreada = fileImagen.mkdirs();
        }

        if (isCreada)
        {
            nombre_imagen = (System.currentTimeMillis()/1000)+".jpg";
        }


        //ruta de almacenamiento
        path = Environment.getExternalStorageDirectory()+ File.separator+RUTA_IMAGEN+File.separator+nombre_imagen;

        //Creando archivo
        File imagen = new File(path);


        //Lanzar la aplicacion de camara
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }


        startActivityForResult(intent,COD_CAMARA);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Bitmap bitmap;
            Bitmap bitmapVideo;

            switch (requestCode)
            {
                case COD_GALERIA:

                    Uri miPath = data.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                        bitmap = redimencionarImagen(bitmap, 300,400);


                        if (slot.equals("perfil"))
                        {
                            imageVProfile.setImageBitmap(bitmap);
                            photoPUserString = convertFileToString(bitmap);
                        }

                        if (slot.equals("hogar"))
                        {
                            imageVHome.setImageBitmap(bitmap);
                            photoHUserString = convertFileToString(bitmap);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;


                case  COD_CAMARA:

                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override //si el proceso termino completamente
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path: "+path);
                        }
                    });

                    bitmap = BitmapFactory.decodeFile(path);
                    bitmap = redimencionarImagen(bitmap, 300,400);


                    if (slot.equals("perfil"))
                    {
                        imageVProfile.setImageBitmap(bitmap);
                        photoPUserString = convertFileToString(bitmap);
                    }

                    if (slot.equals("hogar"))
                    {
                        imageVHome.setImageBitmap(bitmap);
                        photoHUserString = convertFileToString(bitmap);
                    }

                    break;

                default:
                    Toast.makeText(getApplication(),"Otro contactese con servicio tecnico",Toast.LENGTH_SHORT).show();


            }

        }
        else
        {
            Toast.makeText(getApplication(),"Error en la aplicacion porfavor contacte al servicio tecnico",Toast.LENGTH_SHORT).show();
        }
    }

    public String convertFileToString (Bitmap bitmapFile)
    {
        String fileString = "";

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmapFile.compress(Bitmap.CompressFormat.JPEG,100,array);

        byte[] fileByte = array.toByteArray();
        fileString = Base64.encodeToString(fileByte,Base64.DEFAULT);

        Toast.makeText(getApplication(),fileString,Toast.LENGTH_SHORT).show();

        return fileString;
    }

    public Bitmap redimencionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo)
    {

        int ancho=bitmap.getWidth();

        int alto=bitmap.getHeight();

        if (ancho >anchoNuevo || alto>altoNuevo)
        {
            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto =altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }

        return bitmap;

    }

    private void goToFormularioDePedido()
    {

        // Redirigiendo a formulario de menu
        Intent intentFormMenu = new Intent(RegisterActivity.this, DomiciliosFormActivity.class);
        intentFormMenu.putExtra("nombre_menu", nombre_menu);
        intentFormMenu.putExtra("descripcion_menu", descripcion_menu);
        intentFormMenu.putExtra("precio_menu", precio_menu);
        intentFormMenu.putExtra("user_id", user_id);
        intentFormMenu.putExtra("menu_id", menu_id);
        intentFormMenu.putExtra("currentModality", currentModality);
        RegisterActivity.this.startActivity(intentFormMenu);
    }

}
