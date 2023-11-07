package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UserRepositoryImp implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(User user) {
        em.persist(user);
        System.out.println("Создан - " + user);

    }

    @Override
    public void edit(User updatedUser) {
        em.merge(updatedUser);
        System.out.println("Изменен - " + updatedUser);
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id));
        System.out.println("Удален юзер");
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            user = em.createQuery(
                            "SELECT u from User u WHERE u.username = :username", User.class).
                    setParameter("username", username).getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return em.createQuery("select u FROM User u", User.class).getResultList();
    }

}
