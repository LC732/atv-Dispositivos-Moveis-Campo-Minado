package com.example.atvcampominado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    public enum Nivel {
        FACIL, MEDIO, DIFICIL, PERSONALIZADO
    }
    private Nivel nivel = Nivel.MEDIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = new Intent(MainActivity.this, JogoCampoMinado.class);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        findViewById(R.id.radBFacil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nivel = Nivel.FACIL;
            }
        });
        findViewById(R.id.radBMedio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nivel = Nivel.MEDIO;
            }
        });
        findViewById(R.id.radBDificil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nivel = Nivel.DIFICIL;
            }
        });
        findViewById(R.id.radBPersonalisado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nivel = Nivel.PERSONALIZADO;
            }
        });

    }
    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "Rodow", Toast.LENGTH_SHORT).show();
    }


    public void entrarNoJogo(@NonNull View view){
        if(nivel != null){
            intent.putExtra("Nivel", nivel.ordinal());
            if(nivel == Nivel.PERSONALIZADO){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View dialogView = getLayoutInflater().inflate(R.layout.dialogo_personalizado, null);
                builder.setView(dialogView);

                final EditText tamColuna = dialogView.findViewById(R.id.numCol);
                final EditText tamLinha = dialogView.findViewById(R.id.numLinhas);
                final EditText tamBomba = dialogView.findViewById(R.id.numBomb);

                builder.setTitle("Tamanho Personalizado")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String scol = tamColuna.getText().toString();
                                String slin = tamLinha.getText().toString();
                                String sbom = tamBomba.getText().toString();

                                if(!scol.isEmpty() && !slin.isEmpty()){
                                    int col = Integer.parseInt(scol);
                                    int lin = Integer.parseInt(slin);
                                    int bom = Integer.parseInt(sbom);
                                    intent.putExtra("colunas", col);
                                    intent.putExtra("linhas", lin);
                                    intent.putExtra("bombas", bom);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(MainActivity.this, "Adicione numeros validos", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);

                // Exibe o Dialog
                builder.create().show();
            }else {
                startActivity(intent);
            }
        }else{
            Toast.makeText(this, "Escolha um modo", Toast.LENGTH_SHORT).show();
        }
    }

    public void entrarNoRank(View v){
        Intent rank = new Intent(MainActivity.this, Ranque.class);
        startActivity(rank);
    }


}