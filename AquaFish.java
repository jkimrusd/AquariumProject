
import java.awt.Color;
import java.util.Random;


/**
 * Aquarium Lab Series: <br>
 *      The AquaFish class defines a fish in an aquarium.  A fish of this
 * class can move in the aquarium (always staying fully within the aquarium),
 * and can report its color, dimensions, and location, for the purpose of
 * the display.
 * <br> <br>
 * Created: <br>
 *   10 July 2002,  Alyce Brady<br>
 * <br>
 * Modifications: <br>
 *   2008-2009, Alyce Brady, Simplified by replacing two classes
 *                               (AquaPoint and Direction) with single
 *                               NavigationAide class. <br>
 *   (date), (your name), Modified to .... <br>
 * 
 * @author (your name) (with assistance from)
 * @version 18 January 2009
 * @see Aquarium
 **/
public class AquaFish
{
    // STATE

    // Named constants that specify how far a fish may move in one timestep
    public static final int MIN_DISTANCE = 10;
    public static final int MAX_DISTANCE = 70;

    // Class Variables: Shared among ALL fish
    private static int nextAvailableID = 1;   // next avail unique identifier
    private static Random generator = new Random(); // random number generator

    // Instance Variables: Encapsulated data for EACH fish
    private Aquarium theAquarium;    // aquarium in which this fish is swimming
    private int uniqueID;            // unique identifier for this fish
    private Color color;             // fish's color
    private boolean facingRight;     // whether fish is facing right or left
    private NavigationalAide aide;   // object that keeps track of this fish's
                                     //    size, location, and direction

    // OPERATIONS (constructor and methods)

    /**
     *  The AquaFish constructor sets properties of the AquaFish.
     *  This constructor creates a white fish.
     *  Precondition: the aquarium must be big enough to accommodate
     *  the biggest fish (currently 75 pixels long and 30 pixels high)
     *  plus 10 pixels of padding in all four directions.
     *  @param    aqua   the Aquarium in which the fish will live
     **/
    public AquaFish(Aquarium aqua)
    {
        // Use the two-parameter constructor for the real initialization,
        // specifying that the default color should be Color.WHITE.
        this(aqua, Color.WHITE);
    }

    /**
     *  The AquaFish constructor sets properties of the AquaFish.
     *  Precondition: the aquarium must be big enough to accommodate
     *  the biggest fish (currently 75 pixels long and 30 pixels high)
     *  plus 10 pixels of padding in all four directions.
     *  @param    aqua   the Aquarium in which the fish will live
     *  @param    newColor  the color for the new fish
     **/
    public AquaFish(Aquarium aqua, Color newColor)
    {
        // Keep track of the aquarium and initialize ID.
        this.theAquarium = aqua;
        this.uniqueID = nextAvailableID;
        nextAvailableID++;

        // Initialize fish's size, location, and direction.
        this.aide = new NavigationalAide(this);
        this.facingRight = true;

        // Initialize this fish's color.
        this.color = newColor;
    }

    /**
     *  Gets the aquarium in which this fish lives.
     *  @return  the aquarium in which this fish exists
     */
    public Aquarium aquarium()
    {
        return this.theAquarium;
    }

    /**
     *  Gets the unique identifier for this fish.
     *  @return    the ID of the fish
     **/
    public int id()
    {
        return this.uniqueID;
    }

    /** Gets fish's color.
     *  @return        the color of this fish
     **/
    public Color color()
    {
        return this.color;
    }

    /**
     *  Gets this fish's x coordinate in the aquarium.
     *  @return    the x coordinate in the aquarium of the fish's centerpoint
     **/
    public int xCoord()
    {
        return this.aide.centerpointX();
    }

    /**
     *  Gets this fish's y coordinate in the aquarium.
     *  @return    the y coordinate in the aquarium of the fish's centerpoint
     **/
    public int yCoord()
    {
        return this.aide.centerpointY();
    }

    /**
     *  Determines whether this fish is facing right.
     *  @return     <code>true</code> if fish is facing right;
     *              <code>false</code> otherwise
     **/
    public boolean isFacingRight()
    {
        return this.facingRight;
    }

    /**
     *  Determines whether this fish is facing left.
     *  @return     <code>true</code> if fish is facing left;
     *              <code>false</code> otherwise
     **/
    public boolean isFacingLeft()
    {
        return ! isFacingRight();
    }

    /**
     *  Determine whether this fish is at a wall.
     *  A fish is considered at a wall if it cannot move forward; in other
     *  words, if the distance from the fish to the wall it faces is less
     *  than the minimum distance that a fish can move forward.
     *  @returns    <code>true</code> if fish is at a wall;
     *              <code>false</code> otherwise
     **/
    public boolean atWall()
    {
        return (this.aide.fishDistanceToWall() <= MIN_DISTANCE);
    }

    /** Gets the length of this fish.
     *  @return   fish length
     **/
    public int length()
    {
        return this.aide.fishLength();
    }

    /** Gets the height of this fish.
     *  @return    fish height
     **/
    public int height()
    {
        return this.aide.fishHeight();
    }

    /**
     *  This function is provided primarily for debugging purposes.
     *  @return    a string representation of a fish
     **/
    public String toString()
    {
        String s = new String();
        s += this.uniqueID + this.aide.toString();
        return s;
    }

//     /** 
//      *  Moves the fish for one time step.
//      */
//     public void move()
//     {
//         this.moveForward();
//     }
    
    /**
     *  Moves forward horizontally by random increments, staying
     *  within the aquarium.
     **/
//    protected void moveForward()
    public void moveForward()
    {
        // First get random number in range [0, MAX_DISTANCE-MIN_DISTANCE],
        // then shift to [MIN_DISTANCE, MAX_DISTANCE].  If moving that
        // far would mean swimming out of the aquarium, only move to edge
        // of aquarium.  Adjust fish's x coordinate by a positive or 
        // negative amount, depending on whether fish is facing right or left.
        int moveAmt = generator.nextInt(MAX_DISTANCE - MIN_DISTANCE + 1);
        moveAmt += MIN_DISTANCE;
        if ( moveAmt >= this.aide.fishDistanceToWall() )
            moveAmt = this.aide.fishDistanceToWall();
        if ( this.isFacingRight() )
            this.aide.moveFishRight(moveAmt);
        else
            this.aide.moveFishLeft(moveAmt);
    }

    /**
     *  Reverses direction.
     **/
//     protected void changeDir()
    public void changeDir()
    {
        this.facingRight = ! this.facingRight;
    }

}

