package com.login.hofo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*SABER FMCTOKEN FIREBASE*/

        //Log.d("FCMToken", "token "+ FirebaseInstanceId.getInstance().getToken());



    }

    public void goToLogin (View view)
    {
        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intentLogin);
    }

    public void goToModalities(View view)
    {
        Intent intent = new Intent(MainActivity.this,ModalitiesActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
