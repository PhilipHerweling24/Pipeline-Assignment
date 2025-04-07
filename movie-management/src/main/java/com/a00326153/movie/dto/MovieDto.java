package com.a00326153.movie.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class MovieDto extends RepresentationModel<MovieDto> {

    private Long id;
    private String title;
    private String director;
    private int releaseDate;


}
