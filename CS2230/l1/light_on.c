#include <msp430.h>
#include <libemb/serial/serial.h>
#include <libemb/conio/conio.h>

int main(void) {
	WDTCTL  = WDTPW | WDTHOLD;                // Disable Watchdog
	BCSCTL1 = CALBC1_1MHZ;                    // Run @ 1MHz
	DCOCTL  = CALDCO_1MHZ;

	// one time
	P1DIR = BIT6; // 0x20, 32, 0b00100000
	P1OUT = BIT6; // 0x20, 32, 0b00100000
	
	serial_init(9600);

	for (;;) {
		// forever
		cio_printf("%i\n\r",P1IN & BIT3);
		__delay_cycles(500000);
	}

	return 0;
}

