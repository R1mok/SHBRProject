package ru.r1mok.shbrproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.r1mok.shbrproject.repository.entity.SystemItem;

@Repository
public interface SystemItemRepository extends JpaRepository<SystemItem, String> {
}
