package study.data_jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

@SpringBootTest
@Transactional
// @Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

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

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        for (String string : result) {
            System.out.println("findUsernameList = " + string);
        }
    }

    @Test
    public void findMemberDto() {
        Team t1 = new Team("teamA");
        teamRepository.save(t1);

        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        m1.setTeam(t1);

        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto dto : result) {
            System.out.println("findMemberDto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member dto : result) {
            System.out.println("findByNames = " + dto);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findListByUsername("AAA");
        for (Member dto : result) {
            System.out.println("findListByUsername = " + dto);
        }
        Member findResult = memberRepository.findMemberListByUsername("AAA");
        System.out.println("findMemberListByUsername = " + findResult);

        // Optional<Member> optional = memberRepository.findOptionalListByUsername("AAA");
        // System.out.println("findOptionalListByUsername = " + optional);
        //
        // // 컬렉션 조회할 때 문제, 데이터가 없을 경우 empty 컬렉션으로 반환됨
        // Optional<Member> optional_null = memberRepository.findOptionalListByUsername("A");
        // System.out.println("findOptionalListByUsername_null = " + optional_null);
    }
}
