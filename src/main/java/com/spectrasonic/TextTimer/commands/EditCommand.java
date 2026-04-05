package com.spectrasonic.TextTimer.commands;

import com.spectrasonic.TextTimer.Main;
import com.spectrasonic.Utils.MessageUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Comando /tt edit <id> <param> <value> - Edita parámetros de un display
public class EditCommand {

    public static void register(Main plugin) {
        new CommandAPICommand("tt")
                .withSubcommand(new CommandAPICommand("edit")
                        .withArguments(createIdArgument(plugin))
                        .withArguments(new StringArgument("param")
                                .replaceSuggestions(ArgumentSuggestions.strings(
                                        "move", "billboard", "size", "rotation", "render")))
                        .withArguments(new dev.jorel.commandapi.arguments.GreedyStringArgument("value"))
                        .withPermission(CommandPermission.OP)
                        .executesPlayer((Player player, dev.jorel.commandapi.CommandArguments args) -> {
                            String id = (String) args.get("id");
                            String param = (String) args.get("param");
                            String value = (String) args.get("value");
                            handleEdit(plugin, player, id, param, value);
                        }))
                .register(plugin);
    }

    private static void handleEdit(Main plugin, Player player, String id, String param, String value) {
        if (!plugin.getDisplayManager().hasDisplay(id)) {
            sendNotFound(plugin, player, id);
            return;
        }

        switch (param.toLowerCase()) {
            case "move" -> handleMove(plugin, player, id, value);
            case "billboard" -> handleBillboard(plugin, player, id, value);
            case "size" -> handleSize(plugin, player, id, value);
            case "rotation" -> handleRotation(plugin, player, id, value);
            case "render" -> handleRender(plugin, player, id, value);
            default -> {
                String msg = plugin.getMessagesManager().getMessage("commands.edit.invalid-param");
                MessageUtils.denyMessage(player, msg.replace("{prefix}", "").replace("{param}", param));
            }
        }
    }

    // --- Handlers por parámetro ---

    private static void handleMove(Main plugin, Player player, String id, String value) {
        if (value == null || value.isEmpty()) {
            sendUsage(plugin, player, "commands.edit.move.usage");
            return;
        }
        Map<String, Double> offsets = parseAxisValues(value, "xyz");
        if (offsets.isEmpty()) {
            sendInvalidValue(plugin, player, "move", value);
            return;
        }
        double dx = offsets.getOrDefault("x", 0.0);
        double dy = offsets.getOrDefault("y", 0.0);
        double dz = offsets.getOrDefault("z", 0.0);
        plugin.getDisplayManager().moveDisplayRelative(id, dx, dy, dz);
        sendSuccess(plugin, player, id, "move");
    }

    private static void handleBillboard(Main plugin, Player player, String id, String value) {
        if (value == null || value.isEmpty()) {
            sendUsage(plugin, player, "commands.edit.billboard.usage");
            return;
        }
        try {
            org.bukkit.entity.Display.Billboard billboard = org.bukkit.entity.Display.Billboard
                    .valueOf(value.toUpperCase());
            plugin.getDisplayManager().setBillboard(id, billboard);
            sendSuccess(plugin, player, id, "billboard");
        } catch (IllegalArgumentException e) {
            sendInvalidValue(plugin, player, "billboard", value);
        }
    }

    private static void handleSize(Main plugin, Player player, String id, String value) {
        if (value == null || value.isEmpty()) {
            sendUsage(plugin, player, "commands.edit.size.usage");
            return;
        }
        Map<String, Double> dims = parseAxisValues(value, "xy");
        if (dims.isEmpty()) {
            sendInvalidValue(plugin, player, "size", value);
            return;
        }
        float width = dims.getOrDefault("x", 0.0f).floatValue();
        float height = dims.getOrDefault("y", 0.0f).floatValue();
        plugin.getDisplayManager().setSize(id, width, height);
        sendSuccess(plugin, player, id, "size");
    }

    private static void handleRotation(Main plugin, Player player, String id, String value) {
        if (value == null || value.isEmpty()) {
            sendUsage(plugin, player, "commands.edit.rotation.usage");
            return;
        }
        Map<String, Double> angles = parseAxisValues(value, "wp");
        if (angles.isEmpty()) {
            sendInvalidValue(plugin, player, "rotation", value);
            return;
        }
        // w = yaw, p = pitch
        float yaw = angles.getOrDefault("w", 0.0f).floatValue();
        float pitch = angles.getOrDefault("p", 0.0f).floatValue();
        plugin.getDisplayManager().setRotation(id, yaw, pitch);
        sendSuccess(plugin, player, id, "rotation");
    }

    private static void handleRender(Main plugin, Player player, String id, String value) {
        if (value == null || value.isEmpty()) {
            sendUsage(plugin, player, "commands.edit.render.usage");
            return;
        }
        try {
            float range = Float.parseFloat(value);
            plugin.getDisplayManager().setViewRange(id, range);
            sendSuccess(plugin, player, id, "render");
        } catch (NumberFormatException e) {
            sendInvalidValue(plugin, player, "render", value);
        }
    }

    // --- Utilidades de parsing y mensajes ---

    // Parsea valores tipo "3x-6y-3z" o "6w-3.5p" según los ejes dados
    private static Map<String, Double> parseAxisValues(String input, String validAxes) {
        Map<String, Double> result = new HashMap<>();
        Pattern pattern = Pattern.compile("(-?\\d+\\.?\\d*)([" + validAxes + "])");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            double val = Double.parseDouble(matcher.group(1));
            String axis = matcher.group(2);
            result.put(axis, val);
        }
        return result;
    }

    // Argumento con sugerencias de IDs existentes
    private static StringArgument createIdArgument(Main plugin) {
        return (StringArgument) new StringArgument("id")
                .replaceSuggestions(dev.jorel.commandapi.arguments.ArgumentSuggestions.strings(
                        info -> plugin.getDisplayManager().getDisplayIds().toArray(new String[0])));
    }

    // --- Mensajes helper ---
    private static void sendSuccess(Main plugin, CommandSender sender, String id, String param) {
        String msg = plugin.getMessagesManager().getMessage("commands.edit.success");
        MessageUtils.successMessage(sender, msg.replace("{prefix}", "")
                .replace("{id}", id).replace("{param}", param));
    }

    private static void sendNotFound(Main plugin, CommandSender sender, String id) {
        String msg = plugin.getMessagesManager().getMessage("commands.edit.not-found");
        MessageUtils.denyMessage(sender, msg.replace("{prefix}", "").replace("{id}", id));
    }

    private static void sendInvalidValue(Main plugin, CommandSender sender, String param, String value) {
        String msg = plugin.getMessagesManager().getMessage("commands.edit.invalid-value");
        MessageUtils.denyMessage(sender, msg.replace("{prefix}", "")
                .replace("{param}", param).replace("{value}", value));
    }

    private static void sendUsage(Main plugin, CommandSender sender, String key) {
        String msg = plugin.getMessagesManager().getMessage(key);
        MessageUtils.alertMessage(sender, msg.replace("{prefix}", ""));
    }
}
