package com.example.springsecurityoptionsmethod;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/hellos")
public class HelloController {

    private List<Hello> hellos = new ArrayList<>();

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<Hello>> getHellos() {
        return ResponseEntity.ok(hellos);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Hello> getHello(@PathVariable int id) {
        return ResponseEntity.ok(hellos.stream().filter(hello -> hello.id.equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Void> postHello(@RequestBody Hello hello, UriComponentsBuilder b) {
        hello.setId(Hello.ID_GENERATOR.getAndIncrement());
        hellos.add(hello);
        UriComponents uriComponents = b.path("/hellos/{id}").buildAndExpand(hello.getId());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @Data
    private static class Hello {
        static AtomicInteger ID_GENERATOR = new AtomicInteger(1);

        private Integer id;
        private String name;
    }

}
