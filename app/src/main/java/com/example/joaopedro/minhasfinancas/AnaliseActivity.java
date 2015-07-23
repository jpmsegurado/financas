package com.example.joaopedro.minhasfinancas;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.joaopedro.minhasfinancas.fragment.AnaliseFragment;
import com.example.joaopedro.minhasfinancas.fragment.DespesaGastosFragment;
import com.example.joaopedro.minhasfinancas.fragment.MainAnaliseFragment;


public class AnaliseActivity extends AppCompatActivity {

    private DespesaGastosFragment despesaGastosFragment = new DespesaGastosFragment();
    private AnaliseFragment analiseFragment = new AnaliseFragment();
    private MainAnaliseFragment mainAnaliseFragment = new MainAnaliseFragment();
    private static final String showDespesa = "showDespesa";
    private static final String showMain = "showMain";
    private static final String showAnalise = "showAnalise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analise);

        if(savedInstanceState != null){
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, mainAnaliseFragment);
        ft.commit();


    }

    public void showAnalise(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_back, R.anim.left_back, R.anim.left, R.anim.right);
        ft.replace(R.id.container, analiseFragment);
        ft.addToBackStack(showAnalise);
        ft.commit();
    }

    public void showDespesaGastos(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_back, R.anim.left_back, R.anim.left, R.anim.right);
        ft.replace(R.id.container, despesaGastosFragment);
        ft.addToBackStack(showDespesa);
        ft.commit();
    }

    public void showAlert(String msg,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("ok", listener);
        builder.create().show();
    }

}
