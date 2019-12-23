package gabor.csikos.reactive.web.webflux;

import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void fluxTestRange(){
        Flux<Integer> flux = Flux.range(1,3);
        StepVerifier.create(flux).expectNext(1,2,3).verifyComplete();
    }

    @Test
    public void fluxTestFilter(){
        List<String> names = Arrays.asList("Gabor", "Szabina");
        Flux<String> flux = Flux.fromIterable(names).filter(name -> name.startsWith("S"));
        StepVerifier.create(flux).expectNext("Szabina").verifyComplete();
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
