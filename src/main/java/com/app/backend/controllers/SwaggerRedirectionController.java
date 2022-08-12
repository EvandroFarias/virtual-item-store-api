package com.app.backend.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping()
@Api(value = "Redirect Controller")
@ApiIgnore()
public class SwaggerRedirectionController {

    @GetMapping
    public void redirection(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/swagger-ui.html");
    }
}
