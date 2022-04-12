/*
 * Copyright (c) 2021, for DirtyMod by Dirt, Deleteboys, Phantom.
 * All rights reserved.
 *
 * Copyright (c) for Minecraft by Mojang.
 * (This license is not in contact with Mojangs)
 */

package vip.phantom.api.font;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Fonts {
    private static Fonts INSTANCE;

    public static final FontRenderer Light10 = getFont("Mono-Light", 10);
    public static final FontRenderer Light12 = getFont("Mono-Light", 12);

    private static FontRenderer getFont(String name, float size) {
        try {
//            return new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getINSTANCE().getClass().getResourceAsStream("/fonts/" + name + ".ttf"))).deriveFont(size));
            return new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getINSTANCE().getClass().getResourceAsStream("/fonts/" + name + ".ttf"))).deriveFont(size * 1.5f));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Fonts getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Fonts();
        }
        return INSTANCE;
    }
}