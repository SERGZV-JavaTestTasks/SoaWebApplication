package ru.app.web.soa.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController
{
    @GetMapping("/{state}")
    public boolean ReverseState(@PathVariable("state") boolean stateToReverse)
    {
        return !stateToReverse;
    }
}
