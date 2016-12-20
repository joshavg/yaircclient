package de.joshavg.yaircclient;

public class Main {

    private Main() {
        // no construction needed
    }

    public static void main(String... args) {
        Settings.createIfNotExists();
        new GuiController().start();
    }

}
