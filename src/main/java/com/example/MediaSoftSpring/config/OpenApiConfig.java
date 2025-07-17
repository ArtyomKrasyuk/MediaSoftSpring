package com.example.MediaSoftSpring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Rating System API",
                description = "API системы оценок ресторанов",
                version = "1.0.0",
                contact = @Contact(
                        name = "Красюк Артём Максимович",
                        email = "artem.krasyuk2004@gmail.com"
                )
        )
)
public class OpenApiConfig {
}
