package edu.wmich.CS3310.Darryl.LA_2;

public class Node implements INode {
	 private Object content;
	 private INode left, right;
	 
	 public Node(Object c){
		 content=c;
		 left=right=null;
	 }
	@Override
	public Object getContent() {
		// TODO Auto-generated method stub
		return content;
	}

	@Override
	public void setContent(Object o) {
		// TODO Auto-generated method stub
		content=o;
	}

	@Override
	public INode getLeft() {
		// TODO Auto-generated method stub
		return left;
	}

	@Override
	public void setLeft(INode left) {
		// TODO Auto-generated method stub
		this.left=left;
	}

	@Override
	public INode getRight() {
		// TODO Auto-generated method stub
		return right;
	}

	@Override
	public void setRight(INode right) {
		// TODO Auto-generated method stub
		this.right=right;
	}

}
