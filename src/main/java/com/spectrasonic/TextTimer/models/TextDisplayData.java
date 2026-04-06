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

    // Constructor manual para evitar dependencia de Lombok en tiempo de compilación
    public TextDisplayData(String id, String world, double x, double y, double z,
                           float yaw, float pitch, String billboard,
                           float width, float height, float viewRange) {
        this.id = id;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.billboard = billboard;
        this.width = width;
        this.height = height;
        this.viewRange = viewRange;
    }

    // Getters explícitos (Lombok puede no generar en tiempo de análisis)
    public String getId() { return id; }
    public String getWorld() { return world; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public String getBillboard() { return billboard; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getViewRange() { return viewRange; }
}
