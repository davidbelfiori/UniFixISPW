package org.ing.ispw.unifix.utils.observer;

import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

public class EmailNotificationService implements Observer{
    @Override
    public void update() {
        //Non implementata
    }

    @Override
    public void update(Object eventData) {
        if(eventData instanceof Segnalazione s){
            if(s.getStato() != StatoSegnalazione.APERTA && s.getDocente()   != null){
                inviaMailDocente(s);
            }else if(s.getStato() == StatoSegnalazione.APERTA && s.getTecnico()!= null){
                inviaMailTecnico(s);
            }
        }
    }


    private void inviaMailTecnico(Segnalazione eventData) {
            Printer.print("[SIMULAZIONE EMAIL TECNICO -> " + eventData.getTecnico().getEmail() + "]: " +
                    "Ti è stata assegnata una nuova segnalazione #" + eventData.getIdSegnalazione() +
                    " per l'aula " + eventData.getAula());
    }


    private  void inviaMailDocente(Segnalazione eventData) {
            Printer.print("[SIMULAZIONE EMAIL DOCENTE -> " + eventData.getDocente().getEmail() + "]: " +
                    "Lo stato della tua segnalazione #" + eventData.getIdSegnalazione() +
                    " è stato aggiornato a: " + eventData.getStato());
    }

}
