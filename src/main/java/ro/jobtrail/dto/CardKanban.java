package ro.jobtrail.dto;

import ro.jobtrail.entity.Aplicare;
import ro.jobtrail.entity.Companie;
import ro.jobtrail.entity.Interviu;

public class CardKanban {
    private final Aplicare aplicare;
    private final Companie companie;
    private final Interviu urmatorulInterviu;
    private final long zileDeLaAplicare;
    private final boolean necesitaFollowUp;

    public CardKanban(Aplicare aplicare, Companie companie, Interviu urmatorulInterviu, long zileDeLaAplicare, boolean necesitaFollowUp){
        this.aplicare = aplicare;
        this.companie = companie;
        this.urmatorulInterviu = urmatorulInterviu;
        this.zileDeLaAplicare = zileDeLaAplicare;
        this.necesitaFollowUp = necesitaFollowUp;
    }

    public Aplicare getAplicare() {
        return aplicare;
    }

    public boolean isNecesitaFollowUp() {
        return necesitaFollowUp;
    }

    public long getZileDeLaAplicare() {
        return zileDeLaAplicare;
    }

    public Interviu getUrmatorulInterviu() {
        return urmatorulInterviu;
    }

    public Companie getCompanie() {
        return companie;
    }
}
