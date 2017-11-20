package com.researchs.pdi;

import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.services.PesquisaService;
import com.researchs.pdi.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static com.researchs.pdi.utils.DateUtils.getDate;
import static com.researchs.pdi.utils.DateUtils.getParse;

@RunWith(SpringJUnit4ClassRunner.class)
@FunctionalTest
public class PesquisasTest {

	public static final Date DATA_PADRAO = getDate(getParse("01/11/2016"));

	@Autowired
	private PesquisaService pesquisaService;

	private Pesquisa novaPesquisa(String descricaoPesq, Date data) {
		return pesquisaService.novo(descricaoPesq, data);
	}

	@Test
	public void deveExistirUmaPesquisa() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);

		List<Pesquisa> all = pesquisaService.pesquisa();

		Assert.assertEquals("Deveria ter uma pesquisa", 1, all.size());
	}

	@Test
	public void devemExistirDuasPesquisas() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);
		novaPesquisa("Pesquisa Mais um Teste", DATA_PADRAO);

		List<Pesquisa> all = pesquisaService.pesquisa();

		Assert.assertEquals("Deveria ter duas pesquisas", 2, all.size());
	}

	@Test
	public void retornarPesquisaTeste() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);
		novaPesquisa("Pesquisa Mais um Teste", DATA_PADRAO);

		List<Pesquisa> pesquisaTeste = pesquisaService.pesquisa("Pesquisa Teste");

		Assert.assertEquals("Deveria ter uma pesquisa", 1, pesquisaTeste.size());
		Assert.assertEquals("Deveria ser a pesquisa Teste", "Pesquisa Teste", pesquisaTeste.get(0).getDescricao());
	}

	@Test
	public void atualizarPesquisa() {
		novaPesquisa("Pesquisa Teste",DATA_PADRAO);
		novaPesquisa("Pesquisa Mais um Teste", DATA_PADRAO);

		List<Pesquisa> pesquisaTeste = pesquisaService.pesquisa("Pesquisa Teste");

		Pesquisa pesquisaUpdate = pesquisaTeste.get(0);
		pesquisaUpdate.setDescricao("Pesquisa Alterada");
		pesquisaService.atualizar(pesquisaUpdate);

		pesquisaTeste = pesquisaService.pesquisa("Pesquisa Alterada");

		Assert.assertEquals("Deveria ter uma pesquisa", 1, pesquisaTeste.size());
		Assert.assertEquals("Deveria ser a pesquisa Teste", "Pesquisa Alterada", pesquisaTeste.get(0).getDescricao());
		Assert.assertEquals("O total de pesquisas deveria ser", 2, pesquisaService.pesquisa().size());
	}

	@Test
	public void naoPermiteCadastrarPesquisaMesmoNomeMesmaData() {
		novaPesquisa("Pesquisa Teste", DATA_PADRAO);

		String erroMsg = null;
		try {
			novaPesquisa("Pesquisa Teste", DATA_PADRAO);
		}
		catch (RuntimeException e) {
			erroMsg = e.getMessage();
		}
		Assert.assertEquals("Mensagem de Erro", "Pesquisa já cadastrada", erroMsg);

		List<Pesquisa> all = pesquisaService.pesquisa();
		Assert.assertEquals("Deveria ter uma pesquisa", 1, all.size());
	}

	@Test
	public void consultarPesquisaPelaData() {
		novaPesquisa("Pesquisa Setembro", getDate(getParse("30/09/2016")));
		novaPesquisa("Pesquisa Outubro", getDate(getParse("06/10/2016")));
		List<Pesquisa> outubro1 = pesquisaService.pesquisa(getDate(getParse("01/10/2016")));
		Assert.assertEquals("Lista vazia", 0, outubro1.size());

		List<Pesquisa> setembro30 = pesquisaService.pesquisa(getDate(getParse("30/09/2016")));
		Assert.assertEquals("Lista com Pesquisa de 30/09/2016", 1, setembro30.size());
		Assert.assertEquals("Data de 30/09/2016", getDate(getParse("30/09/2016")), setembro30.get(0).getData());
	}

	@Test
	public void naoPermiteAtualizarPesquisaMesmoNomeMesmaData() {
		novaPesquisa("Pesquisa Teste 1", DATA_PADRAO);
		Pesquisa pesquisaTeste2 = novaPesquisa("Pesquisa Teste 2", DATA_PADRAO);

		String erroMsg = null;
		try {
			pesquisaService.atualizar(pesquisaTeste2, "Pesquisa Teste 1", pesquisaTeste2.getData());
		}
		catch (RuntimeException e) {
			erroMsg = e.getMessage();
		}
		Assert.assertEquals("Mensagem de Erro", "Pesquisa já cadastrada", erroMsg);

		List<Pesquisa> all = pesquisaService.pesquisa();
		Assert.assertEquals("Deveriam ter duas pesquisas cadastradas", 2, all.size());
	}

	@Test
	public void devePermitirAtualizarPesquisaComMesmosDados() {
		novaPesquisa("Pesquisa Teste 1", DATA_PADRAO);
		Pesquisa pesquisaTeste2 = novaPesquisa("Pesquisa Teste 2", DATA_PADRAO);

		pesquisaService.atualizar(pesquisaTeste2, "Pesquisa Teste 2", pesquisaTeste2.getData());

		List<Pesquisa> all = pesquisaService.pesquisa();
		Assert.assertEquals("Deveriam ter duas pesquisas cadastradas", 2, all.size());
	}

}
