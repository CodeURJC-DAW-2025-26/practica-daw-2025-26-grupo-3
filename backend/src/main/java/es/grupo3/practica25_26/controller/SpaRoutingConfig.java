package es.grupo3.practica25_26.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;
import java.io.IOException;

@Configuration
public class SpaRoutingConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Intercept everything under /new/.
        registry.addResourceHandler("/new", "/new/", "/new/**")
                .addResourceLocations("classpath:/static/new/")
                .resourceChain(true)
                .addResolver(new SpaRouter()); 
    }

    /**
     * Internal resolver that decides which file to return
     * depending on whether the request targets a static asset or a React Router route.
     */
    private static class SpaRouter extends PathResourceResolver {
        
        @Override
        protected Resource getResource(String requestPath, Resource baseLocation) throws IOException {
            Resource requestedResource = baseLocation.createRelative(requestPath);

            // If the resource exists (js, css, images), return it directly.
            if (requestedResource.exists() && requestedResource.isReadable()) {
                return requestedResource;
            }

            // If a file with an extension (.png, .map) is requested and does not exist, force a 404.
            if (requestPath.contains(".")) {
                return null;
            }

            //  If it is an invented route, hand control to React (index.html).
            return new ClassPathResource("/static/new/index.html");
        }
    }
}
