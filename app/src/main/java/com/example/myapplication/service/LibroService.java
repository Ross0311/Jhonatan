package com.example.myapplication.service;

import com.example.myapplication.entity.Libro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LibroService {
    @GET("libro")
    public abstract Call<List<Libro>> listaLibro();
    @POST("libro")
    public abstract Call<Libro> insertarLibro(@Body Libro obj);
}
