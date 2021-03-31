package cn.oreo.gateway.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 */
@RestController
public class IndexController {

    @RequestMapping("/")
    public Mono<String> index() {
        return Mono.just("oreo cloud gateway");
    }
}
