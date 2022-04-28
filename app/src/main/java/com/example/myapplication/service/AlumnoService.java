package com.example.myapplication.service;

import com.example.myapplication.entity.Alumno;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AlumnoService {
    @GET("alumno")
    public abstract Call<List<Alumno>> listaAlumno();
    @POST("alumno")
    public abstract Call<Alumno> insertarAlumno(@Body Alumno obj);
}
