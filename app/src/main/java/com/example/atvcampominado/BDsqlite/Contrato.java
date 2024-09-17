package com.example.atvcampominado.BDsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Contrato {
    private Contrato() {
    }

    public static class Colunas implements BaseColumns {
        public static final String TABLE_NAME = "vitorias";
        public static final String TABLE_TEMPO = "tempo_de_jogo";
        public static final String TABLE_DATA = "data";
        public static final String TABLE_HORA = "hora";
        public static final String TABLE_CLICKS = "clicks";
        public static final String TABLE_BANDEIRAS = "bandeiras";
    }
}