package edu.bit.interfaces;

public class InterfaceUtility {

    enum H implements G {
        ONE, TWO;

        @Override
        public void s() {
        }
    }

    interface G {
        void s();
    }
}