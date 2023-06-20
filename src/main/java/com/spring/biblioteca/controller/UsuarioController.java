package com.spring.biblioteca.controller;

import com.spring.biblioteca.model.Papel;
import com.spring.biblioteca.model.Usuario;
import com.spring.biblioteca.repository.PapelRepository;
import com.spring.biblioteca.repository.UsuarioRepository;
import com.spring.biblioteca.service.PapelServiceImpl;
import com.spring.biblioteca.service.UsuarioServiceImpl;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private PapelServiceImpl papelService;

    @Autowired
    private BCryptPasswordEncoder criptografia;


    private boolean temAutorizacao(Usuario usuario, String papel) {
        for (Papel pp : usuario.getPapeis()) {
            if (pp.getPapel().equals(papel)) {
                return true;
            }
        }
        return false;
    }
    @GetMapping("/index")
    public String index(@CurrentSecurityContext(expression = "authentication.name") String login){

        Usuario usuario = usuarioService.findByLogin(login);

        String redirectURL = "";
        if (temAutorizacao(usuario, "ADMIN")) {
            redirectURL = "/auth/admin/admin-index";
        } else if (temAutorizacao(usuario, "USER")) {
            redirectURL = "/auth/user/user-index";
        } else if (temAutorizacao(usuario, "BIBLIOTECARIO")) {
            redirectURL = "/auth/biblio/biblio-index";
        }

        return redirectURL;

    }

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
        Usuario user = usuarioService.findByLogin(usuario.getLogin());
        if(user != null){
            model.addAttribute("loginExiste", "Login já existe no sistema");
            return "/publica-criar-usuario";
        }

        usuarioService.save(usuario);

        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!!!");
        return "redirect:/usuario/novo";
    }

    @RequestMapping("/admin/listar")
    public String listarUsuario(Model model){
        model.addAttribute("usuarios", usuarioService.findAll());
        return "/auth/admin/admin-listar-usuario";
    }

    @GetMapping("/admin/apagar/{id}")
    public String deletarUsuario(@PathVariable long id, Model model){
       usuarioService.delete(id);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model){
        Usuario usuarioVelho = usuarioService.findById(id);
        model.addAttribute("usuario", usuarioVelho);
        return "/auth/user/user-alterar-usuario";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") Long id, @Valid Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            usuario.setId(id);
            return "/auth/user/user-alterar-usuario";
        }
        usuarioService.update(usuario);
        return "redirect:/usuario/admin/listar";
    }

    @GetMapping("/editarPapel/{id}")
    public String editarPapel(@PathVariable("id") Long id, Model model){
        Usuario usuario = usuarioService.findById(id);
        model.addAttribute("usuario", usuario);

        List<Papel> papeis = papelService.findAll();
        model.addAttribute("listaPapeis", papeis);

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
            usuarioService.atribuirPapelUsuario(idUsuario, pps, usuario.isAtivo());
        }
        return "redirect:/usuario/admin/listar";
    }
}
