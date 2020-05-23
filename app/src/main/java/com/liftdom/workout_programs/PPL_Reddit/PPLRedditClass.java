package com.liftdom.workout_programs.PPL_Reddit;

/**
 * Created by Brodin on 10/31/2019.
 */
public class PPLRedditClass {
    /**
     * Pull/Push/Legs option as well..
     *
     *
     * "PULL"
     *  - Deadlifts 1x5+/Barbell Rows 4x5, 1x5+ (alternate, so if you did deadlifts on Monday, you
     *      would do rows on Thursday, and so on)
     *  - 3x8-12 Pulldowns OR Pullups OR chinups
     *  - 3x8-12 seated cable rows OR chest supported rows
     *  - 5x15-20 face pulls
     *  - 4x8-12 hammer curls
     *  - 4x8-12 dumbbell curls
     *  - abs on deadlift days
     *      - just do a few sets of (weighted) planks or a few sets of ab wheel and some hanging leg raises.
     *
     * "PUSH"
     *  - 4x5, 1x5+ bench press/4x5, 1x5+ overhead press (alternate in the same fashion as the
     *      rows and deadlifts)
     *  - 3x8-12 overhead press/3x8-12 bench press (do the opposite movement: if you bench pressed
     *      first, overhead press here)
     *  - 3x8-12 incline dumbbell press
     *  - 3x8-12 triceps pushdowns SS 3x15-20 lateral raises
     *  - 3x8-12 overhead triceps extensions SS 3x15-20 lateral raises
     *
     * "LEGS"
     *  - 2x5, 1x5+ squat
     *  - 3x8-12 Romanian Deadlift
     *  - 3x8-12 leg press
     *  - 3x8-12 leg curls
     *  - 5x8-12 calf raises
     *  - abs on squat days
     *      - just do a few sets of (weighted) planks or a few sets of ab wheel and some hanging leg raises.
     *
     * "How much weight per session?"
     *  - 2.5kg/5lbs for upper body lifts (bench press, row, overhead press)
     *  - 2.5kg/5lbs for squats
     *  - 5kg/10lbs for deadlifts
     *  - For accessories:
     *
     * "Warmup"
     *  - Empty bar x 10
     *  - 95lbs x10
     *  - 135lbs x5
     *  - 185lbs x3
     *  - 200lbs 4x5, 1x5+
     *
     * "REST"
     *  - 3-5 minutes between your first exercise of the day
     *  - 1-3 minutes between all your other exercises
     *
     * "DELOADING"
     *  - "Take 10% off your working weights and work back up."
     *
     *  "Strength Version"
     *  - replace 3x8-12 RDLs with 3x5 RDLs.
     *  - Add weighted dips,
     *  - Weighted chins-ups/pull-ups in the 3x5 range (with bodyweight back off sets).
     *  - So if you want to lift heavy weights then feel free to alter the compound movement accessories
     *      (so your presses, your pulldowns or rows, your romanian deadlifts or your leg presses)
     *      to be in the lower rep ranges. 3x4-6 is perfect for things like this.
     *
     * "Endurance Version"
     *  - Keep the first exercise the same, up the rep range on everything else.
     *      15-20 reps is a great range for increasing endurance.
     *
     */


    /**
     * For weight progression, let's add checkmarks for stalling the weight increase and for
     * activating deload (dropping weights by 10%).
     *
     *
     *
     * So what does this all mean practically?
     *
     * We have specific workouts to do each day.
     * The exact main lifts change depending on day.
     * The accessories are fixed in set schemes, but can be changed.
     *  This means that they should have names like "push1accessory1" or something like that, and
     *  we change the value of that depending on what the user chooses. The weight will be
     *  changed depending on the value. This means we'll be storing the exercises as keys with
     *  the values being the weights used. Just like in W531fB.
     *
     * This whole thing is very similar to W531fB. Main lifts + hot swapped accessories.
     *
     * We need to collect begin date and we also need to know the format. This will determine
     * what is returned.
     *
     *
     *
     */

}

/*
 * The best way would probably be to not save to default/empty template with extra info, but
 * actually make it somewhat like a regular template in that all the s/r/w stuff are saved, then
 * do extra operations on them in AssistorSaved.
 *
 * Will need a method here that takes in days and a map of what they did and increments what
 * is needed and returns the new map to be updated to fb.
 *
 *
 * We will auto generate some numbers, but let them know that they can edit the weights as
 * they need to. Also need to allow for changing out exercises.
 */