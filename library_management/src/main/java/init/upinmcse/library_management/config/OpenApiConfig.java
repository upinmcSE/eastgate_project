package init.upinmcse.library_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfig {

    private Server createServer(String url, String description) {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription(description);
        return server;
    }

    @Bean
    public OpenAPI libraryManagementOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                    createServer("http://localhost:8080", "Server URL in Development environment")
                ))
                .info(new Info()
                        .title("Library Management API")
                        .description("API documentation for Library Management System")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Upin Moi")
                                .email("upindzai@gmail.com")));
    }
}
