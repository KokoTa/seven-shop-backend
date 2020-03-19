package com.example.shop.optional;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTest {

    @Test
    public void test() throws Exception {
        Optional<String> empty = Optional.ofNullable("A");

        System.out.println(empty.orElse("other"));

        System.out.println(empty.map(item -> item + "1").orElse("other"));

        empty.ifPresent(System.out::println);

        empty.orElseThrow(() -> new Exception("bad"));
    }
}
