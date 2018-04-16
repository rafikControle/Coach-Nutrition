package com.example.root.projetfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
    public static final String sharedmodifie = "disco";
    String objectif,smin,smax;
    String disco;
    EditText nobj,maxcal,mincal;
    Button bajoutobj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.objectif);

        maxcal=(EditText)findViewById(R.id.maxcal);
        mincal=(EditText)findViewById(R.id.mincal);
        bajoutobj=(Button) findViewById(R.id.bajoutobj);
        bajoutobj.setOnClickListener(this);

        sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sf.contains(sharedmodifie)){
            smin=sf.getString(sharedmincal,null);
            smax=sf.getString(sharedmaxcal,null);
            disco=sf.getString(sharedmodifie,null);
            if (disco.equals("1")){//aller a lactivité main
                Intent intent = new Intent(this,MainActivity.class);
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

            MediaPlayer mp = MediaPlayer.create(Objectif.this,R.raw.bclick);
            mp.start();
            if (!mincal.getText().toString().isEmpty() && !mincal.getText().toString().isEmpty()) {
                //ajout objectif dans shared
                disco = "1";
                SharedPreferences.Editor editor = sf.edit();
                editor.putString(sharedmincal,mincal.getText().toString());
                editor.putString(sharedmaxcal,maxcal.getText().toString());

                editor.putString(sharedmodifie,disco);
                editor.commit();

                Intent intent = new Intent(this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Information vide",
                    Toast.LENGTH_LONG).show();
        }
    }
}
