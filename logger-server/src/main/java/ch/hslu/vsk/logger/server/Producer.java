package ch.hslu.vsk.logger.server;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class Producer {

    HazelcastInstance hazelcastInstance;
    IQueue<String> queue;

    public Producer() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
        queue = hazelcastInstance.getQueue("log-message");
    }

    public void send(String message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Couldn't add Message to Hazelcast Queue");
        }
    }
}
