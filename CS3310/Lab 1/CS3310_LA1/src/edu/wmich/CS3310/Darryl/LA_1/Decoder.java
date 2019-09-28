package edu.wmich.CS3310.Darryl.LA_1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Decoder implements IDecoder {
	private String message;
	private RandomAccessFile f;
	private BufferedReader txtReader;
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	
	@Override
	public void setMessage(String message) {
		// TODO Auto-generated method stub
		this.message=message;
	}

	@Override
	public void encode(String fromfileName, String tofileName) throws IOException {
		// TODO Auto-generated method stub
		
		//File file= new File(fromfileName);
		try {
			txtReader= new BufferedReader(new FileReader(fromfileName));
			message=txtReader.readLine();
			f= new RandomAccessFile(tofileName,"rw");
			
			f.writeInt(message.length());
			f.seek((message.length()*4)+4);
			f.writeChars(message);
			f.seek(4);
			
			for(int i=0;i<message.length();i++) {
				f.writeInt((message.length()*4)+4+i*2);
			}
			
			txtReader.close();
		}
		
		catch(FileNotFoundException e) {
			System.out.println("Text File named: "+fromfileName+" is not found.\n");
			System.exit(0);
		}	
	}

	@Override
	public void decode(String fileName) throws IOException {
		// TODO Auto-generated method stub
		
		message="";
		
		try {
			f = new RandomAccessFile(fileName,"rw");
			int size=f.readInt();
			int arr[]=new int[size];
			
			for(int i=0;i<size;i++) {
				arr[i]=f.readInt();
			}
			for(int i=0;i<size;i++) {
				f.seek(arr[i]);
				message=message+f.readChar();
			}
		}
		
		catch(FileNotFoundException e){
			System.out.println("Random Access File named: "+fileName+" is not found.\n");
			System.exit(0);
		}
	}
}
