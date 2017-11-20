package com.researchs.pdi.models;

import javax.persistence.*;

@Entity
@Table(name = "ENTREVISTADO",
        indexes = {
                @Index(
                        name = "IX_ENTREVISTADO",
                        columnList = "pesquisa, folha, pergunta",
                        unique = true
                )
        }
)
public class Entrevistado {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PESQUISA")
    private Pesquisa pesquisa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FOLHA")
    private Folha folha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERGUNTA")
    private Pergunta pergunta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESPOSTA")
    private Resposta resposta;

    public Integer getId() {
        return id;
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public Folha getFolha() {
        return folha;
    }

    public void setFolha(Folha folha) {
        this.folha = folha;
    }

    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }
}
