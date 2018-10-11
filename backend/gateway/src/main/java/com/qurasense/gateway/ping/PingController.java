package com.qurasense.gateway.ping;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class PingController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Date pingTime() {
        return new Date();
    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public void cloudHealthCheck() {}

}
