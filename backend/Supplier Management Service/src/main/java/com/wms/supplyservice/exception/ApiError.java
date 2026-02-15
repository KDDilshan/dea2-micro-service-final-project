package com.wms.supplyservice.exception;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    private OffsetDateTime timestamp;
    private String path;
    private String errorCode;
    private String message;
    private List<String> details;
}
