package com.example.jpabookshop.util;

import static com.example.jpabookshop.util.OrderSpec.memberNameLike;
import static com.example.jpabookshop.util.OrderSpec.orderStatusEq;

import com.example.jpabookshop.common.OrderStatus;
import com.example.jpabookshop.domain.Order;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

    public Specification<Order> toSpecification() {
        return Specification.where(memberNameLike(memberName)).and(orderStatusEq(orderStatus));
    }

}
