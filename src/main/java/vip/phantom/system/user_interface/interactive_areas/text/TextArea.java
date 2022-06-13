package vip.phantom.system.user_interface.interactive_areas.text;

import lombok.Getter;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.utils.KeybindUtil;
import vip.phantom.api.utils.Methods;
import vip.phantom.api.utils.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static vip.phantom.api.utils.KeybindUtil.*;

public class TextArea {

    private boolean focused = false;

    @Getter
    private float x, y, width, height;
    private final float spacerWidth = 2;

    private float scrollXOffset, scrollYOffset;

    private final FontRenderer fr;
    public Color backgroundColor = new Color(33, 33, 33),
            normalTextColor = new Color(54, 64, 64),
            normalObjectColor = new Color(66, 66, 66),
            cursorColor = new Color(255, 204, 0),
            scrollBarColor = new Color(200, 122, 8);

    private final List<String> lines = new ArrayList<>();

    private float lineNumberWidth, separationLineX;
    private final float lineHeight;
    private int firstLineToDraw, visibleLines;
    private float maxScrollOffsetX, maxScrollOffsetY;

    private final float[] scrollbarX = new float[4], scrollbarY = new float[4];
    private static final int POSX = 0, POSY = 1, WIDTH = 2, HEIGHT = 3;
    private boolean draggingScrollbarX = false, draggingScrollbarY = false;

    private long lastClickTime = 0;
    private final int[] cursorPosition = new int[2], selectionPosition = new int[2];
    private static final int LINE = 0, POSITION = 1;
    private boolean draggingCursor = false;

    private float dragX, dragY, scrollBarOffset;

    public TextArea(FontRenderer fr, float x, float y, float width, float height) {
        this.fr = fr;
        this.x = x;
        this.y = y;
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);

        /* test lines */
//        for (int i = 0; i < 50; i++) {
//            lines.add("Test numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgeroTest numgero" + (i + 1));
//        }
        lines.add(""); // add first line
//        lines.addAll(first);
//        lines.addAll(second);
        setLineNumberVariables();
        lineHeight = fr.getHeight() + spacerWidth / 2f;
//        writeText("");
        updateResize();
    }

    public void drawTextArea(int mouseX, int mouseY) {
        if (draggingCursor) {
            setMarkerPositionThrough(cursorPosition, mouseX, mouseY);
            if (isTextSelected()) {
                showCursorIfOutOfScreen();
            }
        }

        /* rendering lines */
        firstLineToDraw = Math.max(0, Math.min(lines.size() - 1, (int) (-scrollYOffset / lineHeight)));
        visibleLines = Math.min(lines.size() - firstLineToDraw, (int) (height / lineHeight) + 1);
        for (int line = firstLineToDraw; line < firstLineToDraw + visibleLines; line++) {
            String lineString = lines.get(line);
            float lineY = y + scrollYOffset + line * lineHeight;
            if (line == cursorPosition[LINE]) {
                RenderUtil.drawRect(x, lineY, x + width, lineY + lineHeight, backgroundColor.darker());
            }
            if (isTextSelected() && ((selectionPosition[LINE] <= line && cursorPosition[LINE] >= line) || (cursorPosition[LINE] <= line && selectionPosition[LINE] >= line))) {
                float x = separationLineX;
                float endX = x + width;
                if (cursorPosition[LINE] == selectionPosition[LINE]) {
                    if (cursorPosition[POSITION] < selectionPosition[POSITION]) {
                        x = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, cursorPosition[POSITION]));
                        endX = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, selectionPosition[POSITION]));
                    } else {
                        x = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, selectionPosition[POSITION]));
                        endX = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, cursorPosition[POSITION]));
                    }
                    endX += scrollXOffset;
                    x += scrollXOffset;
                } else if (cursorPosition[LINE] > selectionPosition[LINE]) {
                    if (selectionPosition[LINE] == line) {
                        x = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, selectionPosition[POSITION]));
                        x += scrollXOffset;
                    } else if (cursorPosition[LINE] == line) {
                        endX = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, cursorPosition[POSITION]));
                        endX += scrollXOffset;
                    }
                } else {
                    if (cursorPosition[LINE] == line) {
                        x = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, cursorPosition[POSITION]));
                        x += scrollXOffset;
                    } else if (selectionPosition[LINE] == line) {
                        endX = separationLineX + spacerWidth + fr.getWidth(lineString.substring(0, selectionPosition[POSITION]));
                        endX += scrollXOffset;
                    }
                }
                if (x < separationLineX) {
                    x = separationLineX;
                }
                if (endX < separationLineX) {
                    endX = separationLineX;
                } else if (endX > x + width) {
                    endX = x + width;
                }
                RenderUtil.drawRect(x, lineY, endX, lineY + lineHeight, new Color(53, 53, 53));
            }
            float lineFrY = lineY + lineHeight / 2f - fr.getHeight() / 2f + 1;
            fr.drawString(String.valueOf(line + 1), x + spacerWidth + lineNumberWidth - fr.getWidth(String.valueOf(line + 1)), lineFrY, normalTextColor);

            RenderUtil.beginScissor(separationLineX, y, width - (separationLineX - x), height);
            String[] words = lineString.split(" ");
            float wordX = separationLineX + spacerWidth + scrollXOffset;
            for (String word : words) {
                float wordWidth = fr.getWidth(word += " ");
                if (wordX + wordWidth > separationLineX) {
                    Color textColor = normalTextColor.brighter().brighter();

                    fr.drawString(word, wordX, lineFrY, textColor);
                }
                wordX += wordWidth;
            }
//            fr.drawString(lineString, separationLineX + spacerWidth + scrollXOffset, lineFrY, normalTextColor.brighter().brighter());
            RenderUtil.endScissor();
            if (focused && ((System.currentTimeMillis() - lastClickTime) % 1200 < 600) && line == cursorPosition[LINE]) {
                RenderUtil.drawVerticalLine(lineY + spacerWidth / 2f, lineY + lineHeight - spacerWidth / 2f, separationLineX + spacerWidth + scrollXOffset + fr.getWidth(lines.get(line).substring(0, cursorPosition[POSITION])), 1, false, cursorColor);
            }
        }
        RenderUtil.drawVerticalLine(y, y + height, separationLineX, 0.5f, false, normalObjectColor);

        float scrollBarWidth = 4.5f;
        /* scroll bar y */
        {
            float wholeHeight = height + maxScrollOffsetY;
            float displayedHeight = height - spacerWidth * 2;
            float scrollbarHeight = displayedHeight * (displayedHeight / wholeHeight);
            float scrollbarPosY = y + spacerWidth + (displayedHeight - scrollbarHeight) * (-scrollYOffset / (maxScrollOffsetY));
            setScrollbarValues(scrollbarY, x + width - spacerWidth - scrollBarWidth, scrollbarPosY, scrollBarWidth, scrollbarHeight);

            if (draggingScrollbarY) {
                scrollYOffset = Methods.clamp((dragY - mouseY) * (wholeHeight / displayedHeight) + scrollYOffset, -maxScrollOffsetY, 0);
                dragY = mouseY;
            }

            if (wholeHeight > displayedHeight) {
                RenderUtil.drawVerticalLine(scrollbarY[POSY], scrollbarY[POSY] + scrollbarY[HEIGHT], scrollbarY[POSX] + scrollBarWidth / 2f, scrollbarY[WIDTH] / 2f, true, isScrollBarHovered(mouseX, mouseY, scrollbarY) || draggingScrollbarY ? scrollBarColor : scrollBarColor.darker());
            }
        }
        /* scroll bar x */
        {
            float wholeWidth = width + maxScrollOffsetX;
            float displayedWidth = width - (separationLineX - x) - spacerWidth * 3 - scrollBarWidth;
            float scrollbarWidth = displayedWidth * (displayedWidth / wholeWidth);
            float scrollbarPosX = separationLineX + spacerWidth + (displayedWidth - scrollbarWidth) * (-scrollXOffset / (maxScrollOffsetX));
            setScrollbarValues(scrollbarX, scrollbarPosX, y + height - spacerWidth - scrollBarWidth, scrollbarWidth, scrollBarWidth);

            if (draggingScrollbarX) {
                scrollXOffset = Methods.clamp((dragX - mouseX) * (wholeWidth / displayedWidth) + scrollXOffset, -maxScrollOffsetX, 0);
                dragX = mouseX;
            }
//            RenderUtil.drawRect(scrollbarX[POSX], scrollbarX[POSY], scrollbarX[POSX] + scrollbarX[WIDTH], scrollbarX[POSY] + scrollbarX[HEIGHT], Color.green);
            if (wholeWidth > displayedWidth) {
                RenderUtil.drawHorizontalLine(scrollbarX[POSX], scrollbarX[POSX] + scrollbarX[WIDTH], scrollbarX[POSY] + scrollBarWidth / 2f, scrollbarX[HEIGHT] / 2f, true, isScrollBarHovered(mouseX, mouseY, scrollbarX) || draggingScrollbarX ? scrollBarColor : scrollBarColor.darker());
            }
        }

        RenderUtil.endScissor();
    }

    private void setScrollbarValues(float[] scrollbar, float x, float y, float width, float height) {
        scrollbar[POSX] = x;
        scrollbar[POSY] = y;
        scrollbar[WIDTH] = width;
        scrollbar[HEIGHT] = height;
    }

    private boolean isScrollBarHovered(int mouseX, int mouseY, float[] scrollbar) {
        return RenderUtil.isHovered(mouseX, mouseY, scrollbar[POSX], scrollbar[POSY], scrollbar[WIDTH], scrollbar[HEIGHT]);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isTextAreaHovered(mouseX, mouseY)) {
            focused = true;
            boolean doubleClick = System.currentTimeMillis() - lastClickTime < 200;
            lastClickTime = System.currentTimeMillis();
            if (isScrollBarHovered(mouseX, mouseY, scrollbarY)) {
                draggingScrollbarY = true;
                scrollBarOffset = scrollbarY[POSY];
            } else if (isScrollBarHovered(mouseX, mouseY, scrollbarX)) {
                draggingScrollbarX = true;
                scrollBarOffset = scrollbarX[POSX];
            } else {
                if (!isShiftKeyDown()) {
                    setMarkerPositionThrough(selectionPosition, mouseX, mouseY);
                }
                setMarkerPositionThrough(cursorPosition, mouseX, mouseY);
                draggingCursor = true;
            }
            dragX = mouseX;
            dragY = mouseY;
        } else {
            focused = false;
        }
        Keyboard.enableRepeatEvents(focused);
    }

    private void setMarkerPositionThrough(int[] marker, int mouseX, int mouseY) {
        mouseY -= scrollYOffset;
        mouseX -= scrollXOffset;
        int selectedLine = Math.max(0, Math.min(lines.size() - 1, (int) ((mouseY - y) / lineHeight)));
        String selectedLineText = lines.get(selectedLine);
        int selectedPosition = 0;
        if (mouseX > separationLineX + spacerWidth + fr.getWidth(selectedLineText)) {
            selectedPosition = selectedLineText.length();
        } else if (mouseX > separationLineX + spacerWidth) {
            float previous = 0;
            for (int i = 1; i <= selectedLineText.length(); i++) {
                final float textPos = separationLineX + spacerWidth + fr.getWidth(selectedLineText.substring(0, i));
                if (mouseX > previous && mouseX <= textPos) {
                    selectedPosition = i - 1;
                }
                previous = textPos;
            }
        } else if (!draggingCursor && marker == cursorPosition) {
            setMarkerPosition(selectionPosition, selectedLine, 0);
            selectedLine += 1;
        }
        setMarkerPosition(marker, selectedLine, selectedPosition);
    }

    private void setMarkerPosition(int[] marker, int line, int position) {
        lastClickTime = System.currentTimeMillis();
        line = Math.max(0, Math.min(line, lines.size() - 1));
        if (position < 0) {
            line = Math.max(0, line - 1);
            position = lines.get(line).length();
        } else if (position > lines.get(line).length()) {
            if (line + 1 <= lines.size() - 1 && line >= marker[LINE]) {
                line += 1;
                position = 0;
            } else {
                position = lines.get(line).length();
            }
        }
        marker[LINE] = line;
        marker[POSITION] = position;
    }

    private void showCursorIfOutOfScreen() {
        final float cursorY = y + scrollYOffset + cursorPosition[LINE] * lineHeight + lineHeight;
        final float currentDisplayedY = y + scrollYOffset + firstLineToDraw * lineHeight;
        if (cursorY < currentDisplayedY) {
            scrollYOffset = Methods.clamp(-(cursorPosition[LINE]) * lineHeight + 15, -maxScrollOffsetY, 0);
        } else if (cursorY > currentDisplayedY + height) {
            scrollYOffset = Methods.clamp(-(cursorPosition[LINE] - visibleLines) * lineHeight - 15, -maxScrollOffsetY, 0);
        }
        final float cursorOffset = fr.getWidth(lines.get(cursorPosition[LINE]).substring(0, cursorPosition[POSITION]));
        final float cursorX = separationLineX + spacerWidth + scrollXOffset + cursorOffset;
        if (cursorX < separationLineX) {
            scrollXOffset = Methods.clamp(-cursorOffset + 15, -maxScrollOffsetX, 0);
        } else if (cursorX > x + width) {
            scrollXOffset = Methods.clamp(-cursorOffset - 15 + (x + width - separationLineX), -maxScrollOffsetX, 0);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        draggingCursor = false;
        draggingScrollbarX = false;
        draggingScrollbarY = false;
    }

    public void handleMouseInput(int mouseX, int mouseY) {
        if (isTextAreaHovered(mouseX, mouseY)) {
            if (Mouse.isCreated()) {
                int wheel = Mouse.getEventDWheel();
                if (wheel != 0) {
                    final float scrollSpeed = isShiftKeyDown() ? 5 : 20;
                    float delta = wheel > 0 ? scrollSpeed : -scrollSpeed;
                    if (isShiftKeyDown()) {
                        if (maxScrollOffsetX > 0) {
                            if (scrollXOffset + delta > 0) {
                                delta = 0 - scrollXOffset;
                            } else if (scrollXOffset + delta < -maxScrollOffsetX) {
                                delta = -maxScrollOffsetX - scrollXOffset;
                            }
                            scrollXOffset += delta;
                        }
                    } else {
                        if (maxScrollOffsetY > 0) {
                            if (scrollYOffset + delta > 0) {
                                delta = 0 - scrollYOffset;
                            } else if (scrollYOffset + delta < -maxScrollOffsetY) {
                                delta = -maxScrollOffsetY - scrollYOffset;
                            }
                            scrollYOffset += delta;
                        }
                    }
                }
            }
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        if (focused) {
            if (isKeyComboCtrlV(keyCode)) {
                writeText(getClipboardString());
            } else if (isKeyComboCtrlA(keyCode)) {
                if (!lines.isEmpty()) {
                    setMarkerPosition(selectionPosition, 0, 0);
                    setMarkerPosition(cursorPosition, lines.size() - 1, lines.get(lines.size() - 1).length());
                }
            } else if (isKeyComboCtrlC(keyCode)) {
                setClipboardString(getSelectedText());
            } else if (isKeyComboCtrlX(keyCode)) {
                setClipboardString(getSelectedText());
                writeText("");
            } else {
                switch (keyCode) {
                    case Keyboard.KEY_BACK -> {
                        if (!isTextSelected()) {
                            setMarkerPosition(cursorPosition, cursorPosition[LINE], cursorPosition[POSITION] - 1);
                        }
                        writeText("");
                    }
                    case Keyboard.KEY_DELETE -> {
                        if (!isTextSelected()) {
                            setMarkerPosition(cursorPosition, cursorPosition[LINE], cursorPosition[POSITION] + 1);
                        }
                        writeText("");
                    }
                    case Keyboard.KEY_UP -> {
                        if (cursorPosition[LINE] > 0) {
                            setMarkerPositionThrough(cursorPosition, (int) (separationLineX + scrollXOffset + spacerWidth + fr.getWidth(lines.get(cursorPosition[LINE]).substring(0, Math.min(lines.get(cursorPosition[LINE]).length(), cursorPosition[POSITION]))) + 2), (int) (this.y + scrollYOffset + lineHeight * Math.max(0, cursorPosition[LINE] - 1) + lineHeight / 2f));
                        }
                        if (!isShiftKeyDown()) {
                            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
                        }
                        showCursorIfOutOfScreen();
                    }
                    case Keyboard.KEY_DOWN -> {
                        if (cursorPosition[LINE] < lines.size() - 1) {
                            setMarkerPositionThrough(cursorPosition, (int) (separationLineX + scrollXOffset + spacerWidth + fr.getWidth(lines.get(cursorPosition[LINE]).substring(0, Math.min(lines.get(cursorPosition[LINE]).length(), cursorPosition[POSITION]))) + 2), (int) (this.y + scrollYOffset + lineHeight * Math.min(cursorPosition[LINE] + 1, lines.size() - 1) + lineHeight / 2f));
                        }
                        if (!isShiftKeyDown()) {
                            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
                        }
                        showCursorIfOutOfScreen();
                    }
                    case Keyboard.KEY_LEFT -> {
                        setMarkerPosition(cursorPosition, cursorPosition[LINE], cursorPosition[POSITION] - 1);
                        if (!isShiftKeyDown()) {
                            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
                        }
                        showCursorIfOutOfScreen();
                    }
                    case Keyboard.KEY_RIGHT -> {
                        setMarkerPosition(cursorPosition, cursorPosition[LINE], cursorPosition[POSITION] + 1);
                        if (!isShiftKeyDown()) {
                            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
                        }
                        showCursorIfOutOfScreen();
                    }
                    case Keyboard.KEY_RETURN, Keyboard.KEY_NUMPADENTER -> writeText("\n");
                    case Keyboard.KEY_TAB -> writeText("    ");
                    default -> {
                        if (typedChar != 167 && typedChar >= 32 && typedChar != 127) {
                            writeText(String.valueOf(typedChar));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void writeText(String text) {
        if (isTextSelected()) {
            int from = 0, to = 0, first = 0, second = 0;
            if (cursorPosition[LINE] < selectionPosition[LINE]) {
                from = cursorPosition[LINE];
                first = cursorPosition[POSITION];
                to = selectionPosition[LINE];
                second = selectionPosition[POSITION];
            } else {
                from = selectionPosition[LINE];
                first = selectionPosition[POSITION];
                to = cursorPosition[LINE];
                second = cursorPosition[POSITION];
            }
            int removedLines = 0;
            int minMarkerPos = Math.min(cursorPosition[POSITION], selectionPosition[POSITION]),
                    maxMarkerPos = Math.max(cursorPosition[POSITION], selectionPosition[POSITION]);
            if (cursorPosition[LINE] == selectionPosition[LINE]) {
                lines.set(cursorPosition[LINE], lines.get(cursorPosition[LINE]).substring(0, minMarkerPos) + lines.get(cursorPosition[LINE]).substring(maxMarkerPos));
                setMarkerPosition(cursorPosition, cursorPosition[LINE], minMarkerPos);
                setMarkerPosition(selectionPosition, cursorPosition[LINE], minMarkerPos);
            } else {
                for (int i = from; i <= to; i++) {
                    if (i == from) {
                        lines.set(from, lines.get(i).substring(0, first) + lines.get(to).substring(second));
                        setMarkerPosition(cursorPosition, from, first);
                    } else {
                        lines.remove(i - removedLines);
                        removedLines++;
                    }
                }
            }
            if (cursorPosition[POSITION] > lines.get(cursorPosition[LINE]).length()) {
                setMarkerPosition(cursorPosition, cursorPosition[LINE], lines.get(cursorPosition[LINE]).length());
            }
            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
        }
        int originalLineToWriteTo = cursorPosition[LINE],
                lineToWriteTo = originalLineToWriteTo;
        String str = lines.get(lineToWriteTo);
        String textBehindCursor = lines.get(cursorPosition[LINE]).substring(cursorPosition[POSITION]);
        if (text.equalsIgnoreCase("\n")) {
            lines.set(cursorPosition[LINE], lines.get(cursorPosition[LINE]).substring(0, cursorPosition[POSITION]));
            lines.add(cursorPosition[LINE] + 1, textBehindCursor);
            setMarkerPosition(cursorPosition, cursorPosition[LINE] + 1, 0);
            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
        } else {
            String[] newTextList = text.split("\n");
            String s = "";
            for (int i = 0, newTextListLength = newTextList.length; i < newTextListLength; i++) {
                s = newTextList[i];
                if (i == 0) {
                    lines.set(lineToWriteTo, str.substring(0, cursorPosition[POSITION]) + s + (i == newTextListLength - 1 ? textBehindCursor : ""));
                } else if (i == newTextListLength - 1) {
                    lines.add(lineToWriteTo, s + textBehindCursor);
                } else {
                    lines.add(lineToWriteTo, s);
                }
                if (i != newTextListLength - 1) {
                    lineToWriteTo += 1;
                }
            }
            setMarkerPosition(cursorPosition, lineToWriteTo, s.length() > 1 && lineToWriteTo != originalLineToWriteTo ? s.length() : cursorPosition[POSITION] + s.length());
            setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);
        }

        setLineNumberVariables();

        maxScrollOffsetY = Math.max(0, lineHeight * lines.size() - height + lineHeight * 4);
        if (scrollYOffset < -maxScrollOffsetY) {
            scrollYOffset = Math.min(0, -maxScrollOffsetY);
        }
        float widestLine = 0;
        for (String line : lines) {
            float strWidth = fr.getWidth(line);
            if (strWidth > widestLine) {
                widestLine = strWidth;
            }
        }
        maxScrollOffsetX = widestLine - width + (separationLineX - x) + spacerWidth + 20;
        if (scrollXOffset < -maxScrollOffsetX) {
            scrollXOffset = Math.min(0, -maxScrollOffsetX);
        }
        showCursorIfOutOfScreen();
    }

    private void updateResize() {
        int originalLineToWriteTo = cursorPosition[LINE],
                lineToWriteTo = originalLineToWriteTo;
        String str = lines.get(lineToWriteTo);
        String textBehindCursor = lines.get(cursorPosition[LINE]).substring(cursorPosition[POSITION]);

        setMarkerPosition(cursorPosition, lineToWriteTo, cursorPosition[POSITION]);
        setMarkerPosition(selectionPosition, cursorPosition[LINE], cursorPosition[POSITION]);

        setLineNumberVariables();

        maxScrollOffsetY = Math.max(0, lineHeight * lines.size() - height + lineHeight * 4);
        if (scrollYOffset < -maxScrollOffsetY) {
            scrollYOffset = Math.min(0, -maxScrollOffsetY);
        }
        float widestLine = 0;
        for (String line : lines) {
            float strWidth = fr.getWidth(line);
            if (strWidth > widestLine) {
                widestLine = strWidth;
            }
        }
        maxScrollOffsetX = widestLine - width + (separationLineX - x) + spacerWidth + 20;
        if (scrollXOffset < -maxScrollOffsetX) {
            scrollXOffset = Math.min(0, -maxScrollOffsetX);
        }
        showCursorIfOutOfScreen();
    }

    private void setLineNumberVariables() {
        lineNumberWidth = fr.getWidth(String.valueOf(lines.size() + 1));
        separationLineX = x + spacerWidth + lineNumberWidth + spacerWidth;
    }

    private boolean isTextSelected() {
        return cursorPosition[LINE] != selectionPosition[LINE] || cursorPosition[POSITION] != selectionPosition[POSITION];
    }

    private String getSelectedText() {
        if (isTextSelected()) {
            StringBuilder sb = new StringBuilder();
            int from = 0, to = 0, first = 0, second = 0;
            if (cursorPosition[LINE] < selectionPosition[LINE]) {
                from = cursorPosition[LINE];
                first = cursorPosition[POSITION];
                to = selectionPosition[LINE];
                second = selectionPosition[POSITION];
            } else {
                from = selectionPosition[LINE];
                first = selectionPosition[POSITION];
                to = cursorPosition[LINE];
                second = cursorPosition[POSITION];
            }
            for (int i = from; i <= to; i++) {
                String str = lines.get(i);
                if (i == from && i == to) {
                    sb.append(str, Math.min(first, second), Math.max(first, second));
                } else {
                    if (i == from) {
                        sb.append(str.substring(first)).append("\n");
                    } else if (i == to) {
                        sb.append(str, 0, second);
                    } else {
                        sb.append(str).append("\n");
                    }
                }
            }
            return sb.toString();
        }
        return getClipboardString();
    }

    public boolean isTextAreaHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }

    public void setX(float x) {
        this.x = x;
//        writeText("");
        updateResize();
    }

    public void setY(float y) {
        this.y = y;
//        writeText("");
        updateResize();
    }

    public void setWidth(float width) {
        this.width = width;
//        writeText("");
        updateResize();
    }

    public void setHeight(float height) {
        this.height = height;
//        writeText("");
        updateResize();
    }
}