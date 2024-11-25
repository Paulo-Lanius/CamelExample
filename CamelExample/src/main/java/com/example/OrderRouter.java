package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.example.model.Drink;
import com.example.model.Food;
import com.example.model.Payment;
import com.example.model.Dessert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm");

        // Rota para processar arquivos JSON de bebidas
        from("file:orders?include=.*drink.*.json&delete=true")
            .unmarshal().json(JsonLibrary.Jackson, Drink.class)
            .process(exchange -> {
                Drink drink = exchange.getIn().getBody(Drink.class);
                String message = String.format("Pedido mesa %d: %s de %s\nObs: %s", 
                                                drink.getMesa(), drink.getTipo(), drink.getSabor(), drink.getObservacao());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pedido_mesa_%d_drink_%s.txt", drink.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/bar");

        // Rota para processar arquivos JSON de comidas
        from("file:orders?include=.*food.*.json&delete=true")
            .unmarshal().json(JsonLibrary.Jackson, Food.class)
            .process(exchange -> {
                Food food = exchange.getIn().getBody(Food.class);
                String message = String.format("Pedido mesa %d: %s %s\nObs: %s", 
                                                food.getMesa(), food.getMenu(), food.getTipo(), food.getObservacao());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pedido_mesa_%d_food_%s.txt", food.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/kitchen");

        // Rota para processar arquivos JSON de pagamentos
        from("file:orders?include=.*pay.*.json&delete=true")
            .unmarshal().json(JsonLibrary.Jackson, Payment.class)
            .process(exchange -> {
                Payment payment = exchange.getIn().getBody(Payment.class);
                String message = String.format("Pagamento mesa %d: %.2f, no %s.", 
                                                payment.getMesa(), payment.getValor(), payment.getTipoDePagamento());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pagamento_mesa_%d_%s.txt", payment.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/checkout");

        // New route for desserts
        from("file:orders?include=.*dessert.*.json&delete=true")
            .unmarshal().json(JsonLibrary.Jackson, Dessert.class)
            .process(exchange -> {
                Dessert dessert = exchange.getIn().getBody(Dessert.class);
                String message = String.format("Pedido mesa %d: %s de %s\nObs: %s", 
                                                dessert.getMesa(), dessert.getTipo(), dessert.getSabor(), dessert.getObservacao());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pedido_mesa_%d_dessert_%s.txt", dessert.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/kitchen");

        // Configure REST DSL
        restConfiguration().component("servlet").host("localhost").port(8080);

        // Route for processing drink orders
        rest("/orders/drink")
            .post()
            .consumes("application/json")
            .to("direct:processDrink");

        from("direct:processDrink")
            .unmarshal().json(JsonLibrary.Jackson, Drink.class)
            .process(exchange -> {
                Drink drink = exchange.getIn().getBody(Drink.class);
                String message = String.format("Pedido mesa %d: %s de %s\nObs: %s", 
                                                drink.getMesa(), drink.getTipo(), drink.getSabor(), drink.getObservacao());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pedido_mesa_%d_drink_%s.txt", drink.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/bar");

        // Route for processing food orders
        rest("/orders/food")
            .post()
            .consumes("application/json")
            .to("direct:processFood");

        from("direct:processFood")
            .unmarshal().json(JsonLibrary.Jackson, Food.class)
            .process(exchange -> {
                Food food = exchange.getIn().getBody(Food.class);
                String message = String.format("Pedido mesa %d: %s %s\nObs: %s", 
                                                food.getMesa(), food.getMenu(), food.getTipo(), food.getObservacao());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pedido_mesa_%d_food_%s.txt", food.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/kitchen");

        // Route for processing payment orders
        rest("/orders/payment")
            .post()
            .consumes("application/json")
            .to("direct:processPayment");

        from("direct:processPayment")
            .unmarshal().json(JsonLibrary.Jackson, Payment.class)
            .process(exchange -> {
                Payment payment = exchange.getIn().getBody(Payment.class);
                String message = String.format("Pagamento mesa %d: %.2f, no %s.", 
                                                payment.getMesa(), payment.getValor(), payment.getTipoDePagamento());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pagamento_mesa_%d_%s.txt", payment.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/checkout");

        // Route for processing dessert orders
        rest("/orders/dessert")
            .post()
            .consumes("application/json")
            .to("direct:processDessert");

        from("direct:processDessert")
            .unmarshal().json(JsonLibrary.Jackson, Dessert.class)
            .process(exchange -> {
                Dessert dessert = exchange.getIn().getBody(Dessert.class);
                String message = String.format("Pedido mesa %d: %s de %s\nObs: %s", 
                                                dessert.getMesa(), dessert.getTipo(), dessert.getSabor(), dessert.getObservacao());
                exchange.getIn().setBody(message);
                String timestamp = LocalDateTime.now().format(formatter);
                String fileName = String.format("pedido_mesa_%d_dessert_%s.txt", dessert.getMesa(), timestamp);
                exchange.getIn().setHeader("CamelFileName", fileName);
            })
            .to("file:out/kitchen");
    }
}