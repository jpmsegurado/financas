package com.example.joaopedro.minhasfinancas.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.AnaliseActivity;
import com.example.joaopedro.minhasfinancas.GastosActivity;
import com.example.joaopedro.minhasfinancas.PrincipalActivity;
import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.ReceitaActivity;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public MainFragment() {}

    private FloatingActionButton fabReceita,fabDespesa;
    private FloatingActionsMenu menu;
    private PrincipalActivity ac;
    private TextView receita,despesa,salvar,invest,title,title2;
    private RelativeLayout rel_receita,rel_despesa;
    private Button lerMais,verReceitas,verDesesas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        lerMais = (Button) view.findViewById(R.id.lerMais);
        fabDespesa = (FloatingActionButton)view.findViewById(R.id.nova_despesa);
        fabReceita = (FloatingActionButton)view.findViewById(R.id.nova_receita);
        menu = (FloatingActionsMenu)view.findViewById(R.id.menu);
        receita = (TextView)view.findViewById(R.id.receita);
        despesa = (TextView)view.findViewById(R.id.despesa);
        title = (TextView)view.findViewById(R.id.title);
        title2 = (TextView)view.findViewById(R.id.title2);
        verReceitas = (Button)view.findViewById(R.id.verReceitas);
        verDesesas = (Button)view.findViewById(R.id.verDespesas);
        salvar = (TextView)view.findViewById(R.id.salvar);
        invest = (TextView)view.findViewById(R.id.invest);
        ac = (PrincipalActivity) getActivity();
        fabDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ac.showFragmentGastos();
                    }
                },350);
            }
        });

        fabReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ac.showFragmentReceita();
                    }
                }, 350);
            }
        });

        rel_despesa = (RelativeLayout)view.findViewById(R.id.rel_despesas);
        rel_receita = (RelativeLayout)view.findViewById(R.id.rel_receitas);

        rel_receita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), ReceitaActivity.class);
                startActivity(it);
                ac.overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
            }
        });

        rel_despesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), GastosActivity.class);
                startActivity(it);
                ac.overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
            }
        });

        getLoaderManager().initLoader(Provider.despesa, null, this);
        getLoaderManager().initLoader(Provider.receita, null, this);

        lerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.showAnalise();
            }
        });

        verReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.showReceitas();
            }
        });

        verDesesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.showGastos();
            }
        });


        String[] meses = getResources().getStringArray(R.array.meses);

        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        title.setText("Resumo - "+meses[mes]);
        title2.setText("Orientações - "+meses[mes]);

        return view;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] proj = new String[]{"sum(valor)"};

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

        Log.d("first day", formataDataSQL(first));
        Log.d("last day", formataDataSQL(last));
        String where;
        String[] whereArgs;
        Uri uri = null;
        CursorLoader loader = null;
        switch(id){
            case Provider.receita:
                where = "tipo = ? OR (tipo = ? AND data >= ? AND data <= ?)";
                String cont = String.valueOf(Contract.receita_continua);
                String naoCont = String.valueOf(Contract.receita_nao_continua);
                whereArgs = new String[]{cont,naoCont,formataDataSQL(first),formataDataSQL(last)};
                uri = Uri.parse(Provider.URL+ Contract.receita);
                loader = new CursorLoader(ac,uri,proj,where,whereArgs,null);
                return loader;
            case Provider.despesa:
                where = "data >= ? AND data <= ?";
                whereArgs = new String[]{formataDataSQL(first),formataDataSQL(last)};
                uri = Uri.parse(Provider.URL+ Contract.despesa);
                loader = new CursorLoader(ac,uri,proj,where,whereArgs,null);
                return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        data.moveToFirst();
        double valor = data.getDouble(0);

        DecimalFormat dec = new DecimalFormat("R$ #.##");
        dec.setMinimumFractionDigits(2);

        String total = dec.format(valor);

        switch(id){
            case Provider.despesa:

                despesa.setText(total);
                break;
            case Provider.receita:
                double save = valor*0.1;
                double inv = valor*0.05;
                String strSave = dec.format(save);
                String strInv = dec.format(inv);
                invest.setText(strInv);
                salvar.setText(strSave);
                receita.setText(total);
                break;
        }
    }

    public String formataDataSQL(String data){
        // dd/mm/aaaa
        // 0123456789

        String dia = data.substring(0, 2);
        String mes = data.substring(3,5);
        String ano = data.substring(6);

        return ano+"-"+mes+"-"+dia;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
