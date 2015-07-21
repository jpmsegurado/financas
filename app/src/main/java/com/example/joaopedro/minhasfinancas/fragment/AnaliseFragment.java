package com.example.joaopedro.minhasfinancas.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.AnaliseActivity;
import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.adapter.AnaliseAdapter;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class AnaliseFragment extends Fragment {


    public AnaliseFragment() {}

    private AnaliseActivity ac;
    private PieChartView pie;
    private ListView list;
    private TextView titulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analise, container, false);

        ac = (AnaliseActivity) getActivity();
        pie = (PieChartView) view.findViewById(R.id.pie);
        list = (ListView) view.findViewById(R.id.list);
        titulo = (TextView)view.findViewById(R.id.titulo);
        Calendar c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH);

        String[] meses = getResources().getStringArray(R.array.meses);
        titulo.setText("Orientações - "+meses[mes]);

        iniciaPieChart();

        return view;
    }

    public double getTotalReceita(){
        Uri uri = Uri.parse(Provider.URL + Contract.receita);
        String[] proj = new String[]{"sum(valor) as valor"};

        Calendar cal = Calendar.getInstance();
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

        String where = "tipo = ? OR (tipo = ? AND data >= ? AND data <= ?)";
        String cont = String.valueOf(Contract.receita_continua);
        String naoCont = String.valueOf(Contract.receita_nao_continua);
        String[] whereArgs = new String[]{cont,naoCont,formataDataSQL(first),formataDataSQL(last)};

        Cursor receita =  ac.getContentResolver().query(uri, proj, where, whereArgs, null);
        receita.moveToFirst();
        double totalReceita = receita.getDouble(receita.getColumnIndex("valor"));

        return totalReceita;
    }

    public double getTotalDespesas(){

        String[] proj = new String[]{"sum(valor) as valor"};

        Calendar cal = Calendar.getInstance();
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

        String where = "data >= ? AND data <= ?";
        String[] whereArgs = new String[]{formataDataSQL(first),formataDataSQL(last)};
        Uri uri = Uri.parse(Provider.URL+ Contract.despesa);


        Cursor receita =  ac.getContentResolver().query(uri, proj, where, whereArgs, null);
        receita.moveToFirst();
        double totalDespesa = receita.getDouble(receita.getColumnIndex("valor"));

        return totalDespesa;
    }

    public void iniciaPieChart(){
        double totalReceita = getTotalReceita();
        double totalDespesa = getTotalDespesas();

        double saveMoney = 0.1*totalReceita;
        double yourselfMoney = 0.05*totalReceita;

        ArrayList<Double> list = new ArrayList<>();
        list.add(totalReceita);
        list.add(totalDespesa);
        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue(Float.valueOf(String.valueOf(totalDespesa)),ac.getResources().getColor(R.color.perigo_light)));
        if(totalReceita - totalDespesa - saveMoney - yourselfMoney > 0){
            double restante = totalReceita - totalDespesa - saveMoney - yourselfMoney;
            list.add(saveMoney);
            list.add(yourselfMoney);
            list.add(restante);
            values.add(new SliceValue(Float.valueOf(String.valueOf(saveMoney)),ac.getResources().getColor(R.color.sucesso)));
            values.add(new SliceValue(Float.valueOf(String.valueOf(yourselfMoney)), ac.getResources().getColor(R.color.colorPrimary)));
            values.add(new SliceValue(Float.valueOf(String.valueOf(restante)), ac.getResources().getColor(R.color.amber)));
        }



        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOutside(false);
        pie.setPieChartData(data);
        this.list.setAdapter(new AnaliseAdapter(ac,list));

    }

    public String formataDataSQL(String data){
        // dd/mm/aaaa
        // 0123456789

        String dia = data.substring(0, 2);
        String mes = data.substring(3,5);
        String ano = data.substring(6);
        return ano+"-"+mes+"-"+dia;
    }


}
