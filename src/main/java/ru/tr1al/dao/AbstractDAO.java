package ru.tr1al.dao;


import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tr1al.model.Model;
import ru.tr1al.util.TextUtil;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
public abstract class AbstractDAO<T extends Model> implements DAO {

    protected static Logger logger = LoggerFactory.getLogger(AbstractDAO.class);

    @Autowired
    public SessionFactory sessionFactory;

    public List<T> getAll() {
        return getAll(null);
    }

    public List<T> getAll(String sort) {
        logger.debug(sort != null ? "Sorting all {}" : "Retrieving all {}", getEntityClass());
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM " + getEntityClass().getName()
                + (sort != null ? " order by " + sort : ""));
        return (List<T>) query.list();
    }

    public T get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(getEntityClass(), id);
    }

    public List<T> getListByIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        Session session = sessionFactory.getCurrentSession();
        return (List<T>) session.createQuery("from " + getEntityClass().getName() + " as tbl where tbl.id in :ids")
                .setParameterList("ids", ids)
                .list();
    }

    @Transactional
    public Integer create(T obj) {
        logger.debug("Adding new {}", getEntityClass());
        Session session = sessionFactory.getCurrentSession();
        Serializable id = session.save(obj);
        if (id instanceof Integer) {
            return (Integer) id;
        } else {
            return null;
        }
    }

    @Transactional
    public void delete(int id) {
        logger.debug("Deleting existing {} with id {}", getEntityClass(), id);
        Session session = sessionFactory.getCurrentSession();
        T obj = (T) session.get(getEntityClass(), id);
        session.delete(obj);
    }

    @Transactional
    public void delete(T obj) {
        logger.debug("Deleting existing {} with id {}", getEntityClass(), obj.getId());
        Session session = sessionFactory.getCurrentSession();
        session.delete(obj);
    }

    @Transactional
    public void deleteList(List<Integer> ids) {
        if (!ids.isEmpty()) {
            logger.debug("Deleting existing {} with id {}", getEntityClass(), ids);
            Session session = sessionFactory.getCurrentSession();
            session.createQuery("delete from " + getEntityClass().getName() + " as tbl where tbl.id in :ids")
                    .setParameterList("ids", ids)
                    .executeUpdate();
        }
    }

    @Transactional
    public void update(T obj) {
        logger.debug("Editing existing {}", getEntityClass());
        Session session = sessionFactory.getCurrentSession();
        session.update(obj);
    }

    public List<T> getListCriteria(DetachedCriteria detachedCriteria, boolean cacheable, Integer offset, Integer limit) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return getListCriteria(criteria, cacheable, offset, limit);
    }

    public List<T> getListCriteria(Criteria criteria, boolean cacheable, Integer offset, Integer limit) {
        criteria.setCacheable(cacheable);
        if (limit != null) {
            criteria.setMaxResults(limit);
        }
        if (offset != null) {
            criteria.setFirstResult(offset);
        }
        return (List<T>) criteria.list();
    }

    public T uniqueResultCriteria(DetachedCriteria detachedCriteria, boolean cacheable) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return uniqueResultCriteria(criteria, cacheable);
    }

    public T uniqueResultCriteria(Criteria criteria, boolean cacheable) {
        criteria.setCacheable(cacheable);
        return (T) criteria.uniqueResult();
    }

    public Integer getCountCriteria(DetachedCriteria detachedCriteria, boolean cacheable) {
        detachedCriteria.setProjection(Projections.rowCount());
        return getDoubleCriteria(detachedCriteria, cacheable).intValue();
    }

    public Integer getCountCriteria(Criteria criteria, boolean cacheable) {
        criteria.setProjection(Projections.rowCount());
        return getDoubleCriteria(criteria, cacheable).intValue();
    }

    public Double getSumCriteria(DetachedCriteria detachedCriteria, String field, boolean cacheable) {
        detachedCriteria.setProjection(Projections.sum(field));
        return getDoubleCriteria(detachedCriteria, cacheable);
    }

    public Double getSumCriteria(Criteria criteria, String field, boolean cacheable) {
        criteria.setProjection(Projections.sum(field));
        return getDoubleCriteria(criteria, cacheable);
    }

    public Integer getCountDistinctCriteria(DetachedCriteria detachedCriteria, String field, boolean cacheable) {
        detachedCriteria.setProjection(Projections.countDistinct(field));
        return getDoubleCriteria(detachedCriteria, cacheable).intValue();
    }

    public Integer getCountDistinctCriteria(Criteria criteria, String field, boolean cacheable) {
        criteria.setProjection(Projections.countDistinct(field));
        return getDoubleCriteria(criteria, cacheable).intValue();
    }

    public Double getDoubleCriteria(DetachedCriteria detachedCriteria, boolean cacheable) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return getDoubleCriteria(criteria, cacheable);
    }

    public Double getDoubleCriteria(Criteria criteria, boolean cacheable) {
        criteria.setCacheable(cacheable);
        Object o = criteria.uniqueResult();
        return TextUtil.getDoubleValueOrDef(o, 0.);
    }

    public List<T> getList(String query, boolean cacheable, Integer offset, Integer limit, Object... param) {
        Session session = sessionFactory.getCurrentSession();
        Query q = createQuery(session, query);
        applyParameter(q, param);
        return getList(q, cacheable, offset, limit);
    }

    public List<T> getList(Query query, boolean cacheable, Integer offset, Integer limit) {
        query.setCacheable(cacheable);
        if (offset != null) {
            query.setFirstResult(offset);
        }
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return (List<T>) query.list();
    }

    private void applyParameter(Query q, Object... param) {
        int count = 0;
        if (param == null) {
            return;
        }
        for (Object p : param) {
            if (p instanceof List) {
                for (Object ol : (List) p) {
                    q.setParameter(String.valueOf(++count), ol);
                }
            } else if (p instanceof Object[]) {
                for (Object ol : (Object[]) p) {
                    q.setParameter(String.valueOf(++count), ol);
                }
            } else {
                q.setParameter(String.valueOf(++count), p);
            }
        }
    }

    @Transactional
    public int executeUpdateOrDeleteQuery(String query, Object... param) {
        Session session = sessionFactory.getCurrentSession();
        Query q = createQuery(session, query);
        applyParameter(q, param);
        return q.executeUpdate();
    }

    // это для фикса хибернейтовской багофичи
    // https://hibernate.onjira.com/browse/HHH-7023
    // Fixes JPA parameter binding by replacing '?' with '?1' (or whatever the index)
    private Query createQuery(Session sess, String query) {
        StringBuilder ret = new StringBuilder();
        int count = 1;
        for (char c : query.toCharArray()) {
            ret.append(c);
            if (c == '?') {
                ret.append(count++);
            }
        }
        return sess.createQuery(ret.toString());
    }

    public String generateSQL(DetachedCriteria dc) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = dc.getExecutableCriteria(session);
        return generateSQL(criteria);
    }

    public String generateSQL(Criteria criteria) {
        CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
        SessionImplementor session = criteriaImpl.getSession();
        SessionFactoryImplementor factory = session.getFactory();

        CriteriaQueryTranslator translator =
                new CriteriaQueryTranslator(
                        factory,
                        criteriaImpl,
                        criteriaImpl.getEntityOrClassName(),
                        CriteriaQueryTranslator.ROOT_SQL_ALIAS);

        String[] implementors = factory.getImplementors(criteriaImpl.getEntityOrClassName());

        CriteriaJoinWalker walker = new CriteriaJoinWalker(
                (OuterJoinLoadable) factory.getEntityPersister(implementors[0]),
                translator,
                factory,
                criteriaImpl,
                criteriaImpl.getEntityOrClassName(),
                session.getLoadQueryInfluencers());

        return walker.getSQLString();
    }

    public T uniqueResult(String query, Object... param) {
        Session session = sessionFactory.getCurrentSession();
        Query q = createQuery(session, query);
        applyParameter(q, param);
        return (T) q.uniqueResult();
    }

    public List<T> getSQLList(String query, boolean cacheable, Integer offset, Integer limit, Object... param) {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createSQLQuery(query);
        applyParameter(q, param);
        q.setCacheable(cacheable);
        if (offset != null) {
            q.setFirstResult(offset);
        }
        if (limit != null) {
            q.setMaxResults(limit);
        }
        return q.list();
    }

    @Transactional
    public int executeSQLQuery(String query, Object... param) {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createSQLQuery(query);
        applyParameter(q, param);
        return q.executeUpdate();
    }

    public T getFirstListObject(String query, boolean cacheable, Object... param) {
        List<T> l = getList(query, cacheable, null, null, param);
        if (l.isEmpty()) {
            return null;
        }
        return l.get(0);
    }
}
