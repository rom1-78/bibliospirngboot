package fr.ensitech.biblio.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "authors",
        indexes = {
                @Index(name = "idx_author_name", columnList = "lastname,firstname")
        }
)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 48)
    private String firstname;

    @Column(nullable = false, length = 48)
    private String lastname;

    @Column
    private LocalDate birthdate;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books = new HashSet<>();
}
