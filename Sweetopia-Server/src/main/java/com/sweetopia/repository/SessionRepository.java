package com.sweetopia.repository;

import com.sweetopia.entity.Sessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Sessions,String> {

}
