package com.researchs.pdi.models;

import javax.persistence.*;

@Entity
@Table(name = "PERGUNTA",
        indexes = { @Index(name = "IX_PESQPERG", columnList = "pesquisa, numero", unique = true) }
)
public class Pergunta {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "NUMERO")
    private Integer numero;

    @Column(name = "DESCRICAO")
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PESQUISA")
    private Pesquisa pesquisa;

    public Integer getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }
}
