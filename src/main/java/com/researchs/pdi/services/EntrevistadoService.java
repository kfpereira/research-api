package com.researchs.pdi.services;

import com.researchs.pdi.dto.EntrevistadoDTO;
import com.researchs.pdi.dto.EntrevistadoPerguntaRespostaDTO;
import com.researchs.pdi.dto.EntrevistadoReceiveDTO;
import com.researchs.pdi.models.*;
import com.researchs.pdi.repositories.EntrevistadoRepository;
import com.researchs.pdi.rest.SendPesquisa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrevistadoService {

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private PesquisaService pesquisaService;

    @Autowired
    private FolhaService folhaService;

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private EntrevistadoRepository entrevistadoRepository;

    public void novo(Pesquisa pesquisa, EntrevistadoDTO entrevistadoDTO) {
        valida(pesquisa, entrevistadoDTO);

        for (EntrevistadoPerguntaRespostaDTO resposta: entrevistadoDTO.getRespostas())  {
            Entrevistado entrevistado = new Entrevistado();
            entrevistado.setPesquisa(pesquisa);
            entrevistado.setFolha(entrevistadoDTO.getFolha());
            entrevistado.setPergunta(resposta.getPergunta());
            entrevistado.setResposta(resposta.getResposta());

            entrevistadoRepository.saveAndFlush(entrevistado);
        }
    }

    public List<Entrevistado> pesquisa(Pesquisa pesquisa) {
        return entrevistadoRepository.findByPesquisa(pesquisa);
    }

    private void valida(Pesquisa pesquisa, EntrevistadoDTO entrevistadoDTO) {
        int qtdeRespostas = entrevistadoDTO.getRespostas().size();
        int qtdePerguntas = perguntaService.pesquisa(pesquisa).size();

        if (qtdePerguntas != qtdeRespostas)
            throw new RuntimeException("Folha " + entrevistadoDTO.getFolha().getNumero() + " não teve todas as perguntas respondidas");
    }

    public List<Entrevistado> pesquisa(Pesquisa pesquisa, Folha folha) {
        return entrevistadoRepository.findByPesquisaAndFolha(pesquisa, folha);
    }

    public void apagar(Pesquisa pesquisa) {
        for (Entrevistado entrevistado : pesquisa(pesquisa))
            entrevistadoRepository.delete(entrevistado);
    }

    public Entrevistado salvar(EntrevistadoReceiveDTO entrevistado) throws Exception {
        Pesquisa pesquisa = pesquisaService.pesquisa(entrevistado.getPesquisa());
        Folha folha = folhaService.pesquisa(entrevistado.getFolha());
        Pergunta pergunta = perguntaService.pesquisa(entrevistado.getPergunta());
        Resposta resposta = respostaService.pesquisa(entrevistado.getResposta());

        valida(pesquisa, folha, pergunta, resposta);

        Entrevistado e = new Entrevistado();
        e.setPesquisa(pesquisa);
        e.setFolha(folha);
        e.setPergunta(pergunta);
        e.setResposta(resposta);

        return entrevistadoRepository.saveAndFlush(e);
    }

    private void valida(Pesquisa pesquisa, Folha folha, Pergunta pergunta, Resposta resposta) throws Exception {
        if (pesquisa == null)
            throw new Exception("Pesquisa não encontrada");

        if (folha == null)
            throw new Exception("Folha de Pesquisa não encontrada");

        if (pergunta == null)
            throw new Exception("Pergunta não encontrada");

        if (resposta == null)
            throw new Exception("Resposta não encontrada");
    }
}
