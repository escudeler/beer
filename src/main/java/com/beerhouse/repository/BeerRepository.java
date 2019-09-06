package com.beerhouse.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.beerhouse.entidade.Beer;

@Repository
public interface BeerRepository  extends JpaRepository<Beer, Integer>{
	@Transactional(readOnly=true)
	Beer findByName(String name);
}
