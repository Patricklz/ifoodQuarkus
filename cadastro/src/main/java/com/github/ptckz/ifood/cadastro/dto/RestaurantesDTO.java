package com.github.ptckz.ifood.cadastro.dto;

import java.util.Date;

import javax.validation.constraints.Size;

public class RestaurantesDTO {
	
    public String proprietario;
    
    public String cnpj;
    
    public String nome;
    
    public LocalizacaoDTO localizacao;
    
    public Date dataCriacao;


}
