#include <msp430.h>

#define PC r0
#define SP r1
#define SR r2
#define CG r3

;------------------------------------------------------------------------------
; PROGRAM DATA (READ-ONLY)
		.section .rodata	; this should be placed in ROM
;------------------------------------------------------------------------------
FORMAT: .string "> %s\n\r"
	
	.p2align 1,0
ARRAY: 	
	.word OFF
	.word RED
	.word GREEN
	.word YELLOW
	.word BLUE
	.word PURPLE
	.word CYAN
	.word WHITE

OFF: .string "OFF"	
RED: .string "RED"
GREEN: .string "GREEN"
BLUE: .string "BLUE"
YELLOW: .string "YELLOW"
CYAN: .string "CYAN"
PURPLE: .string "PURPLE"
WHITE: .string "WHITE"
;------------------------------------------------------------------------------
; PROGRAM DATA (READ AND WRITE)
		.section .data		; this should be placed in RAM
;------------------------------------------------------------------------------

;------------------------------------------------------------------------------
; PROGRAM INSTRUCTIONS
		.text				; this section is for instructions
		.global MAIN
;------------------------------------------------------------------------------
MAIN:
		; disable watchdog and run at 1Mhz
		mov	#WDTPW|WDTHOLD, &WDTCTL
		mov.b	&CALBC1_1MHZ,	&BCSCTL1
		mov.b	&CALDCO_1MHZ,	&DCOCTL

		; initialize stack pointer
		mov	#0x400, SP
		
		;turning things on
		mov.b	#7, &P2DIR
		mov.b	#0, &P2OUT
		
		mov.b	#0,r4
		mov.b 	#-2,r5

		;button
		mov.b 	#~BIT3,	&P1DIR
		mov.b 	#BIT3,	&P1REN
		mov.b 	#BIT3,	&P1OUT
		bis.b	#BIT3, 	&P1IE
		bis.b	#BIT3, 	&P1IES
		bic.b	#BIT3, 	&P1IFG
	
		mov 	#9600,	r15
		call 	#serial_init
		
		;enable masking interupt
		eint

.Lnopress:
		cmp 	#8, r4
		jge 	check
		
		mov.b 	r4,&P2OUT
		add 	#1,r4
		add	#2,r5
		
		;set time
		mov 	#1000,r15
		call 	#delay_ms
		
		;loop to change light color
		jmp 	.Lnopress

		;resets light
check:
		mov 	#0,r4
		mov	#-2,r5
		
		;loop back to set light
		jmp .Lnopress

;------------------------------------------------------------------------------
; DELAY_MS FUNCTION
;------------------------------------------------------------------------------
delay_ms:
		mov		r15, r14
1:		mov		#245, r15
2:		dec		r15
		tst		r15
		jnz		2b
		dec		r14
		jnz		1b
		ret

;------------------------------------------------------------------------------
; UNEXPECTED INTERRUPT SERVICE ROUTINE
;------------------------------------------------------------------------------
UNEXPECTED_ISR:
		reti

BUTTON_ISR:
		;clear interrupts 
		bic.b 	#BIT3, &P1IFG
		
		;saving the time delay when interrupt happens
		push	r14
		push	r15
		
		;assign ARRAY to r11
		mov	#ARRAY, r11
		add 	r5,	r11 ; The pointer of the name of the color	
		
		;pushing the name to the stack
		push 	@r11
		push 	#FORMAT
		call 	#cio_printf	;printing output
		add  	#4,SP		;change pointer back to before pushing
				
		pop	r15		
		pop	r14

		reti
;------------------------------------------------------------------------------
; INTERRUPT VECTORS
;------------------------------------------------------------------------------
		.section ".vectors", "ax", @progbits
		.word UNEXPECTED_ISR	;0xffe0
		.word UNEXPECTED_ISR	;0xffe2
		.word BUTTON_ISR	;0xffe4 (PORT1_VECTOR)
		.word UNEXPECTED_ISR	;0xffe6 (PORT2_VECTOR)
		.word UNEXPECTED_ISR	;0xffe8
		.word UNEXPECTED_ISR	;0xffea (ADC10_VECTOR)
		.word UNEXPECTED_ISR	;0xffec (USCIAB0TX_VECTOR)
		.word UNEXPECTED_ISR	;0xffee (USCIAB0RX_VECTOR)
		.word UNEXPECTED_ISR	;0xfff0 (TIMER0_A1_VECTOR)
		.word UNEXPECTED_ISR	;0xfff2 (TIMER0_A0_VECTOR)
		.word UNEXPECTED_ISR	;0xfff4 (WDT_VECTOR)
		.word UNEXPECTED_ISR	;0xfff6 (COMPARATORA_VECTOR)
		.word UNEXPECTED_ISR	;0xfff8 (TIMER1_A1_VECTOR)
		.word UNEXPECTED_ISR	;0xfffa (TIMER1_A0_VECTOR)
		.word UNEXPECTED_ISR	;0xfffc (NMI_VECTOR)
		.word MAIN				;0xfffe (RESET_VECTOR)
		.end

