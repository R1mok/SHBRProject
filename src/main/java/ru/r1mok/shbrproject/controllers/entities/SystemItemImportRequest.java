package ru.r1mok.shbrproject.controllers.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.context.annotation.Description;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@Description("Список импортируемых элементов и время импорта")
public class SystemItemImportRequest {
    @Schema(description = "Импортируемые элементы")
    @Column(nullable = false)
    private List<SystemItemImport> items;

    @Schema(defaultValue = "Время обновления добавляемых элементов")
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant updateDate;
}
