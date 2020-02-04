int power(int num, int pwr);
int findSize(char *line);

char buf[128];

char *code(char *line) {
	const int ascii_numbers=48;
	int index=0;
	int buf_size=126;
	int line_size=findSize(line);
	int begin=line_size-1;
	int ascii=0;
	int count=0;//used for calculating how many char numbers read
	int g3=2;//used to know when char in ascii is takes 3 index or 2 index

	buf[127] = 0;//last index is null
	
	//String of numbers to int numbers per each space
	if(line[0]>=48 && line[0]<=57){
		int i;
		for(i=line_size-1;i>-1;i--){
			count++;
			if(line[i]==32){
				index=i;
				for(;begin>index;begin--){
					ascii=ascii+power(line[begin]-ascii_numbers,index-begin+count-1);
				}
				buf[buf_size]=ascii;
				buf_size--;
				begin=index-1;
				count=0;
				ascii=0;
			}

			else if(i==0){
				index=-1;
				for(;begin>index;begin--){
					ascii=ascii+power(line[begin]-ascii_numbers,-1-begin+count);
				}
				buf[buf_size]=ascii;
				buf_size--;
			}
		}
	}

	//String of character to int numbers;
	else{
		int i=0;
		for(i=line_size-1;i>-1;i--){
			ascii=line[i];
			while(ascii!=0){
				if(ascii>=100){
					g3=3;
					buf[buf_size-2]=ascii/100 +ascii_numbers;
					ascii=ascii%100;
					buf[buf_size-1]=ascii/10 +ascii_numbers;
					ascii=ascii%10;
					buf[buf_size]=ascii+ascii_numbers;
					ascii=0;
				}				
				else{	
					g3=2;
					buf[buf_size-1]=ascii/10 +ascii_numbers;
					ascii=ascii%10;
					buf[buf_size]=ascii+ascii_numbers;
					ascii=0;
				}
				buf_size=buf_size-g3;
			}
			buf[buf_size]=32;
			buf_size--;
		}
	}
	return buf + buf_size + 2;
	//return &(buf[125]);
}

int power(int num, int pwr){
	int total=1;
	
	while(pwr!=0){
		total=total*10;
		pwr--;
	}
	return num*total;
}
int findSize(char *line){
	int i=0;
	while(line[i]!=0){
		i++;
	}
	return i;
}	