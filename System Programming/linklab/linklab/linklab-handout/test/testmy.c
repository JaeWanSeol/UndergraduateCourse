#include <stdlib.h>

int main(void) {
	void *a;
	void *b;
	a = malloc(10);
	a = malloc(100);
	a = realloc(a, 200);
	b = calloc(25,1);
	
	free(a);
	free(b);
	free(a);
	free(b);
	free((void*)0x10);

	return 0;
}

