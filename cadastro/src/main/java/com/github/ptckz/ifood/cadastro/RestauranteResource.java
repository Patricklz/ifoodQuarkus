package com.github.ptckz.ifood.cadastro;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.github.ptckz.ifood.cadastro.dto.AdicionarRestauranteDTO;
import com.github.ptckz.ifood.cadastro.dto.AtualizarRestauranteDTO;
import com.github.ptckz.ifood.cadastro.dto.RestauranteMapper;
import com.github.ptckz.ifood.cadastro.dto.RestaurantesDTO;
import com.github.ptckz.ifood.cadastro.infra.ConstraintViolationResponse;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {
	
	@Inject
	RestauranteMapper restauranteMapper;

    @GET
    @Tag(name = "restaurante")
    public List<RestaurantesDTO> buscarRestaurantes() {
    	List<Restaurante> restaurantes = Restaurante.listAll();
    	
    	List<RestaurantesDTO> restaurantesDTO = restauranteMapper.toRestaurantes(restaurantes);
    	
    	return restaurantesDTO;
    }
    
    @POST
    @Transactional
    @Tag(name = "restaurante")
    @APIResponse(responseCode = "201", description = "Caso  restaurante seja cadastrado com sucesso")
    @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    public Response adicionar(@Valid AdicionarRestauranteDTO dto) {
    	Restaurante restaurante = restauranteMapper.toRestaurante(dto);
    	
    	restaurante.persist();
    	
    	return Response.status(Status.CREATED).build();
    	
    }
    
    @PUT
    @Path("{id}")
    @Transactional
    @Tag(name = "restaurante")
    public void atualizar(@PathParam("id") Long id, AtualizarRestauranteDTO dto) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
    	
    	if(restauranteOp.isEmpty()) {
    		throw new NotFoundException();
    	}
    	
    	Restaurante restaurante = restauranteOp.get();
    	
    	restauranteMapper.toAtualizarRestaurante(dto, restaurante);
    	
    	restaurante.persist();
    }
    
    @DELETE
    @Path("{id}")
    @Transactional
    @Tag(name = "restaurante")
    public void delete(@PathParam("id") Long id ) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
    	
    	restauranteOp.ifPresentOrElse(Restaurante::delete, () -> { throw new NotFoundException(); } );

    }
    
    //Pratos
    
    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<Restaurante> buscarPratos(@PathParam("idRestaurante") Long idRestaurante, Restaurante dto) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    	
    	if(restauranteOp.isEmpty()) {
    		throw new NotFoundException("Restaurante n??o existe");
    	}
    	
    	return Prato.list("restaurante", restauranteOp.get());
    }
    
    
    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "prato")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, Prato dto) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    	
    	if(restauranteOp.isEmpty()) {
    		throw new NotFoundException("Restaurante n??o existe");
    	}
    	
    	Prato prato = new Prato();
    	prato.nome = dto.nome;
    	prato.descricao = dto.descricao;
    	
    	prato.preco = dto.preco;
    	prato.restaurante = restauranteOp.get();
    	prato.persist();
    	
    	return Response.status(Status.CREATED).build();
    	
    }
    
    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato dto) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    	
    	if(restauranteOp.isEmpty()) {
    		throw new NotFoundException("Restaurante n??o existe");
    	}
    	
    	Optional<Prato> pratoOp = Prato.findByIdOptional(id);
    	
    	if(pratoOp.isEmpty()) {
    		throw new NotFoundException("Prato n??o existe");
    	}
    	
    	Prato prato = pratoOp.get();   	
    	prato.preco = dto.preco;
    	prato.persist();
   
    }
    
    
    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, Prato dto) {
    	Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
    	
    	if(restauranteOp.isEmpty()) {
    		throw new NotFoundException("Restaurante n??o existe");
    	}
    	
    	Optional<Prato> pratoOp = Prato.findByIdOptional(id);
    	
    	pratoOp.ifPresentOrElse(Prato::delete, () -> { throw new NotFoundException(); });
   
    }
    

}