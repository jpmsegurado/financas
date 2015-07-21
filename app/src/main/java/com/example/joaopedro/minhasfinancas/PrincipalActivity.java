package com.example.joaopedro.minhasfinancas;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.joaopedro.minhasfinancas.data.Provider;
import com.example.joaopedro.minhasfinancas.fragment.InsereGastosFragment;
import com.example.joaopedro.minhasfinancas.fragment.InsereReceitaFragment;
import com.example.joaopedro.minhasfinancas.fragment.MainFragment;


public class PrincipalActivity extends AppCompatActivity {

    private FragmentManager fm;
    private MainFragment mainFragment;
    private ActionBar bar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        fm = getSupportFragmentManager();
        mainFragment = new MainFragment();
        bar = getSupportActionBar();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, mainFragment);
        ft.commit();

    }


    public void showAlert(String msg,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("ok", listener);
        builder.create().show();
    }



    public ActionBar getBar() {
        return bar;
    }

    public void showFragmentGastos(){
        Intent it = new Intent(PrincipalActivity.this,GastosActivity.class);
        it.putExtra("action","inserir");
        startActivity(it);
        overridePendingTransition(R.anim.open_translate,R.anim.close_scale);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
        mainFragment.getLoaderManager().restartLoader(Provider.despesa, null, mainFragment);
        mainFragment.getLoaderManager().restartLoader(Provider.receita, null, mainFragment);
    }

    public void showFragmentReceita(){
        Intent it = new Intent(PrincipalActivity.this,ReceitaActivity.class);
        it.putExtra("action", "inserir");
        startActivity(it);
        overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
    }


    public void showReceitas(){
        Intent it = new Intent(PrincipalActivity.this,ReceitaActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
    }

    public void showGastos(){
        Intent it = new Intent(PrincipalActivity.this,GastosActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
    }

    public void showAnalise(){
        Intent it = new Intent(PrincipalActivity.this, AnaliseActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
    }


    public FragmentManager getFm() {
        return fm;
    }
}
