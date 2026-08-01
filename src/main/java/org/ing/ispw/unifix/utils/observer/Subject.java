package org.ing.ispw.unifix.utils.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void notifyObservers(Object eventdata){

        /*
        * la jvm osserva chi è realmente l'osservatore al primo giro è Docente quindi l'update che si esegue
        * è quello nella classe Docente , menttre il secondo giro è l'update nella classe tecnico
        * */
        for (Observer observer : observers) {
            observer.update(eventdata);
        }
    }
}
