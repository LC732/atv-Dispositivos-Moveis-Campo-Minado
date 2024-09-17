package com.example.atvcampominado.BDsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Contrato.Colunas.TABLE_NAME + " (" +
                    Contrato.Colunas._ID + " INTEGER PRIMARY KEY," +
                    Contrato.Colunas.TABLE_BANDEIRAS + " INTEGER," +
                    Contrato.Colunas.TABLE_CLICKS + " INTEGER," +
                    Contrato.Colunas.TABLE_TEMPO + " TEXT," +
                    Contrato.Colunas.TABLE_DATA + " TEXT," +
                    Contrato.Colunas.TABLE_HORA + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contrato.Colunas.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NOME = "db_CampoMinado";

    public DbHelper(Context context) {
        super(context, DATABASE_NOME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
