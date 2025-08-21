package com.bash.Unitrack.Repositories;

import com.bash.Unitrack.Data.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
