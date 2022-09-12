package ru.r1mok.shbrproject.repository.entity;

import org.springframework.context.annotation.Description;

@Description("Тип элемента - папка или файл")
public enum SystemItemType {
    FILE, FOLDER;
}
