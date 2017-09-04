#include <stdio.h>
#include <stdlib.h>
#include <time.h>


int main() {
	int idx = 0;
	srand(time(NULL));
	
	for(idx = 0 ; idx < 10000000 ; idx++) {
		printf("%c", (rand() % 26) + 'a');
	}

	return 0;
}

