package org.godea.models.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class FileDTO {
    public String name;
    public long size;
    public OffsetDateTime uploadDate;
    public OffsetDateTime expirationDate;
    public boolean isExpired;
    public long downloadCount;

    public FileDTO(String name, long size, OffsetDateTime uploadDate, OffsetDateTime expirationDate, boolean isExpired, long downloadCount) {
        this.name = name;
        this.size = size;
        this.uploadDate = uploadDate;
        this.expirationDate = expirationDate;
        this.isExpired = isExpired;
        this.downloadCount = downloadCount;
    }
}
