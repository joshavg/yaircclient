package de.joshavg.yaircclient.api.listener;

import de.joshavg.yaircclient.api.Client;
import de.joshavg.yaircclient.api.Message;
import de.joshavg.yaircclient.api.ResponseParser;

import java.util.*;
import java.util.function.Consumer;

public class UsersCollector implements ApiListener {

    private final Map<String, Set<String>> buffer;
    private final Map<String, List<Consumer<Set<String>>>> queue;

    private final Client client;

    public UsersCollector(Client client) {
        this.client = client;
        buffer = new HashMap<>();
        queue = new HashMap<>();
    }

    @Override
    public void parsed(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed, Client client) {
        String cmd = parsed.get(ResponseParser.Key.CMD).get();

        if ("353".equals(cmd)) {
            addUsersToBuffer(parsed);
        } else if ("366".equals(cmd)) {
            String channel = notifyQueuedConsumers(parsed);
            buffer.remove(channel);
        }
    }

    private String notifyQueuedConsumers(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed) {
        String channel = parsed.get(ResponseParser.Key.CHANNEL).get();
        if(queue.containsKey(channel)) {
            queue.get(channel).forEach(c -> c.accept(buffer.get(channel)));
        }
        return channel;
    }

    private void addUsersToBuffer(Map<ResponseParser.Key, ResponseParser.ResponseValue> parsed) {
        String channel = parsed.get(ResponseParser.Key.CHANNEL).get();
        String payload = parsed.get(ResponseParser.Key.PAYLOAD).get();

        buffer.putIfAbsent(channel, new HashSet<>());
        buffer.get(channel).addAll(Arrays.asList(payload.split("\\s")));
    }

    public void requestUsersInChannel(String channel, Consumer<Set<String>> callback) {
        client.write(Message.names(channel));
        queue.putIfAbsent(channel, new ArrayList<>());
        queue.get(channel).add(callback);
    }
}
