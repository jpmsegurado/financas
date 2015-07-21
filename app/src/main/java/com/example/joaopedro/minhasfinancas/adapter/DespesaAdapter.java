package com.example.joaopedro.minhasfinancas.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.R;

import java.text.DecimalFormat;

public class DespesaAdapter extends CursorAdapter {

    private String[] color,tipo;

    public DespesaAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        color = context.getResources().getStringArray(R.array.tipos_despesas_color);
        tipo = context.getResources().getStringArray(R.array.tipos_despesas);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.item_despesa,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView desc,data,id;
        ImageView image;

        desc = (TextView) view.findViewById(R.id.desc);
        data = (TextView) view.findViewById(R.id.data);
        id = (TextView) view.findViewById(R.id.id);
        image = (ImageView) view.findViewById(R.id.image);

        double val = cursor.getDouble(cursor.getColumnIndex("valor"));
        DecimalFormat dec = new DecimalFormat("R$ #.##");
        dec.setMinimumFractionDigits(2);

        String total = dec.format(val);
        int tipo = cursor.getInt(cursor.getColumnIndex("tipo"));

        desc.setText(cursor.getString(cursor.getColumnIndex("descricao")) + " - " + total);
        String date = cursor.getString(cursor.getColumnIndex("data"));
        id.setText(cursor.getString(cursor.getColumnIndex("_id")));
        date = formataDataNormal(date);
        data.setText(this.tipo[tipo]+" - "+date);
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
        GradientDrawable shape = (GradientDrawable) ll.getBackground();
        shape.setColor(Color.parseColor(color[tipo]));


        switch(tipo){
            case 0:
                image.setImageResource(R.drawable.ic_school_white_48dp);
                break;
            case 1:
                image.setImageResource(R.drawable.ic_description_white_48dp);
                break;
            case 2:
                image.setImageResource(R.drawable.ic_restaurant_menu_white_48dp);
                break;
            case 3:
                image.setImageResource(R.drawable.ic_home_white_48dp);
                break;
            case 4:
                image.setImageResource(R.drawable.ic_accessibility_white_48dp);
                break;
        }


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
