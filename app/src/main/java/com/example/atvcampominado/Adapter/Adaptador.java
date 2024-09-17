package com.example.atvcampominado.Adapter;

import static com.example.atvcampominado.R.layout.item_ranque;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atvcampominado.Objetos.Vitoria;
import com.example.atvcampominado.R;

public class Adaptador extends RecyclerView.Adapter<Adaptador.Vholder> {

    private Vitoria[] vitorias;

    public static class Vholder extends RecyclerView.ViewHolder {
        final private TextView rank;
        final private TextView tempo;
        final private TextView click;
        final private TextView bandeira;
        final private TextView data;
        final private TextView hora;
        final private ConstraintLayout cmain;

        public TextView getRank() {
            return rank;
        }

        public TextView getTempo() {
            return tempo;
        }

        public TextView getClick() {
            return click;
        }

        public TextView getBandeira() {
            return bandeira;
        }

        public TextView getData() {
            return data;
        }

        public TextView getHora() {
            return hora;
        }

        public ConstraintLayout getCmain() { return cmain; }

        public Vholder(@NonNull View itemView) {
            super(itemView);

            rank = (TextView) itemView.findViewById(R.id.tvRank);
            tempo = (TextView) itemView.findViewById(R.id.tvRTempo);
            click = (TextView) itemView.findViewById(R.id.tvRClick);
            bandeira = (TextView) itemView.findViewById(R.id.tvRBandeira);
            data = (TextView) itemView.findViewById(R.id.tvRData);
            hora = (TextView) itemView.findViewById(R.id.tvRHora);
            cmain = (ConstraintLayout) itemView.findViewById(R.id.cLmain);

        }
    }

    public Adaptador(Vitoria[] vit) {
        if (vit == null) {
            vitorias = new Vitoria[0]; // Inicializa com um array vazio para evitar NullPointerException
        } else {
            vitorias = vit;
        }
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(item_ranque, parent, false);

        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, int position) {

        holder.getBandeira().setText(String.valueOf(String.format(
                "N° de Bandeiras: %d", vitorias[position].getBandeiras())));
        holder.getClick().setText(String.valueOf(String.format(
                "N° de clicks: %d", vitorias[position].getClicks())));
        holder.getRank().setText(String.valueOf(String.format(
                "%d", position + 1)));
        holder.getData().setText(vitorias[position].getData());
        holder.getHora().setText(vitorias[position].getHora());
        holder.getTempo().setText(vitorias[position].getTempoDeJogo());
        switch (position) {
            case 0:
                holder.getRank().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF00")));
                holder.getRank().setTextColor(Color.parseColor("#000000"));
                break;
            case 1:
                holder.getRank().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D7D7D7")));
                holder.getRank().setTextColor(Color.parseColor("#000000"));

                break;
            case 2:
                holder.getRank().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6A3805")));
                holder.getRank().setTextColor(Color.parseColor("#000000"));

                break;
            default:
                holder.getRank().setBackgroundTintList(null);
                holder.getRank().setTextColor(Color.parseColor("#FFFFFF"));
        }
        if(position % 2 != 0){
            holder.getCmain().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#58595B")));
        }else{
            holder.getCmain().setBackgroundTintList(null);
        }
    }

    @Override
    public int getItemCount() {
        return vitorias.length;
    }

}
