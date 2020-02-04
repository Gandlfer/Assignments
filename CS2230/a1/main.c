#include <msp430.h>
#include <libemb/serial/serial.h>
#include <libemb/conio/conio.h>

char* code(char*);

int main(void) {
	// Wizard Words
	WDTCTL  = WDTPW | WDTHOLD;                // Disable Watchdog
	BCSCTL1 = CALBC1_1MHZ;                    // Run @ 1MHz
	DCOCTL  = CALDCO_1MHZ;

	// Set Up Serial Communication
	serial_init(9600);

	// Infinite Loop
	for (;;) {
		int j = 0;                            // Char array counter
		char line[128] = { 0 };                // Init empty array

		cio_print((char *) "> ");             // Display prompt
		char c = cio_getc();                  // Wait for a character

		while(c != '\r') {                    // until return sent then ...
			if(c == 0x08) {                   //  was it the delete key?
				if(j != 0) {                  //  cursor NOT at start?
					line[--j] = 0;            //  delete key logic
					cio_printc(0x08); cio_printc(' '); cio_printc(0x08);
				}
			} else {                          // otherwise ...
				line[j++] = c; cio_printc(c); //  echo received char
			}

			c = cio_getc();                   // Wait for another
		}

		cio_print((char *) "\n\r");

		cio_printf("%s\n\r", code(line));
	}

	return 0;
}

