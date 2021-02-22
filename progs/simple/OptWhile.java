class OptWhile {
    public static void main(String[] a){
	System.out.println(new WhileFalse().f());
    }
}

class WhileFalse {
    public int f() {
        int n;
        n = 0;
        while (false) {
            n = n + 1;
        }
        return n;
    }

}

class WhileTrue {
    public int f() {
        int n;
        n = 0;
        while (true) {
            n = n + 1;
        }
        return n;
    }

}

