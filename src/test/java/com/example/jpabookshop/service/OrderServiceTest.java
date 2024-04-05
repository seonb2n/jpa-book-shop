package com.example.jpabookshop.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.jpabookshop.common.OrderStatus;
import com.example.jpabookshop.domain.Address;
import com.example.jpabookshop.domain.Member;
import com.example.jpabookshop.domain.Order;
import com.example.jpabookshop.domain.item.Book;
import com.example.jpabookshop.domain.item.Item;
import com.example.jpabookshop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("책 1", 10000, 100);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.READY, getOrder.getStatus());
    }

    @Test
    public void 주문취소() {
        //given
        Member member = createMember();
        Item item = createBook("책 1", 10000, 100);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(100, item.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강남대로", "23"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}