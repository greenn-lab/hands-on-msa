package se.magnus.microservices.composite.product.services;

import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.recommendation.RecommendationService;
import se.magnus.api.core.review.Review;
import se.magnus.api.core.review.ReviewService;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;
import se.magnus.util.http.HttpErrorInfo;

@Service
@Slf4j
public class ProductCompositeIntegration
    implements ProductService, RecommendationService, ReviewService {

  private final RestTemplate restTemplate;
  private final ObjectMapper mapper;

  private final String productServiceUrl;
  private final String recommendationServiceUrl;
  private final String reviewServiceUrl;

  @Autowired
  public ProductCompositeIntegration(
      RestTemplate restTemplate,
      ObjectMapper mapper,

      @Value("${app.product-service.host}") String productServiceHost,
      @Value("${app.product-service.port}") int productServicePort,

      @Value("${app.recommendation-service.host}") String recommendationServiceHost,
      @Value("${app.recommendation-service.port}") int recommendationServicePort,

      @Value("${app.review-service.host}") String reviewServiceHost,
      @Value("${app.review-service.port}") int reviewServicePort
  ) {

    this.restTemplate = restTemplate;
    this.mapper = mapper;

    final String protocol = "http://";

    productServiceUrl = protocol + productServiceHost + ":" + productServicePort + "/product/";
    recommendationServiceUrl =
        protocol + recommendationServiceHost + ":" + recommendationServicePort
            + "/recommendation?productId=";
    reviewServiceUrl =
        protocol + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
  }

  public Product getProduct(int productId) {

    try {
      final String url = productServiceUrl + productId;
      logger.debug("Will call getProduct API on URL: {}", url);

      final Product product = restTemplate.getForObject(url, Product.class);

      if (null != product) {
        logger.debug("Found a product with id: {}", product.getProductId());
      }

      return product;
    } catch (HttpClientErrorException ex) {

      switch (ex.getStatusCode()) {

        case NOT_FOUND:
          throw new NotFoundException(getErrorMessage(ex));

        case UNPROCESSABLE_ENTITY:
          throw new InvalidInputException(getErrorMessage(ex));

        default:
          logger.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
          logger.warn("Error body: {}", ex.getResponseBodyAsString());
          throw ex;
      }
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioe) {
      return ex.getMessage();
    }
  }

  public List<Recommendation> getRecommendations(int productId) {

    try {
      String url = recommendationServiceUrl + productId;

      logger.debug("Will call getRecommendations API on URL: {}", url);
      final List<Recommendation> recommendations = restTemplate.exchange(url, GET, null,
          new ParameterizedTypeReference<List<Recommendation>>() {
          }).getBody();

      if (null != recommendations) {
        logger.debug("Found {} recommendations for a product with id: {}", recommendations.size(),
            productId);
      }

      return recommendations;

    } catch (Exception ex) {
      logger.warn(
          "Got an exception while requesting recommendations, return zero recommendations: {}",
          ex.getMessage());
      return new ArrayList<>();
    }
  }

  public List<Review> getReviews(int productId) {

    try {
      String url = reviewServiceUrl + productId;

      logger.debug("Will call getReviews API on URL: {}", url);
      final ResponseEntity<List<Review>> exchange = restTemplate.exchange(url, GET,
          null, ParameterizedTypeReference.forType(List.class));
      final List<Review> reviews = Optional.ofNullable(exchange.getBody())
          .orElse(Collections.emptyList());

      logger.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
      return reviews;

    } catch (Exception ex) {
      logger.warn("Got an exception while requesting reviews, return zero reviews: {}",
          ex.getMessage());
      return new ArrayList<>();
    }
  }
}
