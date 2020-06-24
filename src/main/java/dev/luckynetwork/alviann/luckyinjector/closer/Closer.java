package dev.luckynetwork.alviann.luckyinjector.closer;

import lombok.Getter;

import java.io.Flushable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class Closer implements AutoCloseable {

    private final List<AutoCloseable> closeableList;

    /**
     * constructs the closer instance
     */
    public Closer() {
        this.closeableList = new ArrayList<>();
    }

    /**
     * adds a closable instance
     *
     * @param closeable the closeable instance
     */
    public <T extends AutoCloseable> T add(T closeable) {
        if (closeable == null)
            return null;

        closeableList.add(closeable);
        return closeable;
    }

    /**
     * closes all closable instances
     */
    @Override
    public void close() {
        Iterator<AutoCloseable> iterator = closeableList.iterator();

        while (iterator.hasNext()) {
            AutoCloseable next = iterator.next();

            if (next != null) {
                if (next instanceof Flushable) {
                    try {
                        ((Flushable) next).flush();
                    } catch (Exception ignored) {
                    }
                }

                try {
                    next.close();
                } catch (Exception ignored) {
                }
            }

            iterator.remove();
        }

        closeableList.clear();
    }

}