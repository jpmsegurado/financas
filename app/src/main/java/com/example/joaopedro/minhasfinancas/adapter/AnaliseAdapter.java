package com.example.joaopedro.minhasfinancas.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AnaliseAdapter extends BaseAdapter {

    private ArrayList<Double> list;
    private Context ctx;

    public AnaliseAdapter(Context ctx, ArrayList<Double> list){
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view == null){
            LayoutInflater inf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_despesa_por_tipo,parent,false);
        }

        LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
        GradientDrawable shape = (GradientDrawable) ll.getBackground();
        TextView tipo = (TextView) view.findViewById(R.id.tipo);
        TextView total = (TextView) view.findViewById(R.id.total);

        DecimalFormat dec = new DecimalFormat("R$ #.##");
        dec.setMinimumFractionDigits(2);
        total.setText(dec.format(list.get(position)));
        switch(position){
            case 0:
                tipo.setText("Valor total das receitas");
                ll.setVisibility(View.GONE);
                break;
            case 1:
                tipo.setText("Valor total das despesas");
                ll.setVisibility(View.VISIBLE);
                shape.setColor(ctx.getResources().getColor(R.color.perigo_light));
                break;
            case 2:
                tipo.setText("Valor a ser guardado");
                ll.setVisibility(View.VISIBLE);
                shape.setColor(ctx.getResources().getColor(R.color.sucesso));
                break;
            case 3:
                tipo.setText("Valor a ser investido em você");
                ll.setVisibility(View.VISIBLE);
                shape.setColor(ctx.getResources().getColor(R.color.colorPrimary));
                break;
            case 4:
                tipo.setText("Valor destinado à lazer");
                ll.setVisibility(View.VISIBLE);
                shape.setColor(ctx.getResources().getColor(R.color.amber));
                break;
        }



        return view;
    }
}
