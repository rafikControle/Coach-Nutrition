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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by root on 14/11/17.
 */

public class Assiette extends ListActivity implements View.OnClickListener{

    ArrayList<Unité> listeR= new ArrayList<Unité>();
    Button vrep;
    Cursor cur;
    String itemAliment;
    String itemQuantite;

    double totalCalToday=0;
    int q;
    double qc,qg,qp,ql;
    String calToday;
    String repToday="";
    String a;
    Unité u;
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
        i = new Intent(this,MainActivity0.class);
        Log.i("tttttt","000000000000");



        //Bundle extra = getIntent().getBundleExtra("extra");
        Log.i("tttttt","111111111111");
      //  listeR = (ArrayList<Unité>) getIntent().getSerializableExtra("extra");
       // listeR = (ArrayList<Unité>) extra.getSerializable("objects");
    //    Log.i("tttttt","22222222222");
       // listeR=(ArrayList<Unité>)getIntent().getSerializableExtra("listeRepas");
       // listeR=(ArrayList<Unité>)getIntent().getParcelableExtra("listeRepas");

        Log.i("liste rrrr", String.valueOf(listeR.get(0).getNom_aliment()));
        getNomAl(listeR);
        getCalAl(listeR);
       // Log.i("liste rrrr", String.valueOf(listeR.get(0)));
        /*
        ListAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item, cur,
                // Array of cursor columns to bind to:
                new String[]{Base.PAYS, Base.CAPITALE},
                // Parallel array of which template objects to bind to those columns.
                new int[]{android.R.id.text1, android.R.id.text2});
        // Bind to our new adapter.

        setListAdapter(adapter);*/
        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeNom);

      /*  adapter = new SimpleCursorAdapter(Quiz.this,
                android.R.layout.simple_spinner_item, null,
                new String[]{"payyyyys"},
                new int[]{android.R.id.text1}, 0);*/

        setListAdapter(adapter);
        updateTotal();

    }

    public void updateTotal(){
        textView14.setText("Total: "+String.valueOf(getTotalCalToday(listecal))+"C");
    }

    protected void onListItemClick(ListView l, View v, final int position, final long id) {
        //ouvrire une fenetre alert dialog avec bouton ok et button supprimer
        String x = getListView().getItemAtPosition(position).toString();
        Log.i("xxxxxxxxxx",x);
        String[] parts = x.split(" ");
        itemAliment = parts[0];
        Log.i("yyyyyyyyy",itemAliment);
        itemQuantite = parts[1];
        Log.i("ttttttttt",itemQuantite);
        //itemQuantite += parts[3];
      //  Unité item= (Unité) getListView().getSelectedItem();
       // String itemAliment=item.getNom_aliment();
      //  int itemQuantite=item.getQuantité();
        AlertDialog.Builder builder = new AlertDialog.Builder(Assiette.this);
       builder.setMessage("vous avez : " + "\n" +itemAliment+"\n" +"quantité : "+ itemQuantite+" C");
        builder.setPositiveButton("Supp", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //clique supprimé unité de lassiette

                Log.d("aaaaaaaaa ", String.valueOf(position));
                Log.d("aaaaaaaaa ", String.valueOf(id));
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
       /* for(int i=0;i<listeR.size();i++) {// a modifier
            u = listeR.get(i);
            a = u.getNom_aliment();
            q = u.getQuantité();
            qc = u.getCalQuantité();
            //  qg=u.getGluQuantité();
            //  ql=u.getLipQuantité();
            //  qp=u.getProQuantité();
            compteurCal += qc;
            Log.i("calories today ::::", String.valueOf(compteurCal));
        }*/
        compteurCal = getTotalCalToday(listecal);
        Log.i("calories today ::::", String.valueOf(compteurCal));
            ContentValues values = new ContentValues();
            Calendar calendar = Calendar.getInstance();
            values.put(JOUR,String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+"/"+String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));//get date
           // values.put(JOUR, String.valueOf(calendar.getTime()));//get date

          //  values.put(LIPIDES,ql);
          //  values.put(GLUCIDES,qg);
          //  values.put(PROTEINES,qp);
            //verifi si le jour éxiste deja ou pas
            Log.i("ghoulaaaaaaa","1111111111111");
            if (!jourExiste ()){//nouveau jour nouvelle ligne
                Log.i("ghoulaaaaaaa","22222222222222");
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
               // String[] columns = new String[]{ID, JOUR, CALORIES, LIPIDES,GLUCIDES,PROTEINES};
               // Cursor c = bd.query(GEO, columns, "pays!='"+null+"'", null, null, null,null);
               // Cursor ch = getContentResolver()

                int oo = getContentResolver().update(uri, val,JOUR+"='"+jour+"'",null);
                //uri = getContentResolver().query(uri, columns,JOUR+"='"+calToday+"'", null, null);
                //long id = ContentUris.parseId(uri);
            }
            Intent in = new Intent(this,MainActivity0.class);
            finish();
            startActivity(in);

    }


    boolean jourExiste (){
        int i=0;
        Log.d("aaaaaaaaa ", "00000000000" );
        Log.d("bbbbbbbb ", String.valueOf(listeNom.size()));
        Log.d("dddddddd ", listeNom.get(0));
        Log.d("cccccccccc ", "ooooo"+repToday);
        Log.d("aaaaaaaaa ", "55555555" );
        while (i<listeNom.size()){//enregistrer les repas dans un string
            Log.d("eeeeeeee ", "88888888888" );
            repToday =repToday+listeNom.get(i)+" , ";
            Log.d("eeeeeeeeee ", "99999999999" );
            i++;
        }
        //verifie si il a deja manger aujourdhui ou pas
        Log.d("aaaaaaaaa ", "111111111111" );
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content").authority(authority).appendPath(TABLE_REGIME);
        Log.d("aaaaaaaaa ", "222222222222" );
        String[] columns = new String[]{ID, JOUR, CALORIES, REPA_MANGER,LIPIDES,GLUCIDES,PROTEINES};
        Uri uri = builder.build();
        Log.d("aaaaaaaaa ", "333333333333" );
        Cursor ch = getContentResolver().query(uri, columns,null,null,null);
        ch.moveToFirst();

        while (!ch.isAfterLast()) {
            Log.d("bbbbbbb",ch.getString(0));//id
            Log.d("cursor1",ch.getString(1));//jour
            Log.d("cursor2",ch.getString(2));//cal
            //Log.d("cursor3",ch.getString(3));//lip
            //Log.d("cursor4",ch.getString(4));//glu
            //Log.d("cursor4",ch.getString(5));//pro
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
                    Intent intent = new Intent(this,MainActivity0.class);
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
                Intent intent = new Intent(this,MainActivity0.class);
                finish();
                startActivity(intent);
            }
            updateTotal();
        }
    }

    @Override
    public void onBackPressed()
    {
       // super.onBackPressed();
        //Intent i = new Intent(this,MainActivity0.class);
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
