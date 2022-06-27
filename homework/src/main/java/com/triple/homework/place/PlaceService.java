package com.triple.homework.place;

import java.util.*;

public interface PlaceService {
    Optional<Place> selectById(Long id);

    boolean validPlace(String placeId, Character delYn);

    void insert(Place place) throws Exception;
}
