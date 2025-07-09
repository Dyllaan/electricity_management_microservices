package com.louisfiges.citizen.daos;

import com.louisfiges.common.dtos.provider.ProviderDTO;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.*;

@Entity
@Table(name = "provider")
public class ProviderDAO implements DAO {


    @Id
    private String providerId;

    @Column(nullable = false)
    private String providerName;


    public ProviderDAO() {
    }

    public ProviderDAO(String providerId, String providerName) {
        this.providerId = providerId;
        this.providerName = providerName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public ProviderDTO toDTO() {
        return new ProviderDTO(getProviderName(), getProviderId());
    }


}
