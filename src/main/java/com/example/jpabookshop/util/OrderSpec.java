package com.example.jpabookshop.util;

import com.example.jpabookshop.common.OrderStatus;
import com.example.jpabookshop.domain.Member;
import com.example.jpabookshop.domain.Order;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpec {

    public static Specification<Order> memberNameLike(final String memberName) {
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query,
                CriteriaBuilder criteriaBuilder) {
                if (StringUtils.isEmpty(memberName)) return null;

                Join<Order, Member> m = root.join("member", JoinType.INNER); // 회원과 조인
                return criteriaBuilder.like(m.<String>get("name"), "%" + memberName + "%");
            }
        };
    }

    public static Specification<Order> orderStatusEq(final OrderStatus orderStatus) {
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query,
                CriteriaBuilder criteriaBuilder) {
                if (orderStatus == null) return null;

                return criteriaBuilder.equal(root.get("status"), orderStatus);
            }
        };
    }
}
