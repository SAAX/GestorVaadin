/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *Entity bean do Chat da Tarefa com as namequerys configuradas.<br><br>
 *
 * O objetivo desta entidade é armazenar as mensagens enviadas pelo Chat <br><br>
 * @author Fernando
 */
@Entity
@Table(name = "chattarefa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatTarefa.findAll", query = "SELECT c FROM ChatTarefa c"),
    @NamedQuery(name = "ChatTarefa.findByIdchat", query = "SELECT c FROM ChatTarefa c WHERE c.idchat = :idchat"),
    @NamedQuery(name = "ChatTarefa.findByMensagem", query = "SELECT c FROM ChatTarefa c WHERE c.mensagem = :mensagem"),
    @NamedQuery(name = "ChatTarefa.findByDatahorainclusao", query = "SELECT c FROM ChatTarefa c WHERE c.datahorainclusao = :datahorainclusao")})
public class ChatTarefa implements Serializable {
    
       
    private static final long serialVersionUID = 1L;
    
     // ----------------------------------------------------------------------------------------------------------------
    // ATRIBUTOS PERSISTIDOS
    // ----------------------------------------------------------------------------------------------------------------
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idchat")
    private Integer idchat;
    
    @JoinColumn(name = "idtarefa", referencedColumnName = "idtarefa")
    @ManyToOne(optional = false)
    private Tarefa tarefa;
    
    @Basic(optional = false)
    @NotNull(message = "Informe a mensagem que deverá ser enviada")
    @Size(min = 1, max = 200, message = "A mensagem deve ter de 1 a 200 caracteres.")
    @Column(name = "mensagem")
    private String mensagem;
      
    
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuario;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "datahorainclusao")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime datahorainclusao;

    // ----------------------------------------------------------------------------------------------------------------
    // CONSTRUTORES
    // ----------------------------------------------------------------------------------------------------------------
    
    public ChatTarefa() {
    }

    public ChatTarefa(Integer idchat) {
        this.idchat = idchat;
        
    }
    
    public ChatTarefa(String mensagem, Usuario usuario, Tarefa tarefa, LocalDateTime datahorainclusao) {
        this.mensagem = mensagem;
        this.usuario = usuario;
        this.tarefa = tarefa;
        this.datahorainclusao = datahorainclusao;
    }

    public Integer getIdchat() {
        return idchat;
    }

    public void setIdchat(Integer idchat) {
        this.idchat = idchat;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

  public LocalDateTime getDataHoraInclusao() {
        return datahorainclusao;
    }

    public void setDataHoraInclusao(LocalDateTime datahorainclusao) {
        this.datahorainclusao = datahorainclusao;
    }
    
     public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idchat != null ? idchat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChatTarefa)) {
            return false;
        }
        ChatTarefa other = (ChatTarefa) object;
        if (this == other) {
            return true;
        }

        // se o ID estiver setado, compara por ele
        if (this.getIdchat()!= null) {
            return !((this.getIdchat() == null && other.getIdchat() != null) || (this.getIdchat() != null && !this.idchat.equals(other.idchat)));

        } else {
            // senao compara por campos setados na criação da tarefa
            return this.getMensagem().equals(other.getMensagem())
                    && this.getUsuario().equals(other.getUsuario())
                    && this.getDataHoraInclusao().equals(other.getDataHoraInclusao());

        }
    }

    @Override
    public String toString() {
        return "com.saax.gestorweb.model.datamodel.ChatTarefa[ idchat=" + idchat + " ]";
    }
    
}
