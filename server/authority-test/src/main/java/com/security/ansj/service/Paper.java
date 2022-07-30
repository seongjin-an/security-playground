package com.security.ansj.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paper {
    private Long paperId;
    private String title;
    private String tutorId;
    private List<String> studentIds;
    private State state;

    public static enum State{
        PREPARE,
        READY,
        END
    }
}
