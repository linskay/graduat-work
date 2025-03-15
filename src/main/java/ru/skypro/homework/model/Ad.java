package ru.skypro.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private Integer price;
    private String description;
    private String image;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}