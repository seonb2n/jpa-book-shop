package com.example.jpabookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.jpabookshop.domain.Address;
import com.example.jpabookshop.domain.Member;
import com.example.jpabookshop.domain.item.Book;
import com.example.jpabookshop.domain.item.Item;
import com.example.jpabookshop.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class InheritanceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setup() throws Exception {
        // given
        createBook("책 1", 10000, 100);
        em.flush();

        /*
          inheritance joined 인 경우엔 쿼리가 다음과 같이 발생한다.
          insert into item (name,price,stock_quantity,dtype,item_id) values (?,?,?,'B',?)
          insert into book (author,isbn,book_id) values (?,?,?)
         */

        /*
          inheritance single_table 인 경우엔 쿼리가 다음과 같이 발생한다.
          insert into item (name,price,stock_quantity,author,isbn,dtype,item_id) values (?,?,?,?,?,'B',?)
         */

        /*
          inheritance table_per_class 인 경우엔 쿼리가 다음과 같이 발생한다.
          insert into book (name,price,stock_quantity,author,isbn,item_id) values (?,?,?,?,?,?)
         */
    }

    @Test
    public void 상속관계매핑() {
        //when
        em.clear();
        Item foundItem = itemRepository.findById(1L).get();

        /*
          inheritance joined 인 경우엔 쿼리가 다음과 같이 발생한다.
          select i1_0.item_id,i1_0.dtype,i1_0.name,i1_0.price,i1_0.stock_quantity,i1_1.artist,i1_1.etc,i1_2.author,i1_2.isbn,i1_3.actor,i1_3.director
          from item i1_0
          left join album i1_1
            on i1_0.item_id=i1_1.album_id
          left join book i1_2
            on i1_0.item_id=i1_2.book_id
           left join movie i1_3
            on i1_0.item_id=i1_3.movie_id
            where i1_0.item_id=?
         */

        /*
          inheritance single_table 인 경우엔 쿼리가 다음과 같이 발생한다.
          select i1_0.item_id,i1_0.dtype,i1_0.name,i1_0.price,i1_0.stock_quantity,i1_0.artist,i1_0.etc,i1_0.author,i1_0.isbn,i1_0.actor,i1_0.director
          from item i1_0
          where i1_0.item_id=?
         */

        /*
          inheritance table_per_class 인 경우엔 쿼리가 다음과 같이 발생한다.
          elect i1_0.item_id,i1_0.clazz_,i1_0.name,i1_0.price,i1_0.stock_quantity,i1_0.artist,i1_0.etc,i1_0.author,i1_0.isbn,i1_0.actor,i1_0.director
          from
            (select price, stock_quantity, item_id, artist, etc, name, null as author, null as isbn, null as actor, null as director, 1 as clazz_
              from album
              union all
             select price, stock_quantity, item_id, null as artist, null as etc, name, author, isbn, null as actor, null as director, 2 as clazz_
               from book
              union all
             select price, stock_quantity, item_id, null as artist, null as etc, name, null as author, null as isbn, actor, director, 3 as clazz_
               from movie) i1_0
           where i1_0.item_id=?
         */

        assertEquals("책 1", foundItem.getName());
    }

    @Test
    public void JPQL_영속성_컨텍스트_JPQL먼저() {
        Book book = em.createQuery("SELECT b from Book b where b.id = 1", Book.class)
            .getSingleResult();
        // 쿼리가 나간다 select b1_0.item_id,b1_0.name,b1_0.price,b1_0.stock_quantity,b1_0.author,b1_0.isbn from book b1_0 where b1_0.item_id=1

        Item foundItem = itemRepository.findById(1L).get();
        // 영속성 컨텍스트에 이미 해당 아이템이 있기 때문에 쿼리가 db 로 나가지 않는다.

        assertEquals(book.getName(), foundItem.getName());
    }

    @Test
    public void JPQL_영속성_컨텍스트_JPQL나중에() {
        em.clear();

        Item foundItem = itemRepository.findById(1L).get();
        // select i1_0.item_id,i1_0.clazz_,i1_0.name,i1_0.price,i1_0.stock_quantity,i1_0.artist,i1_0.etc,i1_0.author,i1_0.isbn,i1_0.actor,i1_0.director from (select price, stock_quantity, item_id, artist, etc, name, null as author, null as isbn, null as actor, null as director, 1 as clazz_ from album union all select price, stock_quantity, item_id, null as artist, null as etc, name, author, isbn, null as actor, null as director, 2 as clazz_ from book union all select price, stock_quantity, item_id, null as artist, null as etc, name, null as author, null as isbn, actor, director, 3 as clazz_ from movie) i1_0 where i1_0.item_id=?

        foundItem.setName("책 2");

        Book book = em.createQuery("SELECT b from Book b where b.id = 1", Book.class)
            .getSingleResult();
        // 쿼리가 나간다 select b1_0.item_id,b1_0.name,b1_0.price,b1_0.stock_quantity,b1_0.author,b1_0.isbn from book b1_0 where b1_0.item_id=1

        assertEquals(book.getName(), foundItem.getName()); // db 에서 값을 조회하나, 영속성 컨텍스트에 있는 값이 이미 존재하기에 해당 값을 반환한다.
        assertEquals(book.hashCode(), foundItem.hashCode());
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
