package study.data_jpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    EntityManager em;

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

        Optional<Member> optional = memberRepository.findOptionalListByUsername("AAA");
        //
        // 컬렉션 조회할 때 문제, 데이터가 없을 경우 empty 컬렉션으로 반환됨
        Optional<Member> optional_null = memberRepository.findOptionalListByUsername("A");

        System.out.println("findMemberListByUsername = " + findResult);
        System.out.println("findOptionalListByUsername = " + optional);
        System.out.println("findOptionalListByUsername_null = " + optional_null);
    }

    // 페이징 테스트
    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("AAA1", 10));
        memberRepository.save(new Member("AAA2", 10));
        memberRepository.save(new Member("AAA3", 10));
        memberRepository.save(new Member("AAA4", 10));
        memberRepository.save(new Member("AAA5", 10));
        memberRepository.save(new Member("AAA6", 10));
        memberRepository.save(new Member("AAA7", 10));
        memberRepository.save(new Member("AAA8", 10));
        memberRepository.save(new Member("AAA9", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> content = page.getContent();
        for (Member m : content) {
            System.out.println("member = " + m);
        }
        System.out.println("size=" + page.getTotalPages() + ", total=" + page.getTotalElements());

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(9);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }

    // 페이징 테스트2 : Slice
    @Test
    public void pagingSlice() {
        // given
        memberRepository.save(new Member("AAA1", 10));
        memberRepository.save(new Member("AAA2", 10));
        memberRepository.save(new Member("AAA3", 10));
        memberRepository.save(new Member("AAA4", 10));
        memberRepository.save(new Member("AAA5", 10));
        memberRepository.save(new Member("AAA6", 10));
        memberRepository.save(new Member("AAA7", 10));
        memberRepository.save(new Member("AAA8", 10));
        memberRepository.save(new Member("AAA9", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // dto 로 변환
        Slice<MemberDto> toMap =
                page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        List<Member> content = page.getContent();
        for (Member m : content) {
            System.out.println("member = " + m);
        }
        // System.out.println("size=" + page.getTotalPages() + ", total=" +
        // page.getTotalElements());

        assertThat(content.size()).isEqualTo(3);
        // assertThat(page.getTotalElements()).isEqualTo(9); // Slice는 count query가 없어서 없는 상태
        assertThat(page.getNumber()).isEqualTo(0);
        // assertThat(page.getTotalPages()).isEqualTo(3); // Slice는 count query가 없어서 없는 상태
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("AAA1", 10));
        memberRepository.save(new Member("AAA2", 19));
        memberRepository.save(new Member("AAA3", 20));
        memberRepository.save(new Member("AAA4", 25));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();
        em.clear();


        // then
        assertThat(resultCount).isEqualTo(2);
    }
    // EntityGraph
    @Test
    public void findMemberLazy() {
        
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team t1 = new Team("teamA");
        Team t2 = new Team("teamB");
        teamRepository.save(t1);
        teamRepository.save(t2);

        Member m1 = new Member("AAA1", 10, t1);
        Member m2 = new Member("AAA2", 10, t2);
        memberRepository.save(m1);
        memberRepository.save(m2);
        
        em.flush();
        em.clear();
        
        //when
//        List<Member> members = memberRepository.findAll(); // Member 만 가져옴
        List<Member> members = memberRepository.findMemberFetchJoin(); // fetch join
        //select Member -- N+1 문제
        // fetch join 을 안했을 때..
        // Member 안의 Team은 가짜객체로 가져오기 때문에 또 Query를 요청하게 되는 현상
        // 이후 프록시 객체를 
        for(Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass()); // $HibernateProxy$467O8oa2
            System.out.println("member.team = " + member.getTeam().getName());
        }
        
    }
    // Jpa Hint
    @Test
    public void queryHint() {
        // given
        Member m1 = new Member("AAA1", 10);
        memberRepository.save(m1);
        em.flush(); // query 를 날림 (DB동기화)
        em.clear(); // 영속성 캐시를 날림(1차캐시초기화)
        
        //when
//        Member findMember = memberRepository.findById(m1.getId()).get();
        Member findMember = memberRepository.findReadOnlyByUsername(m1.getUsername());
        findMember.setUsername("AAA2");
        
        em.flush(); // 변경감지 update 쿼리 수행
    }
    // JPA Lcok
    @Test
    public void queryLock() {
        // given
        Member m1 = new Member("AAA1", 10);
        memberRepository.save(m1);
        em.flush(); // query 를 날림 (DB동기화)
        em.clear(); // 영속성 캐시를 날림(1차캐시초기화)
        
        //when
        List<Member> findMember = memberRepository.findLockByUsername("AAA1");
//        findMember.setUsername("AAA2");
        
        em.flush(); // 변경감지 update 쿼리 수행
    }
}
