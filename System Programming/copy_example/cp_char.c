#include <unistd.h>
#include <fcntl.h>

#define BUF_SIZE 1

int main() {
	char buf[BUF_SIZE];
	int fd1, fd2;

	ssize_t rd_size;
	
	
	fd2 = open("./ret1.txt", O_WRONLY);
	if( 0 < (fd1 = open("./src.txt", O_RDONLY))) {
		while( 0 < (rd_size = read(fd1, buf, BUF_SIZE))) {
			write(fd2, buf, BUF_SIZE);
		}
	}
	close(fd1);
	close(fd2);

	return 0;
}

