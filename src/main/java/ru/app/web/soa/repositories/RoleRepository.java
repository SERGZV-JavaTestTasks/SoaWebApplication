package ru.app.web.soa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.app.web.soa.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {}
