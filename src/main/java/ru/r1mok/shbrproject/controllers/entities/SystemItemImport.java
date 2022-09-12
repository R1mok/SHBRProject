package ru.r1mok.shbrproject.controllers.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Description;
import ru.r1mok.shbrproject.repository.entity.SystemItemType;

import javax.persistence.Column;

@Data
@Builder
@Description("Импорт элемента")
public class SystemItemImport {
    @Schema(description = "Уникальный идентификатор")
    @Column(nullable = false)
    private String id;

    @Schema(description = "Ссылка на файл. Для поля равно null")
    private String url;

    @Schema(description = "ID родительской папки")
    private String parentId;

    @Schema(description = "Тип")
    private SystemItemType type;

    @Schema(description = "Целое число, для папки - это суммарный размер всех элеметов")
    private int size;
}
