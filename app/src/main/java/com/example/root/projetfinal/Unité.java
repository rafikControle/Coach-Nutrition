package com.example.root.projetfinal;

/**
 * Created by root on 15/11/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Unité implements Parcelable {


    int quantité;
    double qcal;
    double qglu;
    double qlip;
    double qpro;
    String nom_aliment;

    public Unité(String x, int y, double qc, double qg, double ql, double qp){
        quantité=y;
        nom_aliment=x;
        qcal=qc;
        qglu=qg;
        qlip=ql;
        qpro=qp;
    }

    int getQuantité(){
        return quantité;
    }

    double getCalQuantité(){
        return qcal;
    }
    double getLipQuantité(){
        return qlip;
    }
    double getGluQuantité(){
        return qglu;
    }
    double getProQuantité(){
        return qpro;
    }

    String getNom_aliment(){
        return nom_aliment;
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom_aliment);
        dest.writeInt(quantité);
        dest.writeDouble(qcal);

    }

    public Unité(Parcel in) {
        nom_aliment = in.readString();
        quantité = in.readInt();
        qcal = in.readDouble();
    }

    public static final Parcelable.Creator<Unité> CREATOR = new Parcelable.Creator<Unité>()
    {
        public Unité createFromParcel(Parcel in)
        {
            return new Unité(in);
        }
        public Unité[] newArray(int size)
        {
            return new Unité[size];
        }
    };

}
