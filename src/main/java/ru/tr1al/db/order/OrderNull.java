package ru.tr1al.db.order;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.internal.util.StringHelper;

public class OrderNull extends Order {

    private String propertyName;
    private boolean asc;
    private boolean first;

    public OrderNull(String propertyName, boolean asc, boolean first) {
        super(propertyName, asc);
        this.propertyName = propertyName;
        this.asc = asc;
        this.first = first;
    }

    public String toString() {
        return getSql(propertyName);
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        return StringHelper.join(
                " ",
                StringHelper.suffix(
                        criteriaQuery.getColumns(propertyName, criteria),
                        getSql("")
                )
        );
    }

    private String getSql(String init) {
        StringBuilder sb = new StringBuilder(init);
        if (asc) {
            sb.append(" ASC");
        } else {
            sb.append(" DESC");
        }
        sb.append(" NULLS");
        if (first) {
            sb.append(" FIRST");
        } else {
            sb.append(" LAST");
        }
        return sb.toString();
    }
}
