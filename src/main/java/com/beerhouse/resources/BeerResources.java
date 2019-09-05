package com.beerhouse.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.beerhouse.entidade.Beer;
import com.beerhouse.entidade.dto.BeerDTO;
import com.beerhouse.entidade.dto.BeerDTOPatch;
import com.beerhouse.service.BeerService;

@RestController
@RequestMapping(value = "/beers")
public class BeerResources {

	@Autowired
	BeerService service;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Beer>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Beer> find(@PathVariable Integer id){
		return ResponseEntity.ok().body(service.findById(id));
	}
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody BeerDTO dto) {
		Beer beer = service.fromDTO(dto);
		beer = service.insert(beer);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(beer.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updPut(@Valid @RequestBody BeerDTO dto, @PathVariable Integer id){
		Beer beer = service.fromDTO(dto);
		beer.setId(id); 
		beer = service.update(beer);
		return ResponseEntity.ok().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> updPatch(@Valid @RequestBody BeerDTOPatch dto, @PathVariable Integer id){
		Beer beer = service.fromDTO(dto);
		beer.setId(id); 
		beer = service.update(beer);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
