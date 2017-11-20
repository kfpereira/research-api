package com.researchs.pdi.dto;


import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Resposta;

import java.util.List;

public class EntrevistadoDTO {

    private Folha folha;

    private List<EntrevistadoPerguntaRespostaDTO> respostas;

    public Folha getFolha() {
        return folha;
    }

    public void setFolha(Folha folha) {
        this.folha = folha;
    }

    public List<EntrevistadoPerguntaRespostaDTO> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<EntrevistadoPerguntaRespostaDTO> respostas) {
        this.respostas = respostas;
    }
}
