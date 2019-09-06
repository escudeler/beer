package com.beerhouse.service;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beerhouse.entidade.Beer;
import com.beerhouse.entidade.dto.BeerInsertDTO;
import com.beerhouse.entidade.dto.BeerPatchDTO;
import com.beerhouse.entidade.dto.BeerUpdateDTO;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.resource.exception.ObjectNotFoundException;

@Service
public class BeerService {
	@Autowired
	BeerRepository repository;

	public List<Beer> findAll() {
		return repository.findAll();
	}

	public Beer findById(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Não foi encontrada a cerveja com id " + id + "."));
	}

	public Beer insert(Beer beer) {
		beer.setId(0);
		return repository.save(beer);
	}

	public Beer update(Beer beer) {
		Beer beerDB = findById(beer.getId());
		merge(beerDB, beer);
		return repository.save(beerDB);
	}

	private void merge(Beer beerDB, Beer beer) {
		if (beer.getName() != null && !beer.getName().isEmpty())
			beerDB.setName(beer.getName());
		if (beer.getIngredients() != null && !beer.getIngredients().isEmpty())
			beerDB.setIngredients(beer.getIngredients());
		if (beer.getAlcoholContent() != null && !beer.getAlcoholContent().isEmpty())
			beerDB.setAlcoholContent(beer.getAlcoholContent());
		if (beer.getPrice() != null && beer.getPrice().compareTo(BigDecimal.ZERO) > 0)
			beerDB.setPrice(beer.getPrice());
		if (beer.getCategory() != null && !beer.getCategory().isEmpty())
			beerDB.setCategory(beer.getCategory());
	}

	public void delete(Integer id) {
		findById(id);
		repository.deleteById(id);
	}
	
	public boolean validaNome(String nome, Integer id) {
		Beer bd = repository.findByName(nome);
		if (id == null)
			return bd == null;
		else
			return bd == null || ( bd != null && bd.getId().equals(id));
	}

	public Beer fromDTO(@Valid BeerInsertDTO dto) {
		return new Beer(dto.getId(), dto.getName(), dto.getIngredients(), dto.getAlcoholContent(), dto.getPrice(),
				dto.getCategory());
	}

	public Beer fromDTO(@Valid BeerUpdateDTO dto) {
		return new Beer(dto.getId(), dto.getName(), dto.getIngredients(), dto.getAlcoholContent(), dto.getPrice(),
				dto.getCategory());
	}

	public Beer fromDTO(@Valid BeerPatchDTO dto) {
		return new Beer(dto.getId(), dto.getName(), dto.getIngredients(), dto.getAlcoholContent(), dto.getPrice(),
				dto.getCategory());
	}

}
