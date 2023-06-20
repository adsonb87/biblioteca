package com.spring.biblioteca.service;

import com.spring.biblioteca.model.Usuario;
import com.spring.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface UsuarioService {

    public Usuario findByLogin(String login);

    public Usuario findById(Long id);

    public void delete(Long id);

    public Usuario save(Usuario usuario);

    public void update(Usuario usuario);

    public List<Usuario> findAll();

    public void atribuirPapelUsuario(Long idUsuario, int[] idsPapeis, boolean isAtivo);

}
