package com.example.mux.competition.service;

import com.example.mux.challenge.service.ChallengeService;
import com.example.mux.competition.model.Competition;
import com.example.mux.competition.repository.CompetitionRepository;
import com.example.mux.day.repository.DayRepository;
import com.example.mux.day.service.DayService;
import com.example.mux.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final DayRepository dayRepository;
    private final ChallengeService challengeService;

    public Competition getCompetition() throws EntityNotFoundException {
        List<Competition> competitionList = competitionRepository.findAll();
        if(competitionList.size() > 0){
            return competitionList.get(0);
        }else{
            throw new EntityNotFoundException("No competition found");
        }
    }

    //TODO Start und Enddatum wird jedes mal übergeben und Competition wird jedes mal zurückgesetzt
    public Competition updateCompetition() throws EntityNotFoundException {
        Competition competition = getCompetition();
        if(competition.isStarted()){
            competition.setStarted(false);
            competition.setEndDate(LocalDate.now());
        }else{
            dayRepository.deleteAll();
            competition.setEndDate(null);
            competition.setStartDate(LocalDate.now());
            // TODO Folgenden 2 Zeilen anpassen, je nachdem ob Änderung noch gemacht wird, dass parameter start und end auf jeden Fall übergeben werden
            LocalDate start = Objects.requireNonNullElse(competition.getStartDate(), LocalDate.now());
            LocalDate end = Objects.requireNonNullElse(competition.getEndDate(), start.plusMonths(3));
            challengeService.createChallenges(start, end);
            competition.setStarted(true);
        }

        return competitionRepository.save(competition);
    }

    public Competition createInitialCompetition(){
        List<Competition> competitionList = competitionRepository.findAll();
        if(competitionList.size() > 0){
            return competitionList.get(0);
        }else{
            return competitionRepository.save(new Competition());
        }
    }

    public boolean competitionExists(){
        List<Competition> competitionList = competitionRepository.findAll();
        return competitionList.size() > 0;
    }
}
