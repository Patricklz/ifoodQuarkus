package com.github.ptckz.ifood.cadastro.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.github.ptckz.ifood.cadastro.Restaurante;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {
	
	public Restaurante toRestaurante(AdicionarRestauranteDTO dto);
	

	public List<RestaurantesDTO> toRestaurantes(List<Restaurante> restaurantes);
	
	public void toAtualizarRestaurante(AtualizarRestauranteDTO dto, @MappingTarget Restaurante restaurante);

}
