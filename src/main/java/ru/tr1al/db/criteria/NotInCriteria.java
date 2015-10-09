package ru.tr1al.db.criteria;


import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.util.StringHelper;

import java.util.Collection;

public class NotInCriteria implements Criterion {

    private final String propertyName;
    private final Collection values;

    public NotInCriteria(String propertyName, Collection values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String params;
        if (!values.isEmpty()) {
            params = StringHelper.repeat("?, ", values.size() - 1);
            params += "?";
        } else {
            params = "";
        }
        String condition = " not in (" + params + ')';
        return StringHelper.join(
                " and ",
                StringHelper.suffix(
                        criteriaQuery.getColumns(propertyName, criteria),
                        condition
                )
        );
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        TypedValue[] tvs = new TypedValue[values.size()];
        int i = 0;
        for (Object value : values) {
            tvs[i++] = criteriaQuery.getTypedValue(criteria, propertyName, value);
        }
        return tvs;
    }

    @Override
    public String toString() {
        return propertyName + " not in (" + StringHelper.toString(values.toArray()) + ')';
    }

    public static NotInCriteria notIn(String propertyName, Collection values) {
        return new NotInCriteria(propertyName, values);
    }
}
