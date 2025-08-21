package com.bash.Unitrack.Repositories;

import com.bash.Unitrack.Data.models.Session;
import com.bash.Unitrack.Data.models.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    public List<Session> findByStatus(Stat status);
}
