package com.crusty.blog.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    private int status;
    private String msg;
    private List<Fielderr> errors;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Fielderr{
        private String field;
        private String message;
    }
}
