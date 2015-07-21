package com.example.joaopedro.minhasfinancas.fragment;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.GastosActivity;
import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.adapter.TipoDespesaAdapter;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;

import java.util.Calendar;

public class InsereGastosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public InsereGastosFragment() {}

    private Button inserir;
    private GastosActivity ac;
    private EditText desc,valor,data;
    private Spinner spinner;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private TextView txvId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insere_gastos, container, false);

        desc = (EditText)view.findViewById(R.id.desc);
        valor = (EditText)view.findViewById(R.id.valor);
        data = (EditText)view.findViewById(R.id.data);
        txvId = (TextView)view.findViewById(R.id.id);
        inserir = (Button)view.findViewById(R.id.inserir);
        ac = (GastosActivity) getActivity();
        spinner = (Spinner)view.findViewById(R.id.tipo);
        spinner.setAdapter(new TipoDespesaAdapter(ac));

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dia,mes;
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                myCalendar.set(Calendar.MONTH,monthOfYear);
                myCalendar.set(Calendar.YEAR,year);
                if(dayOfMonth < 10){
                    dia = "0"+dayOfMonth;
                }else{
                    dia = String.valueOf(dayOfMonth);
                }

                if(monthOfYear+1 <= 9){
                    mes = "0"+(monthOfYear+1);
                }else{
                    mes = String.valueOf(monthOfYear+1);
                }
                data.setText(dia+"/"+mes+"/"+year);
            }

        };

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.hasFocus()){
                    new DatePickerDialog(ac, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    InputMethodManager imm = (InputMethodManager)ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(data.getWindowToken(), 0);
                }
            }
        });
        data.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new DatePickerDialog(ac, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    InputMethodManager imm = (InputMethodManager)ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(data.getWindowToken(), 0);
                }
            }
        });

        inserir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                inserirGasto();
            }

        });

        ac.getToolbar().setTitle("Novo gasto");

        return view;
    }



    public void inserirGasto(){
        String desc = this.desc.getText().toString();
        final String valor= this.valor.getText().toString();
        final String data = this.data.getText().toString();

        if(valor.isEmpty() || desc.isEmpty() || data.length() < 10){
            ac.showAlert("Por favor preencha os campos vazios.",null);
        }else{
            ContentValues values = new ContentValues();
            values.put("descricao",desc);
            values.put("tipo", spinner.getSelectedItemPosition());
            values.put("data", formataDataSQL(data));
            values.put("valor", valor);
            Uri uri = Uri.parse(Provider.URL + Contract.despesa);
            Uri _uri = null;
            if(txvId.getText().toString().isEmpty()){
                _uri = ac.getContentResolver().insert(uri,values);
                if(_uri != null){
                    InputMethodManager imm = (InputMethodManager)ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(this.data.getWindowToken(), 0);
                    this.data.setText("");
                    this.valor.setText("");
                    this.desc.setText("");
                    ac.showAlert("Inserido com sucesso", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ac.getFm().popBackStack();
                        }
                    });
                }
            }else{
                String _id = txvId.getText().toString();
                int row = ac.getContentResolver().update(uri,values," _id = ? ",new String[]{_id});
                if(row > 0){
                    InputMethodManager imm = (InputMethodManager)ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(this.data.getWindowToken(), 0);
                    limpaCampos();
                    ac.showAlert("Inserido com sucesso", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ac.getFm().popBackStack();
                        }
                    });
                }
            }

        }
    }

    public void limpaCampos(){
        this.data.setText("");
        this.valor.setText("");
        this.desc.setText("");
        this.txvId.setText("");
        this.spinner.setSelection(0);
    }
    public void carregaItem(String id){
        Bundle args = new Bundle();
        args.putString("_id", id);
        txvId.setText(id);
        getLoaderManager().initLoader(Provider.despesa, args, this);
    }

    public String formataDataSQL(String data){
        // dd/mm/aaaa
        // 0123456789

        String dia = data.substring(0,2);
        String mes = data.substring(3,5);
        String ano = data.substring(6);
        return ano+"-"+mes+"-"+dia;
    }

    public String formataDataNormal(String data){
        // aaaa-mm-dd
        // 0123456789

        String dia = data.substring(8);
        String mes = data.substring(5,7);
        String ano = data.substring(0,4);
        return dia+"/"+mes+"/"+ano;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch(id){
            case Provider.despesa:
                Uri uri = Uri.parse(Provider.URL+Contract.despesa);
                String _id = args.getString("_id");
                CursorLoader loader = new CursorLoader(ac,uri,null,"_id = ?",new String[]{_id},null);
                return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        switch(loader.getId()){
            case Provider.despesa:
                cursor.moveToFirst();
                this.data.setText(formataDataNormal(cursor.getString(cursor.getColumnIndex("data"))));
                this.valor.setText(cursor.getString(cursor.getColumnIndex("valor")));
                this.desc.setText(cursor.getString(cursor.getColumnIndex("descricao")));
                this.spinner.setSelection(cursor.getInt(cursor.getColumnIndex("tipo")),true);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
