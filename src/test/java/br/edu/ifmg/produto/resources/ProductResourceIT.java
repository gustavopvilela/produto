package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.util.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    // Objeto que irá fazer as requisições
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    public void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName () throws Exception {
        ResultActions result = mockMvc.perform(
                get("/product?page=0&size=10&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
    }

    @Test
    public void updateShouldReturnDtoWhenIdExists() throws Exception {
        ProductDTO dto = Factory.createProductDTO();
        String json = objectMapper.writeValueAsString(dto);

        String nameExpected = dto.getName();
        String descriptionExpected = dto.getDescription();

        ResultActions result = mockMvc.perform(
                put("/product/{id}", existingId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(nameExpected));
        result.andExpect(jsonPath("$.description").value(descriptionExpected));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ProductDTO dto = Factory.createProductDTO();
        String json = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                put("/product/{id}", nonExistingId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnNewObjectWhenDataAreCorrect() throws Exception {
        // Checar se status é 202 (created)
        // Checar se ID é 26
        // Checar se o nome é o esperado
        ProductDTO dto = Factory.createProductDTO();
        String json = objectMapper.writeValueAsString(dto);

        Long idExpected = 26L;
        String nameExpected = dto.getName();

        ResultActions result = mockMvc.perform(
                post("/product")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(idExpected));
        result.andExpect(jsonPath("$.name").value(nameExpected));
    }

    @Test
    public void deleteShouldReturnOkNoContentWhenIdExists() throws Exception {
        ProductDTO dto = Factory.createProductDTO();
        String json = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                delete("/product/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ProductDTO dto = Factory.createProductDTO();
        String json = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(
                delete("/product/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/product/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        String resultJson = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultJson);

        ProductDTO dto = objectMapper.readValue(resultJson, ProductDTO.class);

        Assertions.assertEquals(existingId, dto.getId());
        Assertions.assertEquals("The Lord of the Rings", dto.getName());
    }
}
