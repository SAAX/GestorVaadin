package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Avaliacao Meta - Tarefa com as namequerys
 configuradas.<br><br>
 *
 * O objetivo desta entidade e armazenar as avaliações feitas pelos solicitantes
 * de metas e tarefas <br><br>
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

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @JoinColumn(name = "idmeta", referencedColumnName = "idmeta")
    @ManyToOne(optional = true)
    private Meta meta;

    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = true)
    private Tarefa tarefa;

    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    @JoinColumn(name = "idusuarioavaliador", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioAvaliador;

    @JoinColumn(name = "idusuarioavaliado", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioAvaliado;

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

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public Usuario getUsuarioAvaliador() {
        return usuarioAvaliador;
    }

    public void setUsuarioAvaliador(Usuario usuarioAvaliador) {
        this.usuarioAvaliador = usuarioAvaliador;
    }

    public Usuario getUsuarioAvaliado() {
        return usuarioAvaliado;
    }

    public void setUsuarioAvaliado(Usuario usuarioAvaliado) {
        this.usuarioAvaliado = usuarioAvaliado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AvaliacaoMetaTarefa)) {
            return false;
        }
        AvaliacaoMetaTarefa other = (AvaliacaoMetaTarefa) object;
        if ( this == other) return true;

        // se o ID estiver setado, compara por ele
        if ( this.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));
            
        } else {
            // senao compara por campos setados na criação da tarefa
            return this.getUsuarioInclusao().equals(other.getUsuarioInclusao())
                   && this. getDataHoraInclusao().equals(other.getDataHoraInclusao());

        }
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.AvaliacaoMetaTarefa[ idavaliacaometatarefa=" + id + " ]";
    }
/*
    public HistoricoTarefaBackup buildHistorico() {

        StringBuilder descricao = new StringBuilder();

        descricao.append("Avaliada com ");
        descricao.append(getAvaliacao());
        descricao.append(" estrelas: ");
        descricao.append(getComentario() == null ? "" : getComentario());

        return new HistoricoTarefaBackup(dataHoraInclusao, descricao.toString(), getUsuarioAvaliado());
    }
*/
}
