package com.fafabtc.common.networks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by jastrelax on 2018/4/1.
 */
public class Ping {

    private static final int DAYTIME_PORT = 13;
    private static int port = DAYTIME_PORT;

    public static class Target {

        private InetSocketAddress address;
        private SocketChannel channel;
        private Exception failure;
        private long connectStart;
        private long connectFinish = 0;
        boolean shown = false;

        public Target(String host) {
            try {
                address = new InetSocketAddress(InetAddress.getByName(host), port);
            } catch (IOException e) {
                failure = e;
            }
        }

        public void show() {
            String result;
            if (connectFinish != 0) {
                result = Long.toString(connectFinish - connectStart) + "ms";
            } else if (failure != null) {
                result = failure.toString();
            } else {
                result = "Time out";
            }
            System.out.println(address + ": " + result);
            shown = true;
        }

    }

    public static class Printer extends Thread {
        private LinkedList<Target> pending = new LinkedList<>();

        public Printer() {
            setName("Printer");
            setDaemon(true);
        }

        public void add(Target target) {
            synchronized (pending) {
                pending.add(target);
                pending.notify();
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                for (; ; ) {
                    Target t = null;
                    synchronized (pending) {
                        while (pending.size() == 0) {
                            pending.wait();
                            t = pending.removeFirst();
                        }
                        t.show();
                    }
                }
            } catch (InterruptedException e) {

            }
        }
    }

    public static class Connector extends Thread {
        private Selector selector;
        private Printer printer;
        private LinkedList<Target> pending = new LinkedList<>();

        public Connector(Printer printer) throws IOException {
            this.printer = printer;
            this.selector = Selector.open();
            setName("Connector");
        }

        public void addTarget(Target target) {
            SocketChannel sc = null;
            try {
                sc = SocketChannel.open();
                sc.configureBlocking(false);
                boolean connected = sc.connect(target.address);
                target.channel = sc;
                target.connectStart = System.currentTimeMillis();
                if (connected) {
                    target.connectFinish = target.connectStart;
                    sc.close();
                    printer.add(target);
                } else {
                    synchronized (pending) {
                        pending.add(target);
                    }
                    selector.wakeup();
                }
            } catch (IOException e) {
                if (sc != null) {
                    try {
                        sc.close();
                    } catch (IOException ignore) {
                    }
                }
                target.failure = e;
                printer.add(target);
            }
        }

        public void processPendingTarget() throws IOException {
            synchronized (pending) {
                while (pending.size() > 0) {
                    Target t = pending.removeFirst();
                    try {
                        t.channel.register(selector, SelectionKey.OP_CONNECT, t);
                    } catch (IOException e) {
                        t.channel.close();
                        t.failure = e;
                        printer.add(t);
                    }
                }
            }
        }

        public void processSelectedKeys() throws IOException {
            for (Iterator<SelectionKey> i = selector.selectedKeys().iterator(); i.hasNext(); ) {
                SelectionKey sk = i.next();
                i.remove();

                Target t = (Target) sk.attachment();
                SocketChannel sc = (SocketChannel) sk.channel();

                try {
                    if (sc.finishConnect()) {
                        sk.cancel();
                        t.connectFinish = System.currentTimeMillis();
                        sc.close();
                        printer.add(t);
                    }
                } catch (IOException e) {
                    sc.close();
                    t.failure = e;
                    printer.add(t);
                }
            }
        }

        private volatile boolean shutdown = false;

        public void shutdown() {
            shutdown = true;
            selector.wakeup();
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    int n = selector.select();
                    if (n > 0)
                        processSelectedKeys();
                    processPendingTarget();
                    if (shutdown) {
                        selector.close();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] urls) throws InterruptedException, IOException {
        if (urls == null || urls.length < 1) {
            return;
        }
        int firstArg = 0;
        if (Pattern.matches("[0-9]+", urls[0])) {
            port = Integer.parseInt(urls[0]);
            firstArg = 1;
        }
        Printer printer = new Printer();
        printer.start();

        Connector connector = new Connector(printer);
        connector.start();

        LinkedList<Target> targets = new LinkedList<>();
        for (int i = firstArg; i < urls.length; i++) {
            Target t = new Target(urls[i]);
            targets.add(t);
            connector.addTarget(t);
        }

        Thread.sleep(2000);
        connector.shutdown();
        connector.join();

        for (Iterator<Target> i = targets.iterator(); i.hasNext(); ) {
            Target t = i.next();
            if (!t.shown)
                t.show();
        }
    }
}
