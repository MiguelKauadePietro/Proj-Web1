package br.ufscar.pescd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PlanoTrabalhoFormDto {

    @NotBlank
    private String codigoDisciplina;

    @NotBlank
    private String nomeDisciplina;

    @NotBlank
    private String cursoDisciplina;

    @NotNull
    private Long professorSupervisorId;

    private MultipartFile arquivo;
}
