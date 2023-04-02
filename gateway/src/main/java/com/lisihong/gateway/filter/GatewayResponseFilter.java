package com.lisihong.gateway.filter;//package com.lisihong.gateway.filter;
//
//import com.alibaba.fastjson2.JSON;
//import org.reactivestreams.Publisher;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.core.io.buffer.DefaultDataBufferFactory;
//import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Component
//public class GatewayResponseFilter implements GlobalFilter, Ordered {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return chain.filter(exchange.mutate()
//                .response(new ServerHttpResponseDecorator(exchange.getResponse()) {
//                    @Override
//                    public Mono<Void> writeWith(
//                            Publisher<? extends DataBuffer> body) {
//                        return super.writeWith(((Flux<? extends DataBuffer>) body)
//                                .buffer().map(dataBuffers -> {
//                                    DataBuffer join = new DefaultDataBufferFactory()
//                                            .join(dataBuffers);
//                                    byte[] content
//                                            = new byte[join.readableByteCount()];
//                                    join.read(content);
//                                    DataBufferUtils.release(join);
////                                    byte[] wrappedContent =
////                                            JSON.toJSONBytes("");
////                                    getHeaders()
////                                            .setContentLength(wrappedContent.length);
//                                    return bufferFactory().wrap(content);
//                                }));
//                    }
//                }).build());
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}