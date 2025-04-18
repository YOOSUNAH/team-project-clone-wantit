package io.dcns.wantitauction.domain.pointLog.repository;

import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {
}
