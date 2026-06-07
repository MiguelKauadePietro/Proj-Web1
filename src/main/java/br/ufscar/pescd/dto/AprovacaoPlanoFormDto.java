package br.ufscar.pescd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AprovacaoPlanoFormDto {

    @NotBlank
    private String parecer;
}