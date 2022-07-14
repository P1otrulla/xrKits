package net.osnixer.kits.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.stream.Collectors;

public final class ChatUtil {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public final static TextComponent RESET_ITEM = Component.text()
            .decoration(TextDecoration.ITALIC, false)
            .build();

    public static Component mini(String toColor) {
        return RESET_ITEM.append(miniMessage.deserialize(toColor));
    }

    public static List<Component> mini(List<String> toColor) {
        return toColor.stream()
                .map(ChatUtil::mini)
                .collect(Collectors.toList());
    }

    private ChatUtil() {

    }
}
