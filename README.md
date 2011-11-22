Trigrams
========

The Trigram project is my first attempt at a GitHub project.  It is a simple 
program that generates trigrams from a piece of text.

Algorithm
---------

For a given piece of text, every two consecutive words in the text, the word 
that follows it is added to a list, which is uniquely identified with the 
key-pair.

Generating the trigram is now easy.  The first two words in the text serves 
as the starting point.  Using these first two words, choose a random word from 
the associated list and add it to the text.  This process is now repeated with
the new key-pair set.

This process continues until there is no matching word for the current 
word-pair.  

   