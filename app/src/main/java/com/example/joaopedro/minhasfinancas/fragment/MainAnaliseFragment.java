package com.example.joaopedro.minhasfinancas.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.joaopedro.minhasfinancas.AnaliseActivity;
import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.adapter.MenuRelatorioAdapter;

public class MainAnaliseFragment extends Fragment {


    public MainAnaliseFragment() {}

    private AnaliseActivity ac;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_analise, container, false);

        ac = (AnaliseActivity) getActivity();
        list = (ListView) view.findViewById(R.id.list);

        list.setAdapter(new MenuRelatorioAdapter(ac));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        ac.showAnalise();
                        break;
                    case 1:
                        ac.showDespesaGastos();
                        break;
                }
            }
        });

        return view;
    }


}
