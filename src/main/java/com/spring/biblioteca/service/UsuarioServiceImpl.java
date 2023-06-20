package com.spring.biblioteca.service;

import com.spring.biblioteca.model.Papel;
import com.spring.biblioteca.model.Usuario;
import com.spring.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelServiceImpl papelService;

    @Autowired
    private BCryptPasswordEncoder criptografia;

    @Override
    public Usuario findByLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        return usuario;
    }

    @Override
    public Usuario findById(Long id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);

        if(opt.isPresent()){
            return opt.get();
        }else{
            throw new IllegalArgumentException("Usuário com id: " + id + " não existe!");
        }
    }

    @Override
    public void delete(Long id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

    @Override
    public Usuario save(Usuario usuario) {
        Papel papel = papelService.findByPapel("USER");
        List<Papel> papeis = new ArrayList<>();
        papeis.add(papel);
        usuario.setPapeis(papeis);

        String senhaCriptografada = criptografia.encode(usuario.getPassword());
        usuario.setPassword(senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void update(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    @Override
    public void atribuirPapelUsuario(Long idUsuario, int[] idsPapeis, boolean isAtivo) {
        List<Papel> papeis = new ArrayList<Papel>();
        for(int i = 0; i < idsPapeis.length; i++){
            long idPapel = idsPapeis[i];
            Papel papel = papelService.findById(idPapel);
            papeis.add(papel);
        }
        Usuario user = findById(idUsuario);
        user.setPapeis(papeis);
        user.setAtivo(isAtivo);
        update(user);
    }
}
