
---

# METRO  

## What?  

This is an **attempt** at building a - hopefully performant - rogue-like game, inspired by the novel [Metro](https://en.wikipedia.org/wiki/Metro_2033_(novel)) by the Russian author [Dmitry Glukhovsky](https://en.wikipedia.org/wiki/Dmitry_Glukhovsky).  

## How?  

I use the [Component Decoupling Pattern](https://gameprogrammingpatterns.com/component.html) to manage actors and items.  

The plan is to build a robust and - highly - configurable game, in which the creatures can be easily defined. It should be possible to play as different races, including humans, different specified genders, etc., etc.  

## Why?  

School project :))  

Okay, but it was supposed to be that. And then I realized what I had signed myself up for. I'm not even sure if I can incorporate all of the features I promised in the document plan.  
**EDIT**: Well, most of the promised features were delivered - although very fragile in places. Please see [The TODO-List](TODO.md).  

## How to Play?  

Eventually, one has to write a better interface to the world (I'm blind and have settled for an [Interactive Fiction-Style-ish Interface](https://en.wikipedia.org/wiki/Interactive_fiction),  but I tried to keep the systems as decoupled as possible, so adding more views should not be that clunky, right?).  

**EDIT**: [Here](src/DungeonClerkOutputHandler.java) is an example logger output handler - yes, I used loggers to handle output - written for [Live View Programming in Java](https://github.com/denkspuren/LiveViewProgramming) developed by Prof. [Herzberg](https://www.thm.de/mni/dominikus-herzberg). I took advantage of markdown to output to the console.  
For example, you may see the [German Documentation](src/Codedoku/Codedoku.java) to see how it is used in action.  

A list of different commands can be obtained by typing "help, Help, h or H" - whatever pleases you.  

The "look/Look/l/L" command should provide you with a descriptive view of your surroundings.  

"Show map" will dump the whole world to the console (for testing purposes only!). However, the grid is probably displayed in a poorly formatted way - this is because I'm too dumb to properly navigate ASCII maps with a Braille display.  

## What Else?  

There's a list of [TODO](TODO.md) and [German Code Documentation](src/Codedoku/Codedoku.java). The latter is unfortunately compulsory to write!  
**EDIT**: English documentation will follow.  

## Credits/Courtesy  

> Give credit where credit is due.  

I'm really thankful for the folks at [Roguebasin](https://roguebasin.com/index.php/Articles), [Eric Lippert](https://learn.microsoft.com/en-us/archive/blogs/ericlippert/shadowcasting-in-c-part-one), [Adam Millazzo](http://www.adammil.net/blog/v125_Roguelike_Vision_Algorithms.html#shadowcode), [Bob Nystrom](https://gameprogrammingpatterns.com/), and many others for the decent amount of materials they posted online! <3  
I know I have missed others, but I tried to provide direct article links in the code I wrote to signify where I took the inspiration from.  

# License  

Copyright © 2024/2025 Saman Aghajani  
This work is free. You can redistribute it and/or modify it under the  
terms of the **Do What The Fuck You Want To** Public License, Version 2,  
as published by Sam Hocevar. See the [LICENSE](LICENSE) file for more details.  

---
