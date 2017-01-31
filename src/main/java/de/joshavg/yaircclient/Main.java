package de.joshavg.yaircclient;

public class Main {

    private Main() {
        // no construction needed
    }

    public static void main(String... args) {
        Brabbel brabbel = DaggerBrabbel.create();

        brabbel.settings().createIfNotExists();
        brabbel.guiController().start(brabbel);
    }

}
