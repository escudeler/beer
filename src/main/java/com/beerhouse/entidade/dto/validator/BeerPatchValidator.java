package com.beerhouse.entidade.dto.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.beerhouse.entidade.Beer;
import com.beerhouse.entidade.dto.BeerPatchDTO;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.resource.exception.FieldMessage;

public class BeerPatchValidator implements ConstraintValidator<BeerPatch, BeerPatchDTO> {

	@Autowired
	private BeerRepository repository;

	@Autowired
	private HttpServletRequest req;

	@Override
	public void initialize(BeerPatch ann) {
	}

	@Override
	public boolean isValid(BeerPatchDTO dto, ConstraintValidatorContext context) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) req
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer idUri = Integer.parseInt(map.get("id"));

		List<FieldMessage> list = new ArrayList<>();
		
		Optional<Beer> beerBD = repository.findById(idUri);
		
		if (beerBD != null && !idUri.equals(beerBD.get().getId()))
			list.add(new FieldMessage("name", "Nome já cadastrado no sistema."));

		if (dto.getName() != null) {
			if (dto.getName().isEmpty())
				list.add(new FieldMessage("name", "Campo Nome deve ser preenchido."));
			if (dto.getName().length() > 50)
				list.add(new FieldMessage("name", "Campo Nome deve ter no máximo 50 caracteres."));
		}

		if (dto.getIngredients() != null) {
			if (dto.getIngredients().isEmpty())
				list.add(new FieldMessage("ingredients", "Campo Ingredientes deve ser preenchido."));
			if (dto.getIngredients().length() < 5 || dto.getIngredients().length() > 150)
				list.add(new FieldMessage("ingredients", "Campo Ingredientes deve ter de 5 a 150 caracteres."));
		}

		if (dto.getAlcoholContent() != null) {
			if (dto.getAlcoholContent().isEmpty())
				list.add(new FieldMessage("alcoholContent", "Campo Teor Alcóolico deve ser preenchido."));
			if (dto.getName().length() > 20)
				list.add(new FieldMessage("alcoholContent", "Campo Teor Alcóolico deve ter no máximo 20 caracteres."));
		}

		if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) <= 0)
			list.add(new FieldMessage("price", "Campo Preço deve ser maior que zero."));

		if (dto.getCategory() != null) {
			if (dto.getCategory().isEmpty())
				list.add(new FieldMessage("category", "Campo Categoria deve ser preenchido."));
			if (dto.getCategory().length() > 50)
				list.add(new FieldMessage("category", "Campo Categoria deve ter no máximo 50 caracteres."));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}