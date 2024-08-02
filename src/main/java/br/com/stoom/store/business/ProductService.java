package br.com.stoom.store.business;

import br.com.stoom.store.business.interfaces.IProductService;
import br.com.stoom.store.model.Product;
import br.com.stoom.store.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> findAll(){
        return productRepository.findAll();
    }

}
