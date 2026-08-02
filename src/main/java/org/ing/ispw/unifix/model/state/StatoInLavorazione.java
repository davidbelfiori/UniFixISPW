package org.ing.ispw.unifix.model.state;

import org.ing.ispw.unifix.exception.InvalidStateTransitionException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

public class StatoInLavorazione implements StateSegnalazione{
    @Override
    public void inLavorazione(Segnalazione segnalazione) {
        throw new InvalidStateTransitionException("Impossibile mettere in lavorazione una segnalazione già in lavorazione.");
    }

    @Override
    public void chiudi(Segnalazione segnalazione) {
        segnalazione.setStato(new StatoChiusa());
    }

    @Override
    public StatoSegnalazione getStatoEnum() {
        return StatoSegnalazione.IN_LAVORAZIONE;
    }
}
