package com.ray.common.event;

public final class PushEvent {
    public final String userId;
    public final boolean isBind;

    public PushEvent(String userId, boolean isBind) {
        this.userId = userId;
        this.isBind = isBind;
    }
}
