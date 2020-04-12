package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.Client;

public interface ClientRepository extends CustomRepository<Client, Integer>   {

	Client findByClientNameAndIdNot(String name, Integer id);

	Client findByClientIdAndIdNot(String clientId, Integer id);

}
