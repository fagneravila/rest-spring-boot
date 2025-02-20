package br.com.avila.rest_spring_boot.controllers;

import br.com.avila.rest_spring_boot.model.Greenting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreentingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("/greeting")
    public Greenting greenting(@RequestParam(value = "name", defaultValue = "World") String name){
      return new Greenting(counter.incrementAndGet(), String.format(template,name));
    }

}
