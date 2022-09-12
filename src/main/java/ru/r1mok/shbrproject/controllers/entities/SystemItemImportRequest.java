package ru.r1mok.shbrproject.controllers.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Description;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updateDate;
}
