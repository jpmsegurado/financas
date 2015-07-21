package com.example.joaopedro.minhasfinancas.fragment;


import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.GastosActivity;
import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.adapter.DespesaAdapter;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ListBasicFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public ListBasicFragment() {}
    private GastosActivity ac;
    private ListView list;
    private ImageButton anterior,proximo;
    private TextView mes,nenhum;
    private int month,ano;
    private String[] meses;
    private Calendar cal;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_basic, container, false);

        ac = (GastosActivity) getActivity();
        list = (ListView)view.findViewById(R.id.list);
        anterior = (ImageButton) view.findViewById(R.id.anterior);
        proximo = (ImageButton) view.findViewById(R.id.proximo);
        mes = (TextView)view.findViewById(R.id.mes);
        ac.getToolbar().setTitle("Todos os gastos");
        nenhum = (TextView)view.findViewById(R.id.nenhum);
        cal = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        ano = c.get(Calendar.YEAR);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        this.meses = ac.getResources().getStringArray(R.array.meses);
        mes.setText(meses[month]);

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diminuiMes();
            }
        });

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aumentaMes();
            }
        });

        getLoaderManager().initLoader(Provider.despesa, null, this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.showInsere(null);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txvId = (TextView) view.findViewById(R.id.id);
                String _id = txvId.getText().toString();
                Log.d("_id", _id);
                ac.showInsere(_id);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long Id) {
                TextView id = (TextView) v.findViewById(R.id.id);
                final String _id = id.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(ac);
                builder.setMessage("Deseja realmente excluir este item?");
                builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(Provider.URL + Contract.despesa);
                        ac.getContentResolver().delete(uri, " _id = ? ", new String[]{_id});
                        getLoaderManager().restartLoader(Provider.despesa, null, ListBasicFragment.this);
                    }
                });
                builder.setNegativeButton("não", null);
                builder.create().show();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(Provider.despesa, null, this);
        ac.getToolbar().setTitle("Todos os gastos");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getLoaderManager().restartLoader(Provider.despesa, null, this);
            ac.getToolbar().setTitle("Todos os gastos");
        }
    }

    public void diminuiMes(){

        cal.add(Calendar.MONTH,-1);
        int month = cal.get(Calendar.MONTH);
        if(cal.get(Calendar.YEAR) != ano){
            this.mes.setText(meses[month]+" de "+cal.get(Calendar.YEAR));
        }else{
            this.mes.setText(meses[month]);
        }

        getLoaderManager().restartLoader(Provider.despesa, null, this);

    }

    public void aumentaMes(){
        cal.add(Calendar.MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        if(cal.get(Calendar.YEAR) != ano){
            this.mes.setText(meses[month]+" de "+cal.get(Calendar.YEAR));
        }else{
            this.mes.setText(meses[month]);
        }

        getLoaderManager().restartLoader(Provider.despesa, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] proj = new String[]{"*"};
        String where = "data >= ? AND data <= ?";
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

        String[] whereArgs = new String[]{formataDataSQL(first),formataDataSQL(last)};
        Uri uri = null;
        switch(id){
            case Provider.despesa:
                uri = Uri.parse(Provider.URL+ Contract.despesa);
                break;
        }
        CursorLoader loader = new CursorLoader(ac,uri,proj,where,whereArgs,null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();

        switch(id){
            case Provider.despesa:
                list.setAdapter(new DespesaAdapter(getActivity(),data,0));
                if(data.getCount() == 0){
                    nenhum.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(ac,R.anim.fade_in);
                    nenhum.startAnimation(anim);
                    list.setVisibility(View.GONE);
                }else{
                    nenhum.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                }
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
