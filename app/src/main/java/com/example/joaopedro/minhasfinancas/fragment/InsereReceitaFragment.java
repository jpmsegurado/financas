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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.joaopedro.minhasfinancas.R;
import com.example.joaopedro.minhasfinancas.ReceitaActivity;
import com.example.joaopedro.minhasfinancas.data.Contract;
import com.example.joaopedro.minhasfinancas.data.Provider;

import java.util.Calendar;

public class InsereReceitaFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public InsereReceitaFragment() {}

    private ReceitaActivity ac;
    private EditText valor,desc,data;
    private CheckBox box;
    private Button button;
    private ImageView imageData;
    private TextView txvId;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insere_receita, container, false);
        ac = (ReceitaActivity) getActivity();

        valor = (EditText)view.findViewById(R.id.valor);
        desc = (EditText)view.findViewById(R.id.desc);
        data = (EditText)view.findViewById(R.id.data);
        box = (CheckBox)view.findViewById(R.id.check);
        button = (Button)view.findViewById(R.id.button);
        txvId = (TextView)view.findViewById(R.id.id);
        ac.getSupportActionBar().setTitle("Nova receita");
        imageData = (ImageView) view.findViewById(R.id.imageData);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereReceita();
            }
        });

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
                if (hasFocus) {
                    new DatePickerDialog(ac, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    InputMethodManager imm = (InputMethodManager) ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(data.getWindowToken(), 0);
                }
            }
        });

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imageData.setVisibility(View.VISIBLE);
                    data.setVisibility(View.VISIBLE);
                    data.setText("");
                    int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    int marginRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                    int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
                    int marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) box.getLayoutParams();
                    lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
                    box.setLayoutParams(lp);
                }else{
                    imageData.setVisibility(View.GONE);
                    data.setVisibility(View.GONE);
                    data.setText("");
                    int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    int marginRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                    int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    int marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) box.getLayoutParams();
                    lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
                    box.setLayoutParams(lp);
                }
            }
        });

        return view;
    }

    public void insereReceita(){
        String valor = this.valor.getText().toString();
        String desc = this.desc.getText().toString();
        boolean naoFixa = box.isChecked();

        if(!valor.isEmpty() || !desc.isEmpty()){
            ContentValues values = new ContentValues();
            values.put("valor",valor);
            values.put("descricao",desc);
            if(!naoFixa){
                values.put("tipo", Contract.receita_continua);
            }else{
                String data = formataDataSQL(this.data.getText().toString());
                values.put("tipo", Contract.receita_nao_continua);
                values.put("data", data);
            }
            Uri uri = Uri.parse(Provider.URL+Contract.receita);
            if(txvId.getText().toString().isEmpty()){
                Uri _uri = ac.getContentResolver().insert(uri,values);
                if(_uri != null){
                    InputMethodManager imm = (InputMethodManager)ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(this.valor.getWindowToken(), 0);
                    limpaCampos();
                    ac.showAlert("Inserido com sucesso", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ac.getFm().popBackStack();
                        }
                    });
                }
            }else{
                String id = txvId.getText().toString();
                int row = ac.getContentResolver().update(uri, values, " _id = ? ", new String[]{id});
                if(row > 0){
                    InputMethodManager imm = (InputMethodManager)ac.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(this.valor.getWindowToken(), 0);
                    limpaCampos();
                    ac.showAlert("Atualizado com sucesso", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ac.getFm().popBackStack();
                        }
                    });
                }
            }

        }else{
            ac.showAlert("Preencha os campos vazios", null);
        }
    }

    public void limpaCampos(){
        this.box.setSelected(false);
        this.valor.setText("");
        this.desc.setText("");
        this.txvId.setText("");
        this.box.setChecked(false);
    }

    public void carregaItem(String id){
        Bundle args = new Bundle();
        args.putString("_id", id);
        txvId.setText(id);
        getLoaderManager().initLoader(Provider.receita, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case Provider.receita:
                Uri uri = Uri.parse(Provider.URL+Contract.receita);
                String _id = args.getString("_id");
                CursorLoader loader = new CursorLoader(ac,uri,null," _id = ? ",new String[]{_id},null);
                return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case Provider.receita:
                data.moveToFirst();
                String valor = data.getString(data.getColumnIndex("valor"));
                String desc = data.getString(data.getColumnIndex("descricao"));
                int tipo = data.getInt(data.getColumnIndex("tipo"));
                if(tipo == Contract.receita_continua){
                    this.box.setChecked(false);
                }else{
                    this.box.setChecked(true);
                    String dataStr = formataDataNormal(data.getString(data.getColumnIndex("data")));
                    this.data.setText(dataStr);
                }
                this.valor.setText(valor);
                this.desc.setText(desc);

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

    public String formataDataNormal(String data){
        // aaaa-mm-dd
        // 0123456789

        String dia = data.substring(8);
        String mes = data.substring(5,7);
        String ano = data.substring(0,4);
        return dia+"/"+mes+"/"+ano;
    }
}
