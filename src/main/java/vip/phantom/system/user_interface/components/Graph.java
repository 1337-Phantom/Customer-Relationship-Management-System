package vip.phantom.system.user_interface.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.Area;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

public class Graph extends Area {

    private String yAxesName, xAxesName;

    private HashMap<LocalDate, Float> entries;

    private List<Vertex2f> vertexPoints = new ArrayList<>();

    private HashMap<String, Vertex2f> descriptionPoints = new HashMap<>();

    private FontRenderer fr = Fonts.Light8;

    private final Area statisticArea;

    public Graph(float x, float y, float width, float height, String yAxesName, String xAxesName, HashMap<LocalDate, Float> hashMap) {
        super(x, y, width, height);
        this.yAxesName = yAxesName;
        this.xAxesName = xAxesName;
        this.entries = hashMap;
        statisticArea = new Area(x + 50, y + fr.getHeight(), width - 60, height - fr.getHeight() - 5);
        calculatePoints();
    }

    public void drawGraph(int mouseX, int mouseY) {
        RenderUtil.drawRect(x, y, width, height, Color.white);
        RenderUtil.drawOutline(x, y, width, height, 1, Color.black);

        RenderUtil.drawVerticalLine(statisticArea.getY(), statisticArea.getY() + statisticArea.getHeight(), statisticArea.getX(), 1, false, Color.black);
        RenderUtil.drawHorizontalLine(statisticArea.getX(), statisticArea.getX() + statisticArea.getWidth(), statisticArea.getY() + statisticArea.getHeight(), 1.5f, false, Color.black);
        fr.drawString(yAxesName, statisticArea.getX() - fr.getWidth(yAxesName) / 2f, y + 2, Color.black);
        for (Map.Entry<String, Vertex2f> stringVertex2fEntry : descriptionPoints.entrySet()) {
            String s = stringVertex2fEntry.getKey();
            Vertex2f point = stringVertex2fEntry.getValue();
            RenderUtil.drawHorizontalLine(point.getX() - 3, point.getX() + 2, point.getY(), 2, false, Color.black);
            fr.drawString(s, point.getX() - fr.getWidth(s) - 7, point.getY() - fr.getHeight() / 2f, Color.black);
        }
        if (!vertexPoints.isEmpty()) {


            GL11.glPushMatrix();
            GL11.glBegin(GL11.GL_LINE_STRIP);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_TEXTURE_2D);
            glLineWidth(2);
            RenderUtil.setGLColor(255, 0, 0, 255);
            for (Vertex2f vertexPoint : vertexPoints) {
                GL11.glVertex2f(vertexPoint.getX(), vertexPoint.getY());
            }
            GL11.glEnd();
            GL11.glPopMatrix();

//        System.out.println(descriptionPoints.size());
        } else {
            String msg = "No Information on Income";
            fr.drawString(msg, x + width / 2f - fr.getWidth(msg) / 2f, y + height / 2f - fr.getHeight() / 2f, Color.black);
        }
    }


    private void calculatePoints() {
        List<LocalDate> keySet = entries.keySet().stream().sorted(LocalDate::compareTo).collect(Collectors.toList());
        List<LocalDate> xPoints = keySet.stream().collect(Collectors.toList());
        if (keySet.size() > 0) {
            LocalDate firstDate = xPoints.get(0).minusDays(5),
                    lastDate = xPoints.get(xPoints.size() - 1).plusDays(5);
            long displayedDays = ChronoUnit.DAYS.between(firstDate, lastDate);
            xPoints.clear();
            LocalDate currentDate = firstDate;

            while (!currentDate.equals(lastDate)) {
                xPoints.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }
            xPoints = xPoints.stream().sorted(LocalDate::compareTo).toList();

            float totalMoneyEarned = 0;
            for (LocalDate localDate : keySet) {
                totalMoneyEarned += entries.get(localDate);
            }

            float averageTransaction = totalMoneyEarned / entries.size();
            float mostMoney = totalMoneyEarned + averageTransaction;

            float money = 0;
            for (int i = 0; i < xPoints.size(); i++) {
                LocalDate localDate = xPoints.get(i);
                if (entries.get(localDate) != null) {
                    money += entries.get(localDate);
                }
                vertexPoints.add(new Vertex2f(statisticArea.getX() + statisticArea.getWidth() * ((float) i / displayedDays), (int) (statisticArea.getY() + statisticArea.getHeight() * (mostMoney + 100 - money) / (mostMoney + 100)) - 1));
            }

            for (int i = 0; i <= mostMoney / averageTransaction; i++) {
                float tmp = averageTransaction * i;
                if (tmp > mostMoney) {
                    tmp = mostMoney;
                }
                descriptionPoints.put(String.valueOf((int) tmp), new Vertex2f(statisticArea.getX(), statisticArea.getY() + statisticArea.getHeight() * (mostMoney + 100 - tmp) / (mostMoney + 100)));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                float tmp = 250 * i;
                descriptionPoints.put(String.valueOf((int) tmp), new Vertex2f(statisticArea.getX(), statisticArea.getY() + statisticArea.getHeight() * (1100 - tmp) / 1100));
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class Vertex2f {
        public float x, y;
    }
}
