package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.example.model.Drink;
import com.example.model.Food;
import com.example.model.Payment;

@Component
public class OrderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Rota para processar arquivos JSON de bebidas
        from("file:orders?include=.*drink.*.json&delete=true")
            .unmarshal().json(JsonLibrary.Jackson, Drink.class)
            .process(exchange -> {
                Drink drink = exchange.getIn().getBody(Drink.class);
                String message = String.format("Pedido mesa %d: %s de %s\nObs: %s", 
                                                drink.getMesa(), drink.getTipo(), drink.getSabor(), drink.getObservacao());
                exchange.getIn().setBody(message);
                exchange.getIn().setHeader("CamelFileName", exchange.getIn().getHeader("CamelFileName", String.class).replace(".json", ".txt"));
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
                exchange.getIn().setHeader("CamelFileName", exchange.getIn().getHeader("CamelFileName", String.class).replace(".json", ".txt"));
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
                exchange.getIn().setHeader("CamelFileName", exchange.getIn().getHeader("CamelFileName", String.class).replace(".json", ".txt"));
            })
            .to("file:out/checkout");
    }
}