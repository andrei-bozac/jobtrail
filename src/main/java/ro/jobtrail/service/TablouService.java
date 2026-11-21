package ro.jobtrail.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import ro.jobtrail.dto.CardKanban;
import ro.jobtrail.dto.TablouKanban;
import ro.jobtrail.entity.Aplicare;
import ro.jobtrail.entity.Companie;
import ro.jobtrail.entity.Interviu;
import ro.jobtrail.entity.StatusAplicare;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TablouService {
    private static final int ZILE_PANA_LA_FOLLOW_UP = 14;
    @EJB
    private AplicareService aplicareService;
    @EJB
    private CompanieService companieService;
    @EJB
    private InterviuService interviuService;

    public TablouKanban construiesteTablou(){
        List<Aplicare> aplicari = aplicareService.findAll();
        Map<Long,Companie> companii = companieService.findAllCaHarta();
        List<Interviu> viitoare = interviuService.findViitoare();

        Map<Long, Interviu> urmatorulInterviu = new HashMap<>();
        for(Interviu i : viitoare){
            urmatorulInterviu.putIfAbsent(i.getAplicareId(),i);
        }

        Map<StatusAplicare, List<CardKanban>> coloane = new EnumMap<>(StatusAplicare.class);
        for (StatusAplicare s : StatusAplicare.values()) {
            coloane.put(s, new ArrayList<>());
        }

        int followUp = 0;
        LocalDate azi = LocalDate.now();
        for(Aplicare a: aplicari){
            long zile = ChronoUnit.DAYS.between(a.getDataAplicarii(),azi);
            boolean necesitaFollowUp = a.getStatus() == StatusAplicare.APLICAT && zile >= ZILE_PANA_LA_FOLLOW_UP;
            if(necesitaFollowUp){
                followUp++;
            }
            CardKanban card = new CardKanban(
                    a,
                    companii.get(a.getCompanieId()),
                    urmatorulInterviu.get(a.getId()),
                    zile,
                    necesitaFollowUp);
            coloane.get(a.getStatus()).add(card);
        }
        return new TablouKanban(coloane, aplicari.size(), urmatorulInterviu.size(), followUp);
    }
}
