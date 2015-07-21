package com.example.joaopedro.minhasfinancas.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.data.Contract;

import java.text.DecimalFormat;

public class ReceitaAdapter extends CursorAdapter {

    public ReceitaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.item_receita,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        double val = cursor.getDouble(cursor.getColumnIndex("valor"));
        String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
        String fixo = "";

        int tipo = cursor.getInt(cursor.getColumnIndex("tipo"));


        switch (tipo){
            case Contract.receita_continua:
                fixo = "Fixo";
                break;
            case Contract.receita_nao_continua:
                String data = cursor.getString(cursor.getColumnIndex("data"));
                TextView txvData = (TextView) view.findViewById(R.id.data);
                txvData.setText("Recebimento em "+formataDataNormal(data));
                txvData.setVisibility(View.VISIBLE);
                fixo = "Não fixo";
                break;
        }

        DecimalFormat dec = new DecimalFormat("R$ #.##");
        dec.setMinimumFractionDigits(2);
        String total = dec.format(val);

        TextView valor,desc,id;
        valor = (TextView) view.findViewById(R.id.valor);
        desc = (TextView) view.findViewById(R.id.desc);
        id = (TextView) view.findViewById(R.id.id);

        id.setText(cursor.getString(cursor.getColumnIndex("_id")));

        valor.setText(fixo+" - "+total);
        desc.setText(descricao);
    }

    public String formataDataNormal(String data){
        // aaaa-mm-dd
        // 0123456789

        String dia = data.substring(8);
        String mes = data.substring(5,7);
        String ano = data.substring(0,4);
        return dia+"/"+mes+"/"+ano;
    }
}
