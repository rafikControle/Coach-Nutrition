package com.example.root.projetfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by root on 14/11/17.
 */

public class Objectif extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sf;
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
    String objectif,smin,smax;
    String disco;
    EditText nobj,maxcal,mincal,maxpro,minpro,maxglu,minglu,maxlip,minlip;
    Button bajoutobj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.objectif);

        //nobj=(EditText)findViewById(R.id.nobj);
        maxcal=(EditText)findViewById(R.id.maxcal);
        mincal=(EditText)findViewById(R.id.mincal);
       /* maxpro=(EditText)findViewById(R.id.maxpro);
        minpro=(EditText)findViewById(R.id.minpro);
        maxglu=(EditText)findViewById(R.id.maxglu);
        maxlip=(EditText)findViewById(R.id.maxlip);
        minglu=(EditText)findViewById(R.id.minglu);
        minlip=(EditText)findViewById(R.id.minlip);*/
        bajoutobj=(Button) findViewById(R.id.bajoutobj);
        bajoutobj.setOnClickListener(this);

        sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sf.contains(sharedmodifie)){
            //prendre les truc de shared pref
          //  objectif=sf.getString(sharednom,null);
           // Log.i("teeeeeeeeeeeeeeeeel",objectif);
            smin=sf.getString(sharedmincal,null);
           // Log.i("naaaaaaaaaaaaaaaame",smin);
            smax=sf.getString(sharedmaxcal,null);
           // Log.i("naaaaaaaaaaaaaaaame",smax);
            disco=sf.getString(sharedmodifie,null);
            Log.i("naaaaaaaaaaaaaaaame",disco);

            if (disco.equals("1")){//aller a lactivité main
                Intent intent = new Intent(this,MainActivity0.class);
                finish();
                startActivity(intent);
                }
            else{//objectif vide ou je ss la pour modifié
                mincal.setHint(smin);
                maxcal.setHint(smax);
                Toast.makeText(getApplicationContext(),"objectif vide",Toast.LENGTH_LONG).show();

            }
        }


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bajoutobj) {
           // objectif = nobj.getText().toString();
            MediaPlayer mp = MediaPlayer.create(Objectif.this,R.raw.bclick);
            mp.start();
            if (!mincal.getText().toString().isEmpty() && !mincal.getText().toString().isEmpty()) {
                //ajout objectif dans shared
                disco = "1";
                SharedPreferences.Editor editor = sf.edit();
                editor.putString(sharedmincal,mincal.getText().toString());
                editor.putString(sharedmaxcal,maxcal.getText().toString());
             /*   editor.putString(sharedmaxpro,maxpro.getText().toString());
                editor.putString(sharedminpro,minpro.getText().toString());
                editor.putString(sharedmaxglu,maxglu.getText().toString());
                editor.putString(sharedminglu,minglu.getText().toString());
                editor.putString(sharedmaxlip,maxlip.getText().toString());
                editor.putString(sharedminlip,minlip.getText().toString());*/
               // editor.putString(sharednom,objectif);
                editor.putString(sharedmodifie,disco);
                editor.commit();

                Intent intent = new Intent(this,MainActivity0.class);
                finish();
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Information vide",
                    Toast.LENGTH_LONG).show();
        }
    }
}
