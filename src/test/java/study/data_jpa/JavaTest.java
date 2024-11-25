package study.data_jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class JavaTest {
    
    @Setter
    @Getter
    @ToString
    class MyDto {
        private Long id;
        private String username;
        private String teamName;
        public MyDto(Long id, String username, String teamName) {
            this.id = id;
            this.username = username;
            this.teamName = teamName;
        }
    }
    @Test
    public void test() {
        System.out.println("java test..");
        List<MyDto> data = new ArrayList<MyDto>();
        data.add(new MyDto(1L, "joon", "metanet"));
        data.add(new MyDto(2L, "hyeok", "digital"));
        data.add(new MyDto(3L, "lim", "global"));
        data.add(new MyDto(4L, "kim", "metanet"));
        data.add(new MyDto(5L, "lee", "global"));
        // 람다식 단순 반복엔 forEach 를 쓰자
        data.forEach(System.out::println);

        System.out.println("=== metanet 팀만 별도로 추출 ===");
        // 람다식 Stream API의 기능 Filter, map 등 필요한 것은 이것을 사용.
        // 병렬처리도 가능함 parallelStream().forEach
        List<MyDto> d1 = data.stream()
                .filter(s -> s.getTeamName().equals("metanet"))
                .collect(Collectors.toList());

        d1.forEach(System.out::println);
        
        // 스트림은 원본 객체를 건드리지 않는다.
        d1.stream()
            .map(d-> d.getUsername().toUpperCase())
            .forEach(System.out::println);

        d1.forEach(System.out::println);
    }
}
