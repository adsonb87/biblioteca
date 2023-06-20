package com.spring.biblioteca.security;

import com.spring.biblioteca.model.Papel;
import com.spring.biblioteca.model.Usuario;
import com.spring.biblioteca.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class DetalheUsuarioServico implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    public DetalheUsuarioServico(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByLogin(username);

        if(usuario != null && usuario.isAtivo()){
            DetalheUsuario detalheUsuario = new DetalheUsuario(usuario);
            return detalheUsuario;
        }else{
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
