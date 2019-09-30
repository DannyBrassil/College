//danny brassil
//c17351576

#define _CRT_SECURE_NO_WARNINGS
#define bool int
#define false 0
#define true (!false)
#include <stdio.h>
#include <stdlib.h>
#define SIZE 10

// Function prototypes
void menu();
void addBook();
void borrowBook();
void returnBook();
void deleteBook(char *);
void viewAllBooks();
void viewSpecificBook(char *);
void popularBooks();
void saveToFile(FILE *);
void getFromFile(FILE *);
bool checkID(char *);
bool isEmpty();

FILE *fp; //file pointer

int numBooks = 0;
//structure for a book
struct Book {
	char identifier[10];
	char name[30];
	char author[30];
	int year;
	bool loaned;
	char customerName[30];
	int timesLoaned;
};

struct LinearNode {
	struct Book *element;
	struct LinearNode *next;
};
//global variables
struct LinearNode *front = NULL;
struct LinearNode *last = NULL;


// menu option 
int main() {


	if ((fp = fopen("Book.dat", "r")) == NULL) {

		//file DOES NOT exist so get user to input students
		printf("there are no books in the library, please input books by selecting option (1)\n");
		printf("\n");
	}//end if
	else  //file DOES exist
		getFromFile(fp);

	menu();
	return(0);
	getchar();
	getchar();

}
void menu()
{

	int opt;
	printf("-------LIBRARY MENU--------\n");
	printf("(1) add new book to library \n(2) allow customer to take out a book \n(3) allow customer to return a book\n");
	printf("(4) delete an old book from stock \n(5) view all books \n(6) view a specific book\n");
	printf("(7) view most popular and least popular books \n(8) exit system");
	printf("\n---------------------------");
	printf("\n");
	scanf("%d", &opt);

	if (opt == 1) {
		addBook();
	}
	else if (opt == 2) {
		borrowBook();
	}
	else if (opt == 3) {
		returnBook();
	}
	else if (opt == 4) {
		char del[10];
		printf("/nPlease enter the identifier of the book you wish to delete : ");
		scanf("%s", del);
		deleteBook(del);
	}
	else if (opt == 5) {
		viewAllBooks();
	}
	else if (opt == 6) {
		char index[10];
		printf("\n\nPlease enter the identifier of the book you wish to search for : \n");
		scanf("%s", index);
		viewSpecificBook(index);
	}
	else if (opt == 7) {
		popularBooks();
	}
	else {
		printf("saving data to file...");
		saveToFile(fp);
		printf("/npress enter to exit the system");
		return(0);
	}

	getchar();
	getchar();

}


// add a Book to linked list
void addBook() {
	bool id = false;//check if id is in right format

	struct LinearNode *aNode;
	struct Book *aBook;

	if (numBooks >= SIZE)
		printf("\n***Too many books are in the libaray, try deleting some***\n");
	else {

		aBook = (struct data *)malloc(sizeof(struct Book));

		if (aBook == NULL)
			printf("Error - no space for the new book\n");
		else
		{
			printf("\n");
			printf("To add a book to the libaray please input the details below");
			printf("\n---------------------");
			printf("\n");

			//check if the format of the book id is correct
			//didnt seem to work so i have commented it out

	//	while(id == false)
	//	{
			printf("- Enter book identifier: ");
			scanf("%s", aBook->identifier);
		
			getchar();
			printf("- Enter name of book: ");
			fgets(aBook->name, 30, stdin);
			printf("- Enter author name: ");
			fgets(aBook->author, 30, stdin);
			//check if book is older than 2008
			aBook->year = 0;
			while (aBook->year <= 2008) {
				printf("- Enter Year of publication: ");
				scanf("%d", &aBook->year);
				if (aBook->year <= 2008) {
					printf("*** the book must not be published before 2008 ***\n");
				}
			}
			

			aBook->loaned = false;
			aBook->customerName == NULL;
			aBook->timesLoaned = 0;
			printf("---------------------------\n");
			printf("\n");


			aNode = (struct LinearNode *)malloc(sizeof(struct LinearNode));

			if (aNode == NULL)
				printf("Error - no space for the new node\n");
			else { // add data part to the node
				aNode->element = aBook;
				aNode->next = NULL;

				//add node to end of the list
				if (isEmpty())
				{
					front = aNode;
					last = aNode;
				}
				else {
					last->next = aNode;
					last = aNode;
				} //end else
			}//end else
		}//end else
	//}//end for
		numBooks++; //update how many books there is
		menu();
	}//end else

}

void borrowBook() {
	struct LinearNode *current, *previous;
	bool found = false;
	char id[10];

	printf("Please enter the identifier of the book you wish to borrow: \n");
	scanf("%s", id);

	if (isEmpty())
		printf("Error - there are no books in the library\n\n");
	else {
		current = previous = front;

		while (!found && current != NULL) {
			if (strcmp(id, current->element->identifier) == 0) {// string compare
				found = true;
			}
			else {
				current = current->next;
			}

		} //end while
		//check if book with id exists and if so is not aleady being loaned to somebody
		if ((found) && (current->element->loaned == false)) {
			current->element->loaned = true;
			getchar();
			printf("Please enter the name of customer\n");
			fgets(current->element->customerName, 30, stdin); // update the customer name when they borrow a book
			current->element->timesLoaned = current->element->timesLoaned++;

		}
		else {
			printf("book with this id is aleady on loan\n\n");
		}
		main();
	}// end else
}

void returnBook() {
	struct LinearNode *current, *previous;
	bool found = false;
	char id[10];
	char custName[30];

	printf("Please enter the identifier of the book you wish to return: ");
	scanf("%s", id);
	printf("Please enter your name: ");
	getchar();
	fgets(custName, 30, stdin);

	current = previous = front;
	while (!found && current != NULL) {
		if (strcmp(id, current->element->identifier) == 0)// string compare
		{
			printf("id matches");
			if (strcmp(custName, current->element->customerName) == 0)// string compare
			{
				current->element->customerName == NULL;
				current->element->loaned = false;

				found = true;
				printf("name mathes");

			}
			else {
				printf("this name does not match that of the person who borrowed this book");
			}
		}
		else {
			printf("this book does not exist");
		}

		menu();

	} //end while
}

void deleteBook(char *id) {
	struct LinearNode *current, *previous;
	bool notFound = true;

	printf("\n");
	if (isEmpty())
		printf("Error - there are no books in the library\n");
	else {
		current = previous = front;

		while (notFound && current != NULL) {
			if (strcmp(id, current->element->identifier) == 0)// string compare
				notFound = false;
			else {
				previous = current;
				current = current->next;
			}//end else
		} //end while

		if (notFound)
			printf("Error - there is no book with value %d\n", id);
		else {
			if (current == front) { //delete front node
				front = front->next;
				free(current);
			} //end else
			else if (current == last) {//delete last node
				free(current);
				previous->next = NULL;
				last = previous;
			}
			else {//delete node in middle of list
				previous->next = current->next;
				free(current);
			} //end else
			printf("book with identifier %s has been deleted\n", id);
			numBooks--; //update how many books are in library
		}//end else
	}//end else
	menu();
}

void viewAllBooks() {
	struct LinearNode *current;

	printf("\n");
	if (isEmpty())// display error message if there is no books
		printf("Error - there are no books in the library\n");
	else {//display book details for each element
		current = front;
		while (current != NULL) {
			printf("---------------------------\n");
			printf("\n");
			printf("Book identifier:  %s\n", current->element->identifier);
			printf("Title:  %s", current->element->name);
			printf("Author:  %s", current->element->author);
			printf("Year of publication:  %d\n", current->element->year);
			printf("this book has been loaned %d times\n", current->element->timesLoaned);
			printf("\n");

			current = current->next;
		} //end while
	}//end else
	menu();
} //end viewAllNodes


void viewSpecificBook(char *num) {
	struct LinearNode *current;
	bool notFound = true;

	printf("\n");
	if (isEmpty())
		printf("Error - there are no books in the library\n");
	else {
		current = front;

		while (notFound && current != NULL) {
			if (strcmp(num, current->element->identifier) == 0)// string compare
				notFound = false;


			if (!notFound) {
				printf("------------------\n");
				printf("Book identifier:  %s\n", current->element->identifier);
				printf("Book title:  %s", current->element->name);
				printf("Books author:  %s", current->element->author);
				printf("year of publication:  %d\n", current->element->year);
				printf("this book has been loaned '%d' times\n", current->element->timesLoaned);
				printf("------------------");
				printf("\n\n");
			}
			current = current->next;
		}//end while
		if (notFound)
			printf("book with identifier %s cannot be found\n", num);
	}//end else
	menu();
}

void popularBooks() {
	struct LinearNode *current, *max, *min;

	current = front;
	max = front;
	min = front;
	while (current != NULL) {
		if (current->element->timesLoaned > max->element->timesLoaned) {
			max = current;
		}

		if (current->element->timesLoaned < min->element->timesLoaned) {
			min = current;
		}
		current = current->next;
	}

	printf("most popular book:\n");
	printf("------------------\n");
	printf("Book identifier:  %s\n", max->element->identifier);
	printf("Book title:  %s", max->element->name);
	printf("Books author:  %s", max->element->author);
	printf("year of publication:  %d\n", max->element->year);
	printf("this book has been loaned '%d' times\n", max->element->timesLoaned);
	printf("------------------");
	printf("\n\n");
	printf("least popular book:\n  ");
	printf("------------------\n");
	printf("Book identifier:  %s\n", min->element->identifier);
	printf("Book title:  %s", min->element->name);
	printf("Books author:  %s", min->element->author);
	printf("year of publication:  %d\n", min->element->year);
	printf("this book has been loaned '%d' times\n", min->element->timesLoaned);
	printf("------------------");
	printf("\n\n");
	main();
}


//checks to see if list is empty
bool isEmpty() {
	if (front == NULL)
		return true;
	else
		return false;
}

//save the books to the file
void saveToFile(FILE *fp) {
	struct LinearNode *current;
	fp = fopen("Book.dat", "w");
	current = front;
	while (current != NULL) {
		fwrite(&current->element, sizeof(struct Book), 1, fp);
		current = current->next;
	}
	fclose(fp);
}

// read the books from the file 
void getFromFile(FILE *fp) {
	struct LinearNode *current;
	current = front;
	printf("Retriving numbers from file...\n");
	while (current != NULL)
	{
		fread(&current->element, sizeof(struct Book), 1, fp);
		current = current->next;
	}
	fclose(fp);
}


