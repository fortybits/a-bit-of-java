package edu.bit.__dump;

class Derive extends Base {
    private String className = "Derived"; //Compiling successfully

    public String getName() { //Not compiling
        return "derived";
    }
}