package ru.tr1al.util.filter;

import ru.tr1al.util.TextUtil;
import ru.tr1al.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Filter {

    protected int offset;
    protected int limit;
    protected String dateFrom;
    protected String dateTo;
    protected Timestamp dateFromTimestamp;
    protected Timestamp dateToTimestamp;

    public Filter(HttpServletRequest request) {
        this(request, 7);
    }

    public Filter(HttpServletRequest request, int daysFrom) {
        this.offset = TextUtil.getIntegerValueOrDef(request.getParameter("offset"), 0);
        if (this.offset < 0) {
            this.offset = 0;
        }
        Integer limit = TextUtil.getIntegerOrNull(request.getParameter("limit"));
        if (limit == null) {
            limit = 25;
        }
        this.limit = limit;
        String _dateFrom = TextUtil.emptyToNull(request.getParameter("dateFrom"));
        if (_dateFrom != null) {
            try {
                this.dateFromTimestamp = new Timestamp(new SimpleDateFormat("dd-MM-yyyy").parse(_dateFrom).getTime());
            } catch (ParseException e) {
                this.dateFromTimestamp = TimeUtil.timeFromNow(-daysFrom * TimeUtil.DAY);
            }
        } else {
            this.dateFromTimestamp = TimeUtil.timeFromNow(-daysFrom * TimeUtil.DAY);
        }
        this.dateFrom = new SimpleDateFormat("dd-MM-yyyy").format(this.dateFromTimestamp);
        String _dateTo = TextUtil.emptyToNull(request.getParameter("dateTo"));
        if (_dateTo != null) {
            try {
                this.dateToTimestamp = new Timestamp(new SimpleDateFormat("dd-MM-yyyy").parse(_dateTo).getTime());
            } catch (ParseException e) {
                this.dateToTimestamp = TimeUtil.now();
            }
        } else {
            this.dateToTimestamp = TimeUtil.now();
        }
        this.dateTo = new SimpleDateFormat("dd-MM-yyyy").format(this.dateToTimestamp);
    }

    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        for (Field f : Filter.class.getDeclaredFields()) {
            if (f.getName().equals("limit") || f.getName().equals("offset")) {
                continue;
            }
            if (f.getName().equals("dateFromTimestamp") || f.getName().equals("dateToTimestamp")) {
                continue;
            }
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
        return sb.toString();
    }

    public boolean isEmpty() {
        for (Field f : Filter.class.getDeclaredFields()) {
            if (f.getName().equals("limit") || f.getName().equals("offset")) {
                continue;
            }
            if (f.getName().equals("dateFromTimestamp") || f.getName().equals("dateToTimestamp")) {
                continue;
            }
            try {
                if (f.get(this) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public Timestamp getDateFromTimestamp() {
        return dateFromTimestamp;
    }

    public Timestamp getDateToTimestamp() {
        return dateToTimestamp;
    }
}
