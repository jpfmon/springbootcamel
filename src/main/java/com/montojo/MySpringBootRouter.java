package com.montojo;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Objects;

//import static com.montojo.config.CamelConfig.RABBIT_URI;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.sjms2;


/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {


    WeatherDataProvider weatherDataProvider;

    public MySpringBootRouter(WeatherDataProvider weatherDataProvider) {
        this.weatherDataProvider = weatherDataProvider;
    }

    @Override
    public void configure() {


//        from("timer:hello?period={{timer.period}}").routeId("hello")
//            .transform().method("myBean", "saySomething")
//            .filter(simple("${body} contains 'foo'"))
//                .to("log:foo")
//            .end()
//            .to("stream:out");
////
//        from("timer://test1?period=2000")
//                .process(exchange -> exchange.getIn().setBody(new Order()))
//                .marshal().json(JsonLibrary.Gson)
//                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//                .to("http://localhost:8080/order")
//                .process(exchange -> log.info("HTTP POST response is: {}", exchange.getIn().getBody(String.class)));
//
//
//        from("timer://test2?period=1000")
//                .process(exchange -> exchange.getIn().setBody(simple(null)))
//                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
//                .setHeader(Exchange.HTTP_QUERY, constant("name=Frank"))
//                .to("http://localhost:8080/hello")
//                .process(exchange -> log.info("HTTP GET response is: {}", exchange.getIn().getBody(String.class)));

//
//        from("rest:get:/wheather/{city}?produces=application/json")
//                .process(exchange -> {
//                    String city = exchange.getMessage().getHeader("city", String.class);
//                    WeatherDTO cityWeather = (new WeatherDataProvider()).retrieveWeather(city);
//                    System.out.println(cityWeather);
//
//                    if (cityWeather != null) {
//                        Message message = new DefaultMessage(exchange.getContext());
//                        message.setBody(cityWeather);
//                        exchange.setMessage(message);
//                    } else {
//                        exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, HttpStatus.NOT_FOUND.value());
//                        exchange.getMessage().setBody(HttpStatus.NOT_FOUND);
//                    }
//                })
//                .marshal().json(JsonLibrary.Gson);
//
//        from("rest:get:/wheatherOption2/{city}?produces=application/json")
//                .process(this::getWeatherData)
//                .marshal().json(JsonLibrary.Gson);

/*

        restConfiguration()
                .component("servlet")
                        .bindingMode(RestBindingMode.json);

        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/rest/{city}").outType(WeatherDTO.class)
                .route()
                .process(this::getWeatherData)
                .marshal().json(JsonLibrary.Gson)
                .log(LoggingLevel.WARN, "USING REST()");

        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/jms/{city}").outType(WeatherDTO.class).to("direct:get-weather")
                .post("/weather").type(WeatherDTO.class).to("direct:savedata");

        from("direct:get-weather").process(this::getWeatherData);

        from("direct:savedata").process(this::saveData)
                .wireTap("direct:eventtorabbit");

//        docker run -d --hostname rabbit-ui-host --name rabbit-ui -p 15672:15672 -p 5672:5672 rabbitmq:3-management

        from("direct:eventtorabbit")
                .marshal().json(JsonLibrary.Jackson, WeatherDTO.class)
                .toF(RABBIT_URI, "weatherevent", "weatherevent");
*/

        from("timer:hello?period={{timer.period}}").routeId("hello")
            .transform().method("myBean", "saySomething")
                        .to(sjms2("sjms2", "DistributedQueue"));


        from(sjms2("sjms2", "DistributedQueue"))
                .routeId("WeblogicJMS")
                .to("stream::out");

        new WeatherDTO(){{
            setCity("madrid");
        }};

    }

    private void saveData(Exchange exchange) {
        WeatherDTO dto = exchange.getMessage().getBody(WeatherDTO.class);
        System.out.println("DTO is: " + dto);
        weatherDataProvider.saveModifyCity(dto);
    }


    private void getWeatherData(Exchange exchange) {
        String city = exchange.getMessage().getHeader("city", String.class);
        WeatherDTO cityWeather = weatherDataProvider.retrieveWeather(city);

        if (Objects.nonNull(cityWeather)) {
            Message message = new DefaultMessage(exchange.getContext());
            message.setBody(cityWeather);
            exchange.setMessage(message);
        } else {
            exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, HttpStatus.NOT_FOUND.value());
            exchange.getMessage().setBody(HttpStatus.NOT_FOUND);
        }
    }
}
