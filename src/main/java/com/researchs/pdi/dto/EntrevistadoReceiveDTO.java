package com.researchs.pdi.dto;

public class EntrevistadoReceiveDTO {

    private Integer Id;
    private Integer pesquisa;
    private Integer folha;
    private Integer pergunta;
    private Integer resposta;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Integer pesquisa) {
        this.pesquisa = pesquisa;
    }

    public Integer getFolha() {
        return folha;
    }

    public void setFolha(Integer folha) {
        this.folha = folha;
    }

    public Integer getPergunta() {
        return pergunta;
    }

    public void setPergunta(Integer pergunta) {
        this.pergunta = pergunta;
    }

    public Integer getResposta() {
        return resposta;
    }

    public void setResposta(Integer resposta) {
        this.resposta = resposta;
    }
}
