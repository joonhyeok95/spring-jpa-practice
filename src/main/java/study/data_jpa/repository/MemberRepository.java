package study.data_jpa.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // dto 로 리턴
    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 타입
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 반환타입 파트
    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberListByUsername(String username); // 단건

    Optional<Member> findOptionalListByUsername(String username); // 단건에 옵션

    // 페이징 : 카운트쿼리 O
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable page);

    // 페이징 : 카운트쿼리 X
    Slice<Member> findSliceByAge(int age, Pageable page);

    // 벌크성 수정 쿼리
    @Modifying(clearAutomatically = true) // 이게 있어야 executeUpdate 를 호출한다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    
    // EntityGraph 강의파트
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();
    
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();
    
    @EntityGraph(attributePaths = ("team"))
    List<Member> findEntityGrapthByUsername(@Param("username") String username);
    
    // JPA Hint & Lock 강의파트
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
