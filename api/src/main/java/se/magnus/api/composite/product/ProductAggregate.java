package se.magnus.api.composite.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductAggregate {

  private final int productId;
  private final String name;
  private final int weight;
  private final List<RecommendationSummary> recommendations;
  private final List<ReviewSummary> reviews;
  private final ServiceAddresses serviceAddresses;

  public ProductAggregate() {
    this.productId = -1;
    this.name = null;
    this.weight = -1;
    this.recommendations = null;
    this.reviews = null;
    this.serviceAddresses = null;
  }
}
