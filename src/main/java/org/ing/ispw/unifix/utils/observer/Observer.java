package org.ing.ispw.unifix.utils.observer;

public interface Observer {
    void update();
    default  void update(Object eventData) {
        update();
    }
}
