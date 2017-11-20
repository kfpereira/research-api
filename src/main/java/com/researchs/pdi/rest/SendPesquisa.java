package com.researchs.pdi.rest;

import com.researchs.pdi.dto.EntrevistadoReceiveDTO;
import com.researchs.pdi.dto.PesquisaDTO;
import com.researchs.pdi.services.ConsultaService;
import com.researchs.pdi.services.EntrevistadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/dados")
public class SendPesquisa {

    @Autowired
    private ConsultaService service;

    @Autowired
    private EntrevistadoService entrevistadoService;

    @RequestMapping("/estrutura-basica")
    public PesquisaDTO estruturaBasica(HttpServletRequest request) {
        return service.getEstruturaBasica(request);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/initDB")
    public PesquisaDTO initDB(HttpServletRequest request) {
        return service.initDB(request);
    }



    @RequestMapping(method = RequestMethod.POST, value = "/enviar-entrevistados")
    public ResponseEntity enviarEntrevistados(
            HttpServletRequest request,
            @RequestBody List<EntrevistadoReceiveDTO> comando) throws NotFoundException {

        try {
            for (EntrevistadoReceiveDTO entrevistadoReceiveDTO : comando) {
                entrevistadoService.salvar(entrevistadoReceiveDTO);
            }

            return ResponseEntity.ok(null);
        }
        catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public class NotFoundException extends Exception {
        public NotFoundException(String msg) {
            super(msg);
        }
    }
}
