/*
 * Copyright (c) 2022, for DirtyMod by Dirt, Deleteboys, Phantom.
 * All rights reserved.
 *
 * Copyright (c) for Minecraft by Mojang.
 * (This license is not in contact with Mojangs)
 */

package vip.phantom.system.user_interface.interactive_areas.text;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.utils.KeybindUtil;
import vip.phantom.api.utils.Methods;
import vip.phantom.api.utils.RenderUtil;

import java.awt.*;
import java.util.regex.Pattern;

import static vip.phantom.api.utils.KeybindUtil.*;

public class TextField {
    private final FontRenderer fr;
    @Getter
    private String text = "", placeHolder;
    @Setter
    @Getter
    private float x, y, width, height;

    private float scrollOffset;

    private boolean dragging = false;

    private long lastClickTime = 0;
    private int cursorPosition = 0, selectionPosition = 0;

    private final Pattern pattern;
    private boolean matchesPattern;

    @Getter
    @Setter
    private boolean isShown = true, focused, obfuscated;

    public TextField(float x, float y, float width, String placeholder, FontRenderer fr) {
        this(x, y, width, placeholder, fr, "");
    }

    public TextField(float x, float y, float width, String placeholder, FontRenderer fr, String regex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.placeHolder = placeholder + "...";
        this.fr = fr;
        this.height = fr.getHeight("I") + 2;
        pattern = Pattern.compile(regex);
        writeText("");
    }

    public void drawTextField(int mouseX, int mouseY) {
        if (isShown) {
            if (dragging) {
                setCursorPositionByXCoordinate(mouseX);
            }
            RenderUtil.drawOutline(x, y, width, height, 1, matchesPattern ? Color.black : Color.red);
            RenderUtil.beginScissor((int) x, (int) y, (int) width, (int) height);
            String text = this.text;
            if (obfuscated) {
                text = Methods.coverString(text, 0, text.length());
            }
            if (selectionPosition != cursorPosition && focused) {
                RenderUtil.drawRect(x + 4 + fr.getWidth(text.substring(0, Math.min(cursorPosition, selectionPosition))) + scrollOffset, y + 1, fr.getWidth(text.substring(Math.min(cursorPosition, selectionPosition), Math.max(cursorPosition, selectionPosition))), this.y + height, Color.gray);
            }
            fr.drawString(text.equalsIgnoreCase("") ? placeHolder : text, x + 2 + scrollOffset, y + height / 2f - fr.getHeight() / 2f, text.equalsIgnoreCase("") ? Color.gray : Color.black);
            if (isFocused() && ((System.currentTimeMillis() - lastClickTime) % 1200 < 600)) {
                float cursorX = x + 3 + fr.getWidth(text.substring(0, cursorPosition)) + scrollOffset;
                RenderUtil.drawRect(cursorX, y + height / 2f - fr.getHeight() / 2f + 1, 2, fr.getHeight() - 1, Color.black);
                if (cursorX >= x + width - 3) {
                    scrollOffset -= 1;
                }
                if (cursorX <= x) {
                    scrollOffset += 1;
                }
            }
            RenderUtil.endScissor();
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean hovered = isTextFieldHovered(mouseX, mouseY);
        if (isShown) {
            if (hovered) {
                setFocused(true);
                resetCursorTime();
                if (!isShiftKeyDown()) {
                    setSelectionPositionByXCoordinate(mouseX);
                }
                setCursorPositionByXCoordinate(mouseX);
                dragging = true;
            } else {
                setFocused(false);
            }
        }
        return isTextFieldHovered(mouseX, mouseY);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    public void setCursorPositionByXCoordinate(float mouseX) {
        mouseX -= scrollOffset;
        if (mouseX < x + 2) {
            setCursorPosition(0);
        } else if (mouseX > x + 2 + fr.getWidth(text)) {
            setCursorPosition(text.length());
        } else {
            float previous = 0;
            for (int i = 1; i <= text.length(); i++) {
                final float textPos = x + 2 + fr.getWidth(text.substring(0, i));
                if (mouseX >= previous && mouseX < textPos) {
                    setCursorPosition(i - 1);
                }
                previous = textPos;
            }
        }
    }

    public void setSelectionPositionByXCoordinate(float mouseX) {
        mouseX -= scrollOffset;
        if (mouseX < x + 2) {
            setSelectionPosition(0);
        } else if (mouseX > x + 2 + fr.getWidth(text)) {
            setSelectionPosition(text.length());
        } else {
            float previous = 0;
            for (int i = 1; i <= text.length(); i++) {
                final float textPos = x + 2 + fr.getWidth(text.substring(0, i));
                if (mouseX >= previous && mouseX < textPos) {
                    setSelectionPosition(i - 1);
                }
                previous = textPos;
            }
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        if (focused && isShown) {
            if (isKeyComboCtrlV(keyCode)) {
                writeText(getClipboardString());
            } else if (isKeyComboCtrlA(keyCode)) {
                setSelectionPosition(0);
                setCursorToEnd();
            } else if (isKeyComboCtrlC(keyCode)) {
                setClipboardString(getSelectedText());
            } else if (isKeyComboCtrlX(keyCode)) {
                setClipboardString(getSelectedText());
                writeText("");
            } else {
                switch (keyCode) {
                    case Keyboard.KEY_BACK:
                        if (cursorPosition == selectionPosition) {
                            setCursorPosition(cursorPosition - 1);
                        }
                        writeText("");
                        break;
                    case Keyboard.KEY_DELETE:
                        if (cursorPosition == selectionPosition) {
                            setCursorPosition(cursorPosition + 1);
                        }
                        writeText("");
                        break;
                    case Keyboard.KEY_RIGHT:
                        if (isShiftKeyDown()) {
                            setCursorPosition(cursorPosition + 1);
                        } else {
                            moveCursorBy(1);
                        }
                        break;
                    case Keyboard.KEY_LEFT:
                        if (isShiftKeyDown()) {
                            setCursorPosition(cursorPosition - 1);
                        } else {
                            moveCursorBy(-1);
                        }
                        break;
                    default:
                        if (typedChar != 167 && typedChar >= 32 && typedChar != 127) {
                            writeText(String.valueOf(typedChar));
                        }
                        break;
                }
            }
            return true;
        }
        return false;
    }

    public String getSelectedText() {
        if (cursorPosition != selectionPosition && this.text.length() > 0) {
            return text.substring(Math.min(cursorPosition, selectionPosition), Math.max(cursorPosition, selectionPosition));
        }
        return "";
    }

    public void setText(String text) {
        this.text = text;
        setCursorToEnd();
        setSelectionPosition(cursorPosition);
    }

    private void writeText(String text) {
        int beginIndex = Math.min(cursorPosition, selectionPosition);
        this.text = this.text.substring(0, beginIndex) + text + this.text.substring(Math.max(cursorPosition, selectionPosition));
        setCursorPosition(beginIndex);
        setSelectionPosition(beginIndex);
        moveCursorBy(text.length());
        matchesPattern = matchesPattern();
    }

    public boolean matchesPattern() {
        return pattern.pattern().equals("") || pattern.matcher(text).matches();
    }

    private void moveCursorBy(int offset) {
        if (cursorPosition + offset >= 0 && cursorPosition + offset <= text.length()) {
            if (cursorPosition + offset >= 0) {
                setCursorPosition(cursorPosition + offset);
            }
        }
        setSelectionPosition(cursorPosition);
    }

    private void setSelectionPosition(int index) {
        if (index < 0) {
            selectionPosition = 0;
            return;
        }
        if (index > text.length()) {
            selectionPosition = text.length();
            return;
        }
        this.selectionPosition = index;
    }

    private void setCursorToEnd() {
        setCursorPosition(text.length());
    }

    private void setCursorPosition(int index) {
        resetCursorTime();
        if (index < 0) {
            index = 0;
        }
        if (index > text.length()) {
            index = text.length();
        }
        this.cursorPosition = index;
    }

    private void resetCursorTime() {
        lastClickTime = System.currentTimeMillis();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        Keyboard.enableRepeatEvents(focused);
        resetCursorTime();
    }

    private boolean isTextFieldHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }
}
