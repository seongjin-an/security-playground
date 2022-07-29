package com.security.ansj.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usersession {

    private String username;
    private List<SessionInfo> sessions;

    public int getCount(){
        return sessions.size();
    }

}
