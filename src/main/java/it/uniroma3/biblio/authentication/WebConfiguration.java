package it.uniroma3.biblio.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") //applichiamo queste regole agli url /api/**
                .allowedOrigins("http://localhost:5173") //chi è autorizzato a fare chiamate verso il backend
                .allowedMethods("GET", "POST")
                .allowedHeaders("*") //accettiamo qualsiasi header HTTP inviato da Axios
                .allowCredentials(true); //consentiamo al browser di inviare e ricevere le credenziali
    }
}