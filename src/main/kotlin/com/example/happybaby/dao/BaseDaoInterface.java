package com.example.happybaby.dao;

import com.example.happybaby.exception.MyException;
import org.hibernate.Session;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BaseDaoInterface<T> {
    List<T> findAll();

    T findById(Long id);

    List<T> findByIds(Long[] ids);

    void insert(T element) throws MyException;

    void insert(List<T> elements) throws MyException;

    // update
    void update(T element);

    // update
    void update(List<T> elements);

    List<T> nativeSqlQuery(String sql, String[] args);

    HibernateTemplate getTemplate();

    @Transactional
    Session session();

    void nativeSql(String sql, String[] args);

    void delete(T ele);

    @Modifying
    @Transactional(readOnly = false)
    boolean delete();

    Class getTable();
}
