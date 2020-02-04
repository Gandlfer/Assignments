	.file "mod.S"
	.text
.global mod
mod:
	call #divd
	mov r12,r15
	ret

