package com.saax.gestorweb.model.datamodel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entidade que armazena as publicações (posts)
 * @author Rodrigo
 */
@Entity
@Table(name = "publicacao")
@NamedQueries({
    @NamedQuery(name = "Publicacao.findAll", query = "SELECT p FROM Publicacao p")})
public class Publicacao implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpublicacao")
    private Integer id;
    
    @NotNull(message = "Informe a empresa")
    @JoinColumn(name = "idempresa", referencedColumnName = "idempresa")
    @ManyToOne(optional = false)
    private Empresa empresa;

    
    @Basic(optional = false)
    @NotNull(message = "Informe o nome da publicação")
    @Size(min = 1, max = 150, message="O nome da publicação deve ter de 1 a 150 caracteres")
    @Column(name = "nome")
    private String nome;
    
    
    @Column(name = "datareuniao")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataReuniao;

    @Column(name = "deadlineleitura")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate deadlineLeitura;
    
    @Basic(optional = false)
    @NotNull(message = "Informe uma descricação")
    @Size(min = 1, max = 2147483647)
    @Column(name = "descricao")
    private String descricao;
    
    @Column(name = "datapublicacao")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dataPublicacao;

    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime dataHoraInclusao;

    @JoinColumn(name = "idpublicacaoreferencia", referencedColumnName = "idpublicacaoreferencia")
    @ManyToOne(fetch = FetchType.EAGER)
    private PublicacaoReferencia referencia;
    
    @Enumerated(EnumType.STRING)
    private StatusPublicacao status;
    
    @JoinColumn(name = "idpublicacaotipo", referencedColumnName = "idpublicacaotipo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PublicacaoTipo tipo;
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioInclusao;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacao", fetch = FetchType.LAZY)
    private List<Participante> participantes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacao", fetch = FetchType.LAZY)
    private List<Anexo> anexos;

    public Publicacao() {
    }

    public Publicacao(Integer idpublicacao) {
        this.id = idpublicacao;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Meta)) {
            return false;
        }
        Publicacao other = (Publicacao) object;
        if ( this == other) return true;

        // se o ID estiver setado, compara por ele
        if ( this.getId() != null) {
            return !((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id)));
            
        } else {
            return this.getUsuarioInclusao().equals(other.getUsuarioInclusao())
                   && this. getDataHoraInclusao().equals(other.getDataHoraInclusao());

        }
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.Publicacao[ idpublicacao=" + getId() + " ]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the dataReuniao
     */
    public LocalDate getDataReuniao() {
        return dataReuniao;
    }

    /**
     * @param dataReuniao the dataReuniao to set
     */
    public void setDataReuniao(LocalDate dataReuniao) {
        this.dataReuniao = dataReuniao;
    }

    /**
     * @return the deadlineLeitura
     */
    public LocalDate getDeadlineLeitura() {
        return deadlineLeitura;
    }

    /**
     * @param deadlineLeitura the deadlineLeitura to set
     */
    public void setDeadlineLeitura(LocalDate deadlineLeitura) {
        this.deadlineLeitura = deadlineLeitura;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the dataPublicacao
     */
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * @param dataPublicacao the dataPublicacao to set
     */
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * @return the dataHoraInclusao
     */
    public LocalDateTime getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    /**
     * @param dataHoraInclusao the dataHoraInclusao to set
     */
    public void setDataHoraInclusao(LocalDateTime dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    /**
     * @return the referencia
     */
    public PublicacaoReferencia getReferencia() {
        return referencia;
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(PublicacaoReferencia referencia) {
        this.referencia = referencia;
    }

    /**
     * @return the status
     */
    public StatusPublicacao getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(StatusPublicacao status) {
        this.status = status;
    }

    /**
     * @return the tipo
     */
    public PublicacaoTipo getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(PublicacaoTipo tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the usuarioInclusao
     */
    public Usuario getUsuarioInclusao() {
        return usuarioInclusao;
    }

    /**
     * @param usuarioInclusao the usuarioInclusao to set
     */
    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    /**
     * @return the participantes
     */
    public List<Participante> getParticipantes() {
        return participantes;
    }

    /**
     * @param participantes the participantes to set
     */
    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    /**
     * @return the anexos
     */
    public List<Anexo> getAnexos() {
        return anexos;
    }

    /**
     * @param anexos the anexos to set
     */
    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }
    
    
}
