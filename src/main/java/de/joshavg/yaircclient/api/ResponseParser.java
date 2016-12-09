package de.joshavg.yaircclient.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseParser {

    public enum Key {
        PAYLOAD, NICK, USER, SERVER, CMD, TARGET, META
    }

    public static class ResponseValue {
        private final String value;

        ResponseValue(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }

        public String[] getValues() {
            return value.split(" ");
        }

        static ResponseValue of(String value) {
            return new ResponseValue(value);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static final Pattern USER_IDENT = Pattern.compile("^([^!]+)!([^@]+)@(.+)$");

    private final String line;

    ResponseParser(String line) {
        this.line = line;
    }

    Map<Key, ResponseValue> parse() {
        Map<Key, ResponseValue> map = new HashMap<>();
        prepareMap(map);

        String workLine = line;
        if (!workLine.startsWith(":")) {
            workLine = ":" + workLine;
        }

        String[] split = (" " + workLine).split(" :");
        if (split.length < 1) {
            return map;
        }

        if (split.length > 2) {
            String payload = workLine.substring(3 + split[1].length());
            map.put(Key.PAYLOAD, ResponseValue.of(payload));
        }

        String header = split[1];
        String[] headerSplit = header.split(" ");
        boolean isSingleHeader = false;

        Matcher userIdentMatcher = USER_IDENT.matcher(headerSplit[0]);
        if (userIdentMatcher.matches()) {
            map.put(Key.NICK, ResponseValue.of(userIdentMatcher.group(1)));
            map.put(Key.USER, ResponseValue.of(userIdentMatcher.group(2)));
            map.put(Key.SERVER, ResponseValue.of(userIdentMatcher.group(3)));
        } else {
            if (headerSplit.length == 1) {
                isSingleHeader = true;
                map.put(Key.CMD, ResponseValue.of(headerSplit[0]));
            } else {
                map.put(Key.SERVER, ResponseValue.of(headerSplit[0]));
            }
        }

        if (!isSingleHeader) {
            map.put(Key.CMD, ResponseValue.of(headerSplit[1]));
            map.put(Key.TARGET, ResponseValue.of(headerSplit[2]));
        }

        if (headerSplit.length > 3) {
            int metaLength = headerSplit[0].length() + headerSplit[1].length() + headerSplit[2].length() + 3;
            String meta = header.substring(metaLength);
            map.put(Key.META, ResponseValue.of(meta));
        }

        return map;
    }

    private void prepareMap(Map<Key, ResponseValue> map) {
        for (Key k : Key.values()) {
            map.put(k, ResponseValue.of(""));
        }
    }

}
