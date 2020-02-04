	.file "func/mul.S"
	.text
.global mul
mul:
	mov r15,r13

loop:
	cmp #1,r14
	jeq leave

	add r13,r15
	sub #1,r14
	jmp loop

leave:
	ret

