package com.example.root.projetfinal;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 14/11/17.
 */

public class AjoutAliment extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{
    EditText nali,ncal;
    Button badd;
    Button breto;
    String nom_ali,nombre_cal;
    List<String> listeDeja = new ArrayList<String>();
    public static final String sharedmodifie = "disco";
    private String authority = "fr.rafik.projet";
    public final static String TABLE_ALIMENT = "aliment";
    public static final String ID = "id";
    public static final String ALIMENTS = "aliments";
    public static final String CALORIES = "calorie";
    public static final String LIPIDES = "lipides";
    public static final String GLUCIDES = "glucides";
    public static final String PROTEINES = "proteines";


    public final static String TABLE_REGIME = "regime";
    public final static String JOUR = "jour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajout_aliment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nali=(EditText)findViewById(R.id.nali);
        ncal=(EditText)findViewById(R.id.ncal);
        badd=(Button) findViewById(R.id.badd);
        breto=(Button) findViewById(R.id.breto);

        badd.setOnClickListener(this);
        breto.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.badd){
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            nom_ali=nali.getText().toString();
            nombre_cal=ncal.getText().toString();
            if (!nom_ali.isEmpty() && !nombre_cal.isEmpty()){
            antiDuplication();
            }else {
                Toast.makeText(getApplicationContext(), "Information vide",
                        Toast.LENGTH_LONG).show();
            }
        }else if (v.getId()==R.id.breto){
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            Intent intent = new Intent(this,MainActivity0.class);
            finish();
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed()
    {
        MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.alert);
        mp.start();
        //super.onBackPressed();
        final Intent i = new Intent(this,MainActivity0.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(AjoutAliment.this);
        builder.setMessage("Quitter l'activité ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                startActivity(i);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nmain) {
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            //retour vers mais
            Intent intent = new Intent(this,MainActivity0.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.nobj) {
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            //modifier objectif regime
            Intent intent = new Intent(this,Objectif.class);
            //******************************************************
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(sharedmodifie,"0");
            String didi=prefs.getString(sharedmodifie,null);
            Log.i("naaaaaaaaaaaaaaaame",didi);
            editor.apply();

            finish();
            startActivity(intent);
        } else if (id == R.id.naja) {
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            //ajouter aliment
            Intent intent = new Intent(this,AjoutAliment.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.najr) {
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            //ajouter repas aujourdhui
            Intent intent = new Intent(this,AjoutRepa.class);
            intent.putExtra("ou","0");
            finish();
            startActivity(intent);
        } else if (id == R.id.ncon2) {
            MediaPlayer mp = MediaPlayer.create(AjoutAliment.this,R.raw.bclick);
            mp.start();
            //consulter regime
            Intent intent = new Intent(this,Consultation2.class);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void antiDuplication(){
        Log.d("aaaaaaaaa ", "rrrrrrrrrrrrr" );
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_ALIMENT);
        String[] columns = new String[]{ID, ALIMENTS, CALORIES, LIPIDES,GLUCIDES,PROTEINES};
        Uri uri = builder.build();
        Log.d("aaaaaaaaa ", "77777777" );
        Cursor ch = getContentResolver().query(uri, columns,null,null,null);
        ch.moveToFirst();
        while (!ch.isAfterLast()) {
            Log.d("bbbbbbb",ch.getString(0));//id
            Log.d("cursor1",ch.getString(1));//ali
            Log.d("cursor2",ch.getString(2));//cal
            // Log.d("cursor3",ch.getString(3));//lip
            // Log.d("cursor4",ch.getString(4));//glu
            // Log.d("cursor4",ch.getString(5));//pro

            listeDeja.add(ch.getString(1));
            ch.moveToNext();
        }


            //  List scoreList = read();
            read();



    }



    public void read(){


                if (!listeDeja.contains(nom_ali)) {
                    //ajout dans la bdd l'aliment
                    ContentValues values = new ContentValues();
                    values.put(ALIMENTS,nom_ali);
                    values.put(CALORIES,nombre_cal);

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(authority).appendPath(TABLE_ALIMENT);
                    Uri uri = builder.build();
                    uri = getContentResolver().insert(uri,values);
                    long id = ContentUris.parseId(uri);
                    Log.d("aaaaaaaaa ", String.valueOf(id));
                    nali.getText().clear();
                    ncal.getText().clear();
                }else {
                    Toast.makeText(getApplicationContext(), "Aliment éxiste deja",
                            Toast.LENGTH_LONG).show();
                }

        }



        //return resultList;


}
