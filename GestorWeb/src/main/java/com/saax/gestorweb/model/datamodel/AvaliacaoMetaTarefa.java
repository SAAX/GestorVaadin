package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
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
 * Entity bean da tabela Avaliacao Meta - Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar as avaliações feitas pelos solicitantes de metas e tarefas <br><br>
 *
 * @author rodrigo
 */
@Entity
@Table(name = "avaliacaometatarefa")
@NamedQueries({
    @NamedQuery(name = "AvaliacaoMetaTarefa.findAll", query = "SELECT a FROM AvaliacaoMetaTarefa a"),
    @NamedQuery(name = "AvaliacaoMetaTarefa.findByAvaliacao", query = "SELECT a FROM AvaliacaoMetaTarefa a WHERE a.avaliacao = :avaliacao"),
    @NamedQuery(name = "AvaliacaoMetaTarefa.findByComentario", query = "SELECT a FROM AvaliacaoMetaTarefa a WHERE a.comentario = :comentario"),
    @NamedQuery(name = "AvaliacaoMetaTarefa.findByDatahorainclusao", query = "SELECT a FROM AvaliacaoMetaTarefa a WHERE a.dataHoraInclusao = :dataHoraInclusao")})
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
    private LocalDateTime dataHoraInclusao;
    
    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne(optional = false)
    private Meta idMeta;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa idTarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioInclusao;
    
    @JoinColumn(name = "idusuarioavaliador", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioAvaliador;

    @JoinColumn(name = "idusuarioavaliado", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario idUsuarioAvaliado;

    public AvaliacaoMetaTarefa() {
    }

    public AvaliacaoMetaTarefa(Integer idavaliacaometatarefa) {
        this.id = idavaliacaometatarefa;
    }

    public AvaliacaoMetaTarefa(Integer idavaliacaometatarefa, int avaliacao, String comentario, LocalDateTime dataHoraInclusao) {
        this.id = idavaliacaometatarefa;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.dataHoraInclusao = dataHoraInclusao;
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

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Meta getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(Meta idMeta) {
        this.idMeta = idMeta;
    }

    public Tarefa getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(Tarefa idTarefa) {
        this.idTarefa = idTarefa;
    }

    public Usuario getIdUsuarioInclusao() {
        return idUsuarioInclusao;
    }

    public void setIdUsuarioInclusao(Usuario idUsuarioInclusao) {
        this.idUsuarioInclusao = idUsuarioInclusao;
    }

    public Usuario getIdUsuarioAvaliador() {
        return idUsuarioAvaliador;
    }

    public void setIdUsuarioAvaliador(Usuario idUsuarioAvaliador) {
        this.idUsuarioAvaliador = idUsuarioAvaliador;
    }

    public Usuario getIdUsuarioAvaliado() {
        return idUsuarioAvaliado;
    }

    public void setIdUsuarioAvaliado(Usuario idUsuarioAvaliado) {
        this.idUsuarioAvaliado = idUsuarioAvaliado;
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
