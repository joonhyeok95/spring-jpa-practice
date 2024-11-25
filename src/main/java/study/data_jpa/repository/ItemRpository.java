package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.data_jpa.entity.Item;

public interface ItemRpository extends JpaRepository<Item, Long>{

}
