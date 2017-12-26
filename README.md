# Data-Structures
My code from the fall 2017 Data Structures Class at Rutgers University.


Includes four projects, each of which used structures and methds provided by the instructor:

  Big Integer:  
    Performing functions on array lists to deal with numbers larger than long integer values.
    
          Functions I wrote:
            Parse: Stores the input string into a linked list, while checking for errors
            Add: Parses two numbers and combines then, subtrcting is done by making one negative
            Multiply: Parses two numbers and divides them
  
  Trie: 
    Creates a tree of words using their prefixes as roots, an then can search through the tree for said words.
    
          Functions I wrote:
            BuildTrie: Takes in a text file and creates the nodes and branches of the trie using prefixes. 
                              (A word is a child of a node if it beings with the node, so "the" is a child of "th")
            CompletionList: Returns all words in the trie that begin with a given prefix
  
  Little Search Engine: 
    Reads texts files and creates a "dictionary" with a list of words from the document, which then can be
    read and listed by frequency.
      
          Functions I wrote:
             LoadKeywordsFromDocument: Loads the keywords from the document into a hash table, stroing the word and its frequency, calls GetKeyword
             GetKeyword: Checks the word that is scanned to see if it is a valid word by the stanards for the assignment
             MergeKeywords: Loads the words fromt the document into a global hash table storage of all words, 
                             calls InsertLastOccurrence
             InsertLastOccurrence: Inserts the file into the hashtable for that word, using a Binary Search Insert
             Top5Search: When given two words, returns the top 5 documents in which the words appear the most
             
          
            
    
  Friends: 
    Analyzes an undirected and unweighted graph of people linked as friends. 
       
           Functions I wrote:
              ShortestPath: Finds the shortest path from one person to another through friends
                      Calls a private recursive method which I coded
              Cliques: Finds all cliques of friends at a given school
                       Calls a recursive method which I coded as well
              Connectors: Finds all connecting friends in the graph (all friends who are needed to get from one person to 
                          another
                                Calls another recurives method which I coded
            
         
         
         
    
  
