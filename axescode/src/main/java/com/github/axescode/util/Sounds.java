package com.github.axescode.util;

import net.kyori.adventure.sound.Sound;

public class Sounds {
    public static Sound uiOpen = Sound.sound()
        .type(org.bukkit.Sound.UI_TOAST_IN)
        .build();

    public static Sound uiClose = Sound.sound()
        .type(org.bukkit.Sound.UI_TOAST_OUT)
        .build();

    public static Sound challengeCompleted = Sound.sound()
        .type(org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE)
        .build();

    public static Sound ironGolemDamaged = Sound.sound()
        .type(org.bukkit.Sound.ENTITY_IRON_GOLEM_DAMAGE)
        .pitch(0F)
        .seed(2L)
        .build();

    public static Sound click = Sound.sound()
        .type(org.bukkit.Sound.UI_BUTTON_CLICK)
        .build();

    public static Sound questClear = Sound.sound()
        .type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_BELL)
        .pitch(1F)
        .seed(64L)
        .build();

     public static Sound levelUp = Sound.sound()
        .type(org.bukkit.Sound.ENTITY_PLAYER_LEVELUP)
        .pitch(0F)
        .seed(1L)
        .build();
}
