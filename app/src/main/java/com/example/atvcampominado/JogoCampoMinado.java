package com.example.atvcampominado;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.atvcampominado.Objetos.Cela;

import java.util.Random;

public class JogoCampoMinado extends AppCompatActivity {

    private int num = 10;
    private boolean primeiraJogada = true;
    private Cela[][] celas = new Cela[num][num];
    private Button[][] botoes = new Button[num][num];
    private int[][] matriz_ = new int[num][num];
    private int[][] matriz2 = new int[num][num];
    private TextView bombas;

    private int vitoria;

    private enum Estado {
        BOMBA, INTEROGACAO, REMOVER
    }

    Estado estadoAtual = Estado.REMOVER;

    int numBombas = 0;

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

        LinearLayout tlGrid = findViewById(R.id.llGrid); // conecta com o TableLayer
        LayoutInflater inflater = getLayoutInflater(); // cria esse negocio pro botão ser criado
        bombas = findViewById(R.id.tVBombas);

        for (int i = 0; i < num; i++) {

            LinearLayout tableRow = new LinearLayout(this); // cria o tablerow
            tableRow.setOrientation(LinearLayout.HORIZONTAL); // mexe na comfiguração do tablerow
            for (int j = 0; j < num; j++) {

                Cela cela = new Cela();

                View bview = inflater.inflate(R.layout.item_cells, tableRow, false); // chama o botão do outro xml
                Button button = (Button) bview.findViewById(R.id.btCell); // conecta com o botão do xml
                //Button button = new Button(this);
                button.setText("");
                botoes[i][j] = button;
                int a = i;
                int b = j;

                // função onclick
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funcClick(a,b);
                    }
                });
                tableRow.addView(bview); // adiciona o xml no tablerow
                celas[i][j] = cela;
            }
            tlGrid.addView(tableRow);// adiciona o tablerow no tableLayout

        }

    }
    private void funcClick(int a, int b) {

        switch (estadoAtual) {
            case INTEROGACAO:
                switch (celas[a][b].getEstado()) {
                    case VAZIO:
                        celas[a][b].setEstado(Cela.estadocell.INTER);
                        //botoes[a][b].setCompoundDrawables(interogacao, null, null, null);
                        botoes[a][b].setText("?");
                        break;
                    case INTER:
                        celas[a][b].setEstado(Cela.estadocell.VAZIO);
                        //botoes[a][b].setCompoundDrawables(null, null, null, null);
                        botoes[a][b].setText("");
                        break;
                }
                break;
            case REMOVER:
                if (primeiraJogada) {
                    primeiraJogada = false;
                    criarMatrizCelas(a, b);// cria a matriz com os numeros
                }
                if (celas[a][b].getEstado() == Cela.estadocell.VAZIO) {
                    mudarCelulaRemover(a, b);
                }
                break;
            case BOMBA:
                switch (celas[a][b].getEstado()) {
                    case VAZIO:
                        botoes[a][b].setText("\uD83D\uDCA3");
                        celas[a][b].setEstado(Cela.estadocell.BOMBA);
                        break;
                    case BOMBA:
                        botoes[a][b].setText("");
                        celas[a][b].setEstado(Cela.estadocell.VAZIO);
                        break;
                }
                break;
        }
    }


    private void mudarCelulaRemover(int a, int b) {

        int num = this.num;

        if (celas[a][b].getValor() == -1) {
            allLose();
        } else if (celas[a][b].getValor() == 0) {
            celas[a][b].setEstado(Cela.estadocell.CLICADO);
            //botoes[a][b].setText(String.valueOf(celas[a][b].getValor()));
            botoes[a][b].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB703")));
            if (a > 0)
                if (celas[a - 1][b].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a - 1, b);
            if (b > 0)
                if (celas[a][b - 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a, b - 1);
            if (a < num - 1)
                if (celas[a + 1][b].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a + 1, b);
            if (b < num - 1)
                if (celas[a][b + 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a, b + 1);
            if (a > 0 && b > 0)
                if (celas[a - 1][b - 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a - 1, b - 1);
            if (a > 0 && b < num - 1)
                if (celas[a - 1][b + 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a - 1, b + 1);
            if (a < num - 1 && b > 0)
                if (celas[a + 1][b - 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a + 1, b - 1);
            if (a < num - 1 && b < num - 1)
                if (celas[a + 1][b + 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a + 1, b + 1);
        } else {
            celas[a][b].setEstado(Cela.estadocell.CLICADO);
            botoes[a][b].setText(String.valueOf(celas[a][b].getValor()));
            botoes[a][b].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB703")));
        }
        if(celas[a][b].getValor() != -1)
            vitoria++;
        if(vitoria == 85)
            allWin();
    }

    private void allWin(){
        new AlertDialog.Builder(this)
                .setTitle("Fim de Jogo") // Título do diálogo
                .setMessage("Você ganhou!") // Mensagem do diálogo
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação quando o botão "OK" é clicado
                        dialog.dismiss(); // Fechar o diálogo
                    }
                })
                .show(); // Mostrar o diálogo

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (celas[i][j].getEstado() == Cela.estadocell.VAZIO && celas[i][j].getValor() == -1) {
                    celas[i][j].setEstado(Cela.estadocell.CLICADO);
                    botoes[i][j].setText(String.valueOf(celas[i][j].getValor()));
                    botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB8500")));
                }
            }
        }
    }

    private void allLose() {

        new AlertDialog.Builder(this)
                .setTitle("Fim de Jogo") // Título do diálogo
                .setMessage("Você perdeu!") // Mensagem do diálogo
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ação quando o botão "OK" é clicado
                        dialog.dismiss(); // Fechar o diálogo
                    }
                })
                .show(); // Mostrar o diálogo

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (celas[i][j].getEstado() == Cela.estadocell.VAZIO && celas[i][j].getValor() == -1) {
                    celas[i][j].setEstado(Cela.estadocell.CLICADO);
                    botoes[i][j].setText(String.valueOf(celas[i][j].getValor()));
                    botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB8500")));
                }
            }
        }
    }


    public void criarMatrizCelas(int a, int b) {

        int num = this.num;

        Random random = new Random();

        // cria a matriz com -1 e 0
        int cont = 15;
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {

                int numero_random = random.nextInt(100);

                if ((numero_random < 15 || (((num * num) - 1 <= cont + i * 10 + j))) && (cont != 0) && !(i == a && j == b)) {
                    matriz_[i][j] = -1;
                    numBombas++;
                    cont--;
                } else {
                    matriz_[i][j] = 0;
                }
            }
        }
        int soma = 0;

        //crai a matriz com as somas

        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                Cela cela = new Cela();

                if (matriz_[i][j] != -1) {

                    int x1, x2, y1, y2;
                    x1 = i > 0 ? i - 1 : i;
                    x2 = i < num - 1 ? i + 1 : i;
                    y1 = j > 0 ? j - 1 : j;
                    y2 = j < num - 1 ? j + 1 : j;
                    soma = 0;

                    for (int x = x1; x <= x2; x++) {
                        for (int y = y1; y <= y2; y++) {
                            if ((!((x == i) && (y == j))) && matriz_[x][y] == -1) {
                                soma++;
                            }
                        }
                    }
                    matriz2[i][j] = soma;
                } else {
                    matriz2[i][j] = -1;
                }
                cela.setEstado(Cela.estadocell.VAZIO);
                cela.setValor(matriz2[i][j]);
                celas[i][j] = cela;
                botoes[i][j].setText("");
            }
        }
        bombas.setText("Bombas:" + numBombas);
    }

    private void resetCells() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                celas[i][j].setEstado(Cela.estadocell.VAZIO);
                botoes[i][j].setText(String.valueOf(""));

                botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#70FFF3")));
            }
        }
    }

    public void entrarNoJogo(@NonNull View view) {
        /*criarMatrizCelas();
        resetCells();*/
        recreate();
    }

    public void mudarTipoDeClick(View view) {
        ImageButton botao = (ImageButton) view;

        switch (estadoAtual) {
            case BOMBA:
                botao.setImageResource(R.drawable.nullimg);
                estadoAtual = Estado.REMOVER;
                break;
            case REMOVER:
                botao.setImageResource(R.drawable.interrogacao);
                estadoAtual = Estado.INTEROGACAO;
                break;
            case INTEROGACAO:
                botao.setImageResource(R.drawable.bomba);
                estadoAtual = Estado.BOMBA;
                break;
            default:
                break;
        }

    }


}