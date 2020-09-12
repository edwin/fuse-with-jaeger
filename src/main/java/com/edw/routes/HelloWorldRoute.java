package com.edw.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;


/**
 * <pre>
 *     com.edw.routes.HelloWorldRoute
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 12 Sep 2020 22:15
 */
@Component
public class HelloWorldRoute extends RouteBuilder {

    @Autowired
    private Tracer tracer;

    @Override
    public void configure() throws Exception {
        rest()
                .get("hello")
                .route()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("200"))
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"))
                .process(exchange -> {
                    Span span = tracer.getCurrentSpan();
                    span.tag("something", "whatever");
                    span.tag("pretending to do some queries", "select 1 from dual");
                })
                .setBody(constant("{\"world\":\"world\"}"))
                .endRest()
        ;
    }
}
