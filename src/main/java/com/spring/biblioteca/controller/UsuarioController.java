package com.spring.biblioteca.controller;

import com.spring.biblioteca.model.Papel;
import com.spring.biblioteca.model.Usuario;
import com.spring.biblioteca.repository.PapelRepository;
import com.spring.biblioteca.repository.UsuarioRepository;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PapelRepository papelRepository;
    @GetMapping("/novo")
    public String adicionarUsuario(Model model){
        model.addAttribute("usuario", new Usuario());
        return "/publica-criar-usuario";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@Valid Usuario usuario, Model model, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            return "/publica-criar-usuario";
        }
        Usuario user = usuarioRepository.findByLogin(usuario.getLogin());
        if(user != null){
            model.addAttribute("loginExiste", "Login já existe no sistema");
            return "/publica-criar-usuario";
        }
        Papel papel = papelRepository.findByPapel("USER");
        List<Papel> papeis = new ArrayList<>();
        papeis.add(papel);
        usuario.setPapeis(papeis);
        usuarioRepository.save(usuario);
        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!!!");
        return "redirect:/usuario/novo";
    }

    @RequestMapping("/admin/listar")
    public String listarUsuario(Model model){
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "/auth/admin/admin-listar-usuario";
    }

    @GetMapping("/admin/apagar/{id}")
    public String deletarUsuario(@PathVariable long id, Model model){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Id Inválido: " + id));
        usuarioRepository.delete(usuario);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model){
        Optional<Usuario> usuarioVelho = usuarioRepository.findById(id);
        if(!usuarioVelho.isPresent()){
            throw new IllegalArgumentException("Usuário Inválido: " + id);
        }
        Usuario usuario = usuarioVelho.get();
        model.addAttribute("usuario", usuario);
        return "/auth/user/user-alterar-usuario";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id, @Valid Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            usuario.setId(id);
            return "/auth/user/user-alterar-usuario";
        }
        usuarioRepository.save(usuario);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/editarPapel/{id}")
    public String editarPapel(@PathVariable("id") Long id, Model model){
        Optional<Usuario> usuarioVelho = usuarioRepository.findById(id);
        if(!usuarioVelho.isPresent()){
            throw new IllegalArgumentException("Usuário Inválido: " + id);
        }
        Usuario usuario = usuarioVelho.get();
        model.addAttribute("usuario", usuario);
        model.addAttribute("listaPapeis", papelRepository.findAll());
        return "auth/admin/admin-editar-papel-usuario";
    }

    @PostMapping("/editarPapel/{id}")
    public String alterarPapel(@PathVariable("id") Long idUsuario,
                               Usuario usuario,
                               @RequestParam(value = "pps", required = false) int[] pps,
                               RedirectAttributes attributes){
        if(pps == null){
            usuario.setId(idUsuario);
            attributes.addFlashAttribute("mensagem","Pelo menos um papel deve ser informado");
            return "redirect:/usuario/editarPapel/"+idUsuario;
        }else{
            List<Papel> papeis = new ArrayList<Papel>();
            for(int i = 0; i < pps.length; i++){
                long idPapel = pps[i];
                Optional<Papel> papelOptional = papelRepository.findById(idPapel);
                if(papelOptional.isPresent()){
                    Papel papel = papelOptional.get();
                    papeis.add(papel);
                }
            }
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
            if(usuarioOptional.isPresent()){
                Usuario user = usuarioOptional.get();
                user.setPapeis(papeis);
                user.setAtivo(usuario.isAtivo());
                usuarioRepository.save(user);
            }
        }
        return "redirect:/usuario/admin/listar";
    }
}
