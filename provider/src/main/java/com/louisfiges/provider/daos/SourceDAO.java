package com.louisfiges.provider.daos;

import com.louisfiges.common.dtos.source.SourceDTO;
import com.louisfiges.common.http.DAO;
import jakarta.persistence.*;

@Entity
@Table(name = "source")
public class SourceDAO implements DAO {

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

    @Override
    public SourceDTO toDTO() {
        return new SourceDTO(sourceId);
    }
}
