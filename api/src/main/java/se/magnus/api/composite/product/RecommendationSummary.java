package se.magnus.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecommendationSummary {

  private final int recommendationId;
  private final String author;
  private final int rate;

  public RecommendationSummary() {
    recommendationId = -1;
    author = null;
    rate = 0;
  }

}
