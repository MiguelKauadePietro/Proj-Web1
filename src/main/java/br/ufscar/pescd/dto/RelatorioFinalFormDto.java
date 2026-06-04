package br.ufscar.pescd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RelatorioFinalFormDto {

    @NotNull
    @Min(0)
    @Max(100)
    private Integer indicadorFrequencia;

    private MultipartFile arquivo;
}
