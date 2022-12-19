package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateDbStore;

import java.util.Collection;

@ThreadSafe
@Service
public class CandidateService {

    private final CandidateDbStore candidateDbStore;
    private final CityService cityService;

    public CandidateService(CandidateDbStore candidateDbStore, CityService cityService) {
        this.candidateDbStore = candidateDbStore;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
        Collection<Candidate> result = candidateDbStore.findAll();
        result.forEach(candidate -> candidate.setCity(cityService.findById(candidate.getCity().getId())));
        return result;
    }

    public void add(Candidate candidate) {
        candidateDbStore.add(candidate);
    }

    public Candidate findById(int id) {
        return candidateDbStore.findById(id);
    }

    public void update(Candidate candidate) {
        candidateDbStore.update(candidate);
    }

}
