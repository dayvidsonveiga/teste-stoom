package br.com.stoom.store.business.interfaces;

import br.com.stoom.store.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAll();

}
