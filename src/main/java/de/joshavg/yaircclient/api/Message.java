package de.joshavg.yaircclient.api;

public class Message {

    private Message() {
    }

    public static String nick(String n) {
        return "NICK " + n;
    }

    public static String join(String c) {
        return "JOIN " + c;
    }

    public static String part(String c) {
        return "PART " + c;
    }

    public static String user(String u) {
        return String.format("USER %s 0 * :%s", u, u);
    }

    public static String privmsg(String target, String payload) {
        return String.format("PRIVMSG %s %s", target, payload);
    }

}
