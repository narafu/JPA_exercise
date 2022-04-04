package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // protected 기본생성자와 같은 코드 - 기본생성자 방지
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    // @NoArgsConstructor(access = AccessLevel.PROTECTED) 때문에 주석
//    protected OrderItem() {
//    }

    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스 로직
    /*
     * 재고수량 원복
     * */
    public void cancel() {
        this.getItem().addStock(count);
    }

    // 조회 로직
    /*
     * 주문 상품 전체 가격 조회
     * */
    public int getTotalPrice() {
        return this.getOrderPrice() * this.getCount();
    }

}
