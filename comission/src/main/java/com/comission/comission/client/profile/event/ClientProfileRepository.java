package com.comission.comission.client.profile.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long>
{

}
