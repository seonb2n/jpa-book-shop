package com.example.jpabookshop.util;

import com.example.jpabookshop.common.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

}
