package com.example.joaopedro.minhasfinancas;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.joaopedro.minhasfinancas.fragment.InsereReceitaFragment;
import com.example.joaopedro.minhasfinancas.fragment.ListaReceitasFragment;


public class ReceitaActivity extends AppCompatActivity {

    private InsereReceitaFragment insereReceitaFragment;
    private ListaReceitasFragment listaReceitasFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        insereReceitaFragment = new InsereReceitaFragment();
        listaReceitasFragment = new ListaReceitasFragment();
        fm = getSupportFragmentManager();


        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, listaReceitasFragment);
        ft.commit();

        try {
            String action = getIntent().getStringExtra("action");
            if (action.equals("inserir")){
                FragmentTransaction ftt = fm.beginTransaction();
                ftt.replace(R.id.container,insereReceitaFragment);
                ftt.commit();
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    public void showInsere(final String id){
        FragmentTransaction ftt = fm.beginTransaction();
        ftt.setCustomAnimations(R.anim.right_back, R.anim.left_back, R.anim.left, R.anim.right);
        ftt.addToBackStack(null);
        ftt.replace(R.id.container, insereReceitaFragment);
        ftt.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id != null) {
                    insereReceitaFragment.carregaItem(id);
                } else {
                    insereReceitaFragment.limpaCampos();
                }
            }
        }, 300);

    }

    public void showListReceita(){
        FragmentTransaction ftt = fm.beginTransaction();
        ftt.setCustomAnimations(R.anim.right_back, R.anim.left_back, R.anim.left, R.anim.right);
        ftt.addToBackStack(null);
        ftt.replace(R.id.container,listaReceitasFragment);
        ftt.commit();
    }

    public FragmentManager getFm() {
        return fm;
    }

    public void showAlert(String msg,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("ok", listener);
        builder.create().show();
    }
}
