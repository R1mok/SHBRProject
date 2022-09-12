package ru.r1mok.shbrproject.service;

import ru.r1mok.shbrproject.controllers.entities.Error;
import ru.r1mok.shbrproject.controllers.entities.SystemItemImportRequest;
import ru.r1mok.shbrproject.repository.entity.SystemItem;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface SystemItemService {
    Error importItems(SystemItemImportRequest systemItemImportRequest);

    Error deleteItem(String id, Instant date);

    SystemItem getItem(String id);

}
