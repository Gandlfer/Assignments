#include <msp430.h>
#include <libemb/serial/serial.h>
#include <libemb/conio/conio.h>

// this is an in-place string reversing function
void rev(char*);

// this may look weird, but declaring the strings
// in this way places them in RAM instead of ROM
char s1[] = { "peeping" };
char s2[] = { "geordinscupe" };

int main(void) {
    WDTCTL  = WDTPW | WDTHOLD;
    BCSCTL1 = CALBC1_1MHZ;
    DCOCTL  = CALDCO_1MHZ;

    P1DIR   = BIT0;
    P1OUT   = 0;

    serial_init(9600);

	cio_printf("%s\n\r", s1);
	rev(s1);
	cio_printf("%s\n\r", s1);
	cio_printf("%s\n\r", s2);
	rev(s2);
	cio_printf("%s\n\r", s2);

    return 0;
}

