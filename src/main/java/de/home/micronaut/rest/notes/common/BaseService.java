package de.home.micronaut.rest.notes.common;

import io.micronaut.spring.tx.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseService<ID extends Serializable, E extends BaseEntity<ID>> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<E> entityClass;

    public BaseService(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    public void persist(E entity) {
        this.entityManager.persist(entity);
    }

    @Transactional(readOnly = true)
    public Optional<E> find(ID id) {
        return Optional.ofNullable(this.entityManager.find(this.entityClass, id));
    }

    @Transactional(readOnly = true)
    public List<E> find() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(this.entityClass);
        Root<E> qr = cq.from(this.entityClass);
        return this.entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public void remove(E entity) {
        this.entityManager.remove(entity);
    }
}
