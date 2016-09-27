package com.fransis.repository;

import com.fransis.model.FbUsername;
import com.fransis.model.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francisco on 23/09/2016.
 */
public interface UsernameRepository extends JpaRepository<FbUsername, String> {
    List<FbUsername> findByWatcher(Watcher watcher);
}
