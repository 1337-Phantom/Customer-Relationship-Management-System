package vip.phantom.system.user_interface.screens.main_screen.calendar;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.contract.ContractManager;
import vip.phantom.system.task.Task;
import vip.phantom.system.task.TaskManager;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalendarScreen extends MainScreen {

    public CalendarScreen() {
        super("Kalendar");
    }

    private LocalDate shownMonth;

    @Override
    public void initScreen() {
        super.initScreen();
        shownMonth = LocalDate.now().minusDays(LocalDate.now().getDayOfMonth() - 1);
        buttonList.add(new NormalButton(0, 0, 0, (int) fr.getWidth("LAST") + 10, (int) fr.getHeight(), "LAST"));
        buttonList.add(new NormalButton(1, 0, 0, (int) fr.getWidth("NEXT") + 10, (int) fr.getHeight(), "NEXT"));
    }

    @Override
    public void drawMainArea(int mouseX, int mouseY) {
        drawHeadline(mouseX, mouseY);

        long lengthOfMonth = shownMonth.lengthOfMonth();
        int daysPerLine = 7, rows = 6;

        String monthText = "Monat: " + shownMonth.plusDays(1).getMonth().name() + " " + shownMonth.getYear();
        fr.drawString(monthText, mainWindow.getX(), renderY, Color.white);
        getButtonById(0).setX(255);
        getButtonById(0).setY(renderY);
        getButtonById(1).setX(getButtonById(0).getX() + getButtonById(0).getWidth() + 3);
        getButtonById(1).setY(getButtonById(0).getY());
        renderY += fr.getHeight();

        float dayWidth = (mainWindow.getWidth() - 15) / daysPerLine;

        String[] strings = new String[]{"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
        for (int i = 0; i < daysPerLine; i++) {
            float stringWidth = fr.getWidth(strings[i]);
            fr.drawString(strings[i], mainWindow.getX() + dayWidth * i + dayWidth / 2f - stringWidth / 2f, renderY, Color.white);
        }
        renderY += fr.getHeight();

        float dayHeight = (mainWindow.getHeight() - 15 - (renderY - mainWindow.getY())) / rows;

        int minusDays = 0;
        LocalDate tempDay = shownMonth;
        while (tempDay.getDayOfWeek() != DayOfWeek.MONDAY) {
            tempDay = tempDay.minusDays(1);
            minusDays += 1;
        }

        LocalDate shownDay = shownMonth.minusDays(minusDays);
        float xOffset = 2;
        for (int i = 0; i < daysPerLine * rows; i++) {
            if (i % daysPerLine == 0 && i != 0) {
                xOffset = 2;
                renderY += dayHeight + 2;
            }
            float internRenderY = renderY;
            if (LocalDate.now().equals(shownDay)) {
                RenderUtil.drawRect(mainWindow.getX() + xOffset, renderY, dayWidth, dayHeight, new Color(45, 98, 97, 255));
            }
            Color displayColor = shownMonth.getMonth().equals(shownDay.getMonth()) ? Color.white.darker() : Color.gray.darker();
            RenderUtil.drawOutline(mainWindow.getX() + xOffset, renderY, dayWidth, dayHeight, 1, LocalDate.now().equals(shownDay) ? new Color(97, 202, 192) : displayColor);
            fr.drawString(String.valueOf(shownDay.getDayOfMonth()), mainWindow.getX() + xOffset + 3, internRenderY += 3, displayColor);
            internRenderY += fr.getHeight() - 5;

            RenderUtil.beginScissor(mainWindow.getX() + xOffset, renderY, dayWidth, dayHeight);
            for (Contract contract : ContractManager.INSTANCE.getContractsForDay(shownDay)) {
                fr.drawString(contract.getHeadline(), mainWindow.getX() + xOffset + 3, internRenderY, Color.black);
                internRenderY += fr.getHeight();
            }
            for (Task task : TaskManager.INSTANCE.getTasksForDay(shownDay)) {
                fr.drawString(task.getHeadline(), mainWindow.getX() + xOffset + 3, internRenderY, Color.black);
                internRenderY += fr.getHeight();
            }
            RenderUtil.endScissor();

            shownDay = shownDay.plusDays(1);
            xOffset += dayWidth + 2;
        }
        super.drawMainArea(mouseX, mouseY);
    }

    @Override
    public void buttonPressed(int buttonId) {
        switch (buttonId) {
            case 0 -> shownMonth = shownMonth.minusMonths(1);
            case 1 -> shownMonth = shownMonth.plusMonths(1);
        }
        super.buttonPressed(buttonId);
    }
}
