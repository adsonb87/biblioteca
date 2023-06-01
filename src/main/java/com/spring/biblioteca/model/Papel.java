package com.spring.biblioteca.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Papel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String papel;

    @ManyToMany(mappedBy = "papeis", fetch = FetchType.EAGER)
    private List<Usuario> usuarios;

    public Papel() {
    }

    public Papel(String papel) {
        super();
        this.papel = papel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
