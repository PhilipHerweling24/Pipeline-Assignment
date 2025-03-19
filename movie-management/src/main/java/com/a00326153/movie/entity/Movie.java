package com.a00326153.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@NotBlank(message = "Name cannot be left blank")
    //@Size(min = 3, max = 50, message = "Name has to be between 3 and 50 characters long")
    private String title;

    //@NotBlank(message = "Name cannot be left blank")
    //@Size(min = 3, max = 50, message = "Name has to be between 3 and 50 characters long")
    private String director;

    //@NotBlank(message = "release date can not be blank")
    private int releaseDate;

}
