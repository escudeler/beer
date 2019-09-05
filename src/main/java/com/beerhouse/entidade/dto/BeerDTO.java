package com.beerhouse.entidade.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class BeerDTO {
	private Integer id;
	
	@NotEmpty(message = "Name field can't be null.")
	private String name;

	@Length(min = 5, max = 150, message = "Ingredients field size must be between 5 and 150")
	@NotNull(message = "Ingredients field can't be null.")
	private String ingredients;
	
	@NotNull(message = "Alcohol Content field can't be null.")
	private String alcoholContent;

	@DecimalMin(value = "0.01", message = "Price field must be greater than zero")
	private BigDecimal price;
	
	@NotNull(message = "Category field can't be null.")
	private String category;

	public BeerDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getAlcoholContent() {
		return alcoholContent;
	}

	public void setAlcoholContent(String alcoholContent) {
		this.alcoholContent = alcoholContent;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
