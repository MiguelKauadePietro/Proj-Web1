package br.ufscar.pescd.service;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArquivoStorageService {

    private static final long TAMANHO_MAXIMO_BYTES = 5L * 1024 * 1024;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final Path raizStorage = Path.of("uploads");

    public String salvarPdf(MultipartFile arquivo, String pasta) {
        validarPdf(arquivo);

        try {
            Path diretorioDestino = raizStorage.resolve(pasta);
            Files.createDirectories(diretorioDestino);

            String nomeOriginal = arquivo.getOriginalFilename() != null ? arquivo.getOriginalFilename() : "arquivo.pdf";
            String nomeSanitizado = sanitizarNome(nomeOriginal);
            String nomeFinal = LocalDateTime.now().format(FORMATTER) + "-" + UUID.randomUUID() + "-" + nomeSanitizado;

            Path destino = diretorioDestino.resolve(nomeFinal);
            try (InputStream inputStream = arquivo.getInputStream()) {
                Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
            }

            return destino.toString();
        } catch (IOException ex) {
            throw new IllegalStateException("Não foi possível salvar o arquivo enviado.");
        }
    }

    public void validarPdf(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo PDF é obrigatório.");
        }

        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || !nomeOriginal.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            throw new IllegalArgumentException("Envie um arquivo no formato PDF.");
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw new IllegalArgumentException("O arquivo PDF deve ter no máximo 5MB.");
        }
    }

    public Path carregarArquivo(String arquivoPath) {
        if (arquivoPath == null || arquivoPath.isBlank()) {
            throw new EntityNotFoundException("Arquivo não disponível.");
        }

        Path caminho = Paths.get(arquivoPath).normalize();
        Path raizNormalizada = raizStorage.toAbsolutePath().normalize();
        Path caminhoAbsoluto = caminho.toAbsolutePath().normalize();

        if (!caminhoAbsoluto.startsWith(raizNormalizada)) {
            throw new EntityNotFoundException("Arquivo inválido.");
        }

        if (!Files.exists(caminhoAbsoluto) || !Files.isRegularFile(caminhoAbsoluto)) {
            throw new EntityNotFoundException("O arquivo solicitado não foi encontrado no servidor.");
        }

        return caminhoAbsoluto;
    }

    private String sanitizarNome(String nomeArquivo) {
        String semAcentos = Normalizer.normalize(nomeArquivo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        String normalizado = semAcentos.replaceAll("[^a-zA-Z0-9._-]", "_");
        return normalizado.isBlank() ? "arquivo.pdf" : normalizado;
    }
}
