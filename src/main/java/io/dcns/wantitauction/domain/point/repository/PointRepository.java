package io.dcns.wantitauction.domain.point.repository;

import io.dcns.wantitauction.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

}
