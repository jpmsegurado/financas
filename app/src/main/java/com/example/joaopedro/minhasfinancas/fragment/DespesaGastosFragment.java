package com.example.joaopedro.minhasfinancas.fragment;


import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.AnaliseActivity;
import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.adapter.DespesaPorTipoAdapter;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DespesaGastosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public DespesaGastosFragment() {}

    private ListView list;
    private PieChartView pie;
    private AnaliseActivity ac;
    private TextView titulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_despesa_gastos, container, false);

        list = (ListView) view.findViewById(R.id.list);
        pie = (PieChartView)view.findViewById(R.id.pie);
        ac = (AnaliseActivity) getActivity();
        titulo = (TextView) view.findViewById(R.id.titulo);
        getLoaderManager().initLoader(Provider.despesa,null,this);
        getLoaderManager().initLoader(Provider.receita,null,this);

        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH);

        String[] meses = getResources().getStringArray(R.array.meses);
        titulo.setText("Total despesa/tipo - " + meses[mes]);

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case Provider.despesa:
                Calendar cal = Calendar.getInstance();
                String[] proj = new String[]{"tipo","sum(valor) as valor","_id"};
                String where = "data >= ? AND data <= ? GROUP BY tipo";
                int mes = cal.get(Calendar.MONTH)+1;
                int ano = cal.get(Calendar.YEAR);
                int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                String first = "",last = "";

                if(mes < 10){
                    first = "01"+"/0"+mes+"/"+ano;
                    last = lastDay+"/0"+mes+"/"+ano;
                }else{
                    first = "01"+"/"+mes+"/"+ano;
                    last = lastDay+"/"+mes+"/"+ano;
                }

                String[] whereArgs = new String[]{first,last};
                Uri uri = Uri.parse(Provider.URL+ Contract.despesa);
                CursorLoader loader = new CursorLoader(getActivity(),uri,proj,where,whereArgs,null);
                return loader;
            case Provider.receita:
                break;

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case Provider.despesa:
                List<SliceValue> values = new ArrayList<SliceValue>();
                String[] colors = getResources().getStringArray(R.array.tipos_despesas_color);
                while(data.moveToNext()){
                    int tipo = data.getInt(data.getColumnIndex("tipo"));
                    float valor = data.getFloat(data.getColumnIndex("valor"));
                    SliceValue value = new SliceValue(valor, Color.parseColor(colors[tipo]));
                    Log.d("valor",String.valueOf(valor));
                    values.add(value);
                }
                PieChartData dados = new PieChartData(values);
                dados.setHasLabels(true);
                dados.setHasLabelsOutside(false);
                pie.setPieChartData(dados);
                list.setAdapter(new DespesaPorTipoAdapter(ac,data,0));

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public String formataDataSQL(String data){
        // dd/mm/aaaa
        // 0123456789

        String dia = data.substring(0,2);
        String mes = data.substring(3,5);
        String ano = data.substring(6);
        return ano+"-"+mes+"-"+dia;
    }
}
