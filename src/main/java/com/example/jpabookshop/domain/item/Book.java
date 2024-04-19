package com.example.jpabookshop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "BOOK_ID")
public class Book extends Item {

    private String author;
    private String isbn;

}
