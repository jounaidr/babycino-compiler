class ImpFact{
    public static void main(String[] a){
	System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {

    public int ComputeFac(int num){
	int result;
	result = 1;
	while (1 < num) {
	    result = result * num;
	    num = num - 1;
	}
	return result;
    }

}

