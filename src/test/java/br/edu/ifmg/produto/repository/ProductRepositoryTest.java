package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.util.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private Long existingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
    }

    @Test
    @DisplayName(value = "Verificando se o objeto não existe no BD depois de deletado.")
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existingId);
        Optional<Product> obj = productRepository.findById(existingId);

        Assertions.assertFalse(obj.isPresent());
    }

    @Test
    @DisplayName(value = "Verificando o autoincremento da chave primária.")
    public void insertShouldPersistWithAutoincrementIdWhenIdIsZero() {
        Product product = Factory.createProduct();
        product.setId(null);
        Product p = productRepository.save(product);
        Optional<Product> obj = productRepository.findById(p.getId());

        /* Assegura que o objeto foi inserido. */
        Assertions.assertTrue(obj.isPresent());

        /* Assegura que o ID do objeto mudou ao inserir no BD (autoincremento). */
        Assertions.assertNotEquals(0, obj.get().getId());

        Assertions.assertEquals(26, obj.get().getId());
    }
}
