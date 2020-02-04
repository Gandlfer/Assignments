#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>
#include <string.h>

// TODO: what are function prototypes?
void other_base(unsigned, unsigned);
void power2_base(unsigned, unsigned);
void base_256(unsigned, unsigned);
void checking(char*,int);
int pwr(int);

// this will not change, so we can declare it constant
const char *ascii = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+/";

// this will change, however

int buffer_size=30;
char buffer[32];
int argv_deci_size=0;
int argv_base_size=0;

// every C program must implement the main() function
int main(int argc, char *argv[]) {

	// TODO: error check
	if(argc!=3){
		printf("Invalid Format!\n Proper format:\n\t ./conv <decimal> <base>");
		printf("\nIgnore \"< >\" symbol\n");
		printf("*Note:\n\tBase can only be between range 2-64 or 256\n\tDecimal can only be numbers\n");
	}

	else{	
		// properly NULL terminate the buffer
		buffer[sizeof(buffer)-1] = 0;
		
		argv_deci_size=strlen(argv[1]);
		argv_base_size=strlen(argv[2]);

		char argv_deci[argv_deci_size];
		char argv_base[argv_base_size];

		strcpy(argv_deci,argv[1]);
		strcpy(argv_base,argv[2]);
		
		checking(argv_base,argv_base_size);
		

		// TODO: use atoi() to translate characters to numbers
		int base=atoi(argv_base);
		
		if ((base>64 || base<2) && base!=256){
			printf("Base range can only be between 2-64 or 256!\n");
			exit(0);
		}

		checking(argv_deci,argv_deci_size);
			
		// TODO: use atoi() to translate characters to numbers
		int decimal=atoi(argv_deci);
		
		// TODO: decide which function to call based on
		// the user's desired base
		if(decimal==0){
			buffer[buffer_size]=ascii[0];
			buffer_size--;
		}

		else{
			if(base==256){
				base_256(decimal,base);
			}
			else if(pwr(base)==-1){
				other_base(decimal,base);
			}
			else if(pwr(base)!=-1){
				power2_base(decimal,base);
			}
			
		}
		printf("%s\n", buffer+buffer_size+1);
		// remember there should only be a SINGLE call
		// to printf() in your entire program
		
	}
	// exit with 0 if the program was successful
	return 0;
}

void other_base(unsigned decimal, unsigned base) {

	// TODO: for bases that are not a power of 2 or 256

	while(decimal!=0){
		int remainder =decimal%base;
		buffer[buffer_size]=ascii[remainder];
		decimal=decimal/base;
		buffer_size=buffer_size-1;
	}

	return;
}

void power2_base(unsigned decimal, unsigned base) {

	// TODO: for bases that are a power of 2 but still not 256
	int power=pwr(base);
	int remainder=0;

	while(decimal!=0){
		remainder=decimal & (base-1);
		buffer[buffer_size]=ascii[remainder];
		decimal= decimal>>power;
		buffer_size=buffer_size-1;
	}
	
	return;
}

void base_256(unsigned decimal, unsigned base) {
	
	// TODO: for base 256 only
	
	int power=pwr(base);
	int count=3;
	int remainder=0;
	int arr[4];

	while(decimal!=0){
		remainder=decimal & (base-1);
		arr[count]=remainder;
		decimal= decimal>>power;
		count--;
	}

	for(int i=(sizeof(arr)/sizeof(int))-1;i>count;i--){
		while(arr[i]!=0){
			remainder=arr[i]%10;
			buffer[buffer_size]=ascii[remainder];
			arr[i]=arr[i]/10;
			buffer_size=buffer_size-1;
		}
		if(i!=count+1){
			buffer[buffer_size]='.';
			buffer_size=buffer_size-1;
		}
	}

	return;
}

void checking(char *arr, int size){
	for(int i=0;i<size-1;i++){
		if((int)arr[i]==46){
			printf("No non integer numbers allowed!\n");
			exit(0);
		}
		else if(isdigit(arr[i])==0){
			printf("Only Integer number is allowed!\n");
			exit(0);
		}
	}
	
	return;
}

int pwr(int base){
	int count=0;
	while(base!=1){
		if(base==5){
			return -1;
		}
		else{
			base=base/2;
			count++;
			if(base%2==1 && base!=1){
				return -1;
			}
		}
	}
	return count;
}