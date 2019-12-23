package gabor.csikos.reactive.web.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void basicFluxTest(){
        Flux<String> flux = Flux.just("First", "Second", "Third");
        StepVerifier.create(flux).expectNext("First", "Second", "Third").verifyComplete();
    }

    @Test
    public void fluxTest(){
        Flux<String> flux = Flux.just("First", "Second", "Third");
        StepVerifier.create(flux).expectNextCount(3).verifyComplete();
    }

    @Test
    public void fluxErrorTest(){
        Flux<String> flux = Flux.just("First", "Second", "Third").concatWith(Flux.error(new RuntimeException("Own exception")));
        StepVerifier.create(flux).expectNextCount(3).expectErrorMessage("Own exception");
    }

    @Test
    public void monoTest(){
        Mono<String> mono = Mono.just("Only");
        StepVerifier.create(mono).expectNext("Only");
    }

    @Test
    public void emptyMonoTest(){
        Mono<String> mono = Mono.empty();
        StepVerifier.create(mono).verifyComplete();
    }
}
