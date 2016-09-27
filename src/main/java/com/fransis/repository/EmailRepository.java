package com.fransis.repository;

import com.fransis.model.Email;
import com.fransis.model.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by francisco on 23/09/2016.
 */
public interface EmailRepository extends JpaRepository<Email, Long> {
    List<Email> findByWatcher(Watcher watcher);
}
