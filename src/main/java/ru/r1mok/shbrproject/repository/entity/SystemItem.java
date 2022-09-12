package ru.r1mok.shbrproject.repository.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_item")
public class SystemItem {
    @Id
    @Column(nullable = false)
    private String id;

    @Column
    private String url;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    @ManyToOne(targetEntity = SystemItem.class)
    @JoinColumn
    private SystemItem parent;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private SystemItemType type;

    @Column
    private int size;

    @OneToMany
    @Column
    private List<SystemItem> children;
}
