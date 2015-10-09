package ru.tr1al.util.progressbar;

import ru.tr1al.util.MD5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProgressBarManager {

    private static ProgressBarManager ourInstance = new ProgressBarManager();

    public static ProgressBarManager getInstance() {
        return ourInstance;
    }

    private ProgressBarManager() {
    }

    final private static Map<String, ProgressBar> DB = new ConcurrentHashMap<>();

    public ProgressBar createProgressBar(Integer max) {
        ProgressBar bar = new ProgressBar(max);
        DB.put(bar.getId(), bar);
        return bar;
    }

    public void setProgressBarPosition(String id, int position, String message) {
        ProgressBar bar = DB.get(id);
        if (bar != null) {
            bar.setPosition(position, message);
        }
    }

    public ProgressBar getProgressBar(String id) {
        return DB.get(id);
    }

    public void stepProgressBar(String id, String message) {
        ProgressBar bar = DB.get(id);
        if (bar != null) {
            bar.step(message);
        }
    }

    public void setProgressBarMax(String id, int max) {
        ProgressBar bar = DB.get(id);
        if (bar != null) {
            bar.setMax(max);
        }
    }

    public void addProgressBarChild(String id, ProgressBar child, String name) {
        ProgressBar bar = DB.get(id);
        if (bar != null) {
            bar.addChild(child, name);
        }
    }

    public void finishProgressBar(String id, String message) {
        ProgressBar bar = DB.get(id);
        if (bar != null) {
            bar.finish(message);
        }
    }

    final private static Map<String, ProgressBarProcess> processPool = new HashMap<>();

    public ProgressBarProcess getProcessBarProcess(String id) {
//        System.out.println("get " + id);
        synchronized (processPool) {
            return processPool.get(id);
        }
    }

    public String startProgressBarProcess(ProgressBarProcess process) {
        String id = MD5Util.getRandomGUID();
        synchronized (processPool) {
//            System.out.println("start " + id);
            processPool.put(id, process);
        }
        process.start();
        return id;
    }
}
