#include "calc.h"

const char *OPS = "+-/*^%";

int main(void) {
	WDTCTL  = WDTPW | WDTHOLD;                // Disable Watchdog
	BCSCTL1 = CALBC1_1MHZ;                    // Run @ 1MHz
	DCOCTL  = CALDCO_1MHZ;

	serial_init(9600);                        // Initialize Serial Comms

	for (;;) {
		int j = 0;                            // Char array counter
		char line[32] = { 0 };                // Init empty array

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

		cio_print("\n\r");

		char *token, *p = line;
		int a, b, op;

		for (int i = 0; i < 3; ++i) {
			token = strsep(&p, " ");
			if (token == NULL) { break; }

			if (i == 0) {
				a = atoi(token);
			} else if (i == 1) {
				char *pos = strchr(OPS, token[0]);
				if (pos == NULL) { break; }
				op = pos - OPS;
			} else {
				b = atoi(token);
				cio_printf("%i\n\r", (*funcs[op])(a, b));
			}
		}
	}

	return 0;
}

