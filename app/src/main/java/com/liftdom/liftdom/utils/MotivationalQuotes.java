package com.liftdom.liftdom.utils;

import java.util.ArrayList;


/**
 * Created by Brodin on 3/14/2017.
 */

public class MotivationalQuotes {

    String[] quotes = {
            "Lift hard, live easy. - Brodin",
            "The way to get started is to quit talking and begin doing. - Walt Disney",
            "The pessimist sees difficulty in every opportunity. The optimist sees the opportunity in every " +
                    "difficulty. - Winston Churchill",
            "You learn more from failure than from success. Don’t let it stop you. Failure builds character. - Unknown",
            "Strength does not come from winning. Your struggles develop your strengths. When you go through " +
                    "hardships and decide not to surrender, that is strength. - Arnold Schwarzenegger",
            "The world breaks everyone, and afterward, some are strong at the broken places. - Ernest Hemingway",
            "A truly strong person does not need the approval of others any more than a lion needs the approval of " +
                    "sheep. - Vernon Howard",
            "I like criticism. It makes you strong. - Lebron James",
            "There are better starters than me, but I'm a strong finisher - Usain Bolt",
            "Strength and growth come only through continuous effort and struggle. - Napoleon Hill",
            "Tough times never last, but tough people do. - Robert H. Schuller",
            "Difficulties are meant to rouse, not discourage. The human spirit is to grow strong by conflict. - " +
                    "William Ellery Channing",
            "That which does not kill us makes us stronger. - Friedrich Nietzsche",
            "Be very strong... be very methodical in your life if you want to be a champion. - Alberto Juantorena",
            "It’s not whether you get knocked down, it’s whether you get up. - Vince Lombardi",
            "If you are working on something that you really care about, you don’t have to be pushed. The vision " +
                    "pulls you. - Steve Jobs",
            "Strong people are harder to kill than weak people and more useful in general. - Mark Rippetoe",
            "Biceps are like ornaments on a Christmas tree. - Ed Coan",
            "Sell yourself short on nutrition and you’re selling yourself short on maximizing your physique " +
                    "development. - Ernie Taylor",
            "Just remember, somewhere, a little Chinese girl is warming up with your max. - Jim Conroy",
            "Your love for what you do and willingness to push yourself where others aren't prepared to go is what " +
                    "will make you great. - Laurence Shahlaei",
            "My old routine was McDonald's on the way to the gym, coffee during my workout, Burger King and " +
                    "Copenhagen post-workout. - Dave Tate",
            "If you want something you’ve never had, you must be willing to do something you’ve never done. - Thomas" +
                    " Jefferson",
            "If a man tells you he doesn't lift because he doesn't want to get too bulky, then his testicles have " +
                    "been removed. - Paul Carter",
            "For me, life is continuously being hungry. The meaning of life is not simply to exist, to survive, but " +
                    "to move ahead, to go up, to achieve, to conquer. - Arnold Schwarzenegger",
            "If you're capable of sending a legible text message between sets, you probably aren't working hard " +
                    "enough. - Dave Tate",
            "Don’t have $100.00 shoes and a 10 cent squat. - Louie Simmons",
            "Courage doesn’t always roar. Sometimes courage is the quiet voice at the end of the day saying, \"I will " +
                    "try again tomorrow.\" - Mary Anne Radmacher",
            "A champion is someone who gets up when they can’t. - Jack Dempsey",
            "If you can't explain the essence of your program to a three year old in 60 seconds, its too complicated." +
                    " I've trained with Olympic medal winners and I can assure you, they don't do anything mysterious, they just do the exercises better than we do. - Maik Weidenbach",
            "The road to nowhere is paved with excuses. - Mark Bell",
            "There is no such thing as over training, just under nutrition and under sleeping. - The Barbarian " +
                    "Brothers",
            "I don’t do this to be healthy, I do this to get big muscles. - Markus Ruhl",
            "I'm not sold on one diet philosophy. I'm sold on whatever will work for you. - Dave Tate",
            "Discipline is doing what you hate to do, but nonetheless doing it like you love it. - Mike Tyson",
            "Learn it all, then forget it all. - Bruce Lee",
            "Mediocre athletes that tried like hell to get good are the best coaches. - Mark Rippetoe",
            "Strength does not come from physical capacity. It comes from an indomitable will. - Mahatma Gandhi",
            "The single biggest mistake that most beginners make is putting 100% of their effort into the positive " +
                    "(concentric) part of the rep, while paying no attention to the negative (eccentric) segment. - Dorian Yates",
            "There's more to life than training, but training is what puts more in your life. - Brooks Kubik",
            "Most champions are built by punch the clock workouts rather than extraordinary efforts. - Dan John",
            "THERE IS NO REASON TO BE ALIVE IF YOU CANNOT DO THE DEADLIFT! - Jon Pall Sigmarsson",
            "Not only are squats not bad for the knees, every legitimate research study on this subject has shown " +
                    "that squats improve knee stability and therefore help reduce the risk of injuries. - Charles Poliquin",
            "You are right to be wary. There is much bullshit. Be wary of me too, because I may be wrong. Make up " +
                    "your own mind after you evaluate all the evidence and the logic. - Mark Rippetoe",
            "Nothing can stop the man with the right mental attitude from achieving his goal; nothing on earth can " +
                    "help the man with the wrong mental attitude. - Thomas Jefferson",
            "There are no shortcuts. The fact that a shortcut is important to you means that you are a pussy. - " +
                    "Mark Rippetoe",
            "If you think lifting weights is dangerous, try being weak. Being weak is dangerous. - Bret Contreras",
            "Trust me, if you do an honest 20 rep program, at some point Jesus will talk to you. On the last day of " +
                    "the program, he asked if he could work in. - Mark Rippetoe",
            "They can crack jokes. They can sit back and analyze and criticize and make all the fun they want. But " +
                    "I’m living my life, I’m doing it. What are you doing? - Kai Greene",
            "On the Internet, everyone squats. In real life, the squat rack is always empty. You figure out what this" +
                    " means. - Steve Shaw",
            "I’ve made many good friends in bodybuilding, though there are few I’d trust to oil my back. - Lee " +
                    "Labrada",
            "Strength does not come from winning. Your struggles develop your strengths. When you go through " +
                    "hardships and decide not to surrender, that is strength. - Arnold Schwarzenegger",
            "The question isn’t who is going to let me; it’s who is going to stop me. - Ayn Rand",
    };

    public String[] getQuote() {
        int random = (int) (Math.random() * quotes.length);

        String quoteUnSplit = quotes[random];

        String delims = "[-]";

        String[] quoteArray = quoteUnSplit.split(delims);

        return quoteArray;
    }


}
