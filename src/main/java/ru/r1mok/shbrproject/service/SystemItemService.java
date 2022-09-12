package ru.r1mok.shbrproject.service;

import ru.r1mok.shbrproject.controllers.entities.Error;
import ru.r1mok.shbrproject.controllers.entities.SystemItemImportRequest;
import ru.r1mok.shbrproject.repository.entity.SystemItem;

import java.time.LocalDateTime;

public interface SystemItemService {
    Error importItems(SystemItemImportRequest systemItemImportRequest);

    Error deleteItem(String id, LocalDateTime date);

    SystemItem getItem(String id);

}
