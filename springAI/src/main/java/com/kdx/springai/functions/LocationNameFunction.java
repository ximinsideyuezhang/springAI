package com.kdx.springai.functions;



import java.util.Objects;
import java.util.function.Function;


public class LocationNameFunction implements Function<LocationNameFunction.Request, LocationNameFunction.Response> {

    @Override
    public Response apply(Request request) {
        if (Objects.isNull(request.location) || Objects.isNull(request.name)) {
            return new Response("缺少参数");
        }

        return new Response("有10个人");
    }

    //接收提取关键信息

    public record Request(
            String name,
            String location) {
        @Override
        public String name() {
            return name;
        }

        @Override
        public String location() {
            return location;
        }
    }

    //最终响应给gpt
    public record Response(String message) {
    }

}
