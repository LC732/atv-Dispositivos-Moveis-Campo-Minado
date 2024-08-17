package com.example.atvcampominado;

import android.content.Intent;
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

        int cont = (num*num)*15/100;

        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                int numero_random = random.nextInt(100);
                if((numero_random < 20 || (((num*num)-1 <= cont+i*10+j))) && (cont!=0)){
                    matriz_[i][j] = -1;
                    cont--;
                }else{
                    matriz_[i][j] = 0;
                }
            }
        }
        int soma = 0;
        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if(matriz_[i][j] != -1){

                    int x1, x2, y1, y2;
                    x1 = i > 0 ? i-1 : i;
                    x2 = i < num -1 ? i+1 : i;
                    y1 = j > 0 ? j-1 : j;
                    y2 = j < num -1 ? j+1 : j;

                    for(int x = x1; x <= x2; x++){
                        for(int y = y1; y <= y2; y++){
                            if((!((x==i)==(y==j))) && matriz_[x][y]==-1){
                                soma++;
                            }
                        }
                    }
                    matriz2[i][j] = soma;
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

    public void entrarNoJogo(@NonNull View view){
        Intent intent = new Intent(MainActivity.this, JogoCampoMinado.class);
        startActivity(intent);
    }

}