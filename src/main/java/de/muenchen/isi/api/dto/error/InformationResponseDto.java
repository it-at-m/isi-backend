package de.muenchen.isi.api.dto.error;

import de.muenchen.isi.api.dto.enums.InformationResponseType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationResponseDto {

    private InformationResponseType type;

    private LocalDateTime timestamp;

    private String traceId;

    private String spanId;

    private Integer httpStatus;

    private String originalException;

    private List<String> messages;
}
