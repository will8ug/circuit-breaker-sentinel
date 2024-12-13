package net.will.circuitbreaker.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralResource {
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ping() {
        System.out.println("Service is running");
    }
}
