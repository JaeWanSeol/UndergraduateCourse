/* 
 * CS:APP Data Lab 
 * 
 * <Please put your name and userid here>
 * JaeWanSeol from Dept of Coumputer Sceience Engneering 2012-11249 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:
 
  Replace the "return" statement in each function with one
  or more lines of C code that implements the function. Your code 
  must conform to the following style:
 
  int Funct(arg1, arg2, ...) {
      /* brief description of how your implementation works */
      int var1 = Expr1;
      ...
      int varM = ExprM;

      varJ = ExprJ;
      ...
      varN = ExprN;
      return ExprR;
  }

  Each "Expr" is an expression using ONLY the following:
  1. Integer constants 0 through 255 (0xFF), inclusive. You are
      not allowed to use big constants such as 0xffffffff.
  2. Function arguments and local variables (no global variables).
  3. Unary integer operations ! ~
  4. Binary integer operations & ^ | + << >>
    
  Some of the problems restrict the set of allowed operators even further.
  Each "Expr" may consist of multiple operators. You are not restricted to
  one operator per line.

  You are expressly forbidden to:
  1. Use any control constructs such as if, do, while, for, switch, etc.
  2. Define or use any macros.
  3. Define any additional functions in this file.
  4. Call any functions.
  5. Use any other operations, such as &&, ||, -, or ?:
  6. Use any form of casting.
  7. Use any data type other than int.  This implies that you
     cannot use arrays, structs, or unions.

 
  You may assume that your machine:
  1. Uses 2s complement, 32-bit representations of integers.
  2. Performs right shifts arithmetically.
  3. Has unpredictable behavior when shifting an integer by more
     than the word size.

EXAMPLES OF ACCEPTABLE CODING STYLE:
  /*
   * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
   */
  int pow2plus1(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     return (1 << x) + 1;
  }

  /*
   * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
   */
  int pow2plus4(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     int result = (1 << x);
     result += 4;
     return result;
  }

FLOATING POINT CODING RULES

For the problems that require you to implent floating-point operations,
the coding rules are less strict.  You are allowed to use looping and
conditional control.  You are allowed to use both ints and unsigneds.
You can use arbitrary integer and unsigned constants.

You are expressly forbidden to:
  1. Define or use any macros.
  2. Define any additional functions in this file.
  3. Call any functions.
  4. Use any form of casting.
  5. Use any data type other than int or unsigned.  This means that you
     cannot use arrays, structs, or unions.
  6. Use any floating point data types, operations, or constants.


NOTES:
  1. Use the dlc (data lab checker) compiler (described in the handout) to 
     check the legality of your solutions.
  2. Each function has a maximum number of operators (! ~ & ^ | + << >>)
     that you are allowed to use for your implementation of the function. 
     The max operator count is checked by dlc. Note that '=' is not 
     counted; you may use as many of these as you want without penalty.
  3. Use the btest test harness to check your functions for correctness.
  4. Use the BDD checker to formally verify your functions
  5. The maximum number of ops for each function is given in the
     header comment for each function. If there are any inconsistencies 
     between the maximum ops in the writeup and in this file, consider
     this file the authoritative source.

/*
 * STEP 2: Modify the following functions according the coding rules.
 * 
 *   IMPORTANT. TO AVOID GRADING SURPRISES:
 *   1. Use the dlc compiler to check that your solutions conform
 *      to the coding rules.
 *   2. Use the BDD checker to formally verify that your solutions produce 
 *      the correct answers.
 */


#endif
/* Copyright (C) 1991-2016 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */
/* This header is separate from features.h so that the compiler can
   include it implicitly at the start of every compilation.  It must
   not itself include <features.h> or any other header that includes
   <features.h> because the implicit include comes before any feature
   test macros that may be defined in a source file before it first
   explicitly includes a system header.  GCC knows the name of this
   header in order to preinclude it.  */
/* glibc's intent is to support the IEC 559 math functionality, real
   and complex.  If the GCC (4.9 and later) predefined macros
   specifying compiler intent are available, use them to determine
   whether the overall intent is to support these features; otherwise,
   presume an older compiler has intent to support these features and
   define these macros by default.  */
/* wchar_t uses Unicode 8.0.0.  Version 8.0 of the Unicode Standard is
   synchronized with ISO/IEC 10646:2014, plus Amendment 1 (published
   2015-05-15).  */
/* We do not support C11 <threads.h>.  */
/* 
 * bitAnd - x&y using only ~ and | 
 *   Example: bitAnd(6, 5) = 4
 *   Legal ops: ~ |
 *   Max ops: 8
 *   Rating: 1
 */
// using D'morgan's law
int bitAnd(int x, int y) {
  
  return ~(~x|~y);
}
/* 
 * getByte - Extract byte n from word x
 *   Bytes numbered from 0 (LSB) to 3 (MSB)
 *   Examples: getByte(0x12345678,1) = 0x56
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2
 */
// 1byte means 8bit and 1 16's number is 4bit so we can extract 8bits by extracting 2 16's number
int getByte(int x, int n) {
  int temp = 0xff;
  x = x >> (n << 3);
  //temp = temp << (n << 3);
  // printf("x is %x\n", x);
  //printf("n is %d\n", n);
  //printf("temp is %x\n", temp);
  //printf("x&temp is %x\n", x&temp);
  
  return x&temp;
}
/* 
 * logicalShift - shift x to the right by n, using a logical shift
 *   Can assume that 0 <= n <= 31
 *   Examples: logicalShift(0x87654321,4) = 0x08765432
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 20
 *   Rating: 3 
 */
//do arithmetic right shift and remove bits triggered by MSB
int logicalShift(int x, int n) {
  int temp = x >> n;
  int helper = (1<<31)>>n;
  helper = helper << 1;
  helper = ~helper;
  
  return temp&helper;
}
/* 
 * bang - Compute !x without using !
 *   Examples: bang(3) = 0, bang(0) = 1
 *   Legal ops: ~ & ^ | + << >>
 *   Max ops: 12
 *   Rating: 4 
 */
//(MSB of x) | (MSB of -x) is 1 iff x in not 0 also we can reduce 1 operator by using xor(^)
int bang(int x) {
  //int MSBofminusx = ((~x+1)>>31)&1;
  //int MSBofx = x >> 31;

 return ((x | (~x+1)) >> 31) + 1;
}
/* howManyBits - return the minimum number of bits required to represent x in
 *             two's complement
 *  Examples: howManyBits(12) = 5
 *            howManyBits(298) = 10
 *            howManyBits(-5) = 4
 *            howManyBits(0)  = 1
 *            howManyBits(-1) = 1
 *            howManyBits(0x80000000) = 32
 *  Legal ops: ! ~ & ^ | + << >>
 *  Max ops: 90
 *  Rating: 4
 */
//we can know how many bits we need by doing this
//if x's MSB is 0, find the bit 1 that lies leftmost of x
//else(MSB is 1), find the bit 1 that lies leftmost of (~x)
//we can implement if by using (s<<31)>>31 when s is 1bit boolean
int howManyBits(int x) {
  int temp, result, n1, n2, n3, n4, n5;
  int cst;
  int sign = (x>>31); //sign = 0xffffffff if sign is 1, 0x0 if sign is 0
  temp = x ^ sign; //temp = x if x's MSB is 0, ~x if x's MSB is 1
  //now we should find the leftmost1 by 'bit smearing
  temp = temp | (temp >> 16);
  temp = temp | (temp >> 8);
  temp = temp | (temp >> 4);
  temp = temp | (temp >> 2);
  temp = temp | (temp >> 1);
  
  //temp = temp ^ (temp >> 1);
  n1 = 0x55; n1 |= n1<<8; n1 |= n1<<16; //n1 |= n1<<8
  n2 = 0x33; n2 |= n2<<8; n2 |= n2<<16; //n2 |= n2<<8;
  n3 = 0x0f; n3 |= n3<<8; n3 |= n3<<16; //n3 |= n3<<8;
  //n4 = 0x10; n4 |= n4<<8; n4 |= n4<<8; n4 |= n4<<8;
  n4 = 0xff; n4 |= n4<<16;
  n5 = 0xff; n5 |= n5<<8;
  
  result = temp + (~((temp>>1) & n1) + 1);
  result = ((result>>2)&n2) + (result&n2);
  result = ((result>>4) + result) & n3;
  result = ((result>>8) + result) & n4;
  result = ((result>>16) + result) & n5;
  
  //cst = 0x01;
  //cst = cst << 8;
  //cst = cst << 16;
  //result = (result & n2) + ((result >> 2) & n2);
  //result = ((result + (result >> 4) & n3) * cst) >> 24;
  /*temp = temp + ~(temp ((v>>1) & n1)) + 1
  temp = (temp&n2) + ((temp>>2) & n2);
  temp + ((temp>>4) & n3)*/
  return result + 1;
}
/* 
 * tmin - return minimum two's complement integer 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 4
 *   Rating: 1
 */
//tmin is 0x8000000
int tmin(void) {
  int temp = 0x80;
  temp = temp << 24;
  //return 0x80000000
  return temp;
}
/*
 * isTmax - returns 1 if x is the maximum, two's complement number,
 *     and 0 otherwise 
 *   Legal ops: ! ~ & ^ | +
 *   Max ops: 10
 *   Rating: 1
 */
// if x is 0x7fffffff return 1, else return 0
// use ! and xor
int isTmax(int x) {
  //int temp = ~(1<<31); // temp = 0x7fffffff
  //int temp = (x+1) ^ (~0);
  //return (!(temp ^ x)) & (!(x ^ (~0)));
  int temp = ~x;
  temp = temp + temp;
  return (!temp) & !(!(x ^ (~0)));
  //return !(x ^ temp);
}
/* 
 * divpwr2 - Compute x/(2^n), for 0 <= n <= 30
 *  Round toward zero
 *   Examples: divpwr2(15,1) = 7, divpwr2(-33,4) = -2
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 15
 *   Rating: 2
 */
// actually x / 2^n and (x + 2^n-1) / 2^n has same value
int divpwr2(int x, int n) {
  int flag = x>>31; //flag is 11...111 if x<0 , else 00...000
  int sign = (1<<n) + (~0); //sign = 2^n-1
  
  return ((flag&sign) + x) >> n;
}
/* 
 * isPositive - return 1 if x > 0, return 0 otherwise 
 *   Example: isPositive(-1) = 0.
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 3
 */
//if x > 0 than -x < 0 
//we can easily check x < 0 by sign bit
//sorry, above are true but we cannot consider tmin by using above
int isPositive(int x) {
  int temp = (x >> 31)+1; //0 if x<0 , 1 if >= 0;
  int temp2 = !x; //1 if x is 0 , else 0
  
  return temp ^ temp2;
}
/*
 * ilog2 - return floor(log base 2 of x), where x > 0
 *   Example: ilog2(16) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 90
 *   Rating: 4
 */
//this is much same as problem 5 so i will use that again(hamming weight)
int ilog2(int x) {
//first bit smearing
  int temp = x | (x >> 16);
  int n1, n2, n3, n4, n5;
  int result;
  temp = temp | (temp >> 8);
  temp = temp | (temp >> 4);
  temp = temp | (temp >> 2);
  temp = temp | (temp >> 1);
  //find # of 1s
  n1 = 0x55; n1 |= n1<<8; n1 |= n1<<16; //n1 |= n1<<8
  n2 = 0x33; n2 |= n2<<8; n2 |= n2<<16; //n2 |= n2<<8;
  n3 = 0x0f; n3 |= n3<<8; n3 |= n3<<16; //n3 |= n3<<8;
  //n4 = 0x10; n4 |= n4<<8; n4 |= n4<<8; n4 |= n4<<8;
  n4 = 0xff; n4 |= n4<<16;
  n5 = 0xff; n5 |= n5<<8;
  
  result = temp + (~((temp>>1) & n1) + 1);
  result = ((result>>2)&n2) + (result&n2);
  result = ((result>>4) + result) & n3;
  result = ((result>>8) + result) & n4;
  result = ((result>>16) + result) & n5;
  
  return result + (~0);
}

