package com.kosa.polarbookshop.orderservice.book;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.kosa.polarbookshop.orderservice.config.BookClient;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BookClientTests {
    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start(); // 테스트 테이스를 실행하기 위해 모의 서버를 시작한다
        var webCleint = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString())
                .build();

        this.bookClient = new BookClient(webCleint);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown(); // 테스트 테이스가 끝나면 모의 서버를 종료한다
    }

    @Test
    void 도서가_존재하는_경우_도서_반환_테스트() {
        var bookIsbn = "234567890";

        // 모의 서버에 의해 반화되는 응답을 정의한다
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                            "isbn":%s,
                            "title": "Title",
                            "author": "Author",
                            "price": 9.90,
                            "publisher": "Polarsophia"
                        }
                                """.formatted(bookIsbn));

        // 모의 서버가 처리하는 큐에 응답을 추가한다
        mockWebServer.enqueue(mockResponse);

        // 카테고리 서비스에 isbn를 전달하여 책 도서 정보를 얻는다
        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(book) // BookClient가 반환하는 객체로 StepVerifier 객체를 초기화 한다
                .expectNextMatches(b -> b.isbn().equals(bookIsbn)) // 반환된 도서의 isbn이 요청한 isbn과 같은지 확인한다
                .verifyComplete(); // 리텍티브 스트림이 성공적으로 완료됬는지 확인한다
    }

}
