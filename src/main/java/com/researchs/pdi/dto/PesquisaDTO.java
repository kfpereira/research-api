package com.researchs.pdi.dto;


import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;

import java.util.List;

public class PesquisaDTO {

    private Pesquisa pesquisa;

    private List<PerguntasERespostasDTO> perguntasERespostas;

    private List<Folha> folhas;

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public List<PerguntasERespostasDTO> getPerguntasERespostas() {
        return perguntasERespostas;
    }

    public void setPerguntasERespostas(List<PerguntasERespostasDTO> perguntasERespostas) {
        this.perguntasERespostas = perguntasERespostas;
    }

    public List<Folha> getFolhas() {
        return folhas;
    }

    public void setFolhas(List<Folha> folhas) {
        this.folhas = folhas;
    }
}
