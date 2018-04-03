package com.example.root.projetfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 14/11/17.
 */

public class AjoutRepa extends AppCompatActivity implements View.OnClickListener{
    EditText qedit;
    Button bajout,bvoir,bur;
    String repa_selected;
    String savedQuant = "";
    double calunite;
    List<String> l = new ArrayList<String>();
    List<String> lcal = new ArrayList<String>();
    List<String> ljusteNom = new ArrayList<String>();
    List<String> lrepas_manger = new ArrayList<String>();
    List<String> llip = new ArrayList<String>();
    List<String> lglu = new ArrayList<String>();
    List<String> lpro = new ArrayList<String>();
    private String authority = "fr.rafik.projet";
    public final static String TABLE_ALIMENT = "aliment";
    public static final String ID = "id";
    public static final String ALIMENTS = "aliments";
    public static final String CALORIES = "calorie";
    public static final String LIPIDES = "lipides";
    public static final String GLUCIDES = "glucides";
    public static final String PROTEINES = "proteines";
    public static final String sharedmodifie = "disco";

    public final static String TABLE_REGIME = "regime";
    public final static String JOUR = "jour";
    public final static String REPA_MANGER = "repas_manger";
    int quantite_selected;
    Spinner spinner;
    ArrayList<Unité> listeRepas= new ArrayList<Unité>();
   // ArrayList<Unité> arraylist = new ArrayList<Unité>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            listeRepas= savedInstanceState.getParcelableArrayList("data");
            savedQuant=savedInstanceState.getString("kantiti");
        }
        setContentView(R.layout.ajout_repa);

        bajout=(Button)findViewById(R.id.bajout);
        bvoir=(Button)findViewById(R.id.bvoir);

        bur=(Button)findViewById(R.id.bur);
        qedit=(EditText) findViewById(R.id.qedit);
        spinner=(Spinner) findViewById(R.id.spinner);
        bajout.setOnClickListener(this);
        bvoir.setOnClickListener(this);
        bur.setOnClickListener(this);

        if(getIntent().getStringExtra("ou").equals("0")){
            //je viens de main
        }else {
            //je viens de assiette
            Bundle bundle = getIntent().getExtras();
            listeRepas= bundle.getParcelableArrayList("dataAssiette");
            bvoir.setVisibility(View.VISIBLE);
        }

        Log.d("aaaaaaaaa ", "rrrrrrrrrrrrr" );
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_ALIMENT);
        String[] columns = new String[]{ID, ALIMENTS, CALORIES, LIPIDES,GLUCIDES,PROTEINES};
        Uri uri = builder.build();
        Log.d("aaaaaaaaa ", "77777777" );
        Cursor ch = getContentResolver().query(uri, columns,null,null,null);
        ch.moveToFirst();
        while (!ch.isAfterLast()) {
            //prends pas en consideration
            Log.d("bbbbbbb",ch.getString(0));//id
            Log.d("cursor1",ch.getString(1));//ali
            Log.d("cursor2",ch.getString(2));//cal
            //Log.d("cursor3",ch.getString(3));//lip
            //Log.d("cursor4",ch.getString(4));//glu
            //Log.d("cursor4",ch.getString(5));//pro
            l.add(ch.getString(1)+" : "+ch.getString(2)+"C");
            lcal.add(ch.getString(2));
            ljusteNom.add(ch.getString(1));
            //llip.add(ch.getString(3));
            //lglu.add(ch.getString(4));
            //lpro.add(ch.getString(5));
            ch.moveToNext();
        }




        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, l);

      /*  adapter = new SimpleCursorAdapter(Quiz.this,
                android.R.layout.simple_spinner_item, null,
                new String[]{"payyyyys"},
                new int[]{android.R.id.text1}, 0);*/

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //listeRepas.add(***);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bajout){
            MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.bclick);
            mp.start();
            repa_selected=spinner.getSelectedItem().toString();
            quantite_selected= Integer.parseInt(qedit.getText().toString());
            Log.i("yyyyyyy",repa_selected+quantite_selected);
            chercherEnergie();

            bvoir.setVisibility(View.VISIBLE);
            qedit.setText("");

        }else if(v.getId()==R.id.bvoir){
            MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.bclick);
            mp.start();
            Intent intent = new Intent(this,Assiette.class);//ou AssietteFragment
            //test
            //*********************************************************
            Bundle bundle = new Bundle();
           // bundle.putParcelable("data", listeRepas);
            //intent.putExtras(bundle);
            //startActivity(intent);

            bundle.putParcelableArrayList("data", listeRepas);
            intent.putExtras(bundle);


           // intent.putExtra("extra",(Serializable) listeRepas);



            //si jétulise le fragment seulement
            //AssietteFragment fragobj = new AssietteFragment();
            //fragobj.setArguments(extra);
            //fin fragment seulement
            //intent.putExtra("listeRepas",listeRepas);
            Log.i("bbabababababababa","bababababababba");
            //*******************************************************************
            finish();
            startActivity(intent);
        }else if(v.getId()==R.id.bur){
            MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.bclick);
            mp.start();
            Intent intent = new Intent(this,MainActivity0.class);
            finish();
            startActivity(intent);
        }

    }

    void chercherEnergie(){
        if(l.contains(repa_selected)){
            int i=0;

            Log.i("ooooooo","ooooooo");
            while(!l.get(i).equals(repa_selected)){
                i++;//avoir la position du repas
            }
            Log.i("uuuuuuuuu", String.valueOf(i));
            //prends pas en consideration
            int intLcal =Integer.parseInt(lcal.get(i));
            Log.i("wwwwwwww", String.valueOf(intLcal));
            calunite= (quantite_selected*intLcal)/1000;
            String construit = ljusteNom.get(i)+" "+calunite+" C";
            Log.i("caluniteeeeeeeeeeeeee", String.valueOf(calunite));
            //int gluunite= (quantite_selected*Integer.parseInt(llip.get(i)))/1000;
            //int lipunite= (quantite_selected*Integer.parseInt(lglu.get(i)))/1000;
            //int prounite= (quantite_selected*Integer.parseInt(lpro.get(i)))/1000;
            //u=new Unité(repa_selected,quantite_selected,calunite,gluunite,lipunite,prounite);
            Unité u=new Unité(construit,quantite_selected,calunite,0,0,0);
            //Unité u=new Unité("mimi",32,88.8,1.2,1.2,1.2);
            listeRepas.add(u);
        }
    }

    @Override
    public void onBackPressed()
    {
        MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.alert);
        mp.start();
       // super.onBackPressed();
        final Intent i = new Intent(this,MainActivity0.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(AjoutRepa.this);
        builder.setMessage("Etes vous sur de ne pas vouloir sauvegarder votre repas ?");
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



    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("data", listeRepas);
        if (!qedit.getText().toString().equals("")){
            outState.putString("kantiti", qedit.getText().toString());
        }else {
            outState.putString("kantiti", "");
        }


        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        qedit.setText(savedQuant);
        bvoir.setVisibility(View.VISIBLE);
    }

}
