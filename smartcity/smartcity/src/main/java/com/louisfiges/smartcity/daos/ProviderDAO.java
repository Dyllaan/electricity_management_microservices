package com.louisfiges.smartcity.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "provider")
public class ProviderDAO
{

    @Id
    @Column(name="provider_id")
    private String providerId;

    @Column(name="provider_name", nullable = false)
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

}
