#include <msp430.h>
#include <libemb/conio/conio.h>
#include <libemb/serial/serial.h>

#include "colors.h"
#include "dtc.h"

/******
 *
 *    CONSTANTS
 *
 ******/

// large number of data structures should be here

// use `const` keyword

const char patterns1[] = { 0b00000001, 0b00000001, 0b00000000, 0b00000000, 
			0b00000000, 0b00000000, 0b00000000, 0b00000001, 
			0b00000000, 0b00000000, 0b00000000, 0b00000000, 
			0b00000001,0b00000000, 0b00000000, 0b00000000 };

const char patterns2[] = { 0b00000000, 0b11100001, 0b10001000, 0b11000000, 
			0b01100001, 0b01000100, 0b00000100, 0b11100000, 
			0b00000000, 0b01000000, 0b00100000, 0b00000101, 
			0b00001100,0b10000001, 0b00001100, 0b00101100 };
const char arrcolors1[]={0b00000000,0b00000001,0b00000000};
const char arrcolors2[]={0b10101101,0b00000100,0b00000101};

/******

 *

 *    GLOBALS

 *

 ******/

// counters can go here

char cursor = 0;

//char value  = 0b011;
int counter= 0;
int color = 0;

/******

 *

 *    INITIALIZATION

 *

 ******/

int main(void)

{
	/* WIZARD WORDS ***************************/
	serial_init(9600);
	
	P1SEL &= ~BIT1;
	P1SEL2 &= ~BIT1;

        WDTCTL   = WDTPW | WDTHOLD;               // Disable Watchdog

        BCSCTL1  = CALBC1_1MHZ;                   // Run @ 1MHz

        DCOCTL   = CALDCO_1MHZ;

         /* GPIO ***********************************/

         P1DIR = BIT7|BIT6|BIT5|BIT1|BIT0;

         P2DIR = -1;

        P1OUT =BIT7|BIT5|BIT1;

        P2OUT = 0;
	
	P1REN |= BIT3;
	P1OUT |= BIT3;
	P1IE |= BIT3;
	P1IES |= BIT3;
	P1IFG &= BIT3;

          // use P2.6 and P2.7 as GPIO
        P2SEL  &= ~(BIT6|BIT7);
        P2SEL2 &= ~(BIT6|BIT7);

          /* TIMER A0 *******************************/
	P1SEL |= BIT6;
	P2SEL |= BIT1|BIT4;

	//7 segment display
        TA0CTL   = TASSEL_2|ID_0|MC_1;
        TA0CCTL0 = CCIE;
        TA0CCR0  = 1024;
	
	initialize_dtc(INCH_4, &TA0CCR1);
	TA0CCTL1=OUTMOD_7;
	
        /* TIMER A1 *******************************/

        // timer 1 initialization
	TA1CTL   = TASSEL_2|ID_0|MC_1;
        TA1CCTL0 = CCIE;
        TA1CCR0  = 1024;

	TA1CCTL1=OUTMOD_7;
	TA1CCTL2=OUTMOD_7;
         /* ADC10CTL *******************************/

           // or &TA1CCR1 or &TA1CCR2


/******

 *

 *    MAIN LOOP (THERE ISN'T ONE)

 *

 ******/

          // go to sleep with interrupts enabled

          __bis_SR_register(LPM1_bits|GIE);


          // never return

          return 0;

}

/******

 *

 *    INTERRUPTS

 *

 ******/

#pragma vector=TIMER0_A0_VECTOR

__interrupt void timer0 (void)

{
          // insert timer0 code here
	int value=0;
	if(color==0){
		value=TA0CCR1;
	}
	else if(color==1){
		value=TA1CCR1;
	}
	else{
		value=TA1CCR2;
	}

          // 1. turn off digits

          P1OUT &= ~(BIT7|BIT5|BIT1);

          // 2. turn on next digit and configure segment pins for correct pattern
          if (cursor == 0) {
			P1OUT = patterns1[value%16]|BIT3;
			P2OUT = patterns2[value%16];
                        P1OUT |= BIT7;

          } else if (cursor == 1) {
			P1OUT = patterns1[value/64]|BIT3;
			P2OUT = patterns2[value/64];
                        P1OUT |= BIT5;

          } else {
			P1OUT = arrcolors1[color]|BIT3;
			P2OUT = arrcolors2[color];
                        P1OUT |= BIT1;

          }

          // move ahead cursor for next time

          cursor++;
          cursor %= 3;

          // 4. leave the interrupt

}

#pragma vector=TIMER1_A0_VECTOR

__interrupt void timer1 (void)

{
	// insert timer1 code here
	if(counter==0){
		cio_printf("%s\n\r", colors[(TA0CCR1>>7)*1 + (TA1CCR1>>7)*8 + (TA1CCR2>>7)*64]);
	}
	counter++;
	counter%=1000;
}


#pragma vector=PORT1_VECTOR

__interrupt void button (void)

{
	// insert button code here

	color++;
	color%=3;
	if(color==0){
		initialize_dtc(INCH_4, &TA0CCR1);
	}
	else if(color==1){
		initialize_dtc(INCH_4, &TA1CCR1);
	}
	else{
		initialize_dtc(INCH_4, &TA1CCR2);
	}
	
	// button debounce routine

        while (!(BIT3 & P1IN)) {} // is finger off of button yet?

        __delay_cycles(32000);    // wait 32ms

        P1IFG &= ~BIT3;           // clear interrupt flag

}
