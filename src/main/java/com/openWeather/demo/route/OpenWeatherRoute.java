package com.openWeather.demo.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenWeatherRoute extends RouteBuilder {

    @Value("${openweather.api.url}")
    private String openWeatherApiUrl;

    @Value("${openweather.api.key}")
    private String apiKey;

    @Override
    public void configure() throws Exception {
        from("direct:weatherRoute")  // Raccoglie dati ogni minuto
                .routeId("openWeatherRoute")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_QUERY, simple("q=${body}&appid=" + apiKey + "&units=metric"))
                .toD(openWeatherApiUrl)
                .log("Received weather data: ${body}")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getIn().setBody(body);
                });
    }
}

