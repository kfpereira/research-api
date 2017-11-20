package com.researchs.pdi.dto;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Resposta;

public class EntrevistadoPerguntaRespostaDTO {

    private Pergunta pergunta;
    private Resposta resposta;

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }
}
