package com.example.root.projetfinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity0 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sf;
    TextView textView13;
    public static final String pref = "pref";
    public static final String sharednom = "objectif";
    public static final String sharedmincal = "mincal";
    public static final String sharedmaxcal = "maxcal";
    public static final String sharedmaxpro = "maxpro";
    public static final String sharedminpro = "minpro";
    public static final String sharedmaxglu = "maxglu";
    public static final String sharedminglu = "minglu";
    public static final String sharedmaxlip = "maxlip";
    public static final String sharedminlip = "minlip";
    public static final String sharedmodifie = "disco";
    List<String> listeDeja = new ArrayList<String>();
    private String authority = "fr.rafik.projet";
    public final static String TABLE_ALIMENT = "aliment";
    public static final String ID = "id";
    public static final String ALIMENTS = "aliments";
    public static final String CALORIES = "calorie";
    public static final String LIPIDES = "lipides";
    public static final String GLUCIDES = "glucides";
    public static final String PROTEINES = "proteines";
    SharedPreferences prefs;

    private static String url_all_al = "http://10.0.2.2/al_all.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TAXI = "taxi";
    private static final String TAG_TID = "tid";
    private static final String TAG_CLIENT = "client";
    private static final String TAG_CODE = "code";
    private static final String TAG_ID = "id";
    JSONArray alim = null;
    TextView tvc,tvl,tvglu,tvpro;
    ProgressDialog pDialog;
    InputStream inputStream;
    //List<String> resultList = new ArrayList<String>();
    List resultList = new ArrayList();
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    int random;

            /*"Cibler vos objectifs avec justesse","Choisissez le bon moment",
    "Identifiez vos erreurs","Trouvez la méthode adaptée","Restez dans le droit chemin","Bétonnez la stabilisation"
    ,"Changez durablement vos habitudes","Vérifiez, et rectifiez à la moindre alerte",
    "Bougez !","Se fixer un objectif raisonnable : ne pas vouloir perdre plus de 5 kilos en un mois",
    "Faire un choix dans ses aliments","Redécouvrir des aliments que vous aviez oublié",
    "Augmenter les portions de fruits et légumes","Prendre un véritable petit déjeuner",
    "Faire ses courses en ayant mangé avant","Eviter le grignotage entre les repas",
    "Avoir toujours dans son frigidaire","Ne pas sauter de repas","Apprendre à ne pas se resservir","Prendre une collation",
    "Boire suffisament","Ne pas oublier l’activité sportive","Prendre soin de vous",
    "Apprenez à gérer les moments à risque","Reprenez le chemin du marché","Équipez-vous d'ustensiles de cuisine adaptés",
    "À table, ouvrez l'éventail de ses goûts","Mangez 4 fois par jour","Mangez lentement, en sa-vou-rant",
    "Buvez régulièrement"};*/

    PieChart pieChart;
    List<String> conseil;
    int min ;
    int max ;
    float pmin,pmax;
    String minc,maxc;
    int rot =0;
    int maxa;
    Random r = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            random=savedInstanceState.getInt("random");
            conseil = Arrays.asList(getResources().getStringArray(R.array.list_conseil));
            rot=1;
        }else{//pour quil ne la fasse pas 2 fois
           rot=0;
        }
        setContentView(R.layout.activity_main0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textView13 = (TextView) findViewById(R.id.textView13);
        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        if (rot == 0){
            conseil = Arrays.asList(getResources().getStringArray(R.array.list_conseil));
            maxa = conseil.size();
            random = r.nextInt((maxa - 0) + 1);
            textView13.setText(conseil.get(random));
        }


        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.contains(sharedmincal)) {
            minc = prefs.getString(sharedmincal, null);
            maxc = prefs.getString(sharedmaxcal, null);
            Log.i("maxcalconsult",maxc);
            Log.i("mincalconsult",minc);
            min=Integer.valueOf(minc);
            max=Integer.valueOf(maxc);
            pmin=min*100/(min+max);
            pmax=max*100/(min+max);
            Log.d("pppppppppppppmin", String.valueOf(pmin));
            Log.d("pppppppppppppmax", String.valueOf(pmax));
        }


        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(pmin, 0));
        yvalues.add(new Entry(pmax, 1));
        pieChart.setDescription("");
        PieDataSet dataSet = new PieDataSet(yvalues, "Calories");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Min "+minc);
        xVals.add("Max "+maxc);


        PieData data = new PieData(xVals, dataSet);

        // In percentage Term
        //data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        //test
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        });

        data.setValueTextSize(10f);
        //Disable Hole in the Pie Chart
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setHoleRadius(40f);

        //pieChart.setHoleColor(Color.parseColor("#FFE0B2"));
        pieChart.setHoleColor(R.drawable.move);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(data);
/*
        tvc=(TextView)findViewById(R.id.tvc);
        tvl=(TextView)findViewById(R.id.tvl);
        tvglu=(TextView)findViewById(R.id.tvglu);
        tvpro=(TextView)findViewById(R.id.tvpro);*/




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //gère le click sur une action de l'ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MediaPlayer mp = MediaPlayer.create(MainActivity0.this,R.raw.bclick);

        if (item.getItemId() ==R.id.radd){
            mp.start();
            //ajouter repas aujourdhui
            Intent intent = new Intent(this,AjoutRepa.class);
            intent.putExtra("ou","0");
            finish();
            startActivity(intent);
        }else if (item.getItemId() ==R.id.aladd){
            mp.start();
            //ajouter aliment
            Intent intent = new Intent(this,AjoutAliment.class);
            finish();
            startActivity(intent);
        }else if (item.getItemId() ==R.id.mobj){
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
        }else if (item.getItemId() ==R.id.mbdd){
            mp.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity0.this);
            builder.setMessage("Cette fonctionnalité est montanement desactivé.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            //mettre a jout bdd local via bdd externe
            //antiDuplication(0);
        }else if (item.getItemId() ==R.id.mvsc){
            mp.start();
        //mettre a jout bdd local via fichier excel
            antiDuplication(1);
           // read();
    }else if (item.getItemId() ==R.id.cons2){
            mp.start();
        //consulter regime
        Intent intent = new Intent(this,Consultation2.class);
        finish();
        startActivity(intent);
    }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        MediaPlayer mp = MediaPlayer.create(MainActivity0.this,R.raw.bclick);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nmain) {
            mp.start();
            //retour vers mais
            Intent intent = new Intent(this,MainActivity0.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.nobj) {
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
            mp.start();
            //ajouter aliment
            Intent intent = new Intent(this,AjoutAliment.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.najr) {
            mp.start();
            //ajouter repas aujourdhui
            Intent intent = new Intent(this,AjoutRepa.class);
            intent.putExtra("ou","0");
            finish();
            startActivity(intent);
        } else if (id == R.id.ncon2) {
            mp.start();
            //consulter regime 2
            Intent intent = new Intent(this,Consultation2.class);
            finish();
            startActivity(intent);
        } else if (id == R.id.quitter) {
            mp.start();
            //quitter
            System.exit(1);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void antiDuplication(int ok){
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
        if (ok == 0) {
            new LoadAllAliments().execute();
        } else if (ok == 1) {

          //  List scoreList = read();
            read();

        }

    }



    //*************************************************************
    class LoadAllAliments extends AsyncTask<String, String, JSONObject> {
        protected void onPreExecute() {
            //ici
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity0.this);
            pDialog.setMessage("Mise a jout BDD..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            Log.i("aaaaaaaaaaa", "debut de load");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            //  if (delatation==1){SystemClock.sleep(10000);}
            try {

                HashMap<String, String> params = new HashMap<>();

                Log.d("request", "startingverification0");

                JSONObject json = jParser.makeHttpRequest(
                        url_all_al, "GET", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            int success = 0;
            if (json != null) {

                try {
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // aliments trouvé
                        // recevoir tous les aliments dans une list
                        alim = json.getJSONArray(TAG_TAXI);
                        int longeur = alim.length();//la longeur du resultat(combien ya de aliments)

                        // bouclé dans tous les aliments
                        int i = 0;
                        while (i < longeur) {
                            JSONObject t = alim.getJSONObject(i);//++++++++++++++++++
                            // afecter cheke json a une variable
                            if (!listeDeja.contains(t.getString(ALIMENTS))) {
                                String nom_ali = t.getString(ALIMENTS);
                                String nombre_cal = t.getString(CALORIES);
                                String nombre_lip = t.getString(LIPIDES);
                                String nombre_glu = t.getString(GLUCIDES);
                                String nombre_pro = t.getString(PROTEINES);
                                //ajout dans la bdd l'aliment
                                ContentValues values = new ContentValues();
                                values.put(ALIMENTS, nom_ali);
                                values.put(CALORIES, nombre_cal);
                                values.put(LIPIDES, nombre_lip);
                                values.put(GLUCIDES, nombre_glu);
                                values.put(PROTEINES, nombre_pro);

                                Uri.Builder builder = new Uri.Builder();
                                builder.scheme("content").authority(authority).appendPath(TABLE_ALIMENT);
                                Uri uri = builder.build();
                                uri = getContentResolver().insert(uri, values);
                                long id = ContentUris.parseId(uri);
                                Log.d("aaaaaaaaa ", String.valueOf(id));

                                i++;
                            }
                        }
                    } else {
                        // no aliment found

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            pDialog.dismiss();
        }
    }
    //*************************************************************

    public void read(){
        inputStream = getResources().openRawResource(R.raw.test);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");
                Log.i("aaaaaaaaaaaaaaaa",String.valueOf(row[0]));
               // resultList.add(row);
                //*********************************************************
                if (!listeDeja.contains(String.valueOf(row[0]))) {
                    String nom_ali = String.valueOf(row[0]);
                    String nombre_cal = String.valueOf(row[1]);
                  //  String nombre_lip = String.valueOf(row[2]);
                   // String nombre_glu = String.valueOf(row[3]);
                   // String nombre_pro = String.valueOf(row[4]);
                    //ajout dans la bdd l'aliment
                    ContentValues values = new ContentValues();
                    values.put(ALIMENTS, nom_ali);
                    values.put(CALORIES, nombre_cal);
                  //  values.put(LIPIDES, nombre_lip);
                  //  values.put(GLUCIDES, nombre_glu);
                  //  values.put(PROTEINES, nombre_pro);

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(authority).appendPath(TABLE_ALIMENT);
                    Uri uri = builder.build();
                    uri = getContentResolver().insert(uri, values);
                    long id = ContentUris.parseId(uri);
                    Log.d("aaaaaaaaa ", String.valueOf(id));
                //*********************************************************
            }
        }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }

        //return resultList;
    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            MediaPlayer mp = MediaPlayer.create(MainActivity0.this,R.raw.alert);
            mp.start();
           // super.onBackPressed();
            // final Intent i = new Intent(this,MainActivity0.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity0.this);
            builder.setMessage("Etes vous sur de vouloir quittez l'application ?");
            builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                    System.exit(1);

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
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;
        }

        return super.onKeyDown(keycode, e);
    }


    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {


            outState.putInt("random",random);



        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        textView13.setText(conseil.get(random));

    }
}
