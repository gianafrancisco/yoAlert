package com.fransis.repository;

import com.fransis.model.FbFilter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francisco on 9/19/16.
 */
public interface FilterRepository extends JpaRepository<FbFilter, Long> {

}
