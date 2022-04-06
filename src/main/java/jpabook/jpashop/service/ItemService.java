package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        // 다음과 같이 setter를 나열하기 보다는 change() 의미 있는 메서드를 만들어서 하자.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
//        itemRepository.save(findItem); <-- 호출할 필요가 없다. - '변경감지기능'사용
        /**
         *
         * itemRepository.findOne(itemId)로 가져온 엔티티는 영속상태이다.
         * 영속 상태인 엔티티는 값을 셋팅하면, 값 변경을 감지하고 update 쿼리를 날려서
         * DB를 변경한다.
         *
         * merge(변경)과의 차이점은 '변경감지기능'을 사용하면, 원하는 속성만 선택해서 변경할 수 있지만,
         * 병합을 사용하면 모든 속성이 변경된다.
         * 병합시 값이 없으면 null로 update.
         *
         * 모든 값을 바꿔야하는 특별한 상황이 아니라면,
         * 병합 보다는 '변경감지기능'을 사용하자. - 가급적 병합은 쓰지 말자.
         *
         **/
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
