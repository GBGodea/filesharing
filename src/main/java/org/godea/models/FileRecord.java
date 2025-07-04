package org.godea.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "files")
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false, unique = true)
    private String storedName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "size")
    private long size;

    @Column(name = "upload_date", nullable = false)
    private OffsetDateTime uploadTime;

    @Column(name = "expiration_date", nullable = false)
    private OffsetDateTime expirationDate;

    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

    @Column(name = "download_count", nullable = false)
    private long downloadCount;

    public FileRecord() {
    }

    public FileRecord(String originalName, String storedName, String contentType, long size, OffsetDateTime expirationDate) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.contentType = contentType;
        this.size = size;
        this.uploadTime = uploadTime;
        this.expirationDate = expirationDate;
    }

    @PrePersist
    protected void onPrePersist() {
        this.uploadTime = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public String getOriginalName() { return originalName; }
    public String getStoredName() { return storedName; }
    public String getContentType() { return contentType; }
    public long getSize() { return size; }
    public OffsetDateTime getUploadTime() { return uploadTime; }
    public OffsetDateTime getExpirationDate() { return expirationDate; }
    public boolean getIsExpired() { return isExpired; }
    public long getDownloadCount() { return downloadCount; }
    public void setExpired(boolean exp) {
        isExpired = exp;
    }
}
