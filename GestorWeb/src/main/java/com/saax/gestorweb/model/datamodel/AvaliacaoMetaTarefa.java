package com.saax.gestorweb.model.datamodel;

import com.saax.gestorweb.model.datamodel.Meta;
import com.saax.gestorweb.model.datamodel.Usuario;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;

/**
 *
 * @author rodrigo
 */
@Entity
@Table(name = "avaliacaometatarefa")
@NamedQueries({
    @NamedQuery(name = "AvaliacaoMetaTarefa.findAll", query = "SELECT a FROM AvaliacaoMetaTarefa a"),
    @NamedQuery(name = "AvaliacaoMetaTarefa.findByAvaliacao", query = "SELECT a FROM AvaliacaoMetaTarefa a WHERE a.avaliacao = :avaliacao"),
    @NamedQuery(name = "AvaliacaoMetaTarefa.findByComentario", query = "SELECT a FROM AvaliacaoMetaTarefa a WHERE a.comentario = :comentario"),
    @NamedQuery(name = "AvaliacaoMetaTarefa.findByDatahorainclusao", query = "SELECT a FROM AvaliacaoMetaTarefa a WHERE a.datahorainclusao = :datahorainclusao")})
public class AvaliacaoMetaTarefa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idavaliacaometatarefa")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "avaliacao")
    private int avaliacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime datahorainclusao;
    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne(optional = false)
    private Meta idmeta;
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idtarefa;
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioinclusao;
    @JoinColumn(name = "idusuarioavaliador", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioavaliador;
    @JoinColumn(name = "idusuarioavaliado", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idusuarioavaliado;

    public AvaliacaoMetaTarefa() {
    }

    public AvaliacaoMetaTarefa(Integer idavaliacaometatarefa) {
        this.id = idavaliacaometatarefa;
    }

    public AvaliacaoMetaTarefa(Integer idavaliacaometatarefa, int avaliacao, String comentario, LocalDateTime datahorainclusao) {
        this.id = idavaliacaometatarefa;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.datahorainclusao = datahorainclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDatahorainclusao() {
        return datahorainclusao;
    }

    public void setDatahorainclusao(LocalDateTime datahorainclusao) {
        this.datahorainclusao = datahorainclusao;
    }

    public Meta getIdmeta() {
        return idmeta;
    }

    public void setIdmeta(Meta idmeta) {
        this.idmeta = idmeta;
    }

    public Tarefa getIdtarefa() {
        return idtarefa;
    }

    public void setIdtarefa(Tarefa idtarefa) {
        this.idtarefa = idtarefa;
    }

    public Usuario getIdusuarioinclusao() {
        return idusuarioinclusao;
    }

    public void setIdusuarioinclusao(Usuario idusuarioinclusao) {
        this.idusuarioinclusao = idusuarioinclusao;
    }

    public Usuario getIdusuarioavaliador() {
        return idusuarioavaliador;
    }

    public void setIdusuarioavaliador(Usuario idusuarioavaliador) {
        this.idusuarioavaliador = idusuarioavaliador;
    }

    public Usuario getIdusuarioavaliado() {
        return idusuarioavaliado;
    }

    public void setIdusuarioavaliado(Usuario idusuarioavaliado) {
        this.idusuarioavaliado = idusuarioavaliado;
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
        if (!(object instanceof AvaliacaoMetaTarefa)) {
            return false;
        }
        AvaliacaoMetaTarefa other = (AvaliacaoMetaTarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.AvaliacaoMetaTarefa[ idavaliacaometatarefa=" + id + " ]";
    }
    
}
