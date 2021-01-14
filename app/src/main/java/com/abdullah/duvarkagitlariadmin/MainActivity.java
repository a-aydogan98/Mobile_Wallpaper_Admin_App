package com.abdullah.duvarkagitlariadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar_main;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isConnected()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setIcon(R.drawable.internet);
            alertDialog.setTitle("İnternet Erişimi Uyarısı");
            alertDialog.setMessage("Lütfen internet bağlantınızı kontrol ediniz.");

            alertDialog.setPositiveButton("Kapat", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        toolbar_main = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar_main, 0, 0);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View baslik = navigationView.inflateHeaderView(R.layout.navigation_baslik);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_renkler) {

            Intent intent = new Intent(MainActivity.this, RenklerActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_hayvanlar) {

            Intent intent = new Intent(MainActivity.this, HayvanlarActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_siyah) {

            Intent intent = new Intent(MainActivity.this, SiyahActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_diger) {

            Intent intent = new Intent(MainActivity.this, DigerActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}