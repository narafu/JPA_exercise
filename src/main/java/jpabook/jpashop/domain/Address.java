package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    // 값 타입은 변경이 되면 안되므로, setter를 제공하지 않는다.
    // 생성할 때만 값을 초기화해서, 변경 불가능하도록, 생성자만 만든다.

}
