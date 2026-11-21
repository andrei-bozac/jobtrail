package ro.jobtrail.dto;

import ro.jobtrail.entity.StatusAplicare;

import java.util.List;
import java.util.Map;

public class TablouKanban {
    private final Map<StatusAplicare, List<CardKanban>> coloane;
    private final int totalAplicari;
    private final int aplicariCuInterviuProgramat;
    private final int followUpNecesar;

    public TablouKanban(Map<StatusAplicare, List<CardKanban>> coloane, int totalAplicari, int aplicariCuInterviuProgramat, int followUpNecesar){
        this.coloane = coloane;
        this.totalAplicari = totalAplicari;
        this.aplicariCuInterviuProgramat = aplicariCuInterviuProgramat;
        this.followUpNecesar = followUpNecesar;
    }

    public Map<StatusAplicare, List<CardKanban>> getColoane() { return coloane; }
    public int getTotalAplicari() { return totalAplicari; }
    public int getAplicariCuInterviuProgramat() { return aplicariCuInterviuProgramat; }
    public int getFollowUpNecesar() { return followUpNecesar; }
    public int getOferte() { return coloane.get(StatusAplicare.OFERTA).size(); }

}
