package com.example.root.projetfinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 15/11/17.
 */

public class BaseAliment extends SQLiteOpenHelper {


    public final static int VERSION = 10;
    public final static String DB_NAME = "BdAliment";
    public final static String TABLE_ALIMENT = "aliment";
    public static final String ID = "id";
    public static final String ALIMENTS = "aliments";
    public static final String CALORIES = "calorie";
    public static final String LIPIDES = "lipides";
    public static final String GLUCIDES = "glucides";
    public static final String PROTEINES = "proteines";


    public final static String TABLE_REGIME = "regime";
    public final static String JOUR = "jour";
    public final static String REPA_MANGER = "repas_manger";


    /*CREATE TABLE `lol`.`loool` ( `id` INT(10) NOT NULL AUTO_INCREMENT ,
    `jjj` VARCHAR(15) NOT NULL , PRIMARY KEY (id))*/

    public final static String CREATE_AL = "create table " + TABLE_ALIMENT + "(" +
            ID + " integer primary key, " +
            ALIMENTS + " string NOT NULL, " +
            CALORIES + " integer, " +
            LIPIDES + " integer, " +
            GLUCIDES + " integer, " +
            PROTEINES + " integer " + ");";



    public final static String CREATE_REG = "create table " + TABLE_REGIME + "(" +
            ID + " integer primary key, " +
            JOUR + " string NOT NULL, " +
            CALORIES + " integer, " +
            REPA_MANGER + " string, " +
            LIPIDES + " integer, " +
            GLUCIDES + " integer, " +
            PROTEINES + " integer " + ");";

    private static BaseAliment ourInstance;

    public static BaseAliment getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new BaseAliment(context);
        return ourInstance;
    }

    private BaseAliment(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AL);
        db.execSQL(CREATE_REG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + TABLE_ALIMENT);
            db.execSQL("drop table if exists " + TABLE_REGIME);
            onCreate(db);
        }
    }
}
