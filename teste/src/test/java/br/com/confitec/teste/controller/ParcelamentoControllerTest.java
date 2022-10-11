package br.com.confitec.teste.controller;

import br.com.confitec.teste.exception.CoberturaException;
import br.com.confitec.teste.exception.RequestDtoNullException;
import br.com.confitec.teste.model.Dados;
import br.com.confitec.teste.model.RequestDto;
import br.com.confitec.teste.service.ParcelamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ParcelamentoControllerTest {

    @Mock
    private ParcelamentoService parcelamentoService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private EasyRandom easyRandom;
    private RequestDto dto;
    private List<Dados> dados;
    private String rota = "/confitec/teste/parcelamento";

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ParcelamentoController(parcelamentoService))
                .build();

        objectMapper = new ObjectMapper();
        easyRandom = new EasyRandom();
        dto = easyRandom.nextObject(RequestDto.class);
        dados =  easyRandom.objects(Dados.class, 3).collect(Collectors.toList());
    }

    @Test
    public void deveRecuperarParcelamento() throws Exception {
        when(parcelamentoService.recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento()))
                .thenReturn(dados);

        MvcResult result = mockMvc.perform(
                post(rota)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                ).andExpect(status().isOk())
                .andReturn();

        verify(parcelamentoService).recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento());

        List<Dados> listDados = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), new TypeReference<>(){});
        Assertions.assertEquals(listDados, dados);
    }

    @Test
    public void deveRetornarException_quandoRequestEstiverNullo() throws Exception {
        String msgErro = "Requisicao invalida";
        MvcResult result = request(null);
        Assertions.assertEquals(result.getResponse().getContentAsString(),msgErro);
    }

    @Test
    public void deveRetornarRequestDtoNullException_quandoTentarRecuperarParcelamento() throws Exception {

        String msgErro = "Requisicao invalida";
        when(parcelamentoService.recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento()))
                .thenThrow(new RequestDtoNullException(msgErro, HttpStatus.BAD_REQUEST));

        MvcResult result = request(dto);

        verify(parcelamentoService).recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento());
        Assertions.assertEquals(result.getResponse().getContentAsString(),msgErro);
    }


    @Test
    public void deveRetornarCoberturaException_quandoTentarRecuperarParcelamento() throws Exception {

        String msgErro = "Requisicao invalida";
        when(parcelamentoService.recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento()))
                .thenThrow(new CoberturaException(msgErro, HttpStatus.BAD_REQUEST));

        MvcResult result = request(dto);

        verify(parcelamentoService).recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento());
        Assertions.assertEquals(result.getResponse().getContentAsString(),msgErro);
    }

    @Test
    public void deveRetornarException_quandoTentarRecuperarParcelamento() throws Exception {
        String msgErro = "Requisicao invalida";
        when(parcelamentoService.recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento()))
                .thenThrow(new RuntimeException(msgErro));

        MvcResult result  = mockMvc.perform(
                        post(rota)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                ).andExpect(status().isInternalServerError())
                .andReturn();

        verify(parcelamentoService).recuperarParcelamento(dto.getListCobertura(), dto.getListOpcaoParcelamento());
        Assertions.assertEquals(result.getResponse().getContentAsString(),msgErro);
    }

    private MvcResult request(RequestDto requestDto) throws Exception {
        return mockMvc.perform(
                        post(rota)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                ).andExpect(status().isBadRequest())
                .andReturn();
    }

}
