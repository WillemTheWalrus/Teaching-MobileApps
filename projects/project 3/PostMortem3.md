# Post Mortem Project 3
This Project was not nearly as difficult as Project 2 for a various number of reasons. First off, I actually started this project far 
in advance which gave me enough time to complete this project and make it look OK. Unfortunately, I realized that I forgot to button in an
option which allows the user to select a gallery photo. But I realized this on wednesday after I had already submitted the project. 
For PA 4 I will go back over this app and add in that functionality along with a progress loading bar. One issue that I encountered while 
writing this app was that it would take a long time to get a response from the cloud vision API, sometimes up to a minute long. I realized
that this is probably because I did not compress the image at all before sending it out. I will also change this for my PA 4. One struggle 
that I was able to over come was that I  had to figure out how to implement and use a class called AsyncTask because Android Studio does not 
allow an app to run networking functions within its main thread. The implemented AsyncTask class instead takes the information and does the 
network calls in the background. I also saw that it was possible to do this by generating seperate threads for my networking calls but I decided\
that it would be easier to learn to use and implement AsyncTask. The most fun part of this assignment was actually having it work. I was so 
afraid that this was going to take an insane amount of debugging and that it would crash a lot but it suprisingly not as bad as I thought
it would be. This project showed me that it isn't very difficult to use google's API software and it made me curious as to what other software
I could try to implement and use. For my next app I want to do something involving google maps and the firebase chat capabilites. It is exciting
to see how easy it is becoming to make something that I think of. I am now starting to get the confidence to beleive that if I want to make an 
app that I have an idea for that I can actually do it if I just put enough time in. To new students starting this project I would reccomend looking 
into how to use AsyncTask and have a strong understanding of how it works. Another fun part of this app was being able to use it. It was cool to 
show my friends what it could do and it was kind of fun to take pictures of things to see what it would say. I thought it was kinda funny
that it frequently annotated me as 'girl' whenever I took a picture of myself. My friends and I definately got a kick out of that. I think 
in the future it would be cool to implement this as a game where you tell a story and the way you progress in that story is by finding certain 
objects in the world to help your character progress. Depending on the objects you take a picture of, the story would have different outcomes. 
It seems as though we only scratched the surface with what this could do and I look forward to making another app after spring break. 
