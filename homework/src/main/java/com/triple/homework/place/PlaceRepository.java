package com.triple.homework.place;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    boolean existsByPlaceId(String placeId);

    boolean existsByPlaceIdAndDelYn(String placeId, Character delYn);
}
