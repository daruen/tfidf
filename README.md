In this problem we have 2 algorithms tf and idf.

tf algorithm  is O(n^2) because I have needed 2 whiles in order to see if the searched term is also repeated in the line.

idf algorithm is O(n^2) because we need to search in the dictionary and in the files.

In the function tfidf I use the two calculators of each algorithm.

The usage of the application, first you need to compile the application with mvn clean install.


java -jar "jarfile" -d "Dictionary path" -p "period of daemon" -t "term" -n "number of lines in top"

This solution could be improved making multiple threads for searching the tfidf, in this thread we can have the idf as a shared variable between threads.
and the thread just need to calculate the tf, create the tfidf calculations and store it in a shared map in order to get it from the timer.
