package ru.tr1al.util.slider;

public abstract class Slider implements SliderInterface {

    private int total;  //сколько всего элементов для вывода
    private int offset; //с какого элемента начинаем вывод
    private int limit;  //сколько элементов на странице

    private String queryString = "";
    private String lastString = "&larr; Предыдущая ";
    private String nextString = " Следующая &rarr;";
    private String separatorString = "&nbsp;&nbsp;";
    private String ellipsisString = "...";
    private String prefix = "";
    private String postfix = "";
    private int pageLeftLimit = 6;
    private int pageRightLimit = 6;

    protected Slider(int total, int offset, int limit) {
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public String getQueryString() {
        return queryString;
    }

    public Slider setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    public Slider setLastString(String lastString) {
        this.lastString = lastString;
        return this;
    }

    public Slider setNextString(String nextString) {
        this.nextString = nextString;
        return this;
    }

    public Slider setSeparatorString(String separatorString) {
        this.separatorString = separatorString;
        return this;
    }

    public Slider setPageLeftLimit(int pageLeftLimit) {
        this.pageLeftLimit = pageLeftLimit;
        return this;
    }

    public Slider setPageRightLimit(int pageRightLimit) {
        this.pageRightLimit = pageRightLimit;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public void setEllipsisString(String ellipsisString) {
        this.ellipsisString = ellipsisString;
    }

    public boolean needDrawSlider() {
        return offset > 0 || offset + limit < total;
    }

    public int getTotal() {
        return total;
    }

    public String draw() {
        int allPagesCount = total / limit;
        if (allPagesCount * limit < total) {
            allPagesCount++;
        }
        int currentPage = offset / limit + 1;
        int startPage = currentPage - pageLeftLimit;
        if (startPage < 1) {
            startPage = 1;
        }
        int endPage = currentPage + pageRightLimit;
        if (endPage > allPagesCount) {
            endPage = allPagesCount;
        }
        StringBuilder sb = new StringBuilder(prefix);
        if (currentPage > 1) {
            sb.append(printLink((currentPage - 2) * limit, currentPage - 2, lastString));
        }
        if (startPage > 1) {
            sb.append(printLink(0, 1, null)).append(separatorString).append(ellipsisString).append(separatorString);
        }
        for (int i = startPage; i <= endPage; i++) {
            if (i > startPage) {
                sb.append(separatorString);
            }
            if (i == currentPage) {
                sb.append(printCurrentPage(i));
            } else {
                sb.append(printLink((i - 1) * limit, i, null));
            }
        }
        if (endPage < allPagesCount) {
            sb.append(separatorString).append(ellipsisString).append(separatorString).append(printLink((allPagesCount - 1) * limit, allPagesCount, null));
        }
        if (currentPage < allPagesCount) {
            sb.append(printLink((currentPage) * limit, currentPage, nextString));
        }
        sb.append(postfix);
        return sb.toString();
    }

    private String draw = null;

    @Override
    public String toString() {
        if (draw == null) {
            draw = this.draw();
        }
        return draw;
    }
}
