package se.magnus.microservices.core.product.api.core.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Product {

  private final int productId;
  private final String name;
  private final int weight;
  private final String serviceAddress;

}
