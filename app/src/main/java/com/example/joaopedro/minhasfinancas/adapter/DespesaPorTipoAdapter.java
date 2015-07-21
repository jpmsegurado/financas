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

public class DespesaPorTipoAdapter extends CursorAdapter {


    private String[] color,tipo;
    public DespesaPorTipoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        color = context.getResources().getStringArray(R.array.tipos_despesas_color);
        tipo = context.getResources().getStringArray(R.array.tipos_despesas);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inf.inflate(R.layout.item_despesa_por_tipo,parent,false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if(view != null){
            TextView txvTipo,total;
            txvTipo = (TextView) view.findViewById(R.id.tipo);
            total = (TextView) view.findViewById(R.id.total);

            int tipo = cursor.getInt(cursor.getColumnIndex("tipo"));

            double val = cursor.getDouble(cursor.getColumnIndex("valor"));
            DecimalFormat dec = new DecimalFormat("R$ #.##");
            dec.setMinimumFractionDigits(2);

            String strValor = dec.format(val);

            total.setText(strValor);

            ImageView image = (ImageView) view.findViewById(R.id.image);

            LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
            GradientDrawable shape = (GradientDrawable) ll.getBackground();
            shape.setColor(Color.parseColor(color[tipo]));
            txvTipo.setText(this.tipo[tipo]);

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

    }
}
