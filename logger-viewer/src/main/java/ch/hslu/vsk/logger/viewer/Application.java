package ch.hslu.vsk.logger.viewer;

public class Application {

    public static void main(String[] args) {
        Consumer consumer = new Consumer("0.0.0.0:5701");
        consumer.serve();
    }

}
