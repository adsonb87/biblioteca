package com.spring.biblioteca.service;

import com.spring.biblioteca.model.Papel;
import com.spring.biblioteca.repository.PapelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PapelServiceImpl implements PapelService{

    @Autowired
    private PapelRepository papelRepository;

    @Override
    public Papel findById(Long id) {
        Optional<Papel> papelOptional = papelRepository.findById(id);

        if (papelOptional.isPresent()){
            return papelOptional.get();
        }else {
            throw new IllegalArgumentException("Papel com id: " + id + " não existe !");
        }
    }

    @Override
    public Papel findByPapel(String papel) {
        Papel pp = papelRepository.findByPapel(papel);
        return pp;
    }

    @Override
    public List<Papel> findAll() {
        List<Papel> papeis = papelRepository.findAll();
        return papeis;
    }
}
