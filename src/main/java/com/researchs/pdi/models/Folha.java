package com.researchs.pdi.models;

import javax.persistence.*;

@Entity
@Table(name = "FOLHA",
        indexes = {
                @Index(
                        name = "IX_PESQFOLHA",
                        columnList = "pesquisa, numero",
                        unique = true
                )
        }
)
public class Folha {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "NUMERO")
    private Integer numero;

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

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }
}
