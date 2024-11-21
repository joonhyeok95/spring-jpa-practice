package study.data_jpa.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.data_jpa.entity.Member;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    // JPA는 변경감지 기능을 통해 저장/수정이 persist로 끝난다.
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        // JPQL
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // null 일수도있을때
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em
                .createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username).setParameter("age", age).getResultList();
    }

    // 페이징
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age).setFirstResult(offset).setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age).getSingleResult();
    }

    // 벌크
    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age).executeUpdate();

    }
}
