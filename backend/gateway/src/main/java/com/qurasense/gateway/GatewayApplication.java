package com.qurasense.gateway;

import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.gateway.zip.ZipSampleFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.web.reactive.function.server.RouterFunctions.resources;

@Configuration
@SpringBootApplication
public class GatewayApplication {

    private final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    @Autowired
    private SimpleMicroserviceRegistry microserviceRegistry;

    @Autowired
    private ZipSampleFunction zipFunction;

    @Bean
    public WebFilter corsFilter() {
        return new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange ctx, WebFilterChain chain) {
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Methods",
                        "GET, PUT, POST, DELETE, OPTIONS");
                ctx.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
                ctx.getResponse().getHeaders().add("Access-Control-Allow-Headers",
                        "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
                if (ctx.getRequest().getMethod() == HttpMethod.OPTIONS) {
                    ctx.getResponse().setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                } else {
                    return chain.filter(ctx);
                }
            }
        };
    }

    @Bean
    public WebFilter httpsRedirectFilter() {
        return new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                URI originalUri = exchange.getRequest().getURI();
                List<String> forwardedValues = exchange.getRequest().getHeaders().get("x-forwarded-proto");
                if (forwardedValues != null && forwardedValues.contains("http")) {
                    try {
                        URI mutatedUri = new URI("https",
                                originalUri.getUserInfo(),
                                originalUri.getHost(),
                                originalUri.getPort(),
                                originalUri.getPath(),
                                originalUri.getQuery(),
                                originalUri.getFragment());
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                        response.getHeaders().setLocation(mutatedUri);
                        return Mono.empty();
                    } catch (URISyntaxException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
                return chain.filter(exchange);
            }
        };
    }

    @Bean
    public WebFilter rootForwardFilter() {
        return  new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                if (exchange.getRequest().getURI().getPath().equals("/")) {
                    return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build());
                }
                return chain.filter(exchange);
            }
        };
    }

    @Bean
    public RouterFunction<ServerResponse> monoRouterFunction() {
//        return route(RequestPredicates.path("/aggregate/sample"), zipFunction)
//                .and(resources("/**", new ClassPathResource("public/")));
        return resources("/**", new ClassPathResource("public/"));
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/user_api/**")
                .filters(f -> f
                        .rewritePath("/user_api", "")
                        .removeRequestHeader("Referer")
                        .removeRequestHeader("Origin"))
                .uri(microserviceRegistry.getUserUrl()))
            .route(r -> r
                .path("/health_api/**")
                .filters(f -> f
                        .rewritePath("/health_api", "")
                        .removeRequestHeader("Referer")
                        .removeRequestHeader("Origin"))
                .uri(microserviceRegistry.getHealthUrl()))
            .route(r -> r
                .path("/lab_api/**")
                .filters(f -> f
                        .rewritePath("/lab_api", "")
                        .removeRequestHeader("Referer")
                        .removeRequestHeader("Origin"))
                .uri(microserviceRegistry.getLabUrl()))
            .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}