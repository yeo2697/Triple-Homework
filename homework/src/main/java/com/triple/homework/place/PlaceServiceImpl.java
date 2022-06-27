package com.triple.homework.place;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class PlaceServiceImpl implements PlaceService{

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public Optional<Place> selectById(Long id) {
        return placeRepository.findById(id);
    }

    @Override
    public boolean validPlace(String placeId, Character delYn) {
        return placeRepository.existsByPlaceIdAndDelYn(placeId, delYn);
    }

    @Override
    @Transactional
    public void insert(Place place) throws Exception {
        if(!placeRepository.existsByPlaceId(place.getPlaceId())){
            placeRepository.save(place);
        }else{
            throw new Exception("409");
        }
    }
}
