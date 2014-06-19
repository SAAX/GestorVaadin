package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Usuario com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar os Usuarios para controle de acesso<br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id"),
    @NamedQuery(name = "Usuario.findByNome", query = "SELECT u FROM Usuario u WHERE u.nome = :nome"),
    @NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE u.login = :login"),
    @NamedQuery(name = "Usuario.findBySenha", query = "SELECT u FROM Usuario u WHERE u.senha = :senha")})
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idusuario")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sobrenome")
    private String sobrenome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "login")
    
    private String login;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "senha")
    private String senha;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<UsuarioEmpresa> empresas;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsavel")
    private Collection<Meta> metasResponsaveis;
    
    
    public Usuario() {
    }

    public Usuario(Integer idusuario) {
        this.id = idusuario;
    }

    public Usuario(Integer idusuario, String nome, String sobrenome, String login, String senha) {
        this.id = idusuario;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.login = login;
        this.senha = senha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Usuario[ idusuario=" + id + " ]";
    }

    public Collection<UsuarioEmpresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Collection<UsuarioEmpresa> empresas) {
        this.empresas = empresas;
    }

    public Collection<Meta> getMetasResponsaveis() {
        return metasResponsaveis;
    }

    public void setMetasResponsaveis(Collection<Meta> metasResponsaveis) {
        this.metasResponsaveis = metasResponsaveis;
    }

    
}
