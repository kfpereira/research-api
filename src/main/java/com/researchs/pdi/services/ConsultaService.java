package com.researchs.pdi.services;

import com.researchs.pdi.dto.PerguntasERespostasDTO;
import com.researchs.pdi.dto.PesquisaDTO;
import com.researchs.pdi.models.Pergunta;
import com.researchs.pdi.models.Pesquisa;
import com.researchs.pdi.models.Resposta;
import com.researchs.pdi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.researchs.pdi.utils.DateUtils.getDate;
import static com.researchs.pdi.utils.DateUtils.getParse;

@Service
public class ConsultaService {

    public static final String PESQUISA = "PESQUISA TESTE";
    public static final Date DATA = getDate(getParse("20/11/2016"));

    @Autowired
    private PesquisaService pesquisaService;

    @Autowired
    private PerguntaService perguntaService;

    @Autowired
    private RespostaService respostaService;

    @Autowired
    private FolhaService folhaService;

    public PesquisaDTO getEstruturaBasica(HttpServletRequest request) {
        Pesquisa pesquisa = pesquisaService.pesquisa(PESQUISA, DATA);
        return getPesquisaDTO(perguntaService, respostaService, folhaService, pesquisa);
    }

    private PesquisaDTO getPesquisaDTO(PerguntaService perguntaService, RespostaService respostaService, FolhaService folhaService, Pesquisa pesquisa) {
        PesquisaDTO pesquisaDTO = new PesquisaDTO();
        pesquisaDTO.setPesquisa(pesquisa);
        pesquisaDTO.setPerguntasERespostas(getPerguntasERespostas(perguntaService, respostaService, pesquisa));
        pesquisaDTO.setFolhas(folhaService.pesquisa(pesquisa));
        return pesquisaDTO;
    }

    private ArrayList<PerguntasERespostasDTO> getPerguntasERespostas(PerguntaService perguntaService, RespostaService respostaService, Pesquisa pesquisa) {
        ArrayList<PerguntasERespostasDTO> perguntasERespostas = new ArrayList<>();

        for (Pergunta pergunta: perguntaService.pesquisa(pesquisa)) {
            List<Resposta> respostas = respostaService.pesquisa(pergunta);

            PerguntasERespostasDTO perguntasERespostasDTO = new PerguntasERespostasDTO();
            perguntasERespostasDTO.setPergunta(pergunta);
            perguntasERespostasDTO.setRespostas(respostas);

            perguntasERespostas.add(perguntasERespostasDTO);
        }
        return perguntasERespostas;
    }

    public PesquisaDTO initDB(HttpServletRequest request) {
        pesquisaService.apagarTodasAsPesquisas();

        Pesquisa pesquisa = pesquisaService.novo("PESQUISA TESTE", getDate(getParse("20/11/2016")));
        Pergunta pergunta = perguntaService.novo(pesquisa, 1, "Faixa etária");
        respostaService.novo(pergunta, "a", "Entre 16 e 21 anos");
        respostaService.novo(pergunta, "b", "Entre 22 e 28 anos");
        respostaService.novo(pergunta, "c", "Entre 29 e 40 anos");
        respostaService.novo(pergunta, "d", "Entre 41 e 59 anos");
        respostaService.novo(pergunta, "e", "Acima de 60 anos");

        pergunta = perguntaService.novo(pesquisa, 2, "Escolaridade");
        respostaService.novo(pergunta, "a", "Analfabeto");
        respostaService.novo(pergunta, "b", "Fundamental inicial incompleto");
        respostaService.novo(pergunta, "c", "Fundamental inicial completo");
        respostaService.novo(pergunta, "d", "Fundamental final incompleto");
        respostaService.novo(pergunta, "e", "Fundamental final completo");
        respostaService.novo(pergunta, "f", "Ensino médio incompleto");
        respostaService.novo(pergunta, "g", "Ensino médio completo");
        respostaService.novo(pergunta, "h", "Superior incompleto");
        respostaService.novo(pergunta, "i", "Superior completo");

        pergunta = perguntaService.novo(pesquisa, 3, "Sexo");
        respostaService.novo(pergunta, "a", "Masculino");
        respostaService.novo(pergunta, "b", "Feminino");

        pergunta = perguntaService.novo(pesquisa, 4, "Renda Familiar");
        respostaService.novo(pergunta, "a", "Até 1 salário mínimo");
        respostaService.novo(pergunta, "b", "Entre 1 e 5 salários mínimos");
        respostaService.novo(pergunta, "c", "Entre 6 e 9 salários mínimos");
        respostaService.novo(pergunta, "d", "Entre 10 e 19 salários mínimos");
        respostaService.novo(pergunta, "e", "Acima de 19 salários mínimos");

        pergunta = perguntaService.novo(pesquisa, 5, "Mesorregião");
        respostaService.novo(pergunta, "a", "A");
        respostaService.novo(pergunta, "b", "B");
        respostaService.novo(pergunta, "c", "C");
        respostaService.novo(pergunta, "d", "D");
        respostaService.novo(pergunta, "e", "E");

        pergunta = perguntaService.novo(pesquisa, 6, "Em quem você votaria");
        respostaService.novo(pergunta, "a", "Candidato A");
        respostaService.novo(pergunta, "b", "Candidato B");
        respostaService.novo(pergunta, "c", "Candidato C");
        respostaService.novo(pergunta, "d", "Candidato D");
        respostaService.novo(pergunta, "e", "Nenhum / Não sabe / Não opinou");

        pergunta = perguntaService.novo(pesquisa, 7, "Em quem você não votaria");
        respostaService.novo(pergunta, "a", "Candidato A");
        respostaService.novo(pergunta, "b", "Candidato B");
        respostaService.novo(pergunta, "c", "Candidato C");
        respostaService.novo(pergunta, "d", "Candidato D");
        respostaService.novo(pergunta, "e", "Nenhum / Não sabe / Não opinou");

        folhaService.criarFolhas(pesquisa, 3);

        return null;
    }
}
