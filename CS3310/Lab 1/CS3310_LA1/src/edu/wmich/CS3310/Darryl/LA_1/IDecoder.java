package edu.wmich.CS3310.Darryl.LA_1;

import java.io.IOException;

public interface IDecoder {

	//Getter of message
	public String getMessage();

	//Setter of message
	public void setMessage(String message);

	//encode the message from fromfileName, then store it in tofileName. If tofileName does not exist, create a binary file. Otherwise, append the message from fromFileName to the end of tofileName.
	void encode(String fromfileName, String tofileName) throws IOException;

	//decode the file’s message, then store it in the attribute message.
	void decode(String fileName) throws IOException;
              
}

