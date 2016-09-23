package com.fransis.repository;

import com.fransis.model.FbUsername;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francisco on 23/09/2016.
 */
public interface UsernameRepository extends JpaRepository<FbUsername, String> {
}
