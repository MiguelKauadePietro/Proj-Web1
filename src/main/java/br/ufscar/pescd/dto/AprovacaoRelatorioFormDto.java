package br.ufscar.pescd.dto;

import br.ufscar.pescd.entity.enums.Nota;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AprovacaoRelatorioFormDto {

    @NotBlank
    private String parecer;

    @NotNull
    @Min(0)
    @Max(100)
    private Double frequencia;

    @NotNull
    private Nota sugestaoNota;
}