package ru.tr1al.util.progressbar;

public abstract class ProgressBarProcess extends Thread {

    private boolean finish = false;
    private String progressBarId;

    public ProgressBarProcess(String progressBarId) {
        this.progressBarId = progressBarId;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getProgressBarId() {
        return progressBarId;
    }

    public void setProgressBar(String progressBarId) {
        this.progressBarId = progressBarId;
    }
}
