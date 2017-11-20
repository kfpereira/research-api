package com.researchs.pdi.models;

import javax.persistence.*;

@Entity
@Table(name = "RESPOSTA",
        indexes = {
                @Index(
                        name = "IX_PERGRESP",
                        columnList = "pergunta, opcao",
                        unique = true
                )
        }
)
public class Resposta {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "OPCAO")
    private String opcao;

    @Column(name = "DESCRICAO")
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERGUNTA")
    private Pergunta pergunta;

    public Integer getId() {
        return id;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }
}
