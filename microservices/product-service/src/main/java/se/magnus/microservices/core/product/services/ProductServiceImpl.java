package se.magnus.microservices.core.product.services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.util.http.ServiceUtil;

@RestController
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ServiceUtil serviceUtil;


  @Override
  public Product getProduct(int id) {
    final String address = serviceUtil.getServiceAddress();
    return new Product(id, "name-" + id, 123, address);
  }

}
