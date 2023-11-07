package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Role role) {
        em.persist(role);
        System.out.println("Созданa - " + role);
    }

    @Override
    public void edit(Role role) {
        em.merge(role);
        System.out.println("Изменен - " + role);
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id));
        System.out.println("Удален юзер");
    }

    @Override
    public Role findById(long id) {
        return em.find(Role.class, id);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        return em.createQuery("select r FROM Role r", Role.class).getResultList();
    }
}
