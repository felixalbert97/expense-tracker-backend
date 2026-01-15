package de.felixalbert.expense_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // alle Endpoints
                        .allowedOrigins(
                            "http://localhost:3000", 
                            "https://expense-tracker-frontend-blue-nine.vercel.app"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true) // wenn Cookies oder Auth gebraucht werden
                        .allowedHeaders("*");  // alle Header erlauben
            }
        };
    }
}
