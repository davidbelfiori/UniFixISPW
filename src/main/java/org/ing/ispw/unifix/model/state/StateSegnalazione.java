package org.ing.ispw.unifix.model.state;

import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

public interface StateSegnalazione {
    public void inLavorazione(Segnalazione segnalazione);
    public void  chiudi(Segnalazione segnalazione);
    StatoSegnalazione getStatoEnum();
}
