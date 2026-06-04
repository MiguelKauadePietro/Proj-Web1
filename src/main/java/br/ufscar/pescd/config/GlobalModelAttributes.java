package br.ufscar.pescd.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.UriComponentsBuilder;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAttributes {

    @ModelAttribute
    public void addLanguageLinks(Model model, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();

        model.addAttribute("langUrlPt", buildLanguageUrl(requestUri, queryString, "pt_BR"));
        model.addAttribute("langUrlEn", buildLanguageUrl(requestUri, queryString, "en"));
    }

    private String buildLanguageUrl(String requestUri, String queryString, String language) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(requestUri);

        if (queryString != null && !queryString.isBlank()) {
            builder.query(queryString);
        }

        return builder.replaceQueryParam("lang", language)
                .build()
                .toUriString();
    }
}
