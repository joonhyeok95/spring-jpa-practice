package study.data_jpa.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class) // 보통 baseEntity 에 있으니 참고
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String>{
    @Id //@GeneratedValue
    private String id;
    
    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return createdDate == null;
    }
}
