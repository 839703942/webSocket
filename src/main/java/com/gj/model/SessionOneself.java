package com.gj.model;

import lombok.Data;

import javax.websocket.Session;

@Data
public class SessionOneself {
    private String sessionId;
    private Session session;
}
