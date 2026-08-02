package org.ing.ispw.unifix.model.state;

import org.ing.ispw.unifix.exception.InvalidStateTransitionException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

public class StatoChiusa implements StateSegnalazione{
    @Override
    public void inLavorazione(Segnalazione segnalazione) {
        throw new InvalidStateTransitionException("Impossibile mettere in lavorazione una segnalazione chiusa.");
    }

    @Override
    public void chiudi(Segnalazione segnalazione) {
        throw new InvalidStateTransitionException("Impossibile chiudere una segnalazione già chiusa.");
    }

    @Override
    public StatoSegnalazione getStatoEnum() {
        return StatoSegnalazione.CHIUSA;
    }
}
