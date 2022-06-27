package com.triple.homework.mileage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;

@Slf4j
@Service
public class MileageServiceImpl implements MileageService {

    private final MileageRepository mileageRepository;

    @Autowired
    public MileageServiceImpl(MileageRepository mileageRepository) {
        this.mileageRepository = mileageRepository;
    }

    @Override
    public Long getTotalMileages(String userId) {
        Long result = mileageRepository.getTotalMileages(userId);

        if(result == null) return 0L;

        return result;
    }
}
