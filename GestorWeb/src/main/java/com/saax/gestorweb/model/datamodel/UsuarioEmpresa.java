package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Entity bean da tabela Usuario Empresacom as namequerys configuradas.<br><br>
 *
 * O objetivo desta entidade e armazenar o relacionamento entre Usuario e Empresa<br>
 * 
 * Quando o usuário não fizer mais parte de uma empresa o relacionamento ficará inativo<br>
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "usuarioempresa")
@NamedQueries({
    @NamedQuery(name = "UsuarioEmpresa.findAll", query = "SELECT u FROM UsuarioEmpresa u"),
    @NamedQuery(name = "UsuarioEmpresa.findById", 
            query = "SELECT u FROM UsuarioEmpresa u WHERE u.id = :id"),
    @NamedQuery(name = "UsuarioEmpresa.findByAdministrador", query = "SELECT u FROM UsuarioEmpresa u WHERE u.administrador = :administrador"),
    @NamedQuery(name = "UsuarioEmpresa.findByContratacao", query = "SELECT u FROM UsuarioEmpresa u WHERE u.contratacao = :contratacao"),
    @NamedQuery(name = "UsuarioEmpresa.findByDesligamento", query = "SELECT u FROM UsuarioEmpresa u WHERE u.desligamento = :desligamento")})
public class UsuarioEmpresa implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dataHoraInclusao;
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioInclusao;

    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idusuarioempresa")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "administrador")
    private boolean administrador;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "contratacao")
    @Temporal(TemporalType.DATE)
    private LocalDate contratacao;
    
    @Column(name = "desligamento")
    @Temporal(TemporalType.DATE)
    private LocalDate desligamento;
    
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Empresa empresa;
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Usuario usuario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ativo")
    private boolean ativo;
    
    public UsuarioEmpresa() {
    }

    public UsuarioEmpresa(Integer idusuarioempresa) {
        this.id = idusuarioempresa;
    }

    public UsuarioEmpresa(Integer idusuarioempresa, boolean administrador, LocalDate contratacao) {
        this.id = idusuarioempresa;
        this.administrador = administrador;
        this.contratacao = contratacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public LocalDate getContratacao() {
        return contratacao;
    }

    public void setContratacao(LocalDate contratacao) {
        this.contratacao = contratacao;
    }

    public LocalDate getDesligamento() {
        return desligamento;
    }

    public void setDesligamento(LocalDate desligamento) {
        this.desligamento = desligamento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof UsuarioEmpresa)) {
            return false;
        }
        UsuarioEmpresa other = (UsuarioEmpresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.UsuarioEmpresa[ idusuarioempresa=" + id + " ]";
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Usuario getIdUsuarioInclusao() {
        return idUsuarioInclusao;
    }

    public void setIdUsuarioInclusao(Usuario idUsuarioInclusao) {
        this.idUsuarioInclusao = idUsuarioInclusao;
    }
    
}
