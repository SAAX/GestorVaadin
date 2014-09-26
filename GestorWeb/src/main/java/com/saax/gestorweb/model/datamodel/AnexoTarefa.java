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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity bean da tabela Anexo Tarefa com as namequerys configuradas.<br><br>
 * 
 * O objetivo desta entidade e armazenar os arquivos anexados as tarefas / subs <br><br>
 *
 * Revisado em: 26/07/14 
 * 
 * @author rodrigo
 */
@Entity
@Table(name = "anexotarefa")
@NamedQueries({
    @NamedQuery(name = "AnexoTarefa.findAll", query = "SELECT a FROM AnexoTarefa a"),
    @NamedQuery(name = "AnexoTarefa.findByIdanexotarefa", query = "SELECT a FROM AnexoTarefa a WHERE a.id = :idanexotarefa"),
    @NamedQuery(name = "AnexoTarefa.findByNome", query = "SELECT a FROM AnexoTarefa a WHERE a.nome = :nome")})
public class AnexoTarefa implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idanexotarefa")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nome")
    private String nome;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa tarefa;
    
    @JoinColumn(name = "idusuarioinclusao", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioInclusao;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "arquivo")
    private byte[] arquivo;
    

    public AnexoTarefa() {
    }

    public AnexoTarefa(Integer idanexotarefa) {
        this.id = idanexotarefa;
    }

    public AnexoTarefa(Integer idanexotarefa, byte[] arquivo, String nome, LocalDateTime dataHoraInclusao) {
        this.id = idanexotarefa;
        this.arquivo = arquivo;
        this.nome = nome;
        this.dataHoraInclusao = dataHoraInclusao;
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

    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnexoTarefa)) {
            return false;
        }
        AnexoTarefa other = (AnexoTarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.AnexoTarefa[ idanexotarefa=" + id + " ]";
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }
    
}
