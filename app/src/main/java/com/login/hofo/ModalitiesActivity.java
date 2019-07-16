package com.login.hofo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ModalitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modalities);
    }

    public void goToDomicilios(View view) {
        goToMenuList(1);
    }

    public void goToChefEnCasa(View view) {
        goToMenuList(2);
    }

    public void goToEnCasaDeChef(View view) {
        goToMenuList(3);
    }

    public void goToMenuList(int modalityId)
    {
        Intent intent = new Intent(ModalitiesActivity.this,MenuActivity.class);
        intent.putExtra("idModality",modalityId);
        ModalitiesActivity.this.startActivity(intent);
    }
}
