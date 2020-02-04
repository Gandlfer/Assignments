	.file "func/powr.S"
	.text
.global powr
powr:
	mov r15,r11
loop:
	mov r11,r12
	mov r15,r13
	cmp #1,r14
	jl neg
	jeq leave
	sub #1,r14
loop2:
	cmp #1,r12
	jeq loop
	add r13,r15
	sub #1,r12
	jmp loop2

neg:
	cmp #0, r14
	jeq zero
	mov #0, r15
	jmp leave

zero:
	mov #1, r15

leave:
	ret

