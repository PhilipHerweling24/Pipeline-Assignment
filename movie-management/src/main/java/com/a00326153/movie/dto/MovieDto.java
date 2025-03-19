package com.a00326153.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MovieDto extends RepresentationModel<MovieDto> {

    private Long id;
    private String title;
    private String director;
    private int releaseDate;


}
