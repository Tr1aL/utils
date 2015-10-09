package ru.tr1al.db.order;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.internal.util.StringHelper;

public class NativeSQLOrder extends Order {
    private static final long serialVersionUID = 1L;
    private final static String PROPERTY_NAME = "uselessAnyways";
    private Boolean ascending;
    private String sql;

    public NativeSQLOrder(String sql, Boolean ascending) {
        super(PROPERTY_NAME, ascending == null ? false : ascending);
        this.sql = sql;
        this.ascending = ascending;

    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
            throws HibernateException {
        StringBuilder fragment = new StringBuilder();
        if (ascending != null) {
            fragment.append("(");
            fragment.append(sql);
            fragment.append(")");
            fragment.append(ascending ? " asc" : " desc");
        } else {
            fragment.append(sql);
        }
        return StringHelper.replace(fragment.toString(), "{alias}",
                criteriaQuery.getSQLAlias(criteria));
    }

}