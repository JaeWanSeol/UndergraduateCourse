/*
 * mm-naive.c - The fastest, least memory-efficient malloc package.
 * 
 * In this naive approach, a block is allocated by simply incrementing
 * the brk pointer.  A block is pure payload. There are no headers or
 * footers.  Blocks are never coalesced or reused. Realloc is
 * implemented directly using mm_malloc and mm_free.
 *
 * NOTE TO STUDENTS: Replace this header comment with your own header
 * comment that gives a high level description of your solution.
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <string.h>

#include "mm.h"
#include "memlib.h"

/*********************************************************
 * NOTE TO STUDENTS: Before you do anything else, please
 * provide your team information in the following struct.
 ********************************************************/
team_t team = {
    /* Team name */
    "ateam",
    /* First member's full name */
    "JaeWan Seol",
    /* First member's email address */
    "bmy4415@naver.com",
    /* Second member's full name (leave blank if none) */
    "",
    /* Second member's email address (leave blank if none) */
    ""
};

/* single word (4) or double word (8) alignment */
#define ALIGNMENT 8

/* rounds up to the nearest multiple of ALIGNMENT */
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~0x7)


#define SIZE_T_SIZE (ALIGN(sizeof(size_t)))

#define LISTLIMIT 20
#define REALLOC_BUFFER (1<<7)

#define WSIZE 4
#define DSIZE 8
#define INITCHUNKSIZE (1<<6)
#define CHUNKSIZE (1<<14)

#define MAX(x , y) ((x) > (y) ? (x) : (y))
#define MIN(x , y) ((x) < (y) ? (x) : (y))

#define PACK(size , alloc) ((size) | (alloc))

#define GET(p) 				(*(unsigned int *) (p))
#define PUT(p , val) 	(*(unsigned int *) (p) = (val))


#define GET_SIZE(p)		(GET(p) & ~0x7)
#define GET_ALLOC(p)	(GET(p) & 0x1)

#define HDRP(bp)				((char *)(bp) - WSIZE)
#define FTRP(bp)				((char *)(bp) + GET_SIZE(HDRP(bp)) - DSIZE)

#define NEXT_BLKP(bp)		((char *)(bp) + GET_SIZE(((char *)(bp) - WSIZE)))
#define PREV_BLKP(bp)		((char *)(bp) - GET_SIZE(((char *)(bp) - DSIZE)))

#define NEXT_FREE(bp)		((char *)(bp))
#define PREV_FREE(bp)		((char *)(bp) + WSIZE)

#define SET(p , ptr) (*(unsigned int *)(p) = (unsigned int)(ptr))

static void *extend_heap(size_t words);
static void *coalesce(void *bp);
static void *find_fit(size_t asize);
static void place(void *bp, size_t asize);
static void insert(void *bp);
static void delete(void *bp);
static void print_freelist(void *p);

int rqcnt = 0;
char *heap_listp;
char *free_listp = NULL;

static void print_freelist(void *p) {
	printf("start is free_listp\n");
	//printf("free_listp is %x\n", free_listp);
	while(p != NULL) {
		//printf("free_listp is in while %x\n", free_listp);
		printf("RQ   %d\n", rqcnt);
		printf("HD   %x\n", HDRP(p));
		printf("FT   %x\n", FTRP(p));
		printf("SIZE %d\n", GET_SIZE(HDRP(p)));
		printf("NEXT %x\n", *(int*)(NEXT_FREE(p)));
		printf("PREV %x\n", *(int*)(PREV_FREE(p)));
		p = *(int*)(NEXT_FREE(p));

	}
	printf("end of freelist\n");
}



static void insert(void *bp) {
	//printf("start of insert\n");
	//printf("bp is %x\n", bp);
	//printf("HD is %x\n", HDRP(bp));
	//printf("FT is %x\n", FTRP(bp));
	//printf("SZ is %d\n", GET_SIZE(HDRP(bp)));
	//printf("NEXT %x\n", *(int*)(NEXT_FREE(bp)));
	//printf("PREV %x\n", *(int*)(PREV_FREE(bp)));

	if(free_listp == NULL) {
		//printf("free is NULL\n");
		SET(NEXT_FREE(bp) , NULL);
		SET(PREV_FREE(bp) , NULL);
		free_listp = bp;
	return;	
	}		

	//printf("xxxxxxxxxxxxxxxxxxx\n");
	//printf("free p is %x\n", free_listp);
	SET(NEXT_FREE(bp) , free_listp);
	//printf("bp is %x\n", bp);
	//printf("NEXT_FREE(bp) is %x\n", NEXT_FREE(bp));
	//printf("*NEXT_FREE(bp) is %x\n", *((int*)bp));
	//printf("yyyyyyyyyyyyyyyyyy\n");
	SET(PREV_FREE(bp) , NULL);
	//printf("zzzzzzzzzzzzzzzz\n");
	
	//NEXT_FREE(bp) = free_listp;
	//PREV_FREE(bp) = NULL;
	
	SET(PREV_FREE(free_listp) , bp);
	//printf("kkkkkkkkkkkkkk\n");

	free_listp = bp;
	//printf("end of insert\n");
	//printf("in insert, free_listp is %x\n", free_listp);
}

static void delete(void *bp) {
	//printf("RQ = %d\n");
	//printf("start of delete\n");
	//printf("bp is %x\n", bp);
	//printf("HD is %x\n", HDRP(bp));
	//printf("FT is %x\n", FTRP(bp));
	//printf("SZ is %d\n", GET_SIZE(HDRP(bp)));
	
	//printf("NEXT %x\n", *(int*)(NEXT_FREE(bp)));
	//printf("PREV %x\n", *(int*)(PREV_FREE(bp)));

	

	//prev of bp does not exist
	if(*(int*)(PREV_FREE(bp)) == NULL) {
		

		//printf("a\n");
		free_listp = *((int*)NEXT_FREE(bp));
		//printf("%x\n", *((int*)NEXT_FREE(bp)));
		//printf("x\n");
		if(*((int*)NEXT_FREE(bp)) != NULL){
			SET(PREV_FREE(*((int*)NEXT_FREE(bp))) , NULL);
			SET(NEXT_FREE(bp) , NULL);
		}
		//printf("y\n");
	}
	//prev of bp exist
	else {
		SET(NEXT_FREE(*(int*)(PREV_FREE(bp))) , *((int*)(NEXT_FREE(bp))));	
		
		//printf("qqqqqqqqq\n");
		//next of bp exist
		if(*(int *)(NEXT_FREE(bp)) != NULL) {

			SET(PREV_FREE(*(int*)(NEXT_FREE(bp))) , *((int*)(PREV_FREE(bp))));		
			//printf("rrrrrrrr\n");
		}

		SET(NEXT_FREE(bp), NULL);		
		//printf("ttttttt\n");

		SET(PREV_FREE(bp), NULL);		
		//printf("lllllllll\n");
		
	}
}





static void *extend_heap(size_t words) {
	//printf("start of extend heap RQ = %d\n", rqcnt);
	char *bp;
	size_t size;

	size = (words % 2) ? (words + 1) * WSIZE : words * WSIZE;
		

	if((long)(bp = mem_sbrk(size)) == -1)
		return NULL;
	//printf("in extend heap, bp is %x\n", bp);
	PUT(HDRP(bp) , PACK(size , 0));
	PUT(FTRP(bp) , PACK(size , 0));
	PUT(HDRP(NEXT_BLKP(bp)) , PACK(0 , 1));
	
	//printf("before coalesce in extend RQ = %d in extend, size is %d\n", rqcnt, GET_SIZE(HDRP(bp)));
	return coalesce(bp);
}

static void *coalesce(void *bp) {
	//printf("start of coalesce\n");
	size_t prev_alloc = GET_ALLOC(FTRP(PREV_BLKP(bp)));
	size_t next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(bp)));
	size_t size = GET_SIZE(HDRP(bp));


	if(prev_alloc && next_alloc) {
	}

	else if(prev_alloc && !next_alloc) {
		size += GET_SIZE(HDRP(NEXT_BLKP(bp)));
		if(GET_SIZE(HDRP(NEXT_BLKP(bp))) != DSIZE){
			delete(NEXT_BLKP(bp));
		}
		PUT(HDRP(bp) , PACK(size , 0));
		PUT(FTRP(bp) , PACK(size , 0));

	}

	else if(!prev_alloc && next_alloc) {
		size += GET_SIZE(HDRP(PREV_BLKP(bp)));
		if(GET_SIZE(HDRP(PREV_BLKP(bp))) != DSIZE){
			delete(PREV_BLKP(bp));
		}
		bp = PREV_BLKP(bp);
		PUT(HDRP(bp) , PACK(size , 0));
		PUT(FTRP(bp) , PACK(size , 0));
	}

	else {
	
		size += GET_SIZE(HDRP(PREV_BLKP(bp))) + GET_SIZE(FTRP(NEXT_BLKP(bp)));
		if(GET_SIZE(HDRP(NEXT_BLKP(bp))) != DSIZE){
			delete(NEXT_BLKP(bp));
		}
	
		if(GET_SIZE(HDRP(PREV_BLKP(bp))) != DSIZE){
			delete(PREV_BLKP(bp));
		}
	
		bp = PREV_BLKP(bp);
		PUT(HDRP(bp) , PACK(size , 0));
		PUT(FTRP(bp) , PACK(size , 0));
	}

	insert(bp);
	return bp;

}

static void *find_fit(size_t asize) {
	void *bp;

	if(free_listp == NULL) return NULL;
	for(bp = free_listp ; (bp != NULL) && (GET_SIZE(HDRP(bp))  > 0) ; bp = *(int *)(NEXT_FREE(bp))) {
		if(!GET_ALLOC(HDRP(bp)) && (asize <= GET_SIZE(HDRP(bp)))) {
			return bp;
		}
	}

	return NULL;

}

static void place(void *bp, size_t asize) {
	size_t csize = GET_SIZE(HDRP(bp));

	if((csize - asize) >= (2 * DSIZE)) {
		//printf("1\n");
		PUT(HDRP(bp) , PACK(asize , 1));

		//printf("bte pl bp %x\n", bp);
		//printf("btw pl HD %x\n", HDRP(bp));
		//printf("btw pl FT %x\n", FTRP(bp));
		//printf("bte pl SZ %d\n", GET_SIZE(HDRP(bp)));

		PUT(FTRP(bp) , PACK(asize , 1));
		//printf("split in place\n");
		//printf("%dth HD : %x\n", rqcnt, HDRP(bp));
		//printf("%dth FT : %x\n", rqcnt, FTRP(bp));
		//printf("allo bit : %d , size : %d\n", GET_ALLOC(HDRP(bp)), GET_SIZE(HDRP(bp)));
		//printf("2\n");

		delete(bp);
		//printf("3\n");

		bp = NEXT_BLKP(bp);
		PUT(HDRP(bp) , PACK(csize - asize , 0));
		PUT(FTRP(bp) , PACK(csize - asize , 0));
		//printf("4\n");
		//printf("in place\n");
		//printf("bp is %x\n", bp);
		//printf("SZ is %d\n", GET_SIZE(HDRP(bp)));
		//printf("in place\n");
		//printf("allo bit : %d , size : %d\n", GET_ALLOC(HDRP(bp)), GET_SIZE(HDRP(bp)));
		//printf("5\n");
		insert(bp);

	}

	else if((csize - asize == 0)) {		
		PUT(HDRP(bp) , PACK(asize , 1));
		PUT(FTRP(bp) , PACK(asize , 1));
		
		delete(bp);
		
		//printf("NO split in place\n");
		//printf("%dth HD : %x\n", rqcnt, HDRP(bp));
		//printf("%dth FT : %x\n", rqcnt, FTRP(bp));
	}

	else {
		PUT(HDRP(bp) , PACK(asize , 1));
		PUT(FTRP(bp) , PACK(asize , 1));
		PUT(HDRP(NEXT_BLKP(bp)), PACK(8 , 0));
		PUT(FTRP(NEXT_BLKP(bp)) , PACK(8 , 0));

		delete(bp);
	
	}
}

/* 
 * mm_init - initialize the malloc package.
 */
int mm_init(void)
{
	if((heap_listp = mem_sbrk(4 * WSIZE)) == (void *)-1)
		return -1;

	PUT(heap_listp, 0);
	PUT(heap_listp + (1 * WSIZE), PACK(DSIZE , 1));
	PUT(heap_listp + (2 * WSIZE), PACK(DSIZE , 1));
	PUT(heap_listp + (3 * WSIZE), PACK(0 , 1));
	heap_listp += (2 * WSIZE);

	free_listp = NULL;

	if(extend_heap(CHUNKSIZE / WSIZE) == NULL)
		return -1;
	
	//print_freelist(free_listp);

	return 0;
}

/* 
 * mm_malloc - Allocate a block by incrementing the brk pointer.
 *     Always allocate a block whose size is a multiple of the alignment.
 */
void *mm_malloc(size_t size)
{
	size_t asize;
	size_t extendsize;
	char *bp;
	
	rqcnt++;
	//printf("%dth request malloc\n", rqcnt);
	

	if(size == 0)
		return NULL;

	if(size <= DSIZE)
		asize = 2 * DSIZE;
	else
		asize = DSIZE * ((size + (DSIZE) + (DSIZE-1)) / DSIZE);
	
	if((bp = find_fit(asize)) != NULL) {
		//printf("1111111111\n");
		place(bp, asize);
		//printf("%dth end of malloc\n", rqcnt);
		return bp;
	}
	//printf("2222222222\n");
	//printf("allocable block is not found, extend heap\n");
	extendsize = MAX(asize, CHUNKSIZE);
	if((bp = extend_heap(extendsize / WSIZE)) == NULL)
		return NULL;
	//printf("3333333\n");
	place(bp, asize);
	//printf("444444444\n");
	//printf("%dth end of malloc\n", rqcnt);
	//print_freelist(free_listp);
	return bp;
}

/*
 * mm_free - Freeing a block does nothing.
 */
void mm_free(void *ptr)
{

	rqcnt++;
	//printf("%dth request free\n", rqcnt);
	size_t size = GET_SIZE(HDRP(ptr));

	PUT(HDRP(ptr) , PACK(size , 0));
	PUT(FTRP(ptr) , PACK(size , 0));
	coalesce(ptr);

	//printf("%dth free end\n", rqcnt);
	//printf("%dth end of free\n", rqcnt);
	//print_freelist(free_listp);
}

/*
 * mm_realloc - Implemented simply in terms of mm_malloc and mm_free
 */
void *mm_realloc(void *ptr, size_t size)
{
 		
		char *oldbp = ptr;
		size_t prev_size = GET_SIZE(HDRP(PREV_BLKP(oldbp)));
		size_t next_size = GET_SIZE(HDRP(NEXT_BLKP(oldbp)));
		size_t curr_size = GET_SIZE(HDRP(oldbp));
		size_t sumsize;
		size_t allo_size;
		size_t prev_alloc = GET_ALLOC(FTRP(PREV_BLKP(oldbp)));
		size_t next_alloc = GET_ALLOC(HDRP(NEXT_BLKP(oldbp)));
		if(size <= DSIZE)
			allo_size = 2 * DSIZE;
		else
			allo_size = DSIZE * ((size + (DSIZE) + (DSIZE-1)) / DSIZE);
	
		if(size == 0) {
			mm_free(ptr);
			return;
		}
		
		if(ptr == NULL) {
			return mm_malloc(size);
		}

		if(GET_SIZE(HDRP(oldbp)) >= allo_size) {
			return oldbp;
		}
				
		if(prev_alloc && next_alloc) {
			char *newbp = mm_malloc(size);
			memmove(newbp, oldbp, curr_size);
			mm_free(oldbp);
			return newbp;
		}

		else if(!prev_alloc && next_alloc) {
			sumsize = prev_size + curr_size;
			if(sumsize >= allo_size) {
				char *newbp = PREV_BLKP(oldbp);
				delete(newbp);
				memmove(newbp, oldbp, curr_size);
				PUT(HDRP(newbp) , PACK(sumsize , 1));
				PUT(FTRP(newbp) , PACK(sumsize , 1));
				return newbp;
			}
			char *newbp = mm_malloc(size);
			memmove(newbp, oldbp, curr_size);
			mm_free(oldbp);
			return newbp;

		}

		else if(prev_alloc && !next_alloc) {
			sumsize = next_size + curr_size;
			if(sumsize >= allo_size) {
				delete(NEXT_BLKP(oldbp));
				PUT(HDRP(oldbp) , PACK(sumsize , 1));
				PUT(FTRP(oldbp) , PACK(sumsize , 1));
				return oldbp;
			}
			char *newbp = mm_malloc(size);
			memmove(newbp, oldbp, curr_size);
			mm_free(oldbp);
			return newbp;

		}

		else {
			sumsize = prev_size + next_size + curr_size;
			if(sumsize >= allo_size) {
				char *newbp = PREV_BLKP(oldbp);
				delete(newbp);
				delete(NEXT_BLKP(oldbp));
				memmove(newbp, oldbp, allo_size);
				PUT(HDRP(newbp) , PACK(sumsize , 1));
				PUT(FTRP(newbp) , PACK(sumsize , 1));
				return newbp;
			}
			char *newbp = mm_malloc(size);
			memmove(newbp, oldbp, curr_size);
			mm_free(oldbp);
			return newbp;

		}
				
				
		/* 
		void *oldptr = ptr;
    void *newptr;
    size_t copySize;
    
    newptr = mm_malloc(size);
    if (newptr == NULL)
      return NULL;
    copySize = *(size_t *)((char *)oldptr - SIZE_T_SIZE);
    if (size < copySize)
      copySize = size;
    memcpy(newptr, oldptr, copySize);
    mm_free(oldptr);
    return newptr;
		*/
}














