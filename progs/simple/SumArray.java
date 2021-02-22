class SumArray{
    public static void main(String[] a){
	System.out.println(new Sum().Compute());
    }
}

class Sum {

    public int Compute(){
    	int[] data;
    	int n;
    	int result;
    	data = new int[5];
    	data[0] = 0;
    	data[1] = 1;
    	data[2] = 2;
    	data[3] = 3;
    	data[4] = 4;
	result = 0;
	n = 0;
	while (n < 5) {
            result = result + (data[n]);
            n = n + 1;
	}
	return result;
    }

}

