	.file "func/divd.S"
	.text
.global divd
divd:
	mov #0,r11 ; check if ending needs N
	mov #0,r12 ; remainder 
	mov #0,r13 ; r13=count
	cmp #0,r15 ;checking for N r15
	jl r15N
	jeq zero

checking:
	
	cmp #0,r14 ;checking for N r14
	jl r14N
	jeq zero

loop:
	cmp r14,r15
	jl answer
	sub r14,r15
	add #1,r13
	jmp loop

answer:
	mov r15, r12 ;r12= remainding value
	mov r13,r15
	cmp #0,r11
	jeq leave
	inv r15
	inc r15
		
leave:
	ret

r15N:
	inv r15
	inc r15
	xor #1,r11
	jmp checking
	
r14N:
	inv r14
	inc r14
	xor #1,r11
	jmp loop

zero:
	mov #0,r15
	jmp leave