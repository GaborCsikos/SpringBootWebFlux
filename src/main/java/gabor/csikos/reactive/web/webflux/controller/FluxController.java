package gabor.csikos.reactive.web.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxController {


    @GetMapping(value = "/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> returnFluxStream() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping("/flux/exception")
    public Flux<Integer> runtimeException() {

        return Flux.just(1, 2, 3, 4)
                .concatWith(Mono.error(new Exception("Exception Occurred.")));
    }
}