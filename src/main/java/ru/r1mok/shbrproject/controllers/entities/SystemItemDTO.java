package ru.r1mok.shbrproject.controllers.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.r1mok.shbrproject.repository.entity.SystemItemType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "DTO класс элемента")
public class SystemItemDTO implements Serializable {
    @Schema(description = "Тип элемента")
    private SystemItemType type;

    @Schema(description = "Id элемента")
    private String id;

    @Schema(description = "Размер файла или всех файлов, входящих в папку")
    private int size;

    @Schema(description = "URL файла")
    private String url;

    @Schema(description = "Id родителя")
    private String parentId;

    @Schema(description = "Последнее время изменения")
    private LocalDateTime date;

    @Schema(description = "Массив потомков")
    private List<SystemItemDTO> children;
}
