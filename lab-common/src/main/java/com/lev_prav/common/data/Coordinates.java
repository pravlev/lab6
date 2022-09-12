package com.lev_prav.common.data;

import com.opencsv.bean.CsvBindByName;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    @CsvBindByName
    private Long x; //Максимальное значение поля: 177, Поле не может быть null
    @CsvBindByName
    private Float y; //Значение поля должно быть больше -635, Поле не может быть null

    public Coordinates(Long x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates() {
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates that = (Coordinates) o;
        return x.equals(that.x) && y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{"
                + "x=" + x
                + ", y=" + y
                + '}';
    }
}
