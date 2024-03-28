package edu.bit;

public class SynchronisedThread {

    static class Callme {
        void call(String msg) {
            System.out.print("[" + msg);
            System.out.print( " inside " + Thread.currentThread().getName() + ":"+ Thread.currentThread().threadId());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            System.out.println("]");
        }
    }

    static class Caller implements Runnable {
        String msg;
        Callme target;
        Thread t;

        public Caller(Thread thread, Callme targ, String s) {
            target = targ;
            msg = s;
            t = thread;
            t.start();
        }

        // synchronize calls to call()
        public void run() {
            synchronized (target) { // synchronized block
                target.call(msg);
            }
        }
    }

    public static void main(String args[]) {
        Callme target = new Callme();
        Thread commonThread = new Thread();
        Caller ob1 = new Caller(commonThread, target, "Hello");
        Caller ob2 = new Caller(commonThread, target, "Synchronized");
        Caller ob3 = new Caller(commonThread, target, "World");
        // wait for threads to end
        try {
            ob1.t.join();
            ob2.t.join();
            ob3.t.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}