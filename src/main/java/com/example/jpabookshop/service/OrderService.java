package com.example.jpabookshop.service;

import com.example.jpabookshop.domain.Delivery;
import com.example.jpabookshop.domain.Member;
import com.example.jpabookshop.domain.Order;
import com.example.jpabookshop.domain.OrderItem;
import com.example.jpabookshop.domain.item.Item;
import com.example.jpabookshop.repository.MemberRepository;
import com.example.jpabookshop.repository.OrderRepository;
import com.example.jpabookshop.util.OrderSearch;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemService itemService;

    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemService.findOne(itemId);

        Delivery delivery = new Delivery(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findALl(orderSearch);
    }

}
