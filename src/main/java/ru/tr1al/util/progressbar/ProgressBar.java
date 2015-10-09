package ru.tr1al.util.progressbar;

import ru.tr1al.util.MD5Util;

import java.util.*;

public class ProgressBar {

    private String id;
    private int max = Integer.MAX_VALUE;
    private int step = 1;
    private int position = 0;
    private String message;

    private Map<ProgressBar, String> childs = new LinkedHashMap<>();

    public ProgressBar(Integer max) {
        this.id = MD5Util.getRandomGUID();
        if (max != null) {
            this.max = max;
        }
    }

    public void setMax(int max) {
        if (max < 1) {
            max = 1;
        }
        this.max = max;
    }

    public void setPosition(int position, String message) {
        if (position > max) {
            position = max;
        }
        this.position = position;
        this.message = message;
    }

    public void step(String message) {
        setPosition(this.position + step, message);
    }

    public double getProcess() {
        return position / max;
    }

    public void finish(String message) {
        position = max;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public Map<ProgressBar, String> getChilds() {
        return childs;
    }

    public void addChild(ProgressBar child, String name) {
        this.childs.put(child, name);
    }

    public String getMessage() {
        return message;
    }

    public int getMax() {
        return max;
    }

    public int getPosition() {
        return position;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public static Map<Object, Object> serialize(ProgressBar bar) {
        Map<Object, Object> ret = new HashMap<>();
        ret.put("id", bar.getId());
        ret.put("max", bar.getMax());
        ret.put("position", bar.getPosition());
        ret.put("message", bar.getMessage());
        List<Map<String, Object>> childs = new ArrayList<>();
        for (final Map.Entry<ProgressBar, String> e : bar.getChilds().entrySet()) {
            childs.add(new HashMap<String, Object>() {{
                put("name", e.getValue());
                put("item", serialize(e.getKey()));
            }});
        }
        ret.put("childs", childs);
        return ret;
    }
}
