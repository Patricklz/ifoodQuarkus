package com.github.ptckz.ifood.cadastro.dto;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CNPJ;

import com.github.ptckz.ifood.cadastro.Restaurante;
import com.github.ptckz.ifood.cadastro.infra.DTO;
import com.github.ptckz.ifood.cadastro.infra.ValidDTO;

@ValidDTO
public class AdicionarRestauranteDTO implements DTO{
	
	@NotEmpty
	@NotBlank
    public String proprietario;
    
	@Pattern(regexp = "[0-9]{2}\\.[0-9]{3}\\/[0-9]{4}\\-[0-9]{2}")
	@CNPJ
    public String cnpj;
    
    @Size(min = 3, max = 30)
    public String nome;
    
    public LocalizacaoDTO localizacao;
    
    @Override
    public boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
    	constraintValidatorContext.disableDefaultConstraintViolation();
    	
    	if(Restaurante.find("cnpj", cnpj).count() > 0) {
    		constraintValidatorContext.buildConstraintViolationWithTemplate("CNPJ já cadastrado").addPropertyNode("cnpj").addConstraintViolation();
    		return false;
    	}
    	return true;
    }


}
