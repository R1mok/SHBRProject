package ru.r1mok.shbrproject.service;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.r1mok.shbrproject.controllers.entities.Error;
import ru.r1mok.shbrproject.controllers.entities.SystemItemImport;
import ru.r1mok.shbrproject.controllers.entities.SystemItemImportRequest;
import ru.r1mok.shbrproject.exceptions.ServiceException;
import ru.r1mok.shbrproject.repository.SystemItemRepository;
import ru.r1mok.shbrproject.repository.entity.SystemItem;
import ru.r1mok.shbrproject.repository.entity.SystemItemType;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SystemItemServiceImpl implements SystemItemService {

    private final SystemItemRepository systemItemRepository;

    @Autowired
    public SystemItemServiceImpl(SystemItemRepository systemItemRepository) {
        this.systemItemRepository = systemItemRepository;
    }

    private void setSizeAndDate(SystemItem item, int size, Instant updatedDate) {
        SystemItem parent = item.getParent();
        int previousSize = item.getSize();
        int delta = size - previousSize;
        while (parent != null) {
            parent.setDateTime(updatedDate);
            parent.setSize(parent.getSize() + delta);
            parent = parent.getParent();
        }
    }
    @Override
    public Error importItems(SystemItemImportRequest systemItemImportRequest) {
        try {
            Set<String> IDsInRequest = new HashSet<>();
            for (SystemItemImport item : systemItemImportRequest.getItems()) {
                if (item.getId() == null) {
                    throw new ServiceException();
                }
                if (IDsInRequest.contains(item.getId()))
                    throw new ServiceException();
                else
                    IDsInRequest.add(item.getId());
                Instant dateTime = systemItemImportRequest.getUpdateDate();


                if (!DateTimeFormatter.ISO_INSTANT.format(dateTime).equals(dateTime.toString())) {
                    throw new ServiceException();
                }
            }
            for (SystemItemImport item : systemItemImportRequest.getItems()) {
                Optional<SystemItem> elementExistInRepo = systemItemRepository.findById(item.getId());
                SystemItem elementInRepo;
                if (elementExistInRepo.isPresent()) {
                    elementInRepo = elementExistInRepo.get();
                    if ((elementInRepo.getType().equals(SystemItemType.FOLDER) && item.getType().equals(SystemItemType.FILE)) ||
                            (elementInRepo.getType().equals(SystemItemType.FILE) && item.getType().equals(SystemItemType.FOLDER))) {
                        throw new ServiceException();
                    }
                    elementInRepo.setUrl(item.getUrl());
                    if (elementInRepo.getParent() == null) {
                        throw new ServiceException();
                    }
                    if (!elementInRepo.getParent().getId().equals(item.getParentId())) {
                        elementInRepo.getParent().getChildren().remove(elementInRepo);
                        setSizeAndDate(elementInRepo, 0, systemItemImportRequest.getUpdateDate());
                        Optional<SystemItem> newParent = systemItemRepository.findById(item.getParentId());
                        if (newParent.isPresent()) {
                            newParent.get().getChildren().add(elementInRepo);
                            elementInRepo.setParent(newParent.get());
                        } else throw new ServiceException();
                        elementInRepo.setSize(0);
                        setSizeAndDate(elementInRepo, item.getSize(), systemItemImportRequest.getUpdateDate());
                        elementInRepo.setSize(item.getSize());
                    } else {
                        if (elementInRepo.getType() != SystemItemType.FOLDER) {
                            setSizeAndDate(elementInRepo, item.getSize(), systemItemImportRequest.getUpdateDate());
                            elementInRepo.setSize(item.getSize());
                        }
                    }
                }  else {
                    elementInRepo = SystemItem.builder()
                            .url(item.getUrl())
                            .type(item.getType())
                            .id(item.getId())
                            .build();
                    if (item.getParentId() != null) {
                        Optional<SystemItem> parent = systemItemRepository.findById(item.getParentId());
                        if (parent.isPresent()) {
                            elementInRepo.setParent(parent.get());
                            if (parent.get().getChildren() == null) {
                                List<SystemItem> children = new ArrayList<>();
                                children.add(elementInRepo);
                                parent.get().setChildren(children);
                            } else {
                                List<SystemItem> children = parent.get().getChildren();
                                children.add(elementInRepo);
                            }
                        }
                        else throw new ServiceException();
                    }
                    setSizeAndDate(elementInRepo, item.getSize(), systemItemImportRequest.getUpdateDate());
                    elementInRepo.setSize(item.getSize());
                }
                String parentId = item.getParentId();
                if (parentId != null) {
                    Optional<SystemItem> parent = systemItemRepository.findById(parentId);
                    if (parent.isEmpty() || parent.get().getType().equals(SystemItemType.FILE)) {
                        throw new ServiceException();
                    }
                }
                if (item.getType().equals(SystemItemType.FOLDER)) {
                    if (item.getUrl() != null)
                        throw new ServiceException();
                    if (item.getSize() > 0)
                        throw new ServiceException();
                } else {
                    if (item.getUrl() == null || item.getUrl().length() > 255) {
                        throw new ServiceException();
                    }
                    if (item.getSize() <= 0)
                        throw new ServiceException();
                }
                Instant dateTime = systemItemImportRequest.getUpdateDate();
                elementInRepo.setDateTime(dateTime);
                systemItemRepository.save(elementInRepo);
            }
        } catch (ServiceException e) {
            return Error.builder()
                    .message("Validation failed")
                    .code(400)
                    .build();
        }
        return Error.builder()
                .code(200)
                .message("Insertion or update was successful")
                .build();
    }

    private void deleteFolder(SystemItem item) {
        if (item.getType().equals(SystemItemType.FOLDER)) {
            if (item.getChildren() != null) {
                int childrenSize = item.getChildren().size();
                for (int i = 0; i < childrenSize; ++i) {
                    deleteFolder(item.getChildren().get(0));
                }
                if (item.getParent() != null) {
                    item.getParent().getChildren().remove(item);
                }
                systemItemRepository.delete(item);
            }
        } else {
            item.getParent().getChildren().remove(item);
            item.setParent(null);
            systemItemRepository.delete(item);
        }
    }
    @Override
    public Error deleteItem(String id, Instant date) {
        SystemItem item;
        try {
            item = this.getItem(id);
        } catch (ServiceException e) {
            throw e;
        }
        setSizeAndDate(item, 0, date);  
        if (item.getType().equals(SystemItemType.FILE)) {
            SystemItem parent = item.getParent();
            parent.getChildren().remove(item);
            systemItemRepository.delete(item);
        } else {
            deleteFolder(item);
        }
        return Error.builder()
                .code(200)
                .message("Removal was successful")
                .build();
    }

    @Override
    public SystemItem getItem(String id) {
        try {
            Optional<SystemItem> systemItem =  systemItemRepository.findById(id);
            if (systemItem.isPresent()) {
                return systemItem.get();
            } else {
                throw new ServiceException("Item not found", 404);
            }
        } catch (ServiceException e) {
            throw e;
        }
    }
}
