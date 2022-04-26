package com.example.myapplication.service;

import com.example.myapplication.entity.Libro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LibroService {
    @GET("libro")
    public abstract Call<List<Libro>> listaLibro();
}
