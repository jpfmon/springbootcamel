package com.montojo;

import org.springframework.web.bind.annotation.*;

@RestController
public class MyRestController {
    @PostMapping("/order")
    public String postBody(@RequestBody Order order) {
        System.out.println("\nInvoked POST rest with " + order);
        String note = "Item ordered on " + new java.util.Date().toString();
        return note;
    }

    @GetMapping("/hello")
    public String postBody(@RequestParam String name) {
        System.out.println("\nInvoked GET rest with " + name);
        return "Hello " + name;
    }

//    @GetMapping("/weather")
//    public String postBody(@RequestBody WeatherDTO weatherDTO) {
//        System.out.println("\nInvoked POST rest with " + weatherDTO);
//
//        return "Weather";
//    }
}
