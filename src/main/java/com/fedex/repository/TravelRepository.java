package com.fedex.repository;

import com.fedex.domain.Travel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Travel entity.
 */
@SuppressWarnings("unused")
public interface TravelRepository extends JpaRepository<Travel,Long> {

}
