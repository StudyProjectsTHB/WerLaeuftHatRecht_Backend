package com.example.mux.competition.service;

import com.example.mux.competition.model.Competition;
import com.example.mux.competition.repository.CompetitionRepository;
import com.example.mux.day.repository.DayRepository;
import com.example.mux.day.service.DayService;
import com.example.mux.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final DayRepository dayRepository;

    public Competition getCompetition() throws EntityNotFoundException {
        List<Competition> competitionList = competitionRepository.findAll();
        if(competitionList.size() > 0){
            return competitionList.get(0);
        }else{
            throw new EntityNotFoundException("No competition found");
        }
    }

    public Competition updateCompetition() throws EntityNotFoundException {
        Competition competition = getCompetition();
        if(competition.isStarted()){
            competition.setStarted(false);
            competition.setEndDate(LocalDate.now());
        }else{
            dayRepository.deleteAll();
            competition.setEndDate(null);
            competition.setStartDate(LocalDate.now());
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
