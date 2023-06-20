package com.spring.biblioteca.service;

import com.spring.biblioteca.model.Papel;

import java.util.List;

public interface PapelService {

    public Papel findById(Long id);
    public Papel findByPapel(String papel);
    public List<Papel> findAll();

}
