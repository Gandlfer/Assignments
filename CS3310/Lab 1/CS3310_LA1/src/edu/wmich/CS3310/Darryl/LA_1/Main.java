package edu.wmich.CS3310.Darryl.LA_1;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {	
		Scanner sc = new Scanner(System.in);
		int count=1,input = 0;
		boolean flag=false;
		IDecoder d = new Decoder();
		IPalindromeChecker p = new PalindromeChecker();
		p.setDecoder(d);
        String fromfile="", tofile="";
        
        // a loop to ask input via console for both fromfile and tofile with $ indicating the end of the input process.
        while(!fromfile.equals("$") || !tofile.equals("$"))
        {  	
        	System.out.println("Enter \"$\" to end the .txt-.dat conversion. Any missing extensions to the file name will be added automatically");
            System.out.println("What is the name of the text file you would like to convert?\nNote*: File has to be .txt extension");
            fromfile=sc.nextLine();
            int check = fileChecker(fromfile,".txt");
            
            if(fromfile.equals("$")) {
            	break;
            }
            else if(check==-1) {
            	System.out.println("Wrong file format. Format has to be .txt");
            	System.exit(0);
            }
            else if(check==0) {
            	fromfile=fromfile+".txt";
            }
           
            System.out.println("Name the file you would like to store this to.\nNote*: File has to be .dat extension");
            tofile=sc.nextLine();
            check = fileChecker(tofile,".dat");
            
            if(tofile.equals("$")) {
            	break;
            }
            else if(check==-1) {
            	System.out.println("Wrong file format. Format has to be .dat");
            	System.exit(0);
            }
            else if(check==0) {
            	tofile=tofile+".dat";
            }
            
        	d.encode(fromfile, tofile);  // fromfile should exist and tofile will be created if it does not exist. All msgs from textmsg1.txt are copied to tofile; Otherwise, all msgs are copied to the end of tofile.
		  	d.decode(tofile);
			System.out.println(d.getMessage());
			if(p.isPalidrome()){
				System.out.println("The message is palidrome.");
			}
			else{
				System.out.println("The message is not palidrome.");
			}
			
			System.out.println("File encoded and decoded.\n-------------------------------------------------------\n");
        }
        // more writing and reading can be done here….
        System.out.println(".txt to .dat conversion ended.");
		
        // Use a loop to read all existing messageXXX files, where XXX is given as an input from console
        while(flag==false) {
        	System.out.println("Enter the number of files you would like to decode.(Range 1-10)");
	        input=sc.nextInt();
	        if(input>=1 && input<=10) {
	        	flag=true;
	        }
	        else {
	        	System.out.println("The range of files has to be between 1-10");
	        }
        }
        
        while(count<=input)
		{	
        	d.decode("message"+count+".dat");
			System.out.println(d.getMessage());
			if(p.isPalidrome()){
				System.out.println("The message is palidrome.");
			}
			else{
				System.out.println("The message is not palidrome.");
			}
			
			System.out.println("message"+count+".dat decoded\n");
			count++;
		}
        sc.close();
	}
	public static int fileChecker(String fileName,String extension) {
		if(fileName.contains(".")) {
			extension=extension.replace(".", "");
			String []token=fileName.split("\\.");
			if (token[1].equalsIgnoreCase(extension)) {
				return 1;
			}
			else {
				return -1;
			}
		}
		else {
			return 0;
		}
	}
}

