package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.entity.Libro;
import com.example.myapplication.service.LibroService;
import com.example.myapplication.util.Connection;
import com.example.myapplication.util.ValidacionUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Spinner spnCategoria,spnTipo;

    ArrayAdapter<String> adapter;
    ArrayList<String> libros = new ArrayList<String>();
    List<Libro> lstLibros = new ArrayList<Libro>();

    Button btnRegistrar;
    EditText txtTitulo, txtAnio, txtSerie, txtFechaCreacion, txtFechaRegistro, txtEstado;

    LibroService libroService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libroService = Connection.getConnecion().create(LibroService.class);

        spnCategoria = findViewById(R.id.spnECategoria);
        spnTipo = findViewById(R.id.spnETipo);
        txtTitulo = findViewById(R.id.txtEditTitulo);
        txtTitulo.setHint("Nombre del Libro");
        txtAnio = findViewById(R.id.txtEditAnio);
        txtAnio.setHint("Ejemplo: 2022");
        txtSerie = findViewById(R.id.txtEditSerie);
        txtSerie.setHint("Ejemplo: 84675132");
        txtFechaCreacion = findViewById(R.id.txtEditFechaCreacion);
        txtFechaCreacion.setHint("Ejemplo: 2001-01-14");
        txtFechaRegistro = findViewById(R.id.txtEditFechaRegistro);
        LocalTime hora = LocalTime.now();
        LocalDate fecha = LocalDate.now();
        txtFechaRegistro.setText(fecha+"T"+hora+"+00:00");
        txtFechaRegistro.setEnabled(false);
        txtEstado = findViewById(R.id.txtEditEstado);
        txtEstado.setHint("Ejemplo: 1");
        btnRegistrar = findViewById(R.id.btnBRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tit = txtTitulo.getText().toString();
                String ani = txtAnio.getText().toString();
                String cat = spnCategoria.getSelectedItem().toString();
                String ser = txtSerie.getText().toString();
                String tip = spnTipo.getSelectedItem().toString();
                String fcr = txtFechaCreacion.getText().toString();
                String fre = txtFechaRegistro.getText().toString();
                String est = txtEstado.getText().toString();



                if(!tit.matches(ValidacionUtil.NOMBRE)){
                    mensajeAlert("Nombre debe de tener 3 a 30 carácteres.");
                } else if(!ani.matches((ValidacionUtil.ANIO))){
                    mensajeAlert("Error en el año.\nEjemplo: 2022");
                } else if(!ser.matches(ValidacionUtil.SERIE)){
                    mensajeAlert("Error en la serie, entre 3 a 8 digitos.\nEjemplo: 87564123");
                } else if(!fcr.matches(ValidacionUtil.FECHAA)){
                mensajeAlert("Error en el año insertado: AAAA-MM-DD.\nEjemplo: 2022-04-26");
                } else if(!est.matches(ValidacionUtil.ESTADO)){
                    mensajeAlert("Error en el estado insertado.\nEjemplo: 1");
                } else {
                    Libro obj = new Libro();
                    obj.setTitulo(tit);
                    obj.setAnio(ani);
                    obj.setCategoria(cat);
                    obj.setSerie(ser);
                    obj.setTipo(tip);
                    obj.setFechacreacion(fcr);
                    obj.setFechaRegistro(fre);
                    obj.setEstado(Integer.parseInt(est));

                    registar(obj);
                }
            }
        });

        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, libros);
        spnCategoria.setAdapter(adapter);
        spnTipo.setAdapter(adapter);

        btnRegistrar = findViewById(R.id.btnBRegistrar);

        cargarDatos();
    }
    public void cargarDatos(){
        Call<List<Libro>> call = libroService.listaLibro();
        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if(response.isSuccessful()){
                    lstLibros = response.body();
                    for (Libro obj : lstLibros){
                        libros.add(obj.getCategoria());
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
    public void registar(Libro obj){
    Call<Libro> call = libroService.insertarLibro(obj);
    call.enqueue(new Callback<Libro>() {
        @Override
        public void onResponse(Call<Libro> call, Response<Libro> response) {
            if(response.isSuccessful()){
                Libro objRetorno = response.body();
                mensajeAlert("Libro registrado correctamente");
            } else {
                mensajeAlert("Error al registrar libro");
            }
        }

        @Override
        public void onFailure(Call<Libro> call, Throwable t) {
            mensajeToast(t.getMessage());
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