package com.researchs.pdi;

import com.researchs.pdi.models.Folha;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.FolhaService;
import com.researchs.pdi.services.PesquisaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@FunctionalTest
public class FolhaTest {

    @Autowired
    private PesquisaService pesquisaService;

    @Autowired
    private FolhaService folhaService;

    @Test
    public void deveriaTerUmaFolhaCadastrada() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.novo(pesquisa, 1);

        Assert.assertEquals("Deveria ter uma folha cadastrada.", 1, folhaService.pesquisa().size());
    }

    @Test
    public void deveriamTerDuasFolhasCadastradas() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.novo(pesquisa, 1);
        folhaService.novo(pesquisa, 2);

        Assert.assertEquals("Deveriam ter 2 folhas cadastradas.", 2, folhaService.pesquisa().size());
    }

    @Test
    public void deveriamTerDuasFolhasCadastradasUmaParaCadaPesquisa() {
        Pesquisa pesquisaTeste1 = pesquisaService.novo("Pesquisa Teste 1", new Date());
        Pesquisa pesquisaTeste2 = pesquisaService.novo("Pesquisa Teste 2", new Date());

        folhaService.novo(pesquisaTeste1, 1);
        folhaService.novo(pesquisaTeste2, 1);

        Assert.assertEquals("Deveriam ter 2 folhas cadastradas.", 2, folhaService.pesquisa().size());
        Assert.assertEquals("Deveria ter 1 folha cadastrada para a pesquisa Teste 1.", 1, folhaService.pesquisa(pesquisaTeste1).size());
        Assert.assertEquals("Deveria ter 1 folha cadastrada para a pesquisa Teste 2.", 1, folhaService.pesquisa(pesquisaTeste2).size());
    }

    @Test
    public void naoDevePermitirMesmoNumeroDeFolhaParaMesmaPesquisa() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.novo(pesquisa, 1);
        String msg = null;
        try {
            folhaService.novo(pesquisa, 1);
        }
        catch (RuntimeException e) {
            msg = e.getMessage();
        }
        Assert.assertNotNull("Deveriam ter mensagem de Erro.", msg);
    }

    @Test
    public void deveriaCriar10FolhasDePesquisaMostrandoMenorEMaiorNumeroDeFolha() {
        Pesquisa pesquisa = pesquisaService.novo("Pesquisa Teste", new Date());
        folhaService.criarFolhas(pesquisa, 10);
        Assert.assertEquals("Foram criadas 10 folhas", 10, folhaService.pesquisa(pesquisa).size());
        Assert.assertEquals("Menor Numero Folha", Integer.valueOf(1), folhaService.min(pesquisa));
        Assert.assertEquals("Maior Numero Folha", Integer.valueOf(10), folhaService.max(pesquisa));

        Folha folha6 = folhaService.pesquisa(pesquisa, 6);
        Assert.assertEquals("Folha 6", Integer.valueOf(6), folha6.getNumero());
        Assert.assertEquals("Pesquisa Teste", pesquisa, folha6.getPesquisa());
    }

    @Test
    public void deveriaCriar5FolhasParaPesquisaUmE2FolhasParaPesquisa2MostrandoMenorEMaiorNumeroDeFolha() {
        Pesquisa pesquisaUm = pesquisaService.novo("Pesquisa Um", new Date());
        Pesquisa pesquisaDois = pesquisaService.novo("Pesquisa Dois", new Date());

        folhaService.criarFolhas(pesquisaUm, 5);
        folhaService.criarFolhas(pesquisaDois, 2);

        Assert.assertEquals("Foram criadas 5 folhas para Pesquisa Um", 5, folhaService.pesquisa(pesquisaUm).size());
        Assert.assertEquals("Menor Numero Folha", Integer.valueOf(1), folhaService.min(pesquisaUm));
        Assert.assertEquals("Maior Numero Folha", Integer.valueOf(5), folhaService.max(pesquisaUm));

        Assert.assertEquals("Foram criadas 2 folhas para Pesquisa Dois", 2, folhaService.pesquisa(pesquisaDois).size());
        Assert.assertEquals("Menor Numero Folha", Integer.valueOf(1), folhaService.min(pesquisaDois));
        Assert.assertEquals("Maior Numero Folha", Integer.valueOf(2), folhaService.max(pesquisaDois));
    }

}
