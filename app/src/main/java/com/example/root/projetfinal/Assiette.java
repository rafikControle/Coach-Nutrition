package com.example.root.projetfinal;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by root on 14/11/17.
 */

public class Assiette extends ListActivity implements View.OnClickListener{

    ArrayList<Unité> listeR= new ArrayList<Unité>();
    Button vrep;
    String itemAliment;
    String itemQuantite;

    double totalCalToday=0;
    String calToday;
    String repToday="";
    String a;
    private String authority = "fr.rafik.projet";
    public final static String TABLE_ALIMENT = "aliment";
    public static final String ID = "id";
    public static final String ALIMENTS = "aliments";
    public static final String CALORIES = "calorie";
    public static final String LIPIDES = "lipides";
    public static final String GLUCIDES = "glucides";
    public static final String PROTEINES = "proteines";

    ArrayList<Double> listecal = new ArrayList<Double>();
    ArrayList<String> listeNom = new ArrayList<String>();
    public final static String TABLE_REGIME = "regime";
    public final static String JOUR = "jour";
    public final static String REPA_MANGER = "repas_manger";
    ArrayAdapter<String> adapter;
    String jour;
    Intent i;
    Button butoon2;
    TextView textView14;
    //  ArrayList<String> listeRepas;//ke je vais avoir avec intent putstring..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            listeR= savedInstanceState.getParcelableArrayList("data");
        }else{
            Bundle bundle = getIntent().getExtras();
            listeR= bundle.getParcelableArrayList("data");
        }
        setContentView(R.layout.assiete);
        textView14=(TextView)findViewById(R.id.textView14);
        vrep= (Button)findViewById(R.id.vrep);
        butoon2= (Button)findViewById(R.id.butoon2);
        vrep.setOnClickListener(this);
        butoon2.setOnClickListener(this);
        i = new Intent(this,MainActivity.class);

        getNomAl(listeR);
        getCalAl(listeR);

        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeNom);

        setListAdapter(adapter);
        updateTotal();

    }

    public void updateTotal(){
        textView14.setText("Total: "+String.valueOf(getTotalCalToday(listecal))+"C");
    }

    protected void onListItemClick(ListView l, View v, final int position, final long id) {
        //ouvrire une fenetre alert dialog avec bouton ok et button supprimer
        String x = getListView().getItemAtPosition(position).toString();
        String[] parts = x.split(" ");
        itemAliment = parts[0];
        itemQuantite = parts[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(Assiette.this);
       builder.setMessage("vous avez : " + "\n" +itemAliment+"\n" +"quantité : "+ itemQuantite+" C");
        builder.setPositiveButton("Supp", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //clique supprimé unité de lassiette
                //choisir
                //suppressionItemFromListe(itemAliment,position,1);
                //ou
                suppressionItemFromListe(itemAliment,position,2);

            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.vrep){
            MediaPlayer mp = MediaPlayer.create(Assiette.this,R.raw.bclick);
            mp.start();
            //valider l'assiette faut enregistrer les calorie lipide de tous ce qui a dans lassiete... dans bdd
            jour = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            validationAssiette();
        }else if (v.getId() == R.id.butoon2){
            MediaPlayer mp = MediaPlayer.create(Assiette.this,R.raw.bclick);
            mp.start();
            Intent intent = new Intent(this,AjoutRepa.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("dataAssiette", listeR);
            intent.putExtras(bundle);
            intent.putExtra("ou","1");
           // intent.putExtra("extra",(Serializable) listeRepas);
            Log.i("bbabababababababa","bababababababba");
            finish();
            startActivity(intent);
        }
    }

    void validationAssiette(){
        double compteurCal=0;

        compteurCal = getTotalCalToday(listecal);
        Log.i("calories today ::::", String.valueOf(compteurCal));
            ContentValues values = new ContentValues();
            Calendar calendar = Calendar.getInstance();
            values.put(JOUR,String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));//get date

            if (!jourExiste ()){//nouveau jour nouvelle ligne
            values.put(CALORIES,compteurCal);//quantité de calories consommer
            values.put(REPA_MANGER,repToday);//tous les repas consommer
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(authority).appendPath(TABLE_REGIME);
            Uri uri = builder.build();
            uri = getContentResolver().insert(uri,values);
            long id = ContentUris.parseId(uri);}
            else {//jour existe deja alor prendre les calorie de ces jour et juste modifier les caloris
                ContentValues val = new ContentValues();
                int clt = Integer.valueOf(calToday);
                clt += compteurCal;
                calToday = String.valueOf(clt);
                val.put(CALORIES,calToday);//quantité de calories consommer
                val.put(REPA_MANGER,repToday);//tous les repas consommer
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("content").authority(authority).appendPath(TABLE_REGIME);
                Uri uri = builder.build();

                int oo = getContentResolver().update(uri, val,JOUR+"='"+jour+"'",null);

            }
            Intent in = new Intent(this,MainActivity.class);
            finish();
            startActivity(in);

    }


    boolean jourExiste (){
        int i=0;

        while (i<listeNom.size()){//enregistrer les repas dans un string
            repToday =repToday+listeNom.get(i)+" , ";
            i++;
        }
        //verifie si il a deja manger aujourdhui ou pas
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_REGIME);
        String[] columns = new String[]{ID, JOUR, CALORIES, REPA_MANGER,LIPIDES,GLUCIDES,PROTEINES};
        Uri uri = builder.build();
        Cursor ch = getContentResolver().query(uri, columns,null,null,null);
        ch.moveToFirst();

        while (!ch.isAfterLast()) {

            if (jour.equals(ch.getString(1))){//jai deja manger today
                calToday = ch.getString(2);
                repToday = ch.getString(3)+repToday;
                Log.d("calo today deja consomé",calToday);
                ch.close();
                return true ;
            }//le joure éxiste
            ch.moveToNext();
        }
        ch.close();
        return false ;//le jour n'éxiste pas

    }


    public void getNomAl(ArrayList<Unité>liste){

        for (int i=0;i<liste.size();i++){
            listeNom.add(liste.get(i).getNom_aliment());
        }

    }

    public void getCalAl(ArrayList<Unité>liste){

        for (int i=0;i<liste.size();i++){
            listecal.add(liste.get(i).getCalQuantité());
        }

    }

    public  double getTotalCalToday(ArrayList<Double> liscal){
        totalCalToday=0;
        for (int i=0;i<liscal.size();i++){
            totalCalToday += liscal.get(i);
        }
        Log.i("gettotalcaltoday",String.valueOf(totalCalToday));
        return totalCalToday;
    }

    public void suppressionItemFromListe(String aliment_supprimer,int position,int methode){
        if (methode == 1){
        // methode 1 recherche
        for (int i =0;i<listeNom.size();i++){
            if (aliment_supprimer.equals(listeNom.get(i))){
                listeNom.remove(i);
                listecal.remove(i);
                listeR.remove(position);
                adapter.notifyDataSetChanged();
                if (listeNom.isEmpty()){
                    Intent intent = new Intent(this,MainActivity.class);
                    finish();
                    startActivity(intent);
                }
                updateTotal();
            }
        }}else if(methode == 2) {
            //methode 2 utiliser la position direct
            listeNom.remove(position);
            listecal.remove(position);
            listeR.remove(position);
            Log.i("listcalsize",String.valueOf(listecal.size()));
            adapter.notifyDataSetChanged();
            if (listeNom.isEmpty()){
                Intent intent = new Intent(this,MainActivity.class);
                finish();
                startActivity(intent);
            }
            updateTotal();
        }
    }

    @Override
    public void onBackPressed()
    {
        MediaPlayer mp = MediaPlayer.create(Assiette.this,R.raw.alert);
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(Assiette.this);
        builder.setMessage("Etes vous sur de ne pas vouloir sauvegarder votre repas ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MediaPlayer mp = MediaPlayer.create(Assiette.this,R.raw.bclick);
                mp.start();
                finish();
                startActivity(i);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MediaPlayer mp = MediaPlayer.create(Assiette.this,R.raw.bclick);
                mp.start();
                dialog.cancel();
            }
        });
        builder.show();

    }


    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("data", listeR);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

}
