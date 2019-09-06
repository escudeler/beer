package com.beerhouse.entidade.dto.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.beerhouse.entidade.Beer;
import com.beerhouse.entidade.dto.BeerUpdateDTO;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.resource.exception.FieldMessage;


public class BeerUpdateValidator  implements ConstraintValidator<BeerUpdate, BeerUpdateDTO> {
	
	@Autowired
	private BeerRepository repository;

	@Autowired
	private HttpServletRequest req;
	
	@Override
	public void initialize(BeerUpdate ann) {
	}

	@Override
	public boolean isValid(BeerUpdateDTO dto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String,String> map = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer idUri = Integer.parseInt(map.get("id"));
		
		
		List<FieldMessage> list = new ArrayList<>();
		Beer beerBD = repository.findByName(dto.getName());
		
		if (beerBD != null && !idUri.equals(beerBD.getId()))
			list.add(new FieldMessage("name","Nome já cadastrado no sistema."));
					
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}