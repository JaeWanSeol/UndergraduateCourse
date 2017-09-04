#include <stdio.h>

void sum_rows2(double *a, double *b, long n) {
	long i, j;

	for(i=0 ; i<n ; i++) {
		double val = 0;
		for(j=0 ; j<n ; j++) 
			val += a[i*n + j];
		b[i] = val;
	}

	for(int i = 0 ; i < 9 ; i++) 
		printf("%lf ", a[i]);
	printf("\n");
	printf("\n");
	for(int i = 0 ; i < 3 ; i++) 
		printf("%lf ", b[i]);
	printf("\n");

};

void sum_rows1(double *a, double *b, long n) {
	long i,j;

	for(int i = 0 ; i < n ; i++) {
		b[i] = 0;
		for(j = 0 ; j < n ; j++) {
			b[i] += a[i*n + j];
		}

	}
	for(int i = 0 ; i < 9 ; i++) 
		printf("%lf ", a[i]);
	printf("\n");
	printf("\n");
	for(int i = 0 ; i < 3 ; i++) 
		printf("%lf ", b[i]);
	printf("\n");


}

int main() {

	double a[9] = { 0, 1, 2, 4, 8, 16, 32, 64, 128 };
	double *b = a+3;

	sum_rows2(a, b, 3);
	
	sum_rows1(a, b, 3);
	return 0;
}
