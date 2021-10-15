package com.buyukli.ivan.urlshortner.controllers;

import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("rest/url")
@RestController
public class UrlShornterResource {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/{id}")
    public RedirectView redirectWithUsingRedirectView(@PathVariable String id){
        String url = stringRedisTemplate.opsForValue().get(id);
        System.out.println("URL_Retrieved " + url);
        return new RedirectView(url);
    }

    @PostMapping
    public String createUrl(@RequestBody String url) {

        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );

        if (urlValidator.isValid(url)) {
            int hash = url.hashCode();
            String id = String.valueOf(hash);
            System.out.println("Your url was shorted: " + id);
            stringRedisTemplate.opsForValue().set(id, url);

            return id;
        }

        throw new RuntimeException("Illegal url");
    }
}
