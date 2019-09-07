package com.beerhouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.beerhouse.entidade.Beer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackageClasses = Application.class)
public class ApplicationTests {
	@LocalServerPort
	int randomServerPort;

	@Test
	@Order(1)
	public void validaCrudPorRest() throws URISyntaxException, JsonParseException, JsonMappingException, IOException, JSONException {
		// Preparação para o CRUD
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Mapper Para converter o Json para Beer
		ObjectMapper mapper = new ObjectMapper();

		// URI POST
		String urlBase = "http://localhost:" + randomServerPort + "/craftbeer/beers";
		URI uriBase = new URI(urlBase);

		/*
		 * Validação POST 1 - Correto -> /craftbeer/beers
		 */
		// Cerveja 1 Nova com nome inexistente
		Beer b1 = new Beer(0, "Cerveja Teste", "Diversos", "2%", BigDecimal.valueOf(12.55), "Indian Pale Ale");
		HttpEntity<Beer> requestB1 = new HttpEntity<>(b1, headers);

		ResponseEntity<String> respB1 = restTemplate.postForEntity(uriBase, requestB1, String.class);
		assertEquals(201, respB1.getStatusCodeValue());
		assertEquals(respB1.getHeaders().getLocation().toString(), urlBase.concat("/1"));

		/*
		 * Validação POST 2 - Correto {Mesmo com Valor no ID} -> /craftbeer/beers
		 */
		// Cerveja 2 Nova com nome inexistente
		String nomeConflito = "Cerveja Conflitante";
		Beer b2 = new Beer(3, nomeConflito, "Diversos", "1.5 - 2.7%", BigDecimal.valueOf(12.55), "Indian Pale Ale");
		HttpEntity<Beer> requestB2 = new HttpEntity<>(b2, headers);
		ResponseEntity<String> respB2 = restTemplate.postForEntity(uriBase, requestB2, String.class);
		assertEquals(201, respB2.getStatusCodeValue());
		assertEquals(respB2.getHeaders().getLocation().toString(), urlBase.concat("/2"));
		// Seto o ID, para validar o GET posteriormente. Visto que o
		// retorno do POST é apenas a URI
		b2.setId(2);

		/*
		 * Validação POST 3 - Erro Conflito -> /craftbeer/beers
		 */

		Beer b3 = new Beer(0, nomeConflito, "Maltes lupulados", "14%", BigDecimal.valueOf(59.80), "America Pale Ale");

		HttpEntity<Beer> requestB3 = new HttpEntity<>(b3, headers);

		try {
			restTemplate.postForEntity(uriBase, requestB3, String.class);
			fail();
		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertTrue(ex.getResponseBodyAsString().contains("Nome já cadastrado no sistema"));
		}

		/*
		 * Validação POST 3 - Correto -> /craftbeer/beers
		 */
		b3.setName("Cerveja Modelo");
		HttpEntity<Beer> requestB3OK = new HttpEntity<>(b3, headers);

		ResponseEntity<String> respB3 = restTemplate.postForEntity(uriBase, requestB3OK, String.class);
		assertEquals(201, respB3.getStatusCodeValue());
		assertEquals(respB3.getHeaders().getLocation().toString(), urlBase.concat("/3"));

		// Utilizarei em todos os Metodos de Requisição HTTP com parâmetro, o id 2
		URI uriBeer2 = new URI(urlBase.concat("/").concat(b2.getId().toString()));

		/*
		 * Validação PUT Correto -> /craftbeer/beers/{id}
		 */
		b2.setIngredients("Malte extremamente suspeito");
		b2.setAlcoholContent("40 - 50%");
		b2.setCategory("EPA - Nova Categoria");
		b2.setPrice(BigDecimal.valueOf(15.99));
		HttpEntity<Beer> requestB2Put = new HttpEntity<>(b2, headers);
		restTemplate.put(uriBeer2, requestB2Put);
		
		/*
		 *  Validação PUT Campo obrigatório não preenchido -> /craftbeer/beers/{id}
		 */
		b2.setIngredients("");
		try {
			restTemplate.put(uriBeer2, requestB2Put);
			fail();
		} catch (RestClientResponseException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertTrue(ex.getResponseBodyAsString().contains("Campo Ingredientes deve ser preenchido"));	
		}
		
		/*
		 *  Validação PATCH -> /craftbeer/beers/{id}
		 */
		b2.setIngredients("Malte extremamente suspeito");
		b2.setAlcoholContent("40 - 50%");
		b2.setCategory("EPA - Nova Categoria");
		b2.setPrice(BigDecimal.ZERO);
		HttpEntity<Beer> requestB2Patch = new HttpEntity<>(b2, headers);
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();		
		restTemplate.setRequestFactory(requestFactory);
		try {
			restTemplate.patchForObject(uriBeer2, requestB2Patch, String.class);
			fail();
		} catch (RestClientResponseException e) {
			assertEquals(400, e.getRawStatusCode());
			assertTrue(e.getResponseBodyAsString().contains("Campo Preço deve ser maior que zero"));	

		}
		
		/*
		 *   Validação PATCH OK -> /craftbeer/beers/{id} 
		 */
		Beer bPatchOk = new Beer();
		bPatchOk.setIngredients("Alteração via patch OK");

		HttpEntity<Beer> requestB2PatchOK = new HttpEntity<>(bPatchOk, headers);
		restTemplate.patchForObject(uriBeer2, requestB2PatchOK, String.class);
		
		/*
		 * Validação GET id -> /craftbeer/beers/{id}
		 */
		ResponseEntity<String> respB2Get = restTemplate.getForEntity(uriBeer2, String.class);

		assertEquals(200, respB2Get.getStatusCodeValue());
		Beer beer2Get = mapper.readValue(respB2Get.getBody(), new TypeReference<Beer>() {
		});

		assertEquals(b2, beer2Get);

		/*
		 * Validação GET List<Beers> -> /craftbeer/beers
		 */
		ResponseEntity<String> respGetList = restTemplate.getForEntity(uriBase, String.class);

		assertEquals(200, respGetList.getStatusCodeValue());
		List<Beer> beersGet = mapper.readValue(respGetList.getBody(), new TypeReference<List<Beer>>() {
		});
		List<Beer> beersAdc = new ArrayList<Beer>();
		beersAdc.addAll(Arrays.asList(b1, b2, b3));

		assertEquals(beersAdc, beersGet);

		/*
		 * Validação Delete Beers/{id}
		 */
		restTemplate.delete(uriBeer2);
		
		/*
		 * Validação Delete - Não Existente Beers/{id}
		 */
		try {
			restTemplate.delete(uriBeer2);
			fail();
		} catch (RestClientResponseException ex) {
			assertEquals(404, ex.getRawStatusCode());
			assertTrue(ex.getResponseBodyAsString().contains("Não foi encontrada a cerveja com id 2"));
		}
	}

}