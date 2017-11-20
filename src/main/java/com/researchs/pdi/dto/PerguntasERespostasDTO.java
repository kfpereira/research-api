package com.researchs.pdi.dto;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Resposta;

import java.util.List;

public class PerguntasERespostasDTO {

    private Pergunta pergunta;

    private List<Resposta> respostas;

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }
}
