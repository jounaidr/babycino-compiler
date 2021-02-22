class Main{
	public static void main(String[] arg){
		System.out.println(new DoWhileLoopTest().DoWhile());
	}
}

class DoWhileLoopTest {
	public int DoWhile(){
	int n;
	n = 0;
	do {
		n = n + 2;
	}
	while(n < 9);
	return n;
	}
}