package com.crusty.blog.domain.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTagsRequest {

    @NotEmpty(message = "Atleast one tag name is rqrd")
    @Size(max = 2,message = "max {max} tags alowed")
    private Set<
            @Size(min = 2, max = 50, message = "Tag name must be between {min} and {max} characters")
            @Pattern(regexp = "^[\\w\\s-]+$", message = "Tag name can only contain letters, numbers, spaces")
            String> names;
}
