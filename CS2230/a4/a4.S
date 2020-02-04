#include <msp430.h>

#define PC r0
#define SP r1
#define SR r2
#define CG r3

;------------------------------------------------------------------------------
; PROGRAM DATA (READ-ONLY)
		.section .rodata	; this should be placed in ROM
;------------------------------------------------------------------------------

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
		mov		#WDTPW|WDTHOLD, &WDTCTL
		mov.b	&CALBC1_1MHZ,	&BCSCTL1
		mov.b	&CALDCO_1MHZ,	&DCOCTL

		; initialize stack pointer
		mov		#0x400, SP
		
		mov.b		#0,r4

		eint		;enable interrupt
		
		;timer setup
		mov 		#TASSEL_2|ID_3|MC_1, &TA1CTL
		mov 		#TASSEL_2|ID_3|MC_1, &TA0CTL
		mov		#41667,&TA1CCR0; set the number to cause interrupt
		mov 		#CCIE,&TA1CCTL0

		;set p1.6
		mov		#0,&TA0CCR0
		mov		#0,&TA0CCR1
		mov		#OUTMOD_4, &TA0CCTL1

		;button setup
		mov.b 		#~BIT3,	&P1DIR
		mov.b 		#BIT3,	&P1REN
		mov.b 		#BIT3,	&P1OUT
		bis.b		#BIT3, 	&P1IE
		bis.b		#BIT3, 	&P1IES
		bic.b		#BIT3, 	&P1IFG

.Lloop:		
		cmp 		#2,r4
		jge		checkRED

		xor.b		#1,&P1OUT
		mov		#500,r15
		call		#delay_ms	
		jmp		.Lloop

checkRED:	
		bic.b		#BIT0,&P1OUT
		jmp		.Lloop

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
		bic.b 		#BIT3, &P1IFG
		add.b		#BIT0,r4
		and.b		#BIT1|BIT0,r4
		
		bic.b		#BIT6,&P1SEL
		
		cmp		#2,r4
		jeq		TURNBLUE

		cmp		#3,r4
		jeq		TURNBLUEHalf

		jmp		UNEXPECTED_ISR

LIGHTTIMER_ISR:	
		cmp	#1,r4
		jeq	TURNGREEN

		cmp	#2,r4
		jeq	TURNGREEN
		
		bic.b	#2,&P1OUT
		jmp	UNEXPECTED_ISR
		
TURNGREEN:	
		xor.b		#2,&P1OUT
		jmp		UNEXPECTED_ISR
		
TURNBLUE:	
		mov.b		#BIT6,&P1SEL
		mov		#31250,&TA0CCR0
		jmp		UNEXPECTED_ISR

TURNBLUEHalf:
		mov.b		#BIT6,&P1SEL
		bic.b		#3,&P1OUT
		mov		#62500,&TA0CCR0
		jmp		UNEXPECTED_ISR

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
		.word LIGHTTIMER_ISR	;0xfffa (TIMER1_A0_VECTOR)
		.word UNEXPECTED_ISR	;0xfffc (NMI_VECTOR)
		.word MAIN				;0xfffe (RESET_VECTOR)
		.end

