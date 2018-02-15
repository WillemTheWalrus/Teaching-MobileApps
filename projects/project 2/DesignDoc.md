William Doudna
Mobile Apps
Adam Carter
February 12, 2018

                                                            Project 2 Design Doc
  Introduction: This project was brutal for me. I put a lot of time into making this app work and I initially submitted an app that did not work. Even though it is 2 days later, I am happy to say that I now have a functional app that can save modified images. It does not yet display them in the gallery but I intend to continue to work on this project to provide this functionality. 
  
  
  Requirements:
  * You need to be running an Android with at least API 18: 4.3(Jelly Bean) in order to run this App
  * The device must also have a camera equipped
  * There must be enough open storage to save files 
  
  Usage:
  
   Here is the starting UI, where you can either choose to take a picture or select one from the gallery
   
 ![Starting Screen](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%202/Screenshot_20180212-232318.png)
 
 On this page there is a textview, 2 buttons, and 9 image buttons. Upon selecting take a picture, you are then brought to this screen
 
 ![Take Photo](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%202/Screenshot_20180212-232325.png)
 
 From here you can take a picture and then you will be asked to confirm the picture from this screen
 
 ![Confirm Photo](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%202/Screenshot_20180212-232335.png)
 
 You will then be given a preview of all of the effects that can be applied to your photo and select one by pressing an image button
 
 ![choose effect](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%202/Screenshot_20180212-232346.png)
 
 from here you will then be taken to an enlarged screen where you can choose to save you photo to internal storage. 
 
 ![save photo](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%202/Screenshot_20180212-232354.png)
 
 The only difference between selecting from a gallery and this option is that the gallery UI looks as such
 
 ![gallery select](https://github.com/WillemTheWalrus/Teaching-MobileApps/blob/master/projects/project%202/Screenshot_20180212-232407%20(2).png)
 
 
 Usage:
  1. If you wish to take a picture, press 'take a picture'. If you with to select a photo from your gallery, select 'pick a picture'.
  2. From here if you selected 'pick a picture', then select the photo that you wish to have modified. I you chose 'take a picture', use
      your camera device to take and confirm a picture for editing
  3. Now press the image button for the effect you would like to have applied to your picture
  4. From here, if the saving function actually worked, you would press save image and then be redirected to the first screen. 
      Unfortunately I was unable to figure out how to do this. I will see you later this week with questions. 
      
     Note: The location that the files save to (within the file explorer) on my device is: 
          /My Files/Internal Storage/Android/data/com.willemthewalrus.devimgexample/files/Pictures
