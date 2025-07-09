package com.louisfiges.smartcity.services;

import com.louisfiges.common.http.ServiceInterface;
import com.louisfiges.smartcity.daos.ProviderDAO;
import com.louisfiges.smartcity.repositories.ProviderRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    /**
     * Creates the providers in the database, if they do not already exist
     * @return void
     */
    public void createProviders() {
        Map<String, String> providers = new HashMap<>();
        providers.put("prov-a", "Provider A");
        providers.put("prov-b", "Provider B");
        providers.put("prov-c", "Provider C");

        providers.forEach((uri, name) -> {
            if(providerRepository.existsById(uri)) {
                return;
            }
            ProviderDAO provider = new ProviderDAO(uri, name);
            create(provider);
        });
    }

    public ProviderDAO create(ProviderDAO providerDAO) {
        logger.info("Creating provider: {}", providerDAO.getProviderName());
        return providerRepository.save(providerDAO);
    }

    public Optional<ProviderDAO> find(String id) {
        return providerRepository.findById(id);
    }
}
