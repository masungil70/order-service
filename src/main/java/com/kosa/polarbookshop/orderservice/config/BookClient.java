package com.kosa.polarbookshop.orderservice.config;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.kosa.polarbookshop.orderservice.book.Book;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class BookClient {
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient; // 이전에 설정된 WebClient 빈

    public Mono<Book> getBookByIsbn(String isbn) {
        return webClient
                .get() // 요청은 get 메소드를 사용한다
                .uri(BOOKS_ROOT_API + isbn) // 요청 URL은 /books/{isbn}임
                .retrieve() // 요청을 보내고 응답을 받는다
                .bodyToMono(Book.class) // 받은 객체를 Mono<Book>으로 반환한다
                .timeout(Duration.ofSeconds(3), Mono.empty()) // GET 요청에 대한 3초의 타임 아웃을 설정한다
                // 404 오류가 발생하면 빈객체를 리턴한다
                .onErrorResume(WebClientResponseException.NotFound.class, exception -> Mono.empty())
                .retryWhen(
                        // 지수 백어프를 재시도 전략으로 사용 100ms의 초기 백오프로 총 3회까지 시도 한다
                        Retry.backoff(3, Duration.ofMillis(100)))
                // 3회의 재시도 오류가 발생하면 예외를 포작하고 빈 객체를 반환한다
                .onErrorResume(Exception.class, exception -> Mono.empty());

    }
}
