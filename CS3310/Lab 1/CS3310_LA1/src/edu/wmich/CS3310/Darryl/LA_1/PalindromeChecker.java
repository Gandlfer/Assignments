package edu.wmich.CS3310.Darryl.LA_1;

public class PalindromeChecker implements IPalindromeChecker {
	private IDecoder d = new Decoder();
	@Override
	public boolean isPalidrome() {
		// TODO Auto-generated method stub
		
		String s=d.getMessage().replaceAll("\\s", "").toLowerCase();
		String part1, part2;
		int size= s.length();
		int middle=size/2;
		
		if(size%2==0) {
			part1=s.substring(0, middle);
			part2=s.substring(middle,size);
			for(int i=0;i<middle;i++) {
				if(part1.charAt(i)!=part2.charAt(middle-1-i)) {
					return false;
				}
			}
		}
		
		else if(size%2==1) {
			part1=s.substring(0, middle);
			part2=s.substring(middle+1,size);
			for(int i=0;i<middle;i++) {
				if(part1.charAt(i)!=part2.charAt(middle-1-i)) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public IDecoder getDeocoder() {
		// TODO Auto-generated method stub
		return d;
	}

	@Override
	public void setDecoder(IDecoder decoder) {
		// TODO Auto-generated method stub
		d=decoder;	
	}

}
