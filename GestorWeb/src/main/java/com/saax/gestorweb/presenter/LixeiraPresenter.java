package com.saax.gestorweb.presenter;

import com.saax.gestorweb.model.LixeiraModel;
import com.saax.gestorweb.model.datamodel.Tarefa;
import com.saax.gestorweb.view.LixeiraView;
import com.saax.gestorweb.model.datamodel.Meta;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rodrigo
 */
public class LixeiraPresenter {

    private final LixeiraView view;
    private final List<CallBackListener> callBackListeners;
    private final List<Tarefa> tarefasRestauradas;
    private final List<Meta> metasRestauradas;

    public void addTarefaCallBackListener(CallBackListener lixeiraCallBackListener) {
        this.callBackListeners.add(lixeiraCallBackListener);

    }

    public LixeiraPresenter(LixeiraView view) {
        this.view = view;
        this.tarefasRestauradas = new ArrayList<>();
        this.metasRestauradas = new ArrayList<>();
        this.callBackListeners = new ArrayList<>();

        view.setListener(this);
    }

    public void apresentaConfirmacaoRemocaoTarefa(Tarefa tarefa) {
        view.apresentaConfirmacaoRemocaoTarefa(tarefa);
    }

    private void fireEventTarefaRemovida(Tarefa tarefa) {
        for (CallBackListener callBack : callBackListeners) {
            callBack.tarefaRemovida(tarefa);

        }
    }

    private void fireEventMetaRemovida(Meta meta) {
        for (CallBackListener callBack : callBackListeners) {
            callBack.metaRemovida(meta);

        }
    }

    private void fireEventTarefaRestaurada(Tarefa tarefa) {
        for (CallBackListener callBack : callBackListeners) {
            callBack.tarefaRestaurada(tarefa);

        }
    }

    private void fireEventMetaRestaurada(Meta meta) {
        for (CallBackListener callBack : callBackListeners) {
            callBack.metaRestaurada(meta);

        }
    }

    /**
     * Trata o evento disparado ao confirmar uma remoção de tarefa A tarefa é
     * removida um callback é avisado para atualizar a apresentação desta tarefa
     *
     * @param tarefa
     */
    public void remocaoTarefaConfirmada(Tarefa tarefa) {

        LixeiraModel.removerTarefa(tarefa, PresenterUtils.getUsuarioLogado());

        fireEventTarefaRemovida(tarefa);

    }

    /**
     * Apresenta um popup window com as tarefas removidas permitindo que sejam
     * restauradas
     */
    public void aprentarLixeira() {
        List<Tarefa> listaTarefas = LixeiraModel.listarTarefasRemovidas(PresenterUtils.getUsuarioLogado());
        List<Meta> listaMetas = LixeiraModel.listarMetasRemovidas(PresenterUtils.getUsuarioLogado());
        view.apresentarLixeira(listaTarefas, listaMetas);

    }

    /**
     * Trata o envento disparado ao ser acionado o comando para restaurar uma
     * tarefa <br>
     * É apresentado um pop up de confirmação
     *
     * @param tarefa
     */
    public void restaurarTarefaClicked(Tarefa tarefa) {
        view.apresentaConfirmacaoRestauracaoTarefa(tarefa);
    }

    /**
     * Trata o envento disparado ao ser acionado o comando para restaurar uma  meta <br>
     * É apresentado um pop up de confirmação
     *
     * @param meta
     */
    public void restaurarMetaClicked(Meta meta) {
        view.apresentaConfirmacaoRestauracaoMeta(meta);
    }
    
    /**
     * Trata o evento disparado ao ser fechada a janela da lixeira <br>
     * O sistema percorre a lista de tarefas que foram restauradas e atualiza a
     * apresentação das mesmas
     */
    public void janelaFechada() {

        for (Tarefa tarefasRestaurada : tarefasRestauradas) {
            fireEventTarefaRestaurada(tarefasRestaurada);
        }
        for (Meta metaRestaurada : metasRestauradas) {
            fireEventMetaRestaurada(metaRestaurada);
        }

    }

    /**
     * Trata o evento disparado ao ser confirmada a restauração de uma tarefa
     * removida <br>
     * O sistema restaura a tarefa na base de dados, mas mantem a janela aberta
     * para o usuário restaurar outras, se desejar.
     *
     * @param tarefa
     */
    public void restaurarTarefaConfirmada(Tarefa tarefa) {
        LixeiraModel.restaurarTarefa(tarefa, PresenterUtils.getUsuarioLogado());
        tarefasRestauradas.add(tarefa);

    }

    /**
     * Trata o evento disparado ao ser confirmada a restauração de uma meta
     * removida <br>
     * O sistema restaura a meta na base de dados, mas mantem a janela aberta
     * para o usuário restaurar outras, se desejar.
     *
     * @param meta
     */
    public void restaurarMetaConfirmada(Meta meta) {
        LixeiraModel.restaurarMeta(meta, PresenterUtils.getUsuarioLogado());
        metasRestauradas.add(meta);

    }

    public void apresentaConfirmacaoRemocaoMeta(Meta meta) {
        view.apresentaConfirmacaoRemocaoMeta(meta);
    }

    public void remocaoMetaConfirmada(Meta meta) {
        LixeiraModel.removerMeta(meta, PresenterUtils.getUsuarioLogado());

        fireEventMetaRemovida(meta);

    }



}
