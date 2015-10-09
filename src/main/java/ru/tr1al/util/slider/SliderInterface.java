package ru.tr1al.util.slider;


public interface SliderInterface {

    /**
     * Метод тужится и рисует слайдер
     *
     * @return код сладера
     */
    abstract public String draw();

    /**
     * Здесь пишем код ссылки на страницы слайдера, нужные переменные в методе есть
     *
     * @param offset начальное значение номера элемента для страницы
     * @param page   номер страница
     * @param name   название ссылки, для ссылок виде <-Назад или Далее->
     * @return код
     */
    abstract public StringBuilder printLink(int offset, int page, String name);

    /**
     * Здесь пишем код оформления текущей страницы в слайдере
     *
     * @param currentPage номер текущей страницы
     * @return код
     */
    abstract public StringBuilder printCurrentPage(int currentPage);

    /**
     * Возвращает true если слайдер надо показывать
     *
     * @return true/false
     */
    abstract public boolean needDrawSlider();
}
