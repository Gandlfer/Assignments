# Calculator

To practice writing assembly and passing parameters, you will implement the math functions of a calculator.

The `add.S` and `sub.S` files will be trivial to complete, but `mul.S` and `divd.S` will take some careful planning. You must either repeatedly add or repeatedly subtract (and count) the first input number by the second. `mod.S` might be trivial, but `pow.S` will be challenging.

When finished the calculator should be able to add `+`, subtract `-`, multiply `*`, divide `/`, modulo `%` and power `^`.

Here is an example session:

    > 2 + 3
    5
    > 5 - 1
    4
    > 1 - 5
    -4
    > -3 + 2
    -1
    > 6 / 3
    2
    > 7 / 3
    2
    > 5 / 3
    1
    > -5 / 3
    -1
    > -6 / -3
    2
    > -6 * -3
    18
    > 6 * 3
    18
    > 3 ^ 2
    9
    > 2 ^ 4
    16
    > 2 ^ -4
    0
    > 7 % 3
    1

1. You should strive to complete this functionality in as _few instructions as possible_.
1. Only write your solution in the provided assembly files (inside the `func/` directory).
1. Use _repeated addition_ to calculate the result of multiplication. You will need to construct a loop yourself!
1. Use _repeated subtraction_ to calculate the quotient. Again, you will need to construct a loop yourself!
1. Do not simply generate assembly using the compiler and submit it. **I will know**.
1. Only worry about negative numbers for add `+`, subtract `-`, multiply `*` and divide `/`.

### Parameter Passing

Parameters are passed to functions in the registers `r15`, `r14`, `r13` and `r12` in that order (something special happens if there are more than 4 arguments to a function). The functions you are implementing are receiving the first number (left of operator) in `r15` and the second number (right of operator) in `r14`. Remember, when the microcontroller executes the `ret` instruction at the end of a function, the return value should be in `r15`!

### Assembly Instructions

Here are some useful instructions you might need:

```
mov     src, dst    ; move the contents of src to dst
sub     src, dst    ; subtract the value in src from dst and store the result in dst
add     src, dst    ; add the value in src to the value in dst and store the result in dst
clr     dst         ; zero out destination (same as mov #0, dst)
cmp     a, b        ; make the comparison: b [<, >=, ==, !=] a
jl      label       ; jump to label if b < a
jge     label       ; jump to label if b >= a
jeq     label       ; jump to label if b == a
jne     label       ; jump to label if b != a
jn      label       ; jump to label if N is set (result of previous instruction was negative)
jmp     label       ; jump to label unconditionally (always)
```

A more complete listing can be found on page 56 of the [Family User's Guide](http://www.ti.com/lit/ug/slau144j/slau144j.pdf).

### Registers

You can use any of the registers `r4 - r15`, however do _not_ use registers `r0 - r3` for _anything_ in your function. These registers serve special purposes on the **MSP430**.

