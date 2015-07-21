package com.example.joaopedro.minhasfinancas.fragment;


import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
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

import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.ReceitaActivity;
import com.example.joaopedro.minhasfinancas.adapter.ReceitaAdapter;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class ListaReceitasFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public ListaReceitasFragment() {}

    private ListView list;
    private ReceitaActivity ac;
    private FloatingActionButton fab;
    private int tipo = Contract.receita_continua;
    private TextView txvTipo,nenhum;
    private ImageButton anterior,proximo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_receitas, container, false);

        list = (ListView) view.findViewById(R.id.list);
        ac = (ReceitaActivity) getActivity();
        getLoaderManager().initLoader(Provider.receita,null,this);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.showInsere(null);
            }
        });

        txvTipo = (TextView)view.findViewById(R.id.tipo);
        nenhum = (TextView)view.findViewById(R.id.nenhum);
        anterior = (ImageButton) view.findViewById(R.id.anterior);
        proximo = (ImageButton) view.findViewById(R.id.proximo);

        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipo == Contract.receita_continua){
                    tipo = Contract.receita_nao_continua;
                    txvTipo.setText("Receitas não fixas");
                }else{
                    tipo = Contract.receita_continua;
                    txvTipo.setText("Receitas fixas");
                }
                getLoaderManager().restartLoader(Provider.receita, null, ListaReceitasFragment.this);
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipo == Contract.receita_continua) {
                    tipo = Contract.receita_nao_continua;
                    txvTipo.setText("Receitas não fixas");
                } else {
                    tipo = Contract.receita_continua;
                    txvTipo.setText("Receitas fixas");
                }
                getLoaderManager().restartLoader(Provider.receita, null, ListaReceitasFragment.this);
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
                        Uri uri = Uri.parse(Provider.URL + Contract.receita);
                        ac.getContentResolver().delete(uri, " _id = ? ", new String[]{_id});
                        getLoaderManager().restartLoader(Provider.receita, null, ListaReceitasFragment.this);
                    }
                });
                builder.setNegativeButton("não", null);
                builder.create().show();
                return true;
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(Provider.receita, null, this);
        ac.getSupportActionBar().setTitle("Todos as receitas");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getLoaderManager().restartLoader(Provider.receita,null,this);
            ac.getSupportActionBar().setTitle(R.string.title_activity_receita);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where = "tipo = ?";
        Uri uri = null;
        switch(id){
            case Provider.receita:
                uri = Uri.parse(Provider.URL+ Contract.receita);
                break;
        }
        CursorLoader loader = new CursorLoader(ac,uri,new String[]{"*"},where,new String[]{String.valueOf(tipo)},"ORDER BY _id DESC");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();

        switch(id){
            case Provider.receita:
                list.setAdapter(new ReceitaAdapter(getActivity(),data,0));
                if(data.getCount() == 0){
                    nenhum.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(ac, R.anim.fade_in);
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

}
