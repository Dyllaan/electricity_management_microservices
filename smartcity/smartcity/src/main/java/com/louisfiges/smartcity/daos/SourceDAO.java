package com.louisfiges.smartcity.daos;

import com.louisfiges.common.dtos.source.SourceDTO;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "source")
public class SourceDAO{

    private @Id
    @Column(name = "source_id")
    String sourceId;

    public SourceDAO() {
    }

    public SourceDAO(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
    }
}
