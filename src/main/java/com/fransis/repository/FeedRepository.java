package com.fransis.repository;

import com.fransis.model.FbFeed;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francisco on 9/19/16.
 */
public interface FeedRepository extends JpaRepository<FbFeed, String> {

}
