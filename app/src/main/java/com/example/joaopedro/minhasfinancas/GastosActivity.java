package com.example.joaopedro.minhasfinancas;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.joaopedro.minhasfinancas.fragment.InsereGastosFragment;
import com.example.joaopedro.minhasfinancas.fragment.ListBasicFragment;


public class GastosActivity extends AppCompatActivity {

    private InsereGastosFragment insereFragment = new InsereGastosFragment();
    private ListBasicFragment    listBasicFragment = new ListBasicFragment();
    private Toolbar toolbar;
    private static final String show_insere = "showInsere";
    private static final String show_lista = "showInsere";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        if(savedInstanceState != null){
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, listBasicFragment);
        ft.commit();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_gastos);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

    }

    public void showInsere(final String id){
        FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
        ftt.setCustomAnimations(R.anim.right_back, R.anim.left_back, R.anim.left, R.anim.right);
        ftt.addToBackStack(show_insere);
        ftt.replace(R.id.container, insereFragment);
        ftt.commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id != null) {
                    insereFragment.carregaItem(id);
                }else{
                    insereFragment.limpaCampos();
                }
            }
        },300);

    }



    public void showAlert(String msg,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("ok", listener);
        builder.create().show();
    }

    public Toolbar getToolbar(){
        return toolbar;
    }

    public FragmentManager getFm(){
        return getSupportFragmentManager();
    }
}
