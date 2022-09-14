package ru.r1mok.shbrproject.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import net.bytebuddy.asm.Advice;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.r1mok.shbrproject.controllers.entities.Error;
import ru.r1mok.shbrproject.controllers.entities.SystemItemDTO;
import ru.r1mok.shbrproject.controllers.entities.SystemItemImportRequest;
import ru.r1mok.shbrproject.exceptions.ServiceException;
import ru.r1mok.shbrproject.repository.SystemItemRepository;
import ru.r1mok.shbrproject.repository.entity.SystemItem;
import ru.r1mok.shbrproject.repository.entity.SystemItemType;
import ru.r1mok.shbrproject.service.SystemItemMapper;
import ru.r1mok.shbrproject.service.SystemItemService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("")
public class RESTControllers {

    private final SystemItemService systemItemService;
    private final SystemItemMapper systemItemMapper;

    @Autowired
    public RESTControllers(SystemItemService systemItemService, SystemItemMapper systemItemMapper) {
        this.systemItemService = systemItemService;
        this.systemItemMapper = systemItemMapper;
    }

    @PostMapping("/imports")
    public ResponseEntity<?> imports(@RequestBody SystemItemImportRequest systemItemImportRequest) {
        Error error = systemItemService.importItems(systemItemImportRequest);
        if (error.getCode() == 200)
            return ResponseEntity.ok(200);
        else
            return ResponseEntity.badRequest().body(error);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestParam
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant date) {
        Error error;
        try {
            error = systemItemService.deleteItem(id, date);
            if (error.getCode() == 200)
                return ResponseEntity.ok(200);
        } catch (ServiceException e) {
            if (e.getCode() == 404)
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body(400);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<?> getItem(@PathVariable String id) {
        SystemItemDTO itemDTO;
        try {
            itemDTO = systemItemMapper.entityToDTO(systemItemService.getItem(id));
        } catch (ServiceException e) {
            Error error = Error.builder()
                    .message("Item not found")
                    .code(404).build();
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemDTO);
    }

    // additionally
    /*@GetMapping("updates")
    public ResponseEntity<HttpStatus> update() {
        return ResponseEntity.ok(HttpStatus.OK);
    }*/


}
