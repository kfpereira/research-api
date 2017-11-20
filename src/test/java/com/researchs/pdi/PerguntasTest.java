package com.researchs.pdi;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.PerguntaService;
import com.researchs.pdi.services.PesquisaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@FunctionalTest
public class PerguntasTest {

    @Autowired
    private PesquisaService pesquisaService;

    @Autowired
    private PerguntaService perguntaService;

    @Test
    public void deveriaExistirUmaPerguntaCadastrada() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisa, 1, "Idade");

        List<Pergunta> all = perguntaService.pesquisa();
        Assert.assertEquals("Deveria existir uma pergunta", 1, all.size());
    }

    @Test
    public void deveriamExistirDuasPerguntasCadastradas() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisa, 1, "Idade");
        perguntaService.novo(pesquisa, 2, "Sexo");

        List<Pergunta> all = perguntaService.pesquisa();
        Assert.assertEquals("Deveria existir uma pergunta", 2, all.size());
    }

    @Test
    public void naoPermitirCadastrarDuasPerguntasComMesmoNumero() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());

        String msg = null;

        perguntaService.novo(pesquisa, 1, "Idade");
        try {
            perguntaService.novo(pesquisa, 1, "Sexo");
        }
        catch(RuntimeException e) {
            msg = e.getMessage();
        }
        Assert.assertNotNull("Deveria ter uma mensagem de erro", msg);

        List<Pergunta> all = perguntaService.pesquisa();
        Assert.assertEquals("Deveria existir uma pergunta", 1, all.size());
    }

    @Test
    public void permitirCadastrarDuasPerguntasComMesmoNumeroCasoSejaDePesquisaDiferente() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        Pesquisa pesquisaAuxiliar = pesquisaService.novo("Pesquisa Auxiliar", new Date());

        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaAuxiliar, 1, "Sexo");

        List<Pergunta> all = perguntaService.pesquisa();
        Assert.assertEquals("Deveriam existir duas perguntas", 2, all.size());
    }

    @Test
    public void naoPermitirCadastrarDuasPerguntasComMesmaDescricao() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());

        String msg = null;

        perguntaService.novo(pesquisa, 1, "Idade");
        try {
            perguntaService.novo(pesquisa, 2, "Idade");
        }
        catch(RuntimeException e) {
            msg = e.getMessage();
        }
        Assert.assertEquals("Pergunta já cadastrada", msg);

        List<Pergunta> all = perguntaService.pesquisa();
        Assert.assertEquals("Deveria existir uma pergunta", 1, all.size());
    }

    @Test
    public void permitirCadastrarDuasPerguntasComMesmaDescricaoParaPesquisasDiferentes() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        Pesquisa pesquisaAuxiliar = pesquisaService.novo("Pesquisa Auxiliar", new Date());

        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaAuxiliar, 1, "Idade");

        List<Pergunta> all = perguntaService.pesquisa();
        Assert.assertEquals("Deveriam existir duas perguntas", 2, all.size());
    }

    @Test
    public void consultaQtdePerguntasPorPesquisa() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        Pesquisa pesquisaAuxiliar = pesquisaService.novo("Pesquisa Auxiliar", new Date());

        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaTeste, 2, "Sexo");
        perguntaService.novo(pesquisaAuxiliar, 1, "Idade");

        List<Pergunta> allTeste = perguntaService.pesquisa(pesquisaTeste);
        Assert.assertEquals("Deveriam existir duas perguntas", 2, allTeste.size());

        for(Pergunta pergunta: allTeste) {
            Assert.assertEquals("Pesquisa Retornada", pesquisaTeste, pergunta.getPesquisa());
        }
    }

    @Test
    public void atualizarPergunta() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaTeste, 2, "Sexo");

        Pergunta pergunta = perguntaService.pesquisa(pesquisaTeste, 1);
        pergunta.setDescricao("Município");
        perguntaService.atualizar(pergunta);

        Pergunta perguntaAtualizada = perguntaService.pesquisa(pesquisaTeste, 1);

        Assert.assertEquals("Pesquisa com pergunta Atualizada", pesquisaTeste, perguntaAtualizada.getPesquisa());
        Assert.assertEquals("Número da pergunta Atualizada", Integer.valueOf(1), perguntaAtualizada.getNumero());
        Assert.assertEquals("Nova Descrição pergunta Atualizada", "Município", perguntaAtualizada.getDescricao());
    }

    @Test
    public void naoDeveAtualizarNumeroDePerguntaParaNumeroExistente() {
        Pesquisa pesquisaTeste = pesquisaService.novo("Pesquisa Teste", new Date());
        perguntaService.novo(pesquisaTeste, 1, "Idade");
        perguntaService.novo(pesquisaTeste, 2, "Sexo");

        String msg = null;

        Pergunta pergunta = perguntaService.pesquisa(pesquisaTeste, 1);
        pergunta.setNumero(2);
        try {
            perguntaService.atualizar(pergunta);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        Assert.assertNotNull("Pesquisa não pode ser atualizada", msg);
    }

}
