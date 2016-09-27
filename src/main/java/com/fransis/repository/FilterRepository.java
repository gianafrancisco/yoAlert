package com.fransis.repository;

import com.fransis.model.FbFilter;
import com.fransis.model.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francisco on 9/19/16.
 */
public interface FilterRepository extends JpaRepository<FbFilter, Long> {
    List<FbFilter> findByWatcher(Watcher watcher);
}
