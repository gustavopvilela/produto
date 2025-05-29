package br.edu.ifmg.produto.repository;

import br.edu.ifmg.produto.entities.Product;
import br.edu.ifmg.produto.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(nativeQuery = true, value = """
        SELECT * FROM
        (    
            SELECT DISTINCT p.id, p.name, p.image_url, p.price
            FROM tb_product p
            INNER JOIN tb_product_category pc ON pc.product_id = p.id
            WHERE
                (:categoriesID IS NULL || pc.category_id in :categoriesID) AND
                LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        ) as tb_result
    """,
    countQuery = """
        SELECT COUNT(*) FROM
        (
            SELECT DISTINCT p.id, p.name, p.image_url, p.price
            FROM tb_product p
            INNER JOIN tb_product_category pc ON pc.product_id = p.id
            WHERE
                (:categoriesID IS NULL || pc.category_id in :categoriesID) AND
                LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        ) as tb_result
    """)
    /* O atributo countQuery não é necessário quando a consulta não é paginada. */
    public Page<ProductProjection> searchProducts (List<Long> categoriesID, String name, Pageable pageable);
}