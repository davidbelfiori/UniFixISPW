package org.ing.ispw.unifix.model.state;

import org.ing.ispw.unifix.exception.InvalidStateTransitionException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

public class StatoAperta implements StateSegnalazione{
    @Override
    public void inLavorazione(Segnalazione segnalazione) {
        segnalazione.setStato(new StatoInLavorazione());
    }

    @Override
    public void chiudi(Segnalazione segnalazione) {
        throw new InvalidStateTransitionException("Impossibile chiudere una segnalazione aperta. Deve essere prima messa in lavorazione.");
    }

    @Override
    public StatoSegnalazione getStatoEnum() {
        return StatoSegnalazione.APERTA;
    }
}
