package com.beerhouse.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beerhouse.entidade.Beer;
import com.beerhouse.entidade.dto.BeerDTO;
import com.beerhouse.entidade.dto.BeerDTOPatch;
import com.beerhouse.repository.BeerRepository;

@Service
public class BeerService {
	@Autowired
	BeerRepository repository;

	public List<Beer> findAll() {
		return repository.findAll();
	}

	public Beer findById(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Beer with id " + id + " not found."));
	}

	public Beer insert(@Valid Beer beer) {
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


	public Beer fromDTO(@Valid BeerDTO dto) {
		return new Beer(dto.getId(), dto.getName(), dto.getIngredients(), dto.getAlcoholContent(), dto.getPrice(),
				dto.getCategory());
	}

	public Beer fromDTO(BeerDTOPatch dto) {
		return new Beer(dto.getId(), dto.getName(), dto.getIngredients(), dto.getAlcoholContent(), dto.getPrice(),
				dto.getCategory());
	}

}
