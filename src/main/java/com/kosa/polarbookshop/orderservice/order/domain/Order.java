
package com.kosa.polarbookshop.orderservice.order.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

@Table("orders")
public record Order(
        @Id Long id,

        String bookIsbn,
        String bookName,
        Double bookPrice,
        Integer quantity,
        OrderStatus status,

        @CreatedDate LocalDateTime createdDate,

        @LastModifiedDate LocalDateTime lastModifiedDate,

        @Version int version) {
    public static Order of(String bookIsbn,
            String bookName,
            Double bookPrice,
            Integer quantity,
            OrderStatus status) {
        return new Order(null, bookIsbn, bookName, bookPrice, quantity, status, null, null, 0);
    }
}
