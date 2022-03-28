/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import vip.phantom.system.CRM;
import vip.phantom.system.user_interface.screens.LoginScreen;

import static org.lwjgl.opengl.GL11.*;

public class Start {

    public static void main(String[] args) {
        new Start().run(args);
    }

    public void run(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(720, 400));
            Display.setInitialBackground(0, 0, 0);
            Display.setTitle("Customer Relationship System | Yorck Heilmann");
            Display.setResizable(true);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glMatrixMode(GL_MODELVIEW);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        CRM application = CRM.getCrm();
        application.startup();
        Runtime.getRuntime().addShutdownHook(new Thread(application::shutdownHook));

        application.displayScreen(new LoginScreen());

        while (!Display.isCloseRequested()) {
            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glMatrixMode(GL_MODELVIEW);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();

            glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
            glMatrixMode(GL_MODELVIEW);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            /*doing all which has to do with mouse*/
            if (Mouse.isCreated()) {
                while (Mouse.next()) {
                    if (Mouse.getEventButtonState()) {
                        application.mouseClicked(Mouse.getX(), Display.getHeight() - Mouse.getY(), Mouse.getEventButton());
                    } else if (Mouse.getEventButton() != -1) {
                        application.mouseReleased(Mouse.getX(), Display.getHeight() - Mouse.getY(), Mouse.getEventButton());
                    }
                }
            }
            /*doing all which has to do with keyboard*/
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    application.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
                }
            }
            /*doing all which has to do with rendering*/
            application.drawScreen(Mouse.getX(), Display.getHeight() - Mouse.getY());
            Display.update();
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }
}
