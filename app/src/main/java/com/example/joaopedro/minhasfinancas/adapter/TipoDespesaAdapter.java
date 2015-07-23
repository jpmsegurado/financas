package com.example.joaopedro.minhasfinancas.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by joao.pedro on 16/07/2015.
 */
public class TipoDespesaAdapter extends BaseAdapter{

    private Context ctx;
    private String[] tipos,icons,color;

    public TipoDespesaAdapter(Context ctx){
        this.ctx = ctx;
        tipos = ctx.getResources().getStringArray(R.array.tipos_despesas);
        icons = ctx.getResources().getStringArray(R.array.tipos_despesas_icon);
        color = ctx.getResources().getStringArray(R.array.tipos_despesas_color);
    }

    @Override
    public int getCount() {
        return tipos.length;
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
            view = inf.inflate(R.layout.item_tipo_despesa,parent,false);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView tipo = (TextView)view.findViewById(R.id.tipo);
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);
        GradientDrawable shape = (GradientDrawable) ll.getBackground();
        shape.setColor(Color.parseColor(color[position]));

        tipo.setText(tipos[position]);

        switch(position){
            case 0:
                image.setImageResource(R.drawable.ic_school_white_48dp);
                break;
            case 1:
                image.setImageResource(R.drawable.ic_directions_bus_white_48dp);
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
            case 5:
                image.setImageResource(R.drawable.ic_local_hospital_white_48dp);
                break;
            case 6:
                image.setImageResource(R.drawable.ic_indeterminate_check_box_white_24dp);
                break;
        }

        return view;
    }
}
