package br.edu.ifmg.produto.services;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.repository.ProductRepository;
import br.edu.ifmg.produto.services.exceptions.ResourceNotFound;
import br.edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 200L;
        Product product = Factory.createProduct();
        page = new PageImpl<>(List.of(product, product));
    }

    @Test
    @DisplayName(value = "Verificando se o objeto foi deletado do BD.")
    void deleteShouldDoNothingWhenIdExists () {
        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);

        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));
        verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName(value = "Verificando se levanta uma exceção se o objeto não existe no BD.")
    void deleteShouldThrowExceptionWhenIdNotExists () {
        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        //doNothing().when(productRepository).deleteById(nonExistingId);

        Assertions.assertThrows(ResourceNotFound.class, () -> productService.delete(nonExistingId));
        verify(productRepository, Mockito.times(0)).deleteById(nonExistingId);
    }

    @Test
    @DisplayName(value = "Verificando se o findAll retorna os dados paginados.")
    void findAllShouldReturnOnePage () {
        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Pageable pagina = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAll(pagina);

        Assertions.assertNotNull(result);
        verify(productRepository, Mockito.times(1)).findAll(pagina);
    }

    @Test
    @DisplayName(value = "Verificando a busca de um produto por um ID existente.")
    void findByIdShouldReturnProductWhenIdExists () {
        Product p = Factory.createProduct();
        p.setId(existingId);
        when(productRepository.findById(existingId)).thenReturn(Optional.of(p));

        ProductDTO dto = productService.findById(existingId);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(existingId, dto.getId());
        verify(productRepository, Mockito.times(1)).findById(existingId);
    }
}