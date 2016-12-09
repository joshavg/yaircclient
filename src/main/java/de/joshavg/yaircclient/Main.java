package de.joshavg.yaircclient;

public class Main {

    public static void main(String... args) {
        Settings.createIfNotExists();
        new GuiController().start();
    }

}
