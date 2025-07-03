package org.godea.models.dto;

import java.time.LocalDateTime;

public class FileDTO {
    public String name;
    public long size;
    public LocalDateTime uploadDate;
    public LocalDateTime expirationDate;
    public boolean isExpired;
    public long downloadCount;

    public FileDTO(String name, long size, LocalDateTime uploadDate, LocalDateTime expirationDate, boolean isExpired, long downloadCount) {
        this.name = name;
        this.size = size;
        this.uploadDate = uploadDate;
        this.expirationDate = expirationDate;
        this.isExpired = isExpired;
        this.downloadCount = downloadCount;
    }
}
