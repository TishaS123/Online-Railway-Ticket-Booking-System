package com.railway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.railway.entity.Fare;

import feign.Param;

public interface FareRepository extends JpaRepository<Fare, Integer>{


//	@Query("SELECT f.amount FROM Fare f WHERE f.source =: source AND f.destination =: destination AND f.classType =: classType")
	Optional<Fare> findBySourceAndDestinationAndClassType(@Param("source") String source, @Param("destination") String destination, @Param("classType") String classType);

}
