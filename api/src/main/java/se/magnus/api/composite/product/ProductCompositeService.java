package se.magnus.api.composite.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductCompositeService {

  @GetMapping(value = "/product-composite/{id}", produces = "application/json")
  ProductAggregate getProduct(@PathVariable int id);

}
