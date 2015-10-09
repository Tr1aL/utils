package ru.tr1al.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Model implements ModelInterface, Serializable {

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Model))
            return false;

        if (this.getId() == null)
            return false;
        if (((Model) o).getId() == null)
            return false;

        return getId().equals(((Model) o).getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public static <V extends Model> Map<Integer, V> getIdMap(List<V> list) {
        Map<Integer, V> ret = new HashMap<>();
        for (V m : list) {
            ret.put(m.getId(), m);
        }
        return ret;
    }

    public static List<Integer> getIdList(List<? extends Model> list) {
        List<Integer> ret = new ArrayList<>();
        for (Model m : list) {
            ret.add(m.getId());
        }
        return ret;
    }

    public static List<Integer> getIdList(Model[] arr) {
        List<Integer> ret = new ArrayList<>();
        for (Model m : arr) {
            ret.add(m.getId());
        }
        return ret;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
