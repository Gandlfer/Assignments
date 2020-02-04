#include <msp430.h>

	.file "rev.S"
	.text
.global rev
rev:
	mov.b	#1, &P1OUT
	sub		#6, r1

	mov		r15, 0(r1)
	mov		r15, 2(r1)

.Lcount:
	mov		2(r1), r15
	mov.b	@r15, r15
	tst.b	r15
	jz		.Lout
	inc		2(r1)
	jmp		.Lcount

.Lout:
	dec		2(r1)

.Lswap:
	cmp		2(r1), 0(r1)
	jc		.Lexit

	mov		0(r1), r15
	mov.b	@r15, 4(r1)
	mov		2(r1), r15
	mov.b	@r15, 5(r1)

	mov		0(r1), r15
	mov.b	5(r1), @r15
	mov		2(r1), r15
	mov.b	4(r1), @r15

	inc		0(r1)
	dec		2(r1)
	jmp		.Lswap

.Lexit:
	add		#6, r1
	mov.b	#0, &P1OUT
	ret

