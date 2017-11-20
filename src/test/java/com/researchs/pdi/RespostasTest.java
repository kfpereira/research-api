package com.researchs.pdi;

import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import com.researchs.pdi.services.PerguntaService;
import com.researchs.pdi.services.PesquisaService;
import com.researchs.pdi.services.RespostaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@FunctionalTest
public class RespostasTest {
    @Autowired
    private PesquisaService pesquisaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private RespostaService respostaService;

    @Test
    public void deveriaExistirUmaRespostaCadastrada() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");

        List<Resposta> all = respostaService.pesquisa();
        Assert.assertEquals("Deveria existir uma resposta", 1, all.size());
    }

    @Test
    public void deveriamExistirDuasRespostasCadastradas() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        List<Resposta> all = respostaService.pesquisa();
        Assert.assertEquals("Deveriam existir duas respostas", 2, all.size());
    }

    @Test
    public void naoPermitirCadastrarDuasRespostasDeMesmaOpcaoParaAMesmaPergunta() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");

        String msg = null;
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        try {
            respostaService.novo(pergunta, "a", "Entre 24 e 29 anos");
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }
        Assert.assertNotNull("Deveria ter uma mensagem de erro", msg);

        List<Resposta> all = respostaService.pesquisa();
        Assert.assertEquals("Deveria existir uma resposta", 1, all.size());
    }

    @Test
    public void permitirCadastrarDuasRespostasDeMesmaOpcaoParaPerguntasDiferentes() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta1 = perguntaService.novo(pesquisa, 1, "Nota Opcao A");
        respostaService.novo(pergunta1, "a", "1");
        respostaService.novo(pergunta1, "b", "2");
        respostaService.novo(pergunta1, "c", "3");
        respostaService.novo(pergunta1, "d", "4");
        respostaService.novo(pergunta1, "e", "5");

        Pergunta pergunta2 = perguntaService.novo(pesquisa, 2, "Nota Opcao B");
        respostaService.novo(pergunta2, "a", "1");
        respostaService.novo(pergunta2, "b", "2");
        respostaService.novo(pergunta2, "c", "3");
        respostaService.novo(pergunta2, "d", "4");
        respostaService.novo(pergunta2, "e", "5");

        List<Resposta> all = respostaService.pesquisa();
        Assert.assertEquals("Deveria existir 10 respostas", 10, all.size());
    }

    @Test
    public void permitirAtualizarResposta() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        Resposta resposta = respostaService.pesquisa(pesquisa, 1, "b");
        resposta.setDescricao("Entre 24 e 39 anos");
        respostaService.atualizar(resposta);

        Resposta respostaAssert = respostaService.pesquisa(pesquisa, 1, "b");
        Assert.assertEquals("Descrição resposta", "Entre 24 e 39 anos", respostaAssert.getDescricao());
    }

    @Test
    public void naoPermitirAtualizarRespostaParaOpcaoExistente() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        Resposta resposta = respostaService.pesquisa(pesquisa, 1, "b");
        resposta.setOpcao("a");

        String msg = null;
        try {
            respostaService.atualizar(resposta);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }
        Assert.assertNotNull("Deveria ter dado erro", msg);
    }

    @Test
    public void consultarRespostaPelaOpcao() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 23 anos");
        respostaService.novo(pergunta, "b", "Entre 24 e 29 anos");

        Resposta resposta = respostaService.pesquisa(pesquisa, 1, "b");
        Assert.assertEquals("Opção resposta", "b", resposta.getOpcao());
        Assert.assertEquals("Pergunta resposta", "Faixa etária", resposta.getPergunta().getDescricao());
    }

}
