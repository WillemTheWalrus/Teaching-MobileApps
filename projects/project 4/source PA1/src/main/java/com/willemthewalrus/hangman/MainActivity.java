package com.willemthewalrus.hangman;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private boolean isclicked = false; //to see if the button has been clicked yet
    private int chances = 6;
    private String ourword;   //the word inputted buy the user to be guessed
    private char[] wordchars; //contains the word to be guessed
    private char[] strguess; //contains current guesses and blank spots
    private String guessdisplay; //to be passed to TextView to display current guesses
    String guesshistory = "Previous guesses: ";
    Button butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butt = findViewById(R.id.button);
    }

    public void reset(View view) {
        ImageView v = findViewById(R.id.imageView);
        Picasso.with(this).load(R.drawable.hangman0).into(v);
        isclicked = false;
        TextView topbox = (TextView) findViewById(R.id.instructionsText);
        EditText inbox = (EditText) findViewById(R.id.Input);
        topbox.setText("You reset the game! \n Please enter in a new word");
        inbox.setText("");
        butt.setText("Enter Word");
    }

    public void clicked(View view) {
        TextView badgues = findViewById(R.id.badGuess); //displays previously guessed letters
        TextView numgues = findViewById(R.id.chancesLeft);  //chances left textview
        TextView letgues = findViewById(R.id.guessedLetter); //guess letters textview
        TextView topbox = findViewById(R.id.instructionsText); //instructional tectview at the top
        EditText inbox = findViewById(R.id.Input); //input box editText
        ImageView img = findViewById(R.id.imageView); //hangman png images

        //hide the keyboard after the button has been clicked
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //if this is the first click, initialize the UI components' arrays and strings
        if (!isclicked) {
            Picasso.with(this).load(R.drawable.hangman0).into(img);
            chances = 6;
            ourword = inbox.getText().toString().trim();
            wordchars = new char[ourword.length()];
            wordchars = ourword.toCharArray();
            strguess = new char[ourword.length()];
            guesshistory = "Previous guesses: ";


            badgues.setText("You have not guessed any letters yet");

            //check to make sure there are actual letters

            if (ourword.length() < 1) {
                topbox.setText("Please enter a word before pressing the button");
            }
            //this branch executes if there are no blanks in the begining of the word
            else {

                //fill guess array with underscores
                for (int i = 0; i < ourword.length(); i++) {
                    strguess[i] = '_';
                    Log.i("guessarray", Character.toString(strguess[i]));
                }

                butt.setText("Enter Letter");

                //change text in instructionsText TextView
                topbox.setText("Now please pass the phone to a friend \n so that they can try to guess your word");

                //display guesses

                CharSequence guessdisplay = Arrays.toString(strguess);
                Log.i("underscores", (String) guessdisplay);
                letgues.setText(guessdisplay);

                //displays chances left
                numgues.setText( String.format("You have %d chances left", chances));

                //clear the text entry box
                inbox.setText("");

                //the button has now been clicked so set isclicked to true
                isclicked = true;
                Picasso.with(this).load(R.drawable.hangman0).into(img);

            }

        }

        //this will run when it is not the first button usage
        else {
            String test = inbox.getText().toString();

            Log.i("guess", test);
            // check to make sure that the entered char is not a blank

            if (test.length() < 1) {
                topbox.setText("Please enter a single letter before pressing the button");
                inbox.setText("");
            } else {
                char userentry = inbox.getText().toString().charAt(0);
                // will branch here if the entered character is not in the string
                if (ourword.indexOf(userentry) == -1 && chances > 0) {



                    //check to make sure this wasnt their last chance
                    if (chances - 1 != 0) {
                        chances--;

                        //load appropriate png image
                        if(chances == 5){
                            Picasso.with(this).load(R.drawable.hangman1).into(img);
                        }
                        else if(chances==4){
                            Picasso.with(this).load(R.drawable.hangman2).into(img);
                        }
                        else if(chances==3){
                            Picasso.with(this).load(R.drawable.hangman3).into(img);
                        }
                        else if(chances==2){
                            Picasso.with(this).load(R.drawable.hangman4).into(img);
                        }
                        else if(chances==1){
                            Picasso.with(this).load(R.drawable.hangman5).into(img);
                        }


                        topbox.setText("Woops! that letter wasn't in the word, please try again");
                        //displays chances left
                        numgues.setText((CharSequence) String.format("You have %d chances left", chances));

                        //clear Inbox
                        inbox.setText("");
                        //add guessed letter to guesshistory and display it
                        guesshistory = guesshistory + userentry + " ";
                        badgues.setText(guesshistory);
                    }

                    //reset game if the user guesses incorrectly with only one chance left
                    else {

                        chances--;
                        numgues.setText( String.format("You have %d chances left", chances));

                        Picasso.with(this).load(R.drawable.hangman6).into(img);
                        topbox.setText("Oh no! It looks like that was your last chance. \n  please enter " +
                                "in a new word and press the button to try again");
                        isclicked = false;
                        Button ender = (Button) findViewById(R.id.button);
                        ender.setText("PLAY AGAIN");
                        inbox.setText("");
                    }

                }

                //will branch here if the entered character is within the word
                else if (ourword.indexOf(userentry) != -1 && chances > 0) {
                    boolean loopstopper = true;
                    int i = 0;
                    while (loopstopper) {

                        //will update current guesses in ui with new matched letter
                        if (wordchars[i] == userentry) {
                            strguess[i] = userentry;
                            wordchars[i] = '_';
                            i = 0;

                            CharSequence guessdisplay = Arrays.toString(strguess);
                            Log.i("correct guess", (String) guessdisplay);
                            letgues.setText(guessdisplay);
                            inbox.setText("");

                        }

                        //breaks out of loop once the end of the word is reached with no matched chars
                        else if (wordchars[i] != userentry && i == wordchars.length - 1) {
                            loopstopper = false;
                            topbox.setText("Nice guess!");

                            //add guessed letter to guesshistory and display it
                            guesshistory = guesshistory + userentry + " ";
                            badgues.setText(guesshistory);
                        }

                        //increments loop counter
                        else {
                            i++;
                        }
                    }
                    //check to see if there are any letters left to be guessed
                    for (int j = 0; j < wordchars.length; j++) {
                        if (wordchars[j] != '_') {
                            break;
                        } else if (j == wordchars.length - 1) {
                            topbox.setText("congrats on guessing the word!! Enter in another word to start again");
                            isclicked = false;
                        }
                    }
                }
            }

        }
    }

}