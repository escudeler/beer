package com.beerhouse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.entidade.Beer;
import com.beerhouse.entidade.BeerTestCase;
import com.beerhouse.service.BeerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackageClasses = Application.class)
@SuiteClasses({ BeerTestCase.class })
public class ApplicationTests {
	@Autowired
	BeerService service;
	
	@Test
	public void contextLoads() {
		/*
		 *  Inclusão da Cerveja 1 Nova com nome inexistente 
		 */
		String nome = "Cerveja Repetida Teste";
		assertTrue(service.validaNome(nome, null)); //Deve retornar true, por não existir no BD "Cerveja Repetida Teste"
		Beer b1 = new Beer(0, nome, "Diversos", "2%", BigDecimal.valueOf(12.55), "Indian Pale Ale");		
		b1 = service.insert(b1); //Cerveja inserida

		/*
		 *  Alteração da Cerveja 1 com nome inexistente    
		 */
		String nomeAlterado = "Cerveja Repetida";
		b1.setName(nomeAlterado);
		assertTrue(service.validaNome(nomeAlterado, b1.getId())); //Deve retornar true, por não existir no BD "Cerveja Repetida"
		
		/*
		 *  Inclusão de Cerveja 2 com nome inexistente    
		 */
		String nomeConflito = "Cerveja Conflitante";
		assertTrue(service.validaNome(nomeConflito, null)); //Deve retornar true, por não existir no BD "Cerveja Conflito"
		Beer b2 = new Beer(0, nomeConflito, "Diversos", "2%", BigDecimal.valueOf(12.55), "Indian Pale Ale");
		b2 = service.insert(b2); //Cerveja inserida
		
		/*
		 *  Alteração da Cerveja 2 com mesmo nome da cerveja 3
		 */
		b1.setName(nomeConflito);
		assertFalse(service.validaNome(nomeConflito, b1.getId()));
	}
}