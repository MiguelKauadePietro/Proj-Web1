package br.ufscar.pescd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentacaoDocenciaFormDto {

    @NotBlank
    private String nomeInstituicao;

    @NotBlank
    private String nomeDisciplina;

    @NotBlank
    private String cursoDisciplina;

    @NotNull
    @Min(1)
    private Integer cargaHoraria;

    private MultipartFile arquivo;
}
