package apeha.allinone.gui.status;

public class Status {
    private String status = "";
    private boolean done = false;

    public synchronized String getStatus() {
        return status;
    }

    public synchronized void setStatus(String status) {
        this.status = status;
    }

    public synchronized boolean isDone() {
        return done;
    }

    public synchronized void setDone(boolean done) {
        this.done = done;
    }

    public void reset() {
        setStatus("");
        setDone(false);
    }
}
