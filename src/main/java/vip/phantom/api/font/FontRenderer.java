/*
 * Copyright (c) 2021, for DirtyMod by Dirt, Deleteboys, Phantom.
 * All rights reserved.
 *
 * Copyright (c) for Minecraft by Mojang.
 * (This license is not in contact with Mojangs)
 */

package vip.phantom.api.font;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {

    private final Font plainFont;
    private final Font boldFont;
    private final Font italicFont;

    private final File outputFile = new File("C:\\Users\\hanyo\\Desktop\\");

    private final Graphics2D graphicsContext;
    private final FontMetrics fontMetrics;

    private boolean antiAliasing;
    private boolean obfuscate = false, strikethrough = false, underline = false, boldStyle = false, italicStyle = false;
    private final Random fontRandom = new Random();

    private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    private char colorCodePrefix = "§".charAt(0);
    private int[] colorCodes = new int[32];

//    private int noCharFoundTextureID = createTexture(new BufferedImage());


    private HashMap<Character, CharacterData> plainCharacters = new HashMap<>();
    private HashMap<Character, CharacterData> boldCharacters = new HashMap<>();
    private HashMap<Character, CharacterData> italicCharacters = new HashMap<>();

    private static final int MARGIN = 4;
    private static final String CHARS = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000";
//    private static final String CHARS = "Y";

    // https://learnopengl.com/In-Practice/Text-Rendering
    // https://open.gl/textures
    // https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image (answer: https://stackoverflow.com/a/9470843)

    public FontRenderer(Font font) {
        this(font, true);
    }

    public FontRenderer(Font font, boolean antiAliasing) {
        this.plainFont = font.deriveFont(Font.PLAIN);
        this.boldFont = font.deriveFont(Font.BOLD);
        this.italicFont = font.deriveFont(Font.ITALIC);
        this.antiAliasing = antiAliasing;
        final long time = System.currentTimeMillis();
        genColorCodes();

        final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        graphicsContext = image.createGraphics();
        graphicsContext.setFont(font);
        fontMetrics = graphicsContext.getFontMetrics();
        for (char character : CHARS.toCharArray()) {
            setupCharacter(character, true, false, false);
        }
    }

    private void setupFonts(boolean plain, boolean italic, boolean bold) {

    }

    private void setupCharacter(Character character, boolean plain, boolean bold, boolean italic) {
        final Rectangle2D characterBounds = fontMetrics.getStringBounds(Character.toString(character), graphicsContext);
        double width = characterBounds.getWidth();
        double height = characterBounds.getHeight();
        //adding the margin (only for the picture)
        width += MARGIN * 2;
        height += MARGIN;

        for (int i = 0; i < 3; i++) {
            final BufferedImage characterImage = new BufferedImage(ceiling_double_int(width), ceiling_double_int(height), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D characterGraphic = characterImage.createGraphics();
            switch (i) {
                case 0:
                    if (plain) {
                        characterGraphic.setFont(plainFont);
                    } else {
                        continue;
                    }
                    break;
                case 1:
                    if (bold) {
                        characterGraphic.setFont(boldFont);
                    } else {
                        continue;
                    }
                    break;
                case 2:
                    if (italic) {
                        characterGraphic.setFont(italicFont);
                    } else {
                        continue;
                    }
                    break;
                default:
                    return;
            }
            characterGraphic.setColor(new Color(255, 255, 255, 0));
            characterGraphic.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());
            characterGraphic.setColor(Color.white);

            if (antiAliasing) {
                characterGraphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                characterGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                characterGraphic.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            }
            characterGraphic.drawString(Character.toString(character), MARGIN, fontMetrics.getAscent());
            final HashMap<Character, CharacterData> toPut = i == 0 ? plainCharacters : i == 1 ? boldCharacters : italicCharacters;
            if (!toPut.containsKey(character)) {
                toPut.put(character, new CharacterData(characterImage.getWidth(), characterImage.getHeight(), createTextureID(characterImage)));
            }
        }
    }

    private int ceiling_double_int(double value) {
        int i = (int) value;
        return value > (double) i ? i + 1 : i;
    }

    public void drawString(String text, float x, float y, Color color) {
        renderString(text, x, y, false, 100, 100, true, color);
    }

    public void renderString(String text, float x, float y, boolean mirror, float sizeWidthPercentage, float sizeHeightPercentage, boolean useColorCodes, Color color) {
        if (text == null || text.length() == 0) {
            return;
        }
        obfuscate = false;
        boldStyle = false;
        strikethrough = false;
        underline = false;
        italicStyle = false;

        glPushMatrix();
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        glEnable(GL_TEXTURE_2D);
        final boolean lightingFlag = glIsEnabled(GL_LIGHTING);
        glDisable(GL_LIGHTING);

        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        glScaled(0.5, 0.5, 0.5);

        x -= MARGIN / 2f;

        x *= 2;
        y -= 2;
        y *= 2;


        float charX = Math.round(x);
        float charY = Math.round(y);
        char[] charArray = text.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char character = charArray[i];
            final HashMap<Character, CharacterData> styleHashMap = boldStyle ? boldCharacters : italicStyle ? italicCharacters : plainCharacters;
            if (!styleHashMap.containsKey(character)) {
                setupCharacter(character, !boldStyle && !italicStyle, boldStyle, italicStyle);
            }

            final char previousChar = i > 0 ? text.charAt(i - 1) : ' ';
            if (useColorCodes && previousChar == colorCodePrefix) {
                continue;
            }
            if (useColorCodes && character == colorCodePrefix && i + 1 < text.length()) {
                int codeIndex = "0123456789abcdefklmnor".indexOf(text.toLowerCase().charAt(i + 1));

                if (codeIndex < 16) {
                    if (codeIndex >= 0) {
                        final int textColor = colorCodes[codeIndex];
                        glColor4f((float) (textColor >> 16) / 255.0F, (float) (textColor >> 8 & 255) / 255.0F, (float) (textColor & 255) / 255.0F, 255);
                    }
                } else if (codeIndex == 16) {
                    //§k
                    obfuscate = true;
                } else if (codeIndex == 17) {
                    //§l
                    boldStyle = true;
                } else if (codeIndex == 18) {
                    //§m
                    strikethrough = true;
                } else if (codeIndex == 19) {
                    //§n
                    underline = true;
                } else if (codeIndex == 20) {
                    //§o
                    italicStyle = true;
                } else {
                    //§r
                    obfuscate = false;
                    boldStyle = false;
                    strikethrough = false;
                    underline = false;
                    italicStyle = false;
                    glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
                }
            } else {
                if (obfuscate) {
                    final int actualCharWidth = Math.round(fontMetrics.charWidth(character) * (20f / plainFont.getSize()));
                    char c1;
                    do {
                        final int randomIndex = fontRandom.nextInt(CHARS.length());
                        c1 = CHARS.charAt(randomIndex);
                        if (!plainCharacters.containsKey(c1) || !styleHashMap.containsKey(c1)) {
                            setupCharacter(c1, plainCharacters.containsKey(c1), boldStyle, italicStyle);
                        }
                    } while (actualCharWidth != Math.round(fontMetrics.charWidth(c1) * (20f / plainFont.getSize())));
                    character = c1;
                }
                CharacterData characterData = styleHashMap.get(character);
                final float cWidth = characterData.getWidth() * (sizeHeightPercentage / 100);
                final float cHeight = characterData.getHeight() * (sizeHeightPercentage / 100);
                if (underline) {
                    drawHorizontalLine(charX + MARGIN, charX + cWidth - MARGIN, charY + cHeight / 2f + cHeight / 4f, 2, color);
                }


                glBindTexture(GL_TEXTURE_2D, characterData.getTextureID());
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

                glBegin(GL_QUADS);
                final float yOffset = 0;
                glTexCoord2d(0, mirror ? 1 : 0);
                glVertex2d(charX, charY + yOffset);

                glTexCoord2d(0, mirror ? 0 : 1);
                glVertex2d(charX, charY + cHeight + yOffset);

                glTexCoord2d(1, mirror ? 0 : 1);
                glVertex2d(charX + cWidth, charY + cHeight + yOffset);

                glTexCoord2d(1, mirror ? 1 : 0);
                glVertex2d(charX + cWidth, charY + yOffset);

                glEnd();
                charX += cWidth - MARGIN * 2 * (sizeWidthPercentage / 100);
            }
        }

        glBindTexture(GL_TEXTURE_2D, 0);

        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        if (!texture2DFlag) {
            glDisable(GL_TEXTURE_2D);
        }
        if (lightingFlag) {
            glEnable(GL_LIGHTING);
        }
        glPopMatrix();
        glColor4f(1, 1, 1, 1);
    }

    private void drawHorizontalLine(float x, float endX, float y, float lineWidth, Color color) {
        float radius = lineWidth / 2f;
        drawRect(x, y - radius, endX, y + radius, color);
    }

    private void drawRect(float left, float top, float right, float bottom, Color color) {
        if (left > right) {
            final float tmp = right;
            right = left;
            left = tmp;
        }
        if (top > bottom) {
            final float tmp = top;
            top = bottom;
            bottom = tmp;
        }
        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        glBegin(GL_QUADS);
        glVertex2f(left, top);
        glVertex2f(left, bottom);
        glVertex2f(right, bottom);
        glVertex2f(right, top);
        glEnd();
        if (texture2DFlag) {
            glEnable(GL_TEXTURE_2D);
        }
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        glPopMatrix();
    }

    public float getWidth(String text) {
        return getWidth(text, true);
    }

    public float getWidth(String text, boolean stripColorCodes) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        if (stripColorCodes) {
            text = stripControlCodes(text);
        }

        float width = 0;
//        width -= MARGIN / 2f;
        for (char character : text.toCharArray()) {
            if (!plainCharacters.containsKey(character)) {
                setupCharacter(character, true, false, false);
            }
            final CharacterData characterData = plainCharacters.get(character);
            width += characterData.getWidth() - 2 * MARGIN;
        }
        return width / 2f;
    }

    public String stripControlCodes(String text) {
        return patternControlCode.matcher(text).replaceAll("");
    }

    public float getHeight(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        float height = 0;
        for (char character : text.toCharArray()) {
            if (!plainCharacters.containsKey(character)) {
                setupCharacter(character, true, false, false);
            }
            final CharacterData characterData = plainCharacters.get(character);
            if (characterData.getHeight() > height) {
                height = characterData.getHeight();
            }
        }
        /* everything has to be divided by two again because we scale the height and width with the factor 0.5f*/
        return height / 2f - height / 2f / 4f;
    }

    public float getHeight() {
        return this.getHeight(CHARS);
    }

    // minecraft`s method to generate the color codes they use
    private void genColorCodes() {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            colorCodes[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    private int createTextureID(BufferedImage image) {
        // Array of all the colors in the image.
//        int[][] pixels = new int[image.getWidth()][image.getHeight()];
        int[] pixels = new int[image.getWidth() * image.getHeight()];

        // Fetches all the colors in the image.
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
//        pixels = getRGBOfPicture(image);
//        pixels = convertTo2DWithoutUsingGetRGB(image);

        // Buffer that will store the texture data.
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        // Puts all the pixel data into the buffer.
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                // The pixel in the image.
                int pixel = pixels[y * image.getWidth() + x];
//                int pixel = pixels[y][x];

                // Puts the data into the byte buffer.
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        // Flips the byte buffer, not sure why this is needed.
        buffer.flip();

        int textureId = glGenTextures();
        // Binds the opengl texture by the texture id.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Sets the texture parameter stuff.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Uploads the texture to opengl.
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // Binds the opengl texture 0.
        return textureId;
    }


    private class CharacterData {
        private final float width, height;
        private final int textureID;

        public CharacterData(float width, float height, int textureID) {
            this.width = width;
            this.height = height;
            this.textureID = textureID;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public int getTextureID() {
            return textureID;
        }
    }
}
