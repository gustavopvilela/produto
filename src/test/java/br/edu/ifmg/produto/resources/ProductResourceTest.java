package br.edu.ifmg.produto.resources;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.services.ProductService;
import br.edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ProductResource.class)
class ProductResourceTest {
    @Autowired
    private MockMvc mockMvc; // Responsável pelas requisições que quero testar.

    @MockitoBean
    private ProductService productService; // Camada que quero mockar.

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
        productDTO = Factory.createProductDTO();
        productDTO.setId(existingId);
        page = new PageImpl<>(List.of(productDTO));
    }

    @Test
    void findAllShouldReturnAllPages () throws Exception {
        // Criar o método mockado.
        when(productService.findAll(any())).thenReturn(page);

        // Teste da requisição
        ResultActions result = mockMvc.perform(get("/product"));

        result.andExpect(status().isOk());
    }

    @Test
    void findByIdShouldReturnProductWhenIdExists () throws Exception {
        // Criar o método mockado.
        when(productService.findById(existingId)).thenReturn(productDTO);

        // Teste da requisição
        ResultActions result = mockMvc.perform(
                get("/product/{id}", existingId).accept("application/json")
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
    }
}