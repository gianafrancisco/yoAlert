package com.fransis.repository;

import com.fransis.model.FbGroup;
import com.fransis.model.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francisco on 23/09/2016.
 */
public interface GroupRepository extends JpaRepository<FbGroup, Long> {
    List<FbGroup> findByWatcher(Watcher watcher);
}
