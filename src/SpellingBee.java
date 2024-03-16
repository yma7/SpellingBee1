import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee
{

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters)
    {
        this.letters = letters;
        words = new ArrayList<String>();
    }


    public void generate()
    {
        // Calls recursive method
        makeWords("", letters);
    }

    public void makeWords(String word, String letters)
    {
        // Base case
        if (letters.isEmpty())
        {
            return;
        }
        // Creates all possible combinations of a string
        for (int i = 0; i < letters.length(); i++)
        {
            String hold = word + letters.charAt(i);
            words.add(hold);
            makeWords(hold, letters.substring(0,i) + letters.substring(i+1));
        }
    }

    public void sort()
    {
        // Calls another recursive method to sort the words
        words = mergeSortDivision(words, 0, words.size() - 1);

    }

    public ArrayList<String> mergeSortDivision(ArrayList<String> words, int left, int right)
    {
        // Base case
        if (left == right)
        {
            // Adds the single element to a new Arraylist
            ArrayList<String> holder = new ArrayList<String>();
            holder.add(words.get(left));
            return holder;
        }
        else
        {
            // Finds the middle index
            int middle = (left + right) / 2;
            // Splits the initial array into two halfs
            ArrayList<String> leftHalf = mergeSortDivision(words, left, middle);
            ArrayList<String> rightHalf = mergeSortDivision(words, middle + 1, right);
            return merge(leftHalf, rightHalf);
        }
    }
    public ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right)
    {
        // Creates a new sorted Arraylist
        ArrayList<String> merged = new ArrayList<>();
        // Variables to keep track
        int a = 0, b = 0, c = 0;
        // Continues until one or the other Arraylist's runs out
        while (a < left.size() && b < right.size()) {
            // Compares them
            if (left.get(a).compareTo(right.get(b)) < 0)
            {
                // Swaps
                merged.add(c, left.get(a));
                a++;
                c++;
            }
            else
            {
                merged.add(c++, right.get(b++));
            }
        }
        // Adds the elements of the other Arraylist that hasn't run out
        while (a < left.size())
        {
            merged.add(c++, left.get(a++));
        }
        while (b < right.size()) {
            merged.add(c++, right.get(b++));
        }
        // Returns sorted ArrayList
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates()
    {
        int i = 0;
        while (i < words.size() - 1)
        {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords()
    {
        for (int i = 0; i < words.size(); i++)
        {
            // Checks to see if is in dictionary
            if (!found(words.get(i)))
            {
                // If not remove from words
                words.remove(i);
                i--;
            }
            else
            {
                // Otherwise it will be and system will print the word
                System.out.println(words.get(i));
            }

        }
    }
    public boolean found(String s)
    {
        // Variables for binary search
        int low = 0;
        int high = DICTIONARY_SIZE - 1;
        while (low <= high)
        {
            // Calculates middle
            int mid = (high + low) / 2;
            // Checks to see if is in word
            int check = DICTIONARY[mid].compareTo(s);
            if (check == 0)
            {
                return true;
            }
            // If not checks to see if is above or below mid and adjusts accordingly
            else if (check < 0)
            {
                low = mid + 1;
            }
            else
            {
                high = mid - 1;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException
    {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords()
    {
        return words;
    }

    public void setWords(ArrayList<String> words)
    {
        this.words = words;
    }

    public SpellingBee getBee()
    {
        return this;
    }

    public static void loadDictionary()
    {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try
        {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e)
        {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine())
        {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args)
    {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do
        {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try
        {
            sb.printWords();
        } catch (IOException e)
        {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
