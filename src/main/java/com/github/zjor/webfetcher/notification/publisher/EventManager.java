package com.github.zjor.webfetcher.notification.publisher;

import com.github.zjor.webfetcher.dto.Request;
import com.github.zjor.webfetcher.enumeration.EventType;
import com.github.zjor.webfetcher.notification.listeners.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    Map<EventType, List<EventListener>> listeners = new HashMap<>();

    public EventManager(EventType... operations) {
        for (EventType operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(EventType eventType, EventListener listener) {
        var users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(EventType eventType, EventListener listener) {
        var users = listeners.get(eventType);
        users.remove(listener);
    }

    public void notify(EventType eventType, Request request) {
        var users = listeners.get(eventType);
        users.forEach(listener -> listener.update(request));
    }
}