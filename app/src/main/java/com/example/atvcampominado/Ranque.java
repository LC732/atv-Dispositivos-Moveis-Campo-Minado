package com.example.atvcampominado;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atvcampominado.Adapter.Adaptador;
import com.example.atvcampominado.BDsqlite.Contrato;
import com.example.atvcampominado.BDsqlite.DbHelper;
import com.example.atvcampominado.Objetos.Vitoria;

import java.util.ArrayList;
import java.util.List;



public class Ranque extends AppCompatActivity {

    private static final String TAG = "BdParaLista";

    private Vitoria[] vitorias;
    private final DbHelper dhelper = new DbHelper(this);
    private RecyclerView rcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranque);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BdParaLista();
        setarRecicler();
    }

    private void BdParaLista() {

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dhelper.getReadableDatabase();
            String ordem = Contrato.Colunas.TABLE_TEMPO + " ASC";

            cursor = db.query(
                    Contrato.Colunas.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    ordem
            );

            List<Vitoria> vitoriaList = new ArrayList<>();

            while (cursor.moveToNext()){
                Vitoria vit = new Vitoria();
                vit.setBandeiras(cursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Colunas.TABLE_BANDEIRAS)));
                vit.setClicks(cursor.getInt(cursor.getColumnIndexOrThrow(Contrato.Colunas.TABLE_CLICKS)));
                vit.setTempoDeJogo(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Colunas.TABLE_TEMPO)));
                vit.setHora(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Colunas.TABLE_HORA)));
                vit.setData(cursor.getString(cursor.getColumnIndexOrThrow(Contrato.Colunas.TABLE_DATA)));
                vitoriaList.add(vit);
            }

            vitorias = vitoriaList.toArray(new Vitoria[0]);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao carregar dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void setarRecicler(){

        Adaptador adaptador = new Adaptador(vitorias);
        rcView = findViewById(R.id.rcRanque);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(adaptador);
    }

    @Override
    protected void onDestroy() {
        dhelper.close();
        super.onDestroy();
    }

    public void reset(View v){
        SQLiteDatabase db = dhelper.getReadableDatabase();
        dhelper.onUpgrade(db, 1, 2);
    }

}