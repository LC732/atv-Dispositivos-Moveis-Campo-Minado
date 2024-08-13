package com.example.atvcampominado;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView matriz;
    private EditText resultado;

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
        matriz = findViewById(R.id.tVMatriz);
        resultado = findViewById(R.id.eTNumber);
    }

    public void criarMatriz(@NonNull View view){
        int num = 0;
        String num_str = "";
        try {
            num_str = resultado.getText().toString();
            if(!num_str.isEmpty()) {
                try {
                    num = Integer.parseInt(num_str);
                }catch (NumberFormatException e){
                    Toast.makeText(this, "Por favor, insira um número válido", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (NumberFormatException e){
            Toast.makeText(this, "Por favor, insira um número válido", Toast.LENGTH_SHORT).show();
        }

        StringBuilder str_matriz = new StringBuilder();


        int[][] matriz_ = new int[num][num];
        int[][] matriz2 = new int[num][num];

        Random random = new Random();

        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                int numero_random = random.nextInt(100);
                if(numero_random < 15){
                    matriz_[i][j] = -1;
                }else{
                    matriz_[i][j] = 0;
                }
            }
        }

        int soma = 0;
        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {

                if(matriz_[i][j] != -1){

                    if (i > 0 && j > 0)
                        soma += matriz_[i-1][j-1];
                    if (i > 0)
                        soma += matriz_[i-1][j];
                    if (i > 0 && j < num - 1)
                        soma += matriz_[i-1][j+1];
                    if (j > 0)
                        soma += matriz_[i][j-1];
                    if (j < num - 1)
                        soma += matriz_[i][j+1];
                    if (i < num - 1 && j > 0)
                        soma += matriz_[i+1][j-1];
                    if (i < num - 1)
                        soma += matriz_[i+1][j];
                    if (i < num - 1 && j < num - 1)
                        soma += matriz_[i+1][j+1];

                    matriz2[i][j] = soma*(-1);
                    soma = 0;
                }else{
                    matriz2[i][j] = -1;
                }
            }
        }
        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                str_matriz.append(" ");
                if (j < num - 1 && matriz2[i][j] != -1) {
                    str_matriz.append(" ");
                }
                str_matriz.append(matriz2[i][j]);

            }
            str_matriz.append("\n");
        }

        matriz.setText(str_matriz.toString());

    }

}