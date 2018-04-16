package com.example.root.projetfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_ALIMENT);
        String[] columns = new String[]{ID, ALIMENTS, CALORIES, LIPIDES,GLUCIDES,PROTEINES};
        Uri uri = builder.build();
        Cursor ch = getContentResolver().query(uri, columns,null,null,null);
        ch.moveToFirst();
        while (!ch.isAfterLast()) {
            //prends pas en consideration

            l.add(ch.getString(1)+" : "+ch.getString(2)+"C");
            lcal.add(ch.getString(2));
            ljusteNom.add(ch.getString(1));

            ch.moveToNext();
        }

        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, l);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
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
            chercherEnergie();

            bvoir.setVisibility(View.VISIBLE);
            qedit.setText("");

        }else if(v.getId()==R.id.bvoir){
            MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.bclick);
            mp.start();
            Intent intent = new Intent(this,Assiette.class);//ou AssietteFragment

            Bundle bundle = new Bundle();

            bundle.putParcelableArrayList("data", listeRepas);
            intent.putExtras(bundle);

            finish();
            startActivity(intent);
        }else if(v.getId()==R.id.bur){
            MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.bclick);
            mp.start();
            Intent intent = new Intent(this,MainActivity.class);
            finish();
            startActivity(intent);
        }

    }

    void chercherEnergie(){
        if(l.contains(repa_selected)){
            int i=0;
            while(!l.get(i).equals(repa_selected)){
                i++;//avoir la position du repas
            }
            //prends pas en consideration
            int intLcal =Integer.parseInt(lcal.get(i));
            calunite= (quantite_selected*intLcal)/1000;
            String construit = ljusteNom.get(i)+" "+calunite+" C";

            Unité u=new Unité(construit,quantite_selected,calunite,0,0,0);
            listeRepas.add(u);
        }
    }

    @Override
    public void onBackPressed()
    {
        MediaPlayer mp = MediaPlayer.create(AjoutRepa.this,R.raw.alert);
        mp.start();
        final Intent i = new Intent(this,MainActivity.class);

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
