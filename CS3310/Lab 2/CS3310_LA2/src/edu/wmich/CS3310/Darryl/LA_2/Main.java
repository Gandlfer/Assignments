package edu.wmich.CS3310.Darryl.LA_2;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tree t=null;
		Scanner sc= new Scanner(System.in);
		
		System.out.println("*Note: Program does not read parenthesis.\nEnter a string of equation: ");
		String simple_eq=sc.nextLine();
		//String simple_eq="1. 5 + 3 - 4; 2. 5*4 + 3; 3. 5 + 4 * 3; 5 * 6 / 10;";
		//String complex_eq="3 * 2 – 4 * 5 / 3 ;    10 + 30 / 5 * 6 – 19 * 2 / 4";
		
		simple_eq=simple_eq.replaceAll("\\s","");
		//complex_eq=complex_eq.replaceAll("\\s","");
		
		String token[]=simple_eq.split(";");
		System.out.println("Simple Equations\n-------------------------------------------");
		printResult(t,token);
		
		/*token=complex_eq.split(";");
		System.out.println("Complicated Equations\n-------------------------------------------");
		printResult(t,token);*/
		sc.close();
	}
	public static void printResult(Tree t, String token[]) {
		for(int i = 0;i<token.length;i++) {
			t=build(token[i]);
			System.out.print("Prefix: :");
			t.prefix_visit();
			System.out.println();
			System.out.print("Infix: :");
			t.infix_visit();
			System.out.println();
			System.out.print("Postfix: :");
			t.postfix_visit();
			System.out.println("\nTotal: "+t.evaluate(t.getRoot()));
			System.out.println("\n");
		}
	}
	public static Tree build(String equation) {
		INode r = new Node(equation);
		Tree t=new Tree(r);
		buildNodes(r);
		return t;
	}
	public static int searchForPlusMinus(String eq) {
		for(int i=0;i<eq.length();i++) {
			if(eq.charAt(i)=='+' || (eq.charAt(i)=='–' ||eq.charAt(i)=='-') ) { return i;}
		}
		return -1;
	}
	public static int searchForMultiDiv(String eq) {
		for(int i=0;i<eq.length();i++) {
			if(eq.charAt(i)=='*' || eq.charAt(i)=='/') { return i;}
		}
		return -1;
	}
	public static void buildNodes(INode n) {
		String data=n.getContent().toString();
		int checking1=searchForPlusMinus(data);
		int checking2=searchForMultiDiv(data);
		if(checking1==-1 && checking2==-1) {
			return;
		}
		
		if(checking1==-1) {
			n.setLeft(new Node(data.substring(0, checking2)));
			n.setRight(new Node(data.substring(checking2+1,n.getContent().toString().length())));
			n.setContent(data.charAt(checking2));
		}
		else {
			n.setLeft(new Node(data.substring(0, checking1)));
			n.setRight(new Node(data.substring(checking1+1,n.getContent().toString().length())));
			n.setContent(data.charAt(checking1));
		}
		
		buildNodes(n.getLeft());
		buildNodes(n.getRight());

	}
}
