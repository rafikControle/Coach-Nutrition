package com.example.root.projetfinal;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    BaseAliment bg;
    private String authority = "fr.rafik.projet";
    public final static String TABLE_ALIMENT = "aliment";
    public final static String TABLE_REGIME = "regime";
    int ALI = 1;
    SQLiteDatabase bdd;
    int REGI = 2;

    private final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    {
        matcher.addURI(authority, TABLE_ALIMENT, ALI);
        matcher.addURI(authority, TABLE_REGIME, REGI);

    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        bdd = bg.getWritableDatabase();
        int code = matcher.match(uri);
        Log.d("aaaaaaaaa uri : ", "Uri=" + uri.toString());
        long id = 0;
        String path;
        if (code == 1){
            id = bdd.insert(TABLE_ALIMENT, null, values);
            path = TABLE_ALIMENT;
        }else if (code ==2){
            id = bdd.insert(TABLE_REGIME, null, values);
            path = TABLE_REGIME;
        }else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        Uri.Builder builder = (new Uri.Builder())
                .authority(authority)
                .appendPath(path);

        return ContentUris.appendId(builder, id).build();
    }

    @Override
    public boolean onCreate() {
        bg = BaseAliment.getInstance(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int code = matcher.match(uri);
        Log.d("aaaaaaaaa uri : ", "Uri=" + uri.toString());
        long id = 0;
        String path;
        if (code == 1){
            Cursor cursor= bg.getReadableDatabase().query(TABLE_ALIMENT,projection,selection,selectionArgs,null,null,null);

            path = TABLE_ALIMENT;
            return cursor;
        }else if (code ==2){
            Cursor cursor= bg.getReadableDatabase().query(TABLE_REGIME,projection,selection,selectionArgs,null,null,null);

            path = TABLE_REGIME;
            return cursor;
        }else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        int code = matcher.match(uri);
        switch (code) {
            case 2:
                count = bg.getReadableDatabase().update(TABLE_REGIME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("URI inconnue " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
