William Doudna

Mobile Apps

Adam Carter

02/27/2018

                                                      Project 3 Design Document
# Summary:
For this project I learned how to implement the google cloud vision API and how, for some reason, it frequently thinks that I am a girl. Other than that, this project went pretty smoothly. This app prompts the user to take a picture and then sends it off to google to be analyzed by the vision API. This process normally takes 1 minute on my home wifi network. The vision API then returns information as to what it thinks is in the picture. The app then displays the description returned from vision and asks the user if it guessed correctly or not. I wrote this app to complete this homework assignment, but found that I rather enjoyed learning how to use the cloud vision API. It doesnt really serve any purpose other than to entertain the almighty user. 

# System Design:
  ## Requirements: 
    * Has a Camera
    * Has a stable connection via wifi or cellular
    * Has a minimum API level 0f 21 (Android 5.0 Lollipop)
  ## Use Cases (With pictures)
  Initially the user is prompted to take a picture by pressing on the button
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-215942.png "home screen")
  
  The user is then told to wait while the app waits for a response from the cloud vision api
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-215843.png "waiting for analyzation")
  
  Once the vision API returns with a result the buttons become available and the text changes
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-215919.png "prompt if it guessed correctly")
  
  If the user verifies that description provided by the vision API is correct, then it goes to this screen  
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-215928.png "good guess display")
  
  If the user presses the no button, then this screen appears prompting the user to enter in what was actually represented in the picture
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-220059.png "when the user answers no")
  
  If the user enters in a word that was in the annotations description then this is displayed
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-220238.png "when the user puts in something the api had in its annotations")
  
  If the user enters in something that was not in the descriptions then contextview is set to this activity
  ![alt text](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%203/Screenshot_20180227-220410.png "when the user enters in something that was not in the annotaitons descriptions")
  
  # Usage:
  * The user first should take a picture that they want the app to analyze
  * they will then wait for the Vision API to analyze the image
  * Once the image is analyzed, the user can either press the yes or no buttons to verify that the description of theirimae was either correct or incorrect
  * If they select yes, they will redirected to another screen where they can choose to go again
  * If they select no they are directed to a screen in which they are asked to type in what was actually in the picture
  * If what they enter was in the annotations descriptions, the textview will display the score for that description and let them start again if they wish
  * if the description entered was not in the annotations description then the textview will say sorry and give them the option of starting again by pressing the button
  
  
