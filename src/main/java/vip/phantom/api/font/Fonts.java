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

    public static final FontRenderer VERDANA15 = getFont("Verdana", 15);
    public static final FontRenderer VERDANA18 = getFont("Verdana", 18);
    public static final FontRenderer VERDANA20 = getFont("Verdana", 20);
    public static final FontRenderer VERDANA25 = getFont("Verdana", 25);
    public static final FontRenderer VERDANA45 = getFont("Verdana", 45);

    private static FontRenderer getFont(String name, float size) {
        try {
//            return new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getINSTANCE().getClass().getResourceAsStream("/fonts/" + name + ".ttf"))).deriveFont(size));
            return new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getINSTANCE().getClass().getResourceAsStream("/fonts/" + name + ".ttf"))).deriveFont(size * 2));
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
        
    