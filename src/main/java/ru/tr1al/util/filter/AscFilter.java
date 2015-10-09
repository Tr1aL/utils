package ru.tr1al.util.filter;

import ru.tr1al.util.TextUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

public abstract class AscFilter extends Filter {

    protected String orderBy;
    protected boolean orderByAsc;

    public AscFilter(HttpServletRequest request) {
        super(request);
        this.orderBy = TextUtil.emptyToNull(request.getParameter("orderBy"));
        this.orderByAsc = request.getParameter("orderByAsc") == null || request.getParameter("orderByAsc").equals("true");
    }

    public String getOrderByArrow(String order) {
        if (orderBy != null && orderBy.equals(order)) {
            if (orderByAsc) {
                return "<span class=\"glyphicon glyphicon-arrow-down\"></span>";
            } else {
                return "<span class=\"glyphicon glyphicon-arrow-up\"></span>";
            }
        }
        return "";
    }

    public String getQueryString(String order) {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getQueryString());
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.getName().equals("offset")) {
                continue;
            }
            if (f.getName().equals("orderBy") || f.getName().equals("orderByAsc")) {
                continue;
            }
            f.setAccessible(true);
            try {
                if (f.get(this) != null) {
                    if (sb.length() != 0) {
                        sb.append("&");
                    }
                    sb.append(f.getName()).append("=").append(f.get(this));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (order != null) {
            sb.append("&orderBy=").append(order);
            if (orderBy == null) {
                sb.append("&orderByAsc=true");
            } else {
                sb.append("&orderByAsc=").append(!orderByAsc);
            }
        }
        return sb.toString();
    }


    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isOrderByAsc() {
        return orderByAsc;
    }

    public void setOrderByAsc(boolean orderByAsc) {
        this.orderByAsc = orderByAsc;
    }
}
