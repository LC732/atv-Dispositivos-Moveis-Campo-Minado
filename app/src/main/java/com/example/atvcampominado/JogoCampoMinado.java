package com.example.atvcampominado;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.atvcampominado.Objetos.Cela;

import java.util.Random;

public class JogoCampoMinado extends AppCompatActivity {

    private Cela[][] celas = new Cela[10][10];
    private Button[][] botoes = new Button[10][10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jogo_campo_minado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout tlGrid = findViewById(R.id.llGrid);
        LayoutInflater inflater = getLayoutInflater();

        criarMatrizCelas();


        for (int i = 0; i < 10; i++) {
            LinearLayout tableRow = new LinearLayout(this);
            tableRow.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 10; j++) {
                View bview = inflater.inflate(R.layout.item_cells, tableRow, false);
                Button button = (Button) bview.findViewById(R.id.btCell);
                //Button button = new Button(this);
                button.setText("");
                Cela cela = celas[i][j];
                botoes[i][j] = button;
                int a = i;
                int b = j;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!cela.getEstado()){
                            mudarCelula(a,b);
                        }
                    }
                });
                tableRow.addView(bview);
            }
            tlGrid.addView(tableRow);
        }

    }

    private void mudarCelula(int a, int b){


        if(celas[a][b].getValor()==-1){
            botoes[a][b].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            allLose();
        }
        if(celas[a][b].getValor()==0) {
            celas[a][b].setEstado(Boolean.TRUE);
            botoes[a][b].setText(String.valueOf(celas[a][b].getValor()));
            botoes[a][b].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));
            if (a > 0)
                if (!celas[a - 1][b].getEstado()&&celas[a - 1][b].getValor()==0)
                    mudarCelula(a - 1, b);
            if (b > 0)
                if (!celas[a][b - 1].getEstado()&&celas[a][b-1].getValor()==0)
                    mudarCelula(a, b - 1);
            if (a < 9)
                if (!celas[a + 1][b].getEstado()&&celas[a + 1][b].getValor()==0)
                    mudarCelula(a + 1, b);
            if (b < 9)
                if (!celas[a][b + 1].getEstado()&&celas[a][b + 1].getValor()==0)
                    mudarCelula(a, b + 1);
        }else{
            celas[a][b].setEstado(Boolean.TRUE);
            botoes[a][b].setText(String.valueOf(celas[a][b].getValor()));
        }
    }

    private void allLose(){

        new AlertDialog.Builder(this)
                .setTitle("Game Over") // Título do diálogo
                .setMessage("Você perdeu!") // Mensagem do diálogo
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação quando o botão "OK" é clicado
                        dialog.dismiss(); // Fechar o diálogo
                    }
                })
                .show(); // Mostrar o diálogo

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(!celas[i][j].getEstado()&&celas[i][j].getValor()==-1){
                    celas[i][j].setEstado(Boolean.TRUE);
                    botoes[i][j].setText(String.valueOf(celas[i][j].getValor()));
                    botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                }
            }
        }
    }


    public void criarMatrizCelas(){
        int num = 10;

        int[][] matriz_ = new int[num][num];
        int[][] matriz2 = new int[num][num];

        Random random = new Random();

        // cria a matriz com -1 e 0
        int cont = 15;
        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                int numero_random = random.nextInt(100);
                if(numero_random < 15 || ((99 <= cont+i*10+j) && (cont!=0))){
                    matriz_[i][j] = -1;
                    cont--;
                }else{
                    matriz_[i][j] = 0;
                }
            }
        }
        int soma = 0;

        //crai a matriz com as somas

        for(int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                Cela cela = new Cela();

                if(matriz_[i][j] != -1){
                    if (i > 0 && j > 0)
                        if (matriz_[i-1][j-1] != 0)
                            soma++;
                    if (i > 0)
                        if(matriz_[i-1][j] != 0)
                            soma++;
                    if (i > 0 && j < num - 1)
                        if( 0 != matriz_[i-1][j+1])
                            soma++;
                    if (j > 0)
                        if( 0 != matriz_[i][j-1])
                            soma++;
                    if (j < num - 1)
                        if( 0 != matriz_[i][j+1])
                            soma++;
                    if (i < num - 1 && j > 0)
                        if( 0 != matriz_[i+1][j-1])
                            soma++;
                    if (i < num - 1)
                        if( 0 != matriz_[i+1][j])
                            soma++;
                    if (i < num - 1 && j < num - 1)
                        if( 0 != matriz_[i+1][j+1])
                            soma++;
                    matriz2[i][j] = soma;
                    soma = 0;
                }else{
                    matriz2[i][j] = -1;
                }
                cela.setEstado(Boolean.FALSE);
                cela.setValor(matriz2[i][j]);
                celas[i][j] = cela;
            }
        }
    }

    private void resetCells(){
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                celas[i][j].setEstado(Boolean.FALSE );
                botoes[i][j].setText(String.valueOf(""));

                botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#70FFF3")));
            }
        }
    }

    public void entrarNoJogo(@NonNull View view){
        /*criarMatrizCelas();
        resetCells();*/
        recreate();
    }

}