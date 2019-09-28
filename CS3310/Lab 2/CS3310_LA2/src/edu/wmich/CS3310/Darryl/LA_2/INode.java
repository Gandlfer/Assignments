package edu.wmich.CS3310.Darryl.LA_2;

public interface INode {

	//Getter of message
	public Object getContent();

	//Setter of message
	public void setContent(Object o);      // for operator store char such as ‘+’, etc.. // for operand store integer; 

    public INode getLeft() ;

	public void setLeft(INode left) ;

	public INode getRight() ;

	public void setRight(INode right);
              
}

