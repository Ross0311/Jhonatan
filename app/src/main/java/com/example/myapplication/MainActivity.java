package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.entity.Libro;
import com.example.myapplication.service.LibroService;
import com.example.myapplication.util.Connection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Spinner spnCategoria;
    ArrayAdapter<String> adapter;
    ArrayList<String> libros = new ArrayList<String>();
    List<Libro> lstLibros = new ArrayList<Libro>();
    Button btnRegistrar;
    EditText txtInformacion;
    LibroService libroService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnCategoria = findViewById(R.id.spnCategoria);
        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, libros);
        spnCategoria.setAdapter(adapter);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtInformacion = findViewById(R.id.txtInformacion);

        libroService = Connection.getConnecion().create(LibroService.class);

        cargarDatos();
    }
    public void cargarDatos(){
        Call<List<Libro>> call = libroService.listaLibro();
        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if(response.isSuccessful()){
                    mensajeToast("Acceso exitoso al servicio REST");
                    lstLibros = response.body();
                    for (Libro obj : lstLibros){
                        libros.add(obj.getTitulo());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                    mensajeToast("Error al conectarse al servicio REST");
            }
        });
    }
    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    void mensajeToast(String mensaje){
        Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }
}