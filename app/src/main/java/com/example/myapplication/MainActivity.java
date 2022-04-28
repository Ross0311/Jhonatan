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

import com.example.myapplication.entity.Alumno;
import com.example.myapplication.service.AlumnoService;
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
    ArrayAdapter<String> adapter;
    ArrayList<String> alumnos = new ArrayList<String>();
    List<Alumno> lstAlumnos = new ArrayList<Alumno>();
    Button btnRegistrar;
    EditText txtNombre, txtApellido, txtdni, txtDireccion, txtCorreo, txtFechaNacimiento, txtFechaRegistro, txtEstado;
    AlumnoService alumnoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alumnoService = Connection.getConnecion().create(AlumnoService.class);

        adapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, alumnos);

        txtNombre = findViewById(R.id.txtEditNombre);
        txtApellido = findViewById(R.id.txtEditApellido);
        txtdni = findViewById(R.id.txtEditdni);
        txtDireccion = findViewById(R.id.txtEditDireccion);
        txtCorreo = findViewById(R.id.txtEditCorreo);
        txtFechaNacimiento = findViewById(R.id.txtEditFechaNacimiento);
        txtFechaRegistro = findViewById(R.id.txtEditFechaRegistro);
        LocalTime hora = LocalTime.now();
        LocalDate fecha = LocalDate.now();
        txtFechaRegistro.setText(fecha+"T"+hora+"+00:00");
        txtFechaRegistro.setEnabled(false);
        txtEstado = findViewById(R.id.txtEditEstado);
        btnRegistrar = findViewById(R.id.btnBRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = txtNombre.getText().toString();
                String ape = txtApellido.getText().toString();
                String dni = txtdni.getText().toString();
                String dir = txtDireccion.getText().toString();
                String cor = txtCorreo.getText().toString();
                String fna = txtFechaNacimiento.getText().toString();
                String fre = txtFechaRegistro.getText().toString();
                String est = txtEstado.getText().toString();

                    Alumno obj = new Alumno();
                    obj.setNombre(nom);
                 obj.setApellidos(ape);
                 obj.setDni(dni);
                obj.setDireccion(dir);
                obj.setCorreo(cor);
                obj.setFechaNacimiento(fna);
                obj.setFechaRegistro(fre);
                obj.setEstado(1);

                    registar(obj);

            }
        });

    }
    public void registar(Alumno obj){
    Call<Alumno> call = alumnoService.insertarAlumno(obj);
    call.enqueue(new Callback<Alumno>() {
        @Override
        public void onResponse(Call<Alumno> call, Response<Alumno> response) {
            if(response.isSuccessful()){
                Alumno objRetorno = response.body();
                mensajeAlert("Alumno registrado correctamente");
            } else {
                mensajeAlert("Error al registrar libro");
            }
        }

        @Override
        public void onFailure(Call<Alumno> call, Throwable t) {
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