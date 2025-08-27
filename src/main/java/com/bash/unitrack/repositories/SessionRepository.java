package com.bash.unitrack.repositories;

import com.bash.unitrack.data.models.Session;
import com.bash.unitrack.data.models.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    public List<Session> findByStatus(Stat status);

    List<Session> findByStatusAndEndTimeBefore(Stat stat, Instant endTime);
}
