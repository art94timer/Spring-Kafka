package com.art.intex.controller;

import com.art.intex.service.interfaces.PersonProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonProducerService service;

    @PostMapping
    public String savePerson(@RequestBody Object person) {
        service.transferToStream(person);
        return person.toString();
    }
}
