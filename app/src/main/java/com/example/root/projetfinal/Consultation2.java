package com.example.root.projetfinal;

/**
 * Created by root on 11/12/17.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Consultation2 extends AppCompatActivity implements View.OnClickListener{
    BarChart barChart;
    SharedPreferences prefs;
    public static final String pref = "pref";
    public static final String sharednom = "objectif";
    public static final String sharedmincal = "mincal";
    public static final String sharedmaxcal = "maxcal";
    private String authority = "fr.rafik.projet";
    public final static String TABLE_ALIMENT = "aliment";
    public static final String ID = "id";
    public static final String ALIMENTS = "aliments";
    public static final String CALORIES = "calorie";
    public static final String LIPIDES = "lipides";
    public static final String GLUCIDES = "glucides";
    public static final String PROTEINES = "proteines";
    public final static String REPA_MANGER = "repas_manger";


    List<String> listeJourX = new ArrayList<String>();
    List<String> listeCaloriesY = new ArrayList<String>();
    List<String> listeReaps = new ArrayList<String>();
    List<Date> convertedDate = new ArrayList<Date>();
    public final static String TABLE_REGIME = "regime";
    public final static String JOUR = "jour";
    String minc,maxc;
    Spinner spinner2;

    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint>seriesMin;
    LineGraphSeries<DataPoint>seriesMax;
    GraphView graph;
    int rass ;
    int compteur =0;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultation2);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //code for portrait mode
            barChart = (BarChart) findViewById(R.id.barchart);
            button=(Button)findViewById(R.id.button);
            button.setOnClickListener(this);
            BarChart barChart = (BarChart) findViewById(R.id.barchart);
            spinner2 = (Spinner)findViewById(R.id.spinner2);

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (prefs.contains(sharedmincal)) {
                minc = prefs.getString(sharedmincal, null);
                maxc = prefs.getString(sharedmaxcal, null);
                Log.i("maxcalconsult",maxc);
                Log.i("mincalconsult",minc);
            }

            LimitLine upper_limit = new LimitLine(Float.valueOf(maxc), "MAX Obj");
            upper_limit.setLineWidth(4f);
            upper_limit.enableDashedLine(10f, 10f, 0f);
            upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            upper_limit.setTextSize(10f);

            LimitLine lower_limit = new LimitLine(Float.valueOf(minc), "MIN Obj");
            lower_limit.setLineWidth(4f);
            lower_limit.enableDashedLine(10f, 10f, 0f);
            lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            lower_limit.setTextSize(10f);

            YAxis leftAxis = barChart.getAxisLeft();
            // reset all limit lines to avoid overlapping lines
            leftAxis.removeAllLimitLines();
            leftAxis.addLimitLine(upper_limit);
            leftAxis.addLimitLine(lower_limit);
            leftAxis.setAxisMaxValue(10000f);
            leftAxis.setAxisMinValue(0f);
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawZeroLine(false);

            // limit lines are drawn behind data (and not on top)
            leftAxis.setDrawLimitLinesBehindData(false);
            barChart.getAxisRight().setEnabled(false);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(authority).appendPath(TABLE_REGIME);
            String[] columns = new String[]{ID, JOUR, CALORIES, REPA_MANGER,LIPIDES,GLUCIDES,PROTEINES};
            Uri uri = builder.build();
            Cursor ch = getContentResolver().query(uri, columns,null,null,null);
            try {
                ch.moveToLast();
                int i=0;
                while (!ch.isBeforeFirst() && i<7) {//ptr ||

                    listeJourX.add(ch.getString(1));
                    listeCaloriesY.add(ch.getString(2));
                    listeReaps.add(ch.getString(3));
                    try {
                        convertedDate.add(dateFormat.parse(listeJourX.get(i)));
                        Log.d("converteddateeee", String.valueOf(convertedDate.get(i)));//jour
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    compteur++;
                    ch.moveToPrevious();
                    i++;
                }

            }catch (Exception e){

            }

            if (compteur!=0) {
                // il ya des donnée dans les liste
                int comptour = 0;
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();
                int i = listeJourX.size();
                Log.i("i size ", String.valueOf(i));
                while((i>0) && (comptour < 7)){
                    labels.add(listeJourX.get(i-1));
                    entries.add(new BarEntry(Float.parseFloat(listeCaloriesY.get(i-1)), comptour));
                    comptour++;
                    i--;
                }

                BarDataSet bardataset = new BarDataSet(entries, "Calories");
                BarData data = new BarData(labels, bardataset);
                barChart.setData(data); // set the data and list of lables into chart
                barChart.setDescription("");  // set the description
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.animateY(500);

                final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeJourX);

                spinner2.setAdapter(adapter);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        rass = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else {
                //il ya pas de donné dans a liste
                Toast.makeText(this, "Data vide",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            //code for landscape mode
            spinner2 = (Spinner)findViewById(R.id.spinner2);
            button=(Button)findViewById(R.id.button);
            button.setOnClickListener(this);
            graph=(GraphView)findViewById(R.id.graph);

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (prefs.contains(sharedmincal)) {
                minc = prefs.getString(sharedmincal, null);
                maxc = prefs.getString(sharedmaxcal, null);
                Log.i("maxcalconsult",maxc);
                Log.i("mincalconsult",minc);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(authority).appendPath(TABLE_REGIME);
            String[] columns = new String[]{ID, JOUR, CALORIES, REPA_MANGER,LIPIDES,GLUCIDES,PROTEINES};
            Uri uri = builder.build();
            Cursor ch = getContentResolver().query(uri, columns,null,null,null);
            try {
                ch.moveToLast();
                int i=0;
                while (!ch.isBeforeFirst() && i<7) {

                    listeJourX.add(ch.getString(1));
                    listeCaloriesY.add(ch.getString(2));
                    listeReaps.add(ch.getString(3));
                    try {
                        convertedDate.add(dateFormat.parse(listeJourX.get(i)));
                        Log.d("converteddateeee", String.valueOf(convertedDate.get(i)));//jour
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    compteur++;
                    ch.moveToPrevious();
                    i++;
                }

            }catch (Exception e){

            }

            seriesMin = new LineGraphSeries<>(generateDataMin());
            seriesMax = new LineGraphSeries<>(generateDataMax());

            seriesMin.setColor(Color.GREEN);
            seriesMax.setColor(Color.RED);
            graph.addSeries(seriesMin);
            graph.addSeries(seriesMax);

            if (compteur!=0) {
                series = new LineGraphSeries<>(generateData());
                series.setColor(Color.BLACK);
                graph.addSeries(series);
                // set date label formatter
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
                graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

                // set manual x bounds to have nice steps
                //      graph.getViewport().setMinX(d1.getTime());
                //    graph.getViewport().setMaxX(d3.getTime());
                graph.getViewport().setXAxisBoundsManual(true);

                // as we use dates as labels, the human rounding to nice readable numbers
                // is not necessary
                graph.getGridLabelRenderer().setHumanRounding(false);

                final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeJourX);

                spinner2.setAdapter(adapter);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        rass = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else {
                Toast.makeText(this, "Data vide",
                        Toast.LENGTH_LONG).show();
            }


        }

}

    @Override
    public void onBackPressed()
    {
        MediaPlayer mp = MediaPlayer.create(Consultation2.this,R.raw.alert);
        mp.start();
        final Intent i = new Intent(this,MainActivity.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(Consultation2.this);
        builder.setMessage("Quittez l'activité  ?");
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


    public void Selected (){
        MediaPlayer mp = MediaPlayer.create(Consultation2.this,R.raw.alert);
        mp.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(Consultation2.this);
            builder.setMessage("vous avez manger le jour : "+spinner2.getSelectedItem().toString()+"\n" +"les repas : "+listeReaps.get(rass));
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            builder.show();
        }

    private DataPoint[] generateData() {
        // DataPoint[] values = new DataPoint[compteur];
        DataPoint[] values = new DataPoint[listeJourX.size()];

        int j =0;
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        for (int i =listeJourX.size()-1;i>=0;i--){

            values[j] =new DataPoint(convertedDate.get(i), Double.parseDouble(listeCaloriesY.get(i)));//jour courant

            j++;
        }
        return values;
    }

    private DataPoint[] generateDataMin() {
        // DataPoint[] values = new DataPoint[compteur];
        DataPoint[] values = new DataPoint[listeJourX.size()];

        int j =0;

        for (int i =listeJourX.size()-1;i>=0;i--){

            values[j] =new DataPoint(convertedDate.get(i), Double.parseDouble(minc));//jour courant

            j++;
        }
        return values;
    }

    private DataPoint[] generateDataMax() {
        // DataPoint[] values = new DataPoint[compteur];
        DataPoint[] values = new DataPoint[listeJourX.size()];

        int j =0;

        for (int i =listeJourX.size()-1;i>=0;i--){

            values[j] =new DataPoint(convertedDate.get(i), Double.parseDouble(maxc));//jour courant

            j++;
        }
        return values;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button){
            Selected ();
        }
    }
}