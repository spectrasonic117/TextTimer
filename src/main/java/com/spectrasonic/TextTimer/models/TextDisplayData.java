package com.spectrasonic.TextTimer.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Modelo de datos para persistir la configuración de un TextDisplay
@Getter
@Setter
@Builder
public class TextDisplayData {
    private String id;
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String billboard;
    @Builder.Default
    private float width = 2.0f;
    @Builder.Default
    private float height = 0.5f;
    @Builder.Default
    private float viewRange = 16.0f;
}
