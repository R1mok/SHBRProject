package ru.r1mok.shbrproject.controllers.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Description;

import javax.persistence.Column;

@Data
@Builder
@Description("Класс ошибок")
public class Error {
    @Schema(description = "Код ответа")
    @Column(nullable = false)
    private int code;

    @Schema(description = "Сообщение")
    @Column(nullable = false)
    private String message;
}
