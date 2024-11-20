package study.data_jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());

        Member member = new Member("memberB");
        Member saveMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 같은 트랜잭션에서 id 가 같으니 ok
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 전체 조회 크기
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 현재 로우 갯수
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 처리
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        // 삭제 후 검증
        long delCount = memberRepository.count();
        assertThat(delCount).isEqualTo(0);


    }
}
