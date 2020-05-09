package com.example.happybaby.dao;


import com.example.happybaby.exception.BaseHttpStatus;
import com.example.happybaby.exception.MyException;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.orm.hibernate5.HibernateTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import com.example.happybaby.utils.Helper;
import org.springframework.transaction.annotation.Transactional;

//@Repository("BaseDaoImpl")
public class BaseDao<T> implements BaseDaoInterface<T> {
    protected SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(getTableName());
    private DetachedCriteria dc;
    private final String TAG = this.getTableName();
    private Session session;

    public BaseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.session = this.sessionFactory.openSession();
    }

    @Override
    public Session session() {
        return sessionFactory.openSession();
    }

    private void setDc() {
        if (this.dc == null) {
            String tableName = getTableName();//表名
            this.dc = DetachedCriteria.forClass(getTable(), tableName);//第一个参数实体类，第二个参数表名
        }
    }

    public String getTableName() {
        return Helper.HumpToUnderline(getTable().getSimpleName()).toLowerCase();//表名
    }

    // 过滤域，只查询需要的属性
    public BaseDao<T> filterField(String[] params) {
        if (this.dc == null) {
            setDc();
        }
        ProjectionList pList = Projections.projectionList();
        for (String param : params) {
            pList.add(Projections.property(getTableName() + "." + param).as(param)); // 设置需要查询的域
        }
        this.dc.setProjection(pList);
        return this;
    }

    // 设置查询where语句
    public BaseDao<T> where(String arg1, Object... params) {
        if (this.dc == null) {
            setDc();
        }
        if (arg1 == null || params == null || params.length == 0) {
            return null;
        }
        if (params.length == 1) {
            dc.add(Restrictions.eq(arg1, params[0]));
        } else if (params.length == 2) {
            switch (params[1].toString()) {
                case "=": {
                    dc.add(Restrictions.eq(arg1, params[0]));
                }
                ;
                break;
                case ">": {
                    dc.add(Restrictions.gt(arg1, params[0]));
                }
                break;
                case ">=": {
                    dc.add(Restrictions.ge(arg1, params[0]));
                }
                break;
                case "<": {
                    dc.add(Restrictions.le(arg1, params[0]));
                }
                break;
                case "<=": {
                    dc.add(Restrictions.le(arg1, params[0]));
                }
                ;
                break;
                case "!=": {
                    dc.add(Restrictions.ne(arg1, params[0]));
                }
                break;
            }
        }
        return this;
    }

    // 通过Criteria条件查询
    public List<T> findByDC() {
        List<T> reuslt = (List<T>) this.getTemplate().findByCriteria(this.dc);
        this.dc = null;
        return reuslt;
    }

    // 查询全部
    @Override
    public List<T> findAll() {
        if (this.dc != null) {
            List<T> result = (List<T>) this.getTemplate().findByCriteria(this.dc);
            this.dc = null;
            return result;
        }
        return this.getTemplate().loadAll(getTable());
    }

    //通过Id查询
    @Override
    public T findById(Long id) {
        return (T) this.getTemplate().get(getTable(), id);
    }

    // 通过id数组查询多个
    @Override
    public List<T> findByIds(Long[] ids) {
        List<T> elements = new LinkedList<>();
        Session session = this.session;
        for (int i = 0; i < ids.length; i++) {
            T element = (T) session.get(getTable(), ids[i]);
            elements.add(element);
        }

        return elements;
    }

    //插入一个
    @Override
    public void insert(T element) throws MyException {
        try {
            this.getTemplate().save(getTableName(),element);
        } catch (Exception e) {
            throw new MyException(BaseHttpStatus.SQL_ADD_FAIL, e.getCause().getCause().getMessage());
        }
    }

    //插入多个
    @Override
    public void insert(List<T> elements) throws MyException {
        Session session = this.session;
        Transaction ts = session.beginTransaction();
        session.setHibernateFlushMode(FlushMode.ALWAYS);
        try {
            for (T element : elements) {
                session.save(element);
            }
            ts.commit();
        } catch (Exception e) {
            if (ts != null)
                ts.rollback();
            throw new MyException(BaseHttpStatus.SQL_ADD_FAIL);
        }
    }

    // 更新
    @Override
    public void update(T element) {
        Session session = this.session;
        Transaction ts = session.beginTransaction();
        try {
            session.update(element);
            ts.commit();
        } catch (Exception e) {
            if (ts != null)
                ts.rollback();
            throw new MyException(BaseHttpStatus.getByCode(5002), e.getMessage());
        } finally {

        }
    }

    /**
     * 原生sql查询操作
     *
     * @param sql
     * @param args
     * @return
     */
    @Override
    /*@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)*/
    // @Transactional(rollbackFor = Exception.class)
    public List<T> nativeSqlQuery(String sql, String[] args) {
        List<T> data = null;
        Session session = this.session;
        Transaction ts = session.beginTransaction();
        try {
            NativeQuery q = session.createNativeQuery(sql);
            q.addEntity(getTable());
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    q.setParameter(i + 1, args[i]);
                }
            }
            String sqlString = q.getQueryString();
            data = q.getResultList();
            ts.commit();
            return data;
        } catch (Exception e) {
            if (ts != null)
                ts.rollback();
        } finally {

        }

        return data;
    }


    // update多个
    @Override
    public void update(List<T> elements) {
        Session session = this.session;
        Transaction ts = session.beginTransaction();
        try {
            for (T element : elements) {
                session.update(element);
            }
            ts.commit();
        } catch (Exception e) {
            if (ts != null)
                ts.rollback();
            throw new MyException(BaseHttpStatus.getByCode(5003), e.getMessage());
        } finally {

        }
    }


    @Override
    public HibernateTemplate getTemplate() {
        return new HibernateTemplate(sessionFactory);
    }


    /**
     * 原生sql操作
     *
     * @param sql
     * @param args
     */
    @Override
    public void nativeSql(String sql, String[] args) {
        Session session = this.session;
        NativeQuery q = session.createNativeQuery(sql);
        Transaction ts = session.beginTransaction();
        try {
            q.addEntity(getTable());
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    q.setParameter(i + 1, args[i]);
                }
            }
            q.executeUpdate();
            ts.commit();
        } catch (Exception e) {
            logger.error(TAG, e.getCause());
            ts.rollback();
        } finally {

        }
    }

    @Override
    @Transactional
    public void delete(T ele) {
        Transaction ts = this.session.beginTransaction();
        try {
            this.session.delete(ele);
            ts.commit();
        } catch (Exception e) {
            ts.rollback();
            throw new MyException(BaseHttpStatus.SQL_DELETE_FAIL);
        }
    }

    @Override
    //@Modifying
    public boolean delete() {
        String tableName = Helper.HumpToUnderline(getTableName()).toLowerCase();
        String sql = "delete from " + tableName;
        String sql2 = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";
        boolean reuslt = false;
        try {
            nativeSql(sql, null);
            nativeSql(sql2, null);
            reuslt = true;
        } catch (Exception e) {
            logger.error(getTable() + "delete:" + e.getMessage());
        } finally {

        }

        return reuslt;
    }

    @Override
    public Class getTable() {
        return null;
    }
}
