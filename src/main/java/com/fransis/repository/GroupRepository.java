package com.fransis.repository;

import com.fransis.model.FbGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francisco on 23/09/2016.
 */
public interface GroupRepository extends JpaRepository<FbGroup, Long> {
}
