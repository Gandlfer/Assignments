import java.sql.*;
import java.util.Scanner;

public class MainDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind","cs4430","Darryllee99"); 
			
			Statement stmt=con.createStatement();  
			Scanner userinput = new Scanner(System.in);
			int selectedchoice=0;
			
			while(selectedchoice!=7) {

				System.out.println("1. add a customer\n" + 
						"2. add an order\n" + 
						"3. remove an order\n" + 
						"4. ship an order\n" + 
						"5. print pending orders (not shipped yet) with customer information\n" + 
						"6. restock products\n" + 
						"7. exit\n" + 
						"Please select your choice");
				try {
					String str=userinput.nextLine();
					selectedchoice=Integer.parseInt(str);
					if(selectedchoice>0 && selectedchoice<8)
						getResult(selectedchoice,stmt);
					else
						System.out.println("Please enter the choice between 1 - 7!\n");
				}
				catch(NumberFormatException e) {
					System.out.println("Please enter a valid number!\n");
				}
				
			}

			con.close();  
			userinput.close();
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
	}
	
	public static void getResult(int input,Statement stmt) throws SQLException {
		ResultSet rs;
		boolean flag=false;
		Scanner sc=new Scanner(System.in);
		//switch case for user selection 
		switch(input) {
		//Add a customer 
		case 1:
			//query to get the total number of columns
			rs=stmt.executeQuery("SELECT count(COLUMN_NAME) as 'count' FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'northwind' AND TABLE_NAME = 'customers';");
			rs.next();
			int size=rs.getInt("count");
			//Array used to store all values given by user for each field
			String arr[]= new String[11];
			//query to get the header of each columns
			rs=stmt.executeQuery("SELECT COLUMN_NAME as 'col' FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'northwind' AND TABLE_NAME = 'customers';");
			rs.next();
			//For loop the whole array while acquiring input from user field by field
			for(int i=1;i<=size;i++) {
				System.out.println("Please enter your "+rs.getString(1));
				arr[i-1]=sc.nextLine();
				
				//if user's input is blank, prompt user for input again
				if(arr[i-1].isBlank()) {
					System.out.println(rs.getString(1)+" is blank!");
					i--;
				}
				//if user's customerID is a duplicate value
				else if(i==1) {
					ResultSet get=stmt.executeQuery("SELECT count(*) as 'count' FROM customers WHERE CustomerID = '"+arr[1]+"'");
					get.next();
					if(rs.getInt("count(*)")>0) {
						System.out.println("CustomerID has been taken!\n Re-enter:");
						i--;
					}
				}
				else {
					rs.next();
				}
			}
			//Adding proper formating to use for MySQL statements
			String query="";
			for(int i=0;i<size;i++) {
				if(i==size-1) {
					query=query+ "'"+arr[i]+"'"+")";
				}
				else
					query=query+"'"+arr[i]+"'"+",";
			}
			stmt.execute("INSERT INTO customers VALUES ("+query);//Query to add values into database
			
			break;
		
		//Add order
		case 2://
			break;
		
		//Remove order
		case 3:
			int oid=0;
			System.out.println("Enter the OrderID that you want to remove:");
			while (flag==false) {
				try {
					String str=sc.nextLine();
					oid=Integer.parseInt(str);
					rs=stmt.executeQuery("SELECT count(*) from Orders where OrderID = "+oid);
					rs.next();
					if(rs.getInt("count(*)")<1) {
						System.out.println("OrderID not found\n");
						System.out.println("Enter the OrderID that you want to remove:");
					}
					else
						flag=true;
				}
				catch(NumberFormatException e) {
					System.out.println("OrderID contains NUMERICAL only!\n");
					System.out.println("Enter the OrderID that you want to remove:");
				}catch(Exception e) {
					System.out.println(e);
				}

			}
			
			stmt.execute("DELETE FROM Orders where OrderID='"+oid+"'");
			stmt.execute("DELETE FROM Order_details where OrderID='"+oid+"'");
			break;
		//Ship order
		case 4:
			break;
		//print pending orders
		case 5://
			break;
		
		//Restock
		case 6:
			//flag=false;
			int amount=0;
			System.out.println("Enter the productID that you want to restock");
			//Validation loop
			while(flag==false) {
				try {
					String str=sc.nextLine();
					int pid=Integer.parseInt(str);
					rs=stmt.executeQuery("SELECT * from products where ProductID = "+pid);//Query to acquire the product name
					rs.next();
					System.out.println(rs.getString(2));//printing the name of the item
					System.out.println("Enter the number of items you want to restock:");
					//Validation loop
					while(flag==false) {
						try {
							str=sc.nextLine();
							amount=Integer.parseInt(str);
							flag=true;
						}
						//check if the amount given by user is a character or number
						catch(NumberFormatException e){
							System.out.println("Please enter a valid number!\n");
							System.out.println("Enter the number of items you want to restock:");
						}
					}
					stmt.execute("Update Products Set UnitsInStock= UnitsInStock + "+amount+" where ProductID="+pid);//Query to add the amount into the UnitsInStock
				}
				//check if productID given by user is a character or a number
				catch(NumberFormatException e) {
					System.out.println("Please enter a valid number!\n");
					System.out.println("Enter the productID that you want to restock");
				}
				//check if the productID given by user is not in the products list
				catch(SQLException e) {
					System.out.println("Product does not exist");
					System.out.println("Enter the productID that you want to restock");
				}
			}
			
			System.out.println("Successfully Restocked!\n");
			break;
			
		}
		sc.close();
	}
}
