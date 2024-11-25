package study.data_jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.data_jpa.entity.Item;

@SpringBootTest
class itemrRpositoryTest {

    @Autowired
    ItemRpository itemRpository;
    
    @Test
    public void save() {
        Item item = new Item("A");
        itemRpository.save(item);
    }

}
