package com.louisfiges.citizen.services;

import com.louisfiges.citizen.daos.ProviderDAO;
import com.louisfiges.citizen.repositories.ProviderRepository;
import com.louisfiges.common.http.ServiceInterface;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProviderService implements ServiceInterface<String, ProviderDAO> {

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

    @Override
    public ProviderDAO create(ProviderDAO providerDAO) {
        logger.info("Creating provider: {}", providerDAO.getProviderName());
        return providerRepository.save(providerDAO);
    }

    @Override
    public ProviderDAO read(String id) {
        return providerRepository.findById(id).orElseThrow();
    }

    @Override
    public ProviderDAO update(ProviderDAO providerDAO) {
        return providerRepository.save(providerDAO);
    }

    @Override
    public void delete(String id) {
        providerRepository.deleteById(id);
    }

    @Override
    public Optional<ProviderDAO> find(String id) {
        return providerRepository.findById(id);
    }

    @Override
    public boolean exists(String providerUri) {
        return providerRepository.existsById(providerUri);
    }

    public List<String> findAllUris() {
        return providerRepository.findAllProviderUris();
    }
}
