package edu.wmich.CS3310.Darryl.LA_2;

public class Tree implements ITree {
	private INode root;
	
	public Tree(INode r) {
		this.root=r;
	}
	@Override
	public void setRoot(INode root) {
		// TODO Auto-generated method stub
		this.root=root;
	}

	@Override
	public INode getRoot() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public void prefix_visit() {
		// TODO Auto-generated method stub
		//System.out.print("( ");
		System.out.print(pre(root));
		//System.out.print(")");
	}
	
	public String pre(INode r) {
		if(r.getRight()==null && r.getLeft()==null) {return r.getContent().toString();}
		
		return "( "+r.getContent()+" "+pre(r.getLeft())+" "+pre(r.getRight())+" )";
	}
	
	@Override
	public void infix_visit() {
		// TODO Auto-generated method stub
		System.out.print(in(root));
	}
	
	public String in(INode r) {
		if(r.getRight()==null && r.getLeft()==null) {return r.getContent().toString();}
		
		return "( "+in(r.getLeft())+" "+ r.getContent()+" "+in(r.getRight())+" )";
	}
	@Override
	public void postfix_visit() {
		// TODO Auto-generated method stub
		System.out.print(post(root));
	}
	public String post(INode r) {
		if(r.getRight()==null && r.getLeft()==null) {return r.getContent().toString();}
		
		return "( "+post(r.getLeft())+" "+post(r.getRight())+" "+r.getContent()+" )";
	}
	@Override
	public double evaluate(INode node) {
		// TODO Auto-generated method stub
		double sum=0;
		if(node.getRight()==null & node.getLeft()==null) {return Double.parseDouble(node.getContent().toString());}
		switch(node.getContent().toString().charAt(0)) {
		case '*':
			sum= evaluate(node.getLeft())*evaluate(node.getRight());
			break;
		case '/':
			sum= evaluate(node.getLeft())/evaluate(node.getRight());
			break;
		case '–':
			sum= evaluate(node.getLeft())-evaluate(node.getRight());
			break;
		case '-':
			sum= evaluate(node.getLeft())-evaluate(node.getRight());
			break;
		case '+':
			sum= evaluate(node.getLeft())+evaluate(node.getRight());
			break;
		}
		
		return sum;
	}

}
