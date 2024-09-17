package com.example.atvcampominado;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.atvcampominado.BDsqlite.Contrato;
import com.example.atvcampominado.BDsqlite.DbHelper;
import com.example.atvcampominado.Objetos.Cela;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.temporal.ValueRange;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class JogoCampoMinado extends AppCompatActivity {

    private int numx;
    private int numy;
    private int numb;
    private int numBandeiras = 0;
    private int numClicks = 0;
    private boolean primeiraJogada = true;
    private Cela[][] celas;
    private Button[][] botoes;
    private int[][] matriz_;
    private int[][] matriz2;
    private LinearLayout tlGrid;
    private LinearLayout[] lLLinhas;
    private TextView bandeiras;
    private TextView clicks;
    private int vitoria;
    private Vibrator vibrador; // o nome ficou estranho :/
    private FloatingActionButton ftbReset;
    private MediaPlayer playWin, playLose;
    private TextView tvTimer;
    private Timer timer;
    private TimerTask timerTask;
    private double time = 0.0;
    private TextView hora;
    private TextView dia;


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


        setNivel();
        conector();
        criarBotoes();
        conferirOrientacao();
        atualizarText();
        timeAndDay();

    }

    private void conector() {
        playWin = MediaPlayer.create(this, R.raw.win2);
        playLose = MediaPlayer.create(this, R.raw.lose2);
        celas = new Cela[numx][numy];
        botoes = new Button[numx][numy];
        matriz_ = new int[numx][numy];
        matriz2 = new int[numx][numy];
        lLLinhas = new LinearLayout[numx];
        tlGrid = findViewById(R.id.llGrid); // conecta com o TableLayer
        bandeiras = findViewById(R.id.tvNBandeiras);
        clicks = findViewById(R.id.tvNClicks);
        vibrador = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ftbReset = findViewById(R.id.flbutReset);
        tvTimer = findViewById(R.id.tvTimer);
        timer = new Timer();
        hora = findViewById(R.id.tvHora);
        dia = findViewById(R.id.tvDia);
    }

    private void criarBotoes() {
        LayoutInflater inflater = getLayoutInflater(); // cria esse negocio pro botão ser criado

        for (int i = 0; i < numx; i++) {

            LinearLayout tableRow = new LinearLayout(this); // cria o tablerow
            lLLinhas[numx - 1 - i] = tableRow;

            for (int j = 0; j < numy; j++) {
                Cela cela = new Cela();

                View bview = inflater.inflate(R.layout.item_cells, tableRow, false); // chama o botão do outro xml
                Button button = bview.findViewById(R.id.btCell); // conecta com o botão do xml

                button.setText("");
                botoes[i][j] = button;
                int a = i;
                int b = j;

                // função onclick
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        funcClick(a, b);
                    }
                });

                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        funcLongClick(a, b);
                        return true;
                    }
                });

                tableRow.addView(bview); // adiciona o xml no tablerow
                celas[i][j] = cela;
            }
            tlGrid.addView(tableRow);// adiciona o tablerow no tableLayout

        }
    }

    private void setNivel() {
        Intent intent = getIntent();
        MainActivity.Nivel nivel = MainActivity.Nivel.values()[intent.getIntExtra("Nivel", 1)];

        switch (nivel) {
            case FACIL:
                numy = 9;
                numx = 9;
                numb = 10;
                break;
            case MEDIO:
                numy = 11;
                numx = 19;
                numb = 30;
                break;
            case DIFICIL:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                numy = 9;
                numx = 23;
                numb = 50;
                break;
            case PERSONALIZADO:
                numy = intent.getIntExtra("colunas", -1);
                numx = intent.getIntExtra("linhas", -1);
                numb = intent.getIntExtra("bombas", -1);
                break;
        }
    }

    private void funcClick(int a, int b) {
        if (celas[a][b].getEstado() == Cela.estadocell.VAZIO) {
            if (primeiraJogada) {
                primeiraJogada = false;
                if (numb >= numy * numx) numb = (numx * numy) - 1;
                criarMatrizCelas(a, b);// cria a matriz com os numeros
                mudarCelulaRemover(a, b);
                numClicks = 1;
                atualizarText();
                startTimer();
            } else {
                mudarCelulaRemover(a, b);
                numClicks++;
                atualizarText();
            }
        }
        atualizarText();
    }


    private void funcLongClick(int a, int b) {
        switch (celas[a][b].getEstado()) {
            case VAZIO:
                vibrador.vibrate(100);
                celas[a][b].setEstado(Cela.estadocell.INTER);
                botoes[a][b].setText("\uD83D\uDEA9");
                numBandeiras++;
                atualizarText();
                break;
            case INTER:
                vibrador.vibrate(100);
                celas[a][b].setEstado(Cela.estadocell.VAZIO);
                botoes[a][b].setText("");
                numBandeiras--;
                atualizarText();
                break;

        }
    }


    private void mudarCelulaRemover(int a, int b) {

        int numx = this.numx;
        int numy = this.numy;

        if (celas[a][b].getValor() == -1) {
            allLose();
        } else if (celas[a][b].getValor() == 0) {
            celas[a][b].setEstado(Cela.estadocell.CLICADO);
            botoes[a][b].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB703")));
            if (a > 0)
                if (celas[a - 1][b].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a - 1, b);
            if (b > 0)
                if (celas[a][b - 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a, b - 1);
            if (a < numx - 1)
                if (celas[a + 1][b].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a + 1, b);
            if (b < numy - 1)
                if (celas[a][b + 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a, b + 1);
            if (a > 0 && b > 0)
                if (celas[a - 1][b - 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a - 1, b - 1);
            if (a > 0 && b < numy - 1)
                if (celas[a - 1][b + 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a - 1, b + 1);
            if (a < numx - 1 && b > 0)
                if (celas[a + 1][b - 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a + 1, b - 1);
            if (a < numx - 1 && b < numy - 1)
                if (celas[a + 1][b + 1].getEstado() == Cela.estadocell.VAZIO)
                    mudarCelulaRemover(a + 1, b + 1);
        } else {
            celas[a][b].setEstado(Cela.estadocell.CLICADO);
            botoes[a][b].setText(String.valueOf(celas[a][b].getValor()));
            botoes[a][b].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFB703")));
        }
        if (celas[a][b].getValor() != -1)
            vitoria++;
        if (vitoria == numx * numy - numb)
            allWin();
    }

    private void allWin() {

        playWin.start();
        if(timerTask != null){
            timerTask.cancel();
        }
        adicionarVitoria();

        new AlertDialog.Builder(this)
                .setTitle("Fim de Jogo") // Título do diálogo
                .setMessage("Você ganhou!") // Mensagem do diálogo
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Fechar o diálogo
                    }
                })
                .show(); // Mostrar o diálogo
        ftbReset.setVisibility(View.VISIBLE);
        for (int i = 0; i < numx; i++) {
            for (int j = 0; j < numy; j++) {

                if (celas[i][j].getEstado() == Cela.estadocell.VAZIO && celas[i][j].getValor() == -1) {

                    botoes[i][j].setText(String.valueOf(celas[i][j].getValor()));
                    botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB8500")));
                }
                celas[i][j].setEstado(Cela.estadocell.CLICADO);
            }
        }
    }

    private void allLose() {

        playLose.start();
        if(timerTask != null){
            timerTask.cancel();
        }

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
        ftbReset.setVisibility(View.VISIBLE);
        for (int i = 0; i < numx; i++) {
            for (int j = 0; j < numy; j++) {

                if (celas[i][j].getEstado() == Cela.estadocell.VAZIO && celas[i][j].getValor() == -1) {

                    botoes[i][j].setText("\uD83D\uDCA3");
                    botoes[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FB8500")));
                }
                celas[i][j].setEstado(Cela.estadocell.CLICADO);
            }
        }
    }


    public void criarMatrizCelas(int a, int b) {

        int numx = this.numx;
        int numy = this.numy;

        Random random = new Random();

        // cria a matriz com -1 e 0
        int cont = numb;
        for (int i = 0; i < numx; i++) {
            for (int j = 0; j < numy; j++) {

                int numero_random = random.nextInt(numx * numy);

                if ((numero_random < numb || (((numx * numy) - 1 <= cont + i * numy + j))) && (cont != 0) && !(i == a && j == b)) {
                    matriz_[i][j] = -1;
                    cont--;
                } else {
                    matriz_[i][j] = 0;
                }
            }
        }
        int soma = 0;

        //crai a matriz com as somas

        for (int i = 0; i < numx; i++) {
            for (int j = 0; j < numy; j++) {
                Cela cela = new Cela();

                if (matriz_[i][j] != -1) {

                    int x1, x2, y1, y2;
                    x1 = i > 0 ? i - 1 : i;
                    x2 = i < numx - 1 ? i + 1 : i;
                    y1 = j > 0 ? j - 1 : j;
                    y2 = j < numy - 1 ? j + 1 : j;
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
    }

    private void atualizarText(){
        bandeiras.setText(String.format("Bandeiras: %d", numBandeiras));
        clicks.setText(String.format("Clicks: %d",numClicks));
    }


    public void recrearJogo(View v) {
        recreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tlGrid.setOrientation(LinearLayout.HORIZONTAL);
            for (LinearLayout x : lLLinhas) {
                x.setOrientation(LinearLayout.VERTICAL);
                x.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            tlGrid.setOrientation(LinearLayout.VERTICAL);
            for (LinearLayout x : lLLinhas) {
                x.setOrientation(LinearLayout.HORIZONTAL);
                x.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    private void conferirOrientacao() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tlGrid.setOrientation(LinearLayout.HORIZONTAL);
            for (LinearLayout x : lLLinhas) {
                x.setOrientation(LinearLayout.VERTICAL);
                x.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tlGrid.setOrientation(LinearLayout.VERTICAL);
            for (LinearLayout x : lLLinhas) {
                x.setOrientation(LinearLayout.HORIZONTAL);
                x.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    private void startTimer(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        tvTimer.setText(getTimerTick());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerTick() {
        int arredondar =(int) Math.round(time);
        int seg = ((arredondar % 86400) % 3600) % 60;
        int min = ((arredondar % 86400) % 3600) / 60;
        return formatarTime(seg, min);
    }

    private String formatarTime(int seg, int min){
        return String.format("%02d:%02d", min, seg);
    }

    private void timeAndDay(){

        TimerTask ttData = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    final Calendar cal = Calendar.getInstance();
                    @Override
                    public void run() {
                        hora.setText(String.format("Hora: %02d:%02d:%02d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
                        dia.setText(String.format("Data: %02d/%02d/%04d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)));
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(ttData, 0, 1000);
    }

    private void adicionarVitoria(){
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Contrato.Colunas.TABLE_CLICKS, String.valueOf(numClicks));
            valores.put(Contrato.Colunas.TABLE_BANDEIRAS, String.valueOf(numBandeiras));
            valores.put(Contrato.Colunas.TABLE_TEMPO, tvTimer.getText().toString());
            valores.put(Contrato.Colunas.TABLE_DATA, dia.getText().toString());
            valores.put(Contrato.Colunas.TABLE_HORA, hora.getText().toString());

            long newRowId = db.insert(Contrato.Colunas.TABLE_NAME, null, valores);

            if (newRowId == -1) {
                // Se o ID da nova linha for -1, a inserção falhou
                Toast.makeText(this, "Erro ao adicionar vitória.", Toast.LENGTH_SHORT).show();
            } else {
                // A inserção foi bem-sucedida
                Toast.makeText(this, "Vitória adicionada com sucesso!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Captura qualquer exceção que ocorra durante o processo
            Toast.makeText(this, "Erro ao adicionar vitória: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


}