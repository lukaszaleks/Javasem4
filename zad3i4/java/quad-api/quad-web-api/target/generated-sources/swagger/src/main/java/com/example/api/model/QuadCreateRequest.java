package com.example.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * QuadCreateRequest
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-05-16T20:44:17.635226+02:00[Europe/Warsaw]")


public class QuadCreateRequest  implements Serializable  {
  private static final long serialVersionUID = 1L;

  @JsonProperty("brand")
  private String brand = null;

  @JsonProperty("model")
  private String model = null;

  @JsonProperty("year")
  private Integer year = null;

  public QuadCreateRequest brand(String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Get brand
   * @return brand
   **/
  @Schema(description = "")
  
    public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public QuadCreateRequest model(String model) {
    this.model = model;
    return this;
  }

  /**
   * Get model
   * @return model
   **/
  @Schema(description = "")
  
    public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public QuadCreateRequest year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   **/
  @Schema(description = "")
  
    public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuadCreateRequest quadCreateRequest = (QuadCreateRequest) o;
    return Objects.equals(this.brand, quadCreateRequest.brand) &&
        Objects.equals(this.model, quadCreateRequest.model) &&
        Objects.equals(this.year, quadCreateRequest.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brand, model, year);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuadCreateRequest {\n");
    
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
