package com.example.joaopedro.minhasfinancas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {



    public DbHelper(Context context) {
        super(context, Contract.nome_banco, null, Contract.versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS "+Contract.despesa+"(");
        sql.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql.append("data DATE NOT NULL,");
        sql.append("descricao varchar(200) NOT NULL,");
        sql.append("tipo int(1),");
        sql.append("valor double(10,2)");
        sql.append(")");

        StringBuilder sql2 = new StringBuilder();
        sql2.append("CREATE TABLE IF NOT EXISTS "+Contract.receita+"(");
        sql2.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sql2.append("tipo int(1),");
        sql2.append("data DATE,");
        sql2.append("descricao varchar(100),");
        sql2.append("valor double(10,2)");
        sql2.append(");");

        db.execSQL(sql.toString());
        db.execSQL(sql2.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE "+Contract.nome_banco);
        onCreate(db);
    }
}

