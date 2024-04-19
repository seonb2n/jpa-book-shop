package com.example.jpabookshop.domain.item;

import com.example.jpabookshop.common.exception.NotEnoughStockException;
import com.example.jpabookshop.domain.Category;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name  = "DTYPE")
@Getter
@Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /**
     * 상품 재고를 증가시킨다.
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 상품 재고를 감소시킨다.
     * @param quantity
     * @throws NotEnoughStockException
     */
    public void removeStock(int quantity) {
        if (this.stockQuantity - quantity < 0) {
            throw new NotEnoughStockException("need More Stock");
        }
        this.stockQuantity -= quantity;
    }
}
