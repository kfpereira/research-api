package com.researchs.pdi;

import com.researchs.pdi.dto.EntrevistadoDTO;
import com.researchs.pdi.dto.EntrevistadoPerguntaRespostaDTO;
import com.researchs.pdi.dto.EntrevistadoReceiveDTO;
import com.researchs.pdi.models.*;
import com.researchs.pdi.rest.SendPesquisa;
import com.researchs.pdi.services.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.researchs.pdi.utils.DateUtils.getDate;
import static com.researchs.pdi.utils.DateUtils.getParse;

@RunWith(SpringJUnit4ClassRunner.class)
@FunctionalTest
public class EntrevistadoTest {

    public static final Date DATA_PADRAO = getDate(getParse("01/11/2016"));

    @Autowired
    private PesquisaService pesquisaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private FolhaService folhaService;

    @Autowired
    private EntrevistadoService entrevistadoService;

    @Autowired
    private SendPesquisa sendPesquisa;

    @Test
    public void deveriaTerUmaEntrevistaCadastrada() {
        Pesquisa pesquisa = getEnvironmentDeTresFolhasDePesquisa();

        for (Folha folha: folhaService.pesquisa(pesquisa))
            entrevistadoService.novo(pesquisa, montaRespostas(folha));

        List<Entrevistado> entrevistadoFolha1 = entrevistadoService.pesquisa(pesquisa, folhaService.pesquisa(pesquisa, 1));
        List<Entrevistado> entrevistadoFolha2 = entrevistadoService.pesquisa(pesquisa, folhaService.pesquisa(pesquisa, 2));
        List<Entrevistado> entrevistadoFolha3 = entrevistadoService.pesquisa(pesquisa, folhaService.pesquisa(pesquisa, 3));

        Assert.assertEquals("Deveriam ter 3 folhas cadastradas", 3, folhaService.pesquisa(pesquisa).size());
        Assert.assertEquals("Deveriam ter 3 respostas para a folha 1", 3, entrevistadoFolha1.size());
        Assert.assertEquals("Deveriam ter 3 respostas para a folha 2", 3, entrevistadoFolha2.size());
        Assert.assertEquals("Deveriam ter 3 respostas para a folha 3", 3, entrevistadoFolha3.size());
        Assert.assertEquals("Deveriam ter 9 registros de entrevistados", 9, entrevistadoService.pesquisa(pesquisa).size());
    }

    @Test
    public void validacaoDeFolhaPesquisaEOpcao() {
        Pesquisa pesquisa = getEnvironmentDeTresFolhasDePesquisa();

        for (Folha folha: folhaService.pesquisa(pesquisa))
            entrevistadoService.novo(pesquisa, montaRespostas(folha));

        Folha folha1 = folhaService.pesquisa(pesquisa, 1);
        Folha folha2 = folhaService.pesquisa(pesquisa, 2);
        Folha folha3 = folhaService.pesquisa(pesquisa, 3);

        List<Entrevistado> entrevistadoFolha1 = entrevistadoService.pesquisa(pesquisa, folha1);
        List<Entrevistado> entrevistadoFolha2 = entrevistadoService.pesquisa(pesquisa, folha2);
        List<Entrevistado> entrevistadoFolha3 = entrevistadoService.pesquisa(pesquisa, folha3);

        for (Entrevistado entrevistado: entrevistadoFolha1) {
            Assert.assertEquals("Folha 1", folha1, entrevistado.getFolha());
            Assert.assertEquals("PesquisaTeste", pesquisa, entrevistado.getPesquisa());
            Assert.assertEquals("Resposta opção A", "a", entrevistado.getResposta().getOpcao());
        }

        for (Entrevistado entrevistado: entrevistadoFolha2) {
            Assert.assertEquals("Folha 2", folha2, entrevistado.getFolha());
            Assert.assertEquals("PesquisaTeste", pesquisa, entrevistado.getPesquisa());
            Assert.assertEquals("Resposta opção A", "a", entrevistado.getResposta().getOpcao());
        }

        for (Entrevistado entrevistado: entrevistadoFolha3) {
            Assert.assertEquals("Folha 3", folha3, entrevistado.getFolha());
            Assert.assertEquals("PesquisaTeste", pesquisa, entrevistado.getPesquisa());
            Assert.assertEquals("Resposta opção A", "a", entrevistado.getResposta().getOpcao());
        }
    }

    @Test
    public void naoDeveAceitarFolhaRespondidaIncompleta() {
        Pesquisa pesquisa = getEnvironmentDeTresFolhasDePesquisa();
        List<EntrevistadoPerguntaRespostaDTO> respostas = new ArrayList<EntrevistadoPerguntaRespostaDTO>();

        EntrevistadoPerguntaRespostaDTO e1 = new EntrevistadoPerguntaRespostaDTO();
        e1.setPergunta(perguntaService.pesquisa(pesquisa, 1));
        e1.setResposta(respostaService.pesquisa(pesquisa, 1, "a"));

        EntrevistadoPerguntaRespostaDTO e2 = new EntrevistadoPerguntaRespostaDTO();
        e2.setPergunta(perguntaService.pesquisa(pesquisa, 2));
        e2.setResposta(respostaService.pesquisa(pesquisa, 2, "a"));

        respostas.add(e1);
        respostas.add(e2);

        EntrevistadoDTO entrevistadoDTO = new EntrevistadoDTO();
        entrevistadoDTO.setFolha(folhaService.pesquisa(pesquisa, 1));
        entrevistadoDTO.setRespostas(respostas);

        String msg = null;
        try {
            entrevistadoService.novo(pesquisa, entrevistadoDTO);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }

        Assert.assertNotNull("Deveria ter dado erro", msg);
    }

    @Test
    public void deveSalvarEntrevistaExterna() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getId();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveDTO folha1Pergunta1 = new EntrevistadoReceiveDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setResposta(pergunta1RespostaA.getId());

        EntrevistadoReceiveDTO folha1Pergunta2 = new EntrevistadoReceiveDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setResposta(pergunta2RespostaB.getId());

        EntrevistadoReceiveDTO folha1Pergunta3 = new EntrevistadoReceiveDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setResposta(pergunta3RespostaC.getId());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisa.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        Assert.assertNull("Não deveria ter erro de Integração", erro);

        List<Entrevistado> entrevistaFolha1 = entrevistadoService.pesquisa(pesquisaTeste, folhaService.pesquisa(folha1));
        for (Entrevistado entrevistado : entrevistaFolha1) {
            deveHaverUmaOpcaoPergunta(entrevistado, pergunta1, pergunta2, pergunta3);
        }
    }

    @Test
    public void pesquisaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getId();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveDTO folha1Pergunta1 = new EntrevistadoReceiveDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(10);
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setResposta(pergunta1RespostaA.getId());

        EntrevistadoReceiveDTO folha1Pergunta2 = new EntrevistadoReceiveDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setResposta(pergunta2RespostaB.getId());

        EntrevistadoReceiveDTO folha1Pergunta3 = new EntrevistadoReceiveDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setResposta(pergunta3RespostaC.getId());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisa.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        Assert.assertNotNull("Pesquisa não encontrada", erro);
        Assert.assertEquals("Pesquisa não encontrada", "Pesquisa não encontrada", erro);
    }

    @Test
    public void folhaPesquisaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveDTO folha1Pergunta1 = new EntrevistadoReceiveDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(1001);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setResposta(pergunta1RespostaA.getId());

        EntrevistadoReceiveDTO folha1Pergunta2 = new EntrevistadoReceiveDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(1001);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setResposta(pergunta2RespostaB.getId());

        EntrevistadoReceiveDTO folha1Pergunta3 = new EntrevistadoReceiveDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(1001);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setResposta(pergunta3RespostaC.getId());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisa.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        Assert.assertNotNull("Folha de Pesquisa não encontrada", erro);
        Assert.assertEquals("Folha de Pesquisa não encontrada", "Folha de Pesquisa não encontrada", erro);
    }

    @Test
    public void perguntaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getId();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta1RespostaA = respostaService.pesquisa(pesquisaTeste, pergunta1.getNumero(), "a");
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveDTO folha1Pergunta1 = new EntrevistadoReceiveDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(1001);
        folha1Pergunta1.setResposta(pergunta1RespostaA.getId());

        EntrevistadoReceiveDTO folha1Pergunta2 = new EntrevistadoReceiveDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(1002);
        folha1Pergunta2.setResposta(pergunta2RespostaB.getId());

        EntrevistadoReceiveDTO folha1Pergunta3 = new EntrevistadoReceiveDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(1003);
        folha1Pergunta3.setResposta(pergunta3RespostaC.getId());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisa.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        Assert.assertNotNull("Pergunta não encontrada", erro);
        Assert.assertEquals("Pergunta não encontrada", "Pergunta não encontrada", erro);
    }

    @Test
    public void respostaNaoEncontrada() {
        getEnvironmentDeTresFolhasDePesquisa();

        Pesquisa pesquisaTeste = pesquisaService.pesquisa("Teste", DATA_PADRAO);
        Integer folha1 = folhaService.pesquisa(pesquisaTeste).get(0).getId();
        Pergunta pergunta1 = perguntaService.pesquisa(pesquisaTeste, 1);
        Pergunta pergunta2 = perguntaService.pesquisa(pesquisaTeste, 2);
        Pergunta pergunta3 = perguntaService.pesquisa(pesquisaTeste, 3);
        Resposta pergunta2RespostaB = respostaService.pesquisa(pesquisaTeste, pergunta2.getNumero(), "b");
        Resposta pergunta3RespostaC = respostaService.pesquisa(pesquisaTeste, pergunta3.getNumero(), "c");

        ArrayList<EntrevistadoReceiveDTO> entrevistas = new ArrayList<>();

        EntrevistadoReceiveDTO folha1Pergunta1 = new EntrevistadoReceiveDTO();
        folha1Pergunta1.setId(1);
        folha1Pergunta1.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta1.setFolha(folha1);
        folha1Pergunta1.setPergunta(pergunta1.getId());
        folha1Pergunta1.setResposta(1001);

        EntrevistadoReceiveDTO folha1Pergunta2 = new EntrevistadoReceiveDTO();
        folha1Pergunta2.setId(2);
        folha1Pergunta2.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta2.setFolha(folha1);
        folha1Pergunta2.setPergunta(pergunta2.getId());
        folha1Pergunta2.setResposta(pergunta2RespostaB.getId());

        EntrevistadoReceiveDTO folha1Pergunta3 = new EntrevistadoReceiveDTO();
        folha1Pergunta3.setId(3);
        folha1Pergunta3.setPesquisa(pesquisaTeste.getId());
        folha1Pergunta3.setFolha(folha1);
        folha1Pergunta3.setPergunta(pergunta3.getId());
        folha1Pergunta3.setResposta(pergunta3RespostaC.getId());

        entrevistas.add(folha1Pergunta1);
        entrevistas.add(folha1Pergunta2);
        entrevistas.add(folha1Pergunta3);

        String erro = null;
        try {
            sendPesquisa.enviarEntrevistados(null, entrevistas);
        } catch (Exception e) {
            erro = e.getMessage();
        }

        Assert.assertNotNull("Resposta não encontrada", erro);
        Assert.assertEquals("Resposta não encontrada", "Resposta não encontrada", erro);
    }

    private void deveHaverUmaOpcaoPergunta(Entrevistado entrevistado, Pergunta pergunta1, Pergunta pergunta2, Pergunta pergunta3) {
        Boolean achou = false;

        if (entrevistado.getPergunta() == pergunta1)
            achou = true;
        if (entrevistado.getPergunta() == pergunta2)
            achou = true;
        if (entrevistado.getPergunta() == pergunta3)
            achou = true;

        Assert.assertTrue("Pergunta não encontrada", achou);
    }

    private Pesquisa getEnvironmentDeTresFolhasDePesquisa() {
        Pesquisa pesquisa = pesquisaService.novo("Teste", DATA_PADRAO);
        Pergunta pergunta1 = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta1, "a", "Entre 16 e 24 anos");
        respostaService.novo(pergunta1, "b", "Entre 25 e 34 anos");
        respostaService.novo(pergunta1, "c", "Entre 35 e 49 anos");
        respostaService.novo(pergunta1, "d", "Entre 50 e 60 anos");
        respostaService.novo(pergunta1, "e", "Acima de 60 anos");

        Pergunta pergunta2 = perguntaService.novo(pesquisa, 2, "Sexo");
        respostaService.novo(pergunta2, "a", "M");
        respostaService.novo(pergunta2, "b", "F");

        Pergunta pergunta3 = perguntaService.novo(pesquisa, 3, "Salário familiar");
        respostaService.novo(pergunta3, "a", "Até 1 salário mínimo");
        respostaService.novo(pergunta3, "b", "Entre 1 e 3 salários mínimos");
        respostaService.novo(pergunta3, "c", "Entre 4 e 7 salários mínimos");
        respostaService.novo(pergunta3, "d", "Entre 8 e 10 salários mínimos");
        respostaService.novo(pergunta3, "e", "Acima de 10 salários mínimos");

        folhaService.criarFolhas(pesquisa, 3);

        return pesquisa;
    }

    private EntrevistadoDTO montaRespostas(Folha folha) {
        Pesquisa pesquisa = pesquisaService.pesquisa("Teste", DATA_PADRAO);

        List<EntrevistadoPerguntaRespostaDTO> respostas = new ArrayList<EntrevistadoPerguntaRespostaDTO>();

        EntrevistadoPerguntaRespostaDTO e1 = new EntrevistadoPerguntaRespostaDTO();
        e1.setPergunta(perguntaService.pesquisa(pesquisa, 1));
        e1.setResposta(respostaService.pesquisa(pesquisa, 1, "a"));

        EntrevistadoPerguntaRespostaDTO e2 = new EntrevistadoPerguntaRespostaDTO();
        e2.setPergunta(perguntaService.pesquisa(pesquisa, 2));
        e2.setResposta(respostaService.pesquisa(pesquisa, 2, "a"));

        EntrevistadoPerguntaRespostaDTO e3 = new EntrevistadoPerguntaRespostaDTO();
        e3.setPergunta(perguntaService.pesquisa(pesquisa, 3));
        e3.setResposta(respostaService.pesquisa(pesquisa, 3, "a"));

        respostas.add(e1);
        respostas.add(e2);
        respostas.add(e3);

        EntrevistadoDTO entrevistadoDTO = new EntrevistadoDTO();
        entrevistadoDTO.setFolha(folha);
        entrevistadoDTO.setRespostas(respostas);
        return entrevistadoDTO;
    }

}
