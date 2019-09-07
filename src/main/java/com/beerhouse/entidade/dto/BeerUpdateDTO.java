package com.beerhouse.entidade.dto;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.beerhouse.entidade.dto.validator.BeerUpdate;
@BeerUpdate
public class BeerUpdateDTO {
	
	private Integer id;
	
	@Length(max = 50, message = "Campo Nome deve ter no máximo 50 caracteres.")
	@NotEmpty(message = "Campo Nome deve ser preenchido.")
	private String name;

	@Length(min = 5, max = 150, message = "Campo Ingredientes deve ter de 5 a 150 caracteres.")
	@NotBlank(message = "Campo Ingredientes deve ser preenchido.")
	private String ingredients;
	
	@Length(max = 20, message = "Campo Teor Alcóolico deve ter no máximo 20 caracteres.")
	@NotBlank(message = "Campo Teor Alcóolico deve ser preenchido.")
	private String alcoholContent;

	@DecimalMin(value = "0.01", message = "Campo Preço deve ser maior que zero.")
	private BigDecimal price;
	
	@Length(max = 50, message = "Campo Categoria deve ter no máximo 50 caracteres.")
	@NotBlank(message = "Campo Categoria deve ser preenchido.")
	private String category;

	public BeerUpdateDTO() {
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
