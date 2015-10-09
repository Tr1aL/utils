package ru.tr1al.db.order;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class OrderRandom extends Order {

    private String propertyName;

    public OrderRandom(String propertyName) {
        super(propertyName, false);
        this.propertyName = propertyName;
    }

    public String toString() {
        return propertyName;
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return propertyName;
    }

    public static Order random() {
        return new OrderRandom("random()");
    }
}
