package com.example.joaopedro.minhasfinancas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.R;

/**
 * Created by joao.pedro on 20/07/2015.
 */
public class MenuRelatorioAdapter extends BaseAdapter {

    private Context ctx;
    private String[] menu,desc;
    public MenuRelatorioAdapter(Context ctx){
        this.ctx = ctx;
        menu = ctx.getResources().getStringArray(R.array.menu_relatorios);
        desc = ctx.getResources().getStringArray(R.array.menu_relatorios_desc);
    }

    @Override
    public int getCount() {
        return menu.length;
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
            view = inf.inflate(R.layout.item_list_relatorio,parent,false);
        }

        TextView nome,desc;
        nome = (TextView) view.findViewById(R.id.nome);
        desc = (TextView)view.findViewById(R.id.desc);

        nome.setText(menu[position]);
        desc.setText(this.desc[position]);

        return view;
    }
}
