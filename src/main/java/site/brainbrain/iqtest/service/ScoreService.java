package site.brainbrain.iqtest.service;

import org.springframework.stereotype.Service;

import site.brainbrain.iqtest.controller.dto.CreateResultRequest;

@Service
public class ScoreService {
    public Integer calculate(final CreateResultRequest request) {
        return 100;
    }
}
