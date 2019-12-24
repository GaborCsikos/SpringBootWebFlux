package gabor.csikos.reactive.web.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTest {

    @Test
    public void basicFluxTest() {
        Flux<String> flux = Flux.just("First", "Second", "Third");
        StepVerifier.create(flux).expectNext("First", "Second", "Third").verifyComplete();
    }

    @Test
    public void fluxTest() {
        Flux<String> flux = Flux.just("First", "Second", "Third");
        StepVerifier.create(flux).expectNextCount(3).verifyComplete();
    }

    @Test
    public void fluxMerge() {
        Flux<String> flux1 = Flux.just("First", "Second", "Third");
        Flux<String> flux2 = Flux.just("First1", "Second2", "Third3");
        Flux<String> flux = Flux.merge(flux1, flux2);
        StepVerifier.create(flux).expectNextCount(6).verifyComplete();
    }

    @Test
    public void fluxTestRange() {
        Flux<Integer> flux = Flux.range(1, 3);
        StepVerifier.create(flux).expectNext(1, 2, 3).verifyComplete();
    }


    @Test
    public void fluxErrorTest() {
        Flux<String> flux = Flux.just("First", "Second", "Third").concatWith(Flux.error(new RuntimeException("Own exception")));
        StepVerifier.create(flux).expectNextCount(3).expectErrorMessage("Own exception").verify();
    }

    @Test
    public void returnOnErrorTest() {
        Flux<String> flux = Flux.just("First", "Second", "Third")
                .concatWith(Flux.error(new RuntimeException("Own exception"))).onErrorReturn("Error");
        StepVerifier.create(flux).expectNext("First", "Second", "Third", "Error")
                .verifyComplete();
    }

    @Test
    public void monoTest() {
        Mono<String> mono = Mono.just("Only");
        StepVerifier.create(mono).expectNext("Only");
    }

    @Test
    public void emptyMonoTest() {
        Mono<String> mono = Mono.empty();
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    public void fluxTestFilter() {
        List<String> names = Arrays.asList("Gabor", "Szabina");
        Flux<String> flux = Flux.fromIterable(names).filter(name -> name.startsWith("S"));
        StepVerifier.create(flux).expectNext("Szabina").verifyComplete();
    }

    @Test
    public void fluxTestRepeat() {
        List<String> names = Arrays.asList("Gabor", "Szabina");
        Flux<Integer> flux = Flux.fromIterable(names).map(s -> s.length()).repeat(1);
        StepVerifier.create(flux).expectNext(5, 7, 5, 7).verifyComplete();
    }

    @Test
    public void fluxTestMap() {
        List<String> names = Arrays.asList("Gabor", "Szabina");
        Flux<String> flux = Flux.fromIterable(names).map(s -> s.toUpperCase());
        StepVerifier.create(flux).expectNext("GABOR", "SZABINA").verifyComplete();
    }

    @Test
    public void testParallelSequentialFlatMap() {
        List<String> names = Arrays.asList("Gabor", "Szabina", "Aizhan", "Gabor", "Szabina", "Aizhan");

        Flux<String> flux = Flux.fromIterable(names)
                .window(2)
                .flatMapSequential(s -> s.map(a -> a.concat(" Csikos")).subscribeOn(parallel()));
        StepVerifier.create(flux).expectNext("Gabor Csikos", "Szabina Csikos",
                "Aizhan Csikos", "Gabor Csikos", "Szabina Csikos",
                "Aizhan Csikos").verifyComplete();

    }


    @Test
    public void fluxWithDuration() {
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1)).take(3);
        StepVerifier.create(flux.log()).expectSubscription()
                .expectNext(0L, 1L, 2L).verifyComplete();
    }

    //Improved
    @Test
    public void fluxWithVirtualTime() {
        VirtualTimeScheduler.getOrSet();
        Flux<Long> flux = Flux.interval(Duration.ofSeconds(1)).take(3);
        StepVerifier.withVirtualTime(() -> flux.log())
                .expectSubscription().thenAwait(Duration.ofSeconds(3))
                .expectNext(0L, 1L, 2L).verifyComplete();
    }
}
