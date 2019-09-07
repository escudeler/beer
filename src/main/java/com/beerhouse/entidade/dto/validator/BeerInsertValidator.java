package com.beerhouse.entidade.dto.validator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.beerhouse.entidade.dto.BeerInsertDTO;
import com.beerhouse.resource.exception.FieldMessage;
import com.beerhouse.service.BeerService;


public class BeerInsertValidator  implements ConstraintValidator<BeerInsert, BeerInsertDTO> {
	
	@Autowired
	private BeerService service;
	
	@Override
	public void initialize(BeerInsert ann) {
	}

	@Override
	public boolean isValid(BeerInsertDTO dto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if (!service.validaNome(dto.getName(), null))
			list.add(new FieldMessage("name","Nome já cadastrado no sistema."));
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}