package br.edu.ifmg.produto.util;

import br.edu.ifmg.produto.dtos.ProductDTO;
import br.edu.ifmg.produto.entities.Category;
import br.edu.ifmg.produto.entities.Product;

public class Factory {
    public static Product createProduct () {
        Product p = new Product();
        p.setName("iPhone XXX");
        p.setPrice(5000);
        p.setImageUrl("https://i1.sndcdn.com/artworks-VUPODJGHzFVyhXgV-ccH6ug-t500x500.jpg");
        p.getCategories().add(new Category(3L, "News"));
        return p;
    }

    public static ProductDTO createProductDTO () {
        Product p = createProduct();
        return new ProductDTO(p);
    }

}
