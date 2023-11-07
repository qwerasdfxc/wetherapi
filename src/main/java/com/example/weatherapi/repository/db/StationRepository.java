package com.example.weatherapi.repository.db;

import com.example.weatherapi.model.Station;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends ReactiveCrudRepository<Station, Long> {
}
