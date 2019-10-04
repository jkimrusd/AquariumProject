
import java.awt.Color;
import java.util.Random;


/**
 * Aquarium Lab Series: <br>
 *      The NavigationalAide class defines some of the more complex methods
 *      for keeping track of a fish's size and location in an aquarium.
 * <br> <br>
 * Created: <br>
 *   23 March 2008, Alyce Brady, from the previous version of AquaFish.<br>
 * Modifications: <br>
 *   18 January 2009, Alyce Brady, moved determination of direction to AquaFish.<br>
 * <br>
 * 
 * @author Alyce Brady
 * @version 18 January 2009
 */
public class NavigationalAide
{
    // STATE

    // Instance Variables: Encapsulated data for EACH fish
    private AquaFish theFish;        // the fish this aide is helping
    private int centerX;             // x-coordinate of fish's centerpoint
    private int centerY;             // y-coordinate of fish's centerpoint
    private int length, height; // define size of fish
    private int halfLength, halfHeight; // useful for knowing perimeter of fish

    // OPERATIONS (constructor and methods)

    /**
     *  The NavigationalAide constructor sets properties of the fish.
     *  Precondition: the aquarium must be big enough to accomodate
     *  the biggest fish (currently 75 pixels long and 30 pixels high)
     *  plus 10 pixels of padding in all four directions.
     *  @param    fish   the fish whose size and location this aide is
     *                      keeping track of
     **/
    public NavigationalAide(AquaFish fish)
    {
        // Keep track of which fish we are dealing with.
        theFish = fish;

        // Initialize size, position, and direction.
        initSize();
        initPos();
    }

    /**
     *  Initializes fish size:
     *  This helper function determines the height and length of the fish.
     *  Fish are evenly distributed among 4 different sizes based on their 
     *  ID numbers.
     **/
    private void initSize()
    {
        // Possible fish lengths are: ?, ?, ?, and ?.
        // The height of a fish is always 40% of its length.
        length = 30 + (theFish.id() % 4) * 15;
        height = (int)Math.round(0.4*length);

        // The halfLength and halfHeight instance variables are useful to
        // determine the left, right, top, and bottom edges of the fish,
        // starting from the centerpoint indicated by (centerX, centerY).
        halfLength = (int)Math.round(length/2.0);
        halfHeight = (int)Math.round(height/2.0);
    }

    /**
     *  Initializes fish position and direction.
     *  This helper function assigns coordinates to a fish such that the
     *  fish is placed within the bounds of the Aquarium.
     *  Precondition: the aquarium must be big enough to accomodate
     *  the biggest fish (currently 75 pixels long and 30 pixels high)
     *  plus 10 pixels of padding in all four directions.
     **/
    private void initPos()
    {
        // Initialize my position and direction.
        centerX = theFish.aquarium().randomCenterX(length);
        centerY = theFish.aquarium().randomCenterY(height);
    }

    /**
     *  Gets the x coordinate in the aquarium of the fish's centerpoint.
     *  @return    the x coordinate of the fish's centerpoint
     **/
    public int centerpointX()
    {
        return centerX;
    }

    /**
     *  Gets the y coordinate in the aquarium of the fish's centerpoint.
     *  @return    the y coordinate of the fish's centerpoint
     **/
    public int centerpointY()
    {
        return centerY;
    }

    /**
     *  Determines whether the fish is facing right.
     *  @return     <code>true</code> if fish is facing right;
     *              <code>false</code> otherwise
     **/
    public boolean isFishFacingRight()
    {
        return theFish.isFacingRight();
    }

    /** Gets the length of the fish.
     *  @return   fish length
     **/
    public int fishLength()
    {
        return length;
    }

    /** Gets the height of the fish.
     *  @return    fish height
     **/
    public int fishHeight()
    {
        return height;
    }

    /** Gets half the length of the fish.
     *  @return    half the fish length (rounded if necessary)
     **/
    public int halfFishLength()
    {
        return halfLength;
    }

    /** Gets half the height of the fish.
     *  @return    half the fish height (rounded if necessary)
     **/
    public int halfFishHeight()
    {
        return halfHeight;
    }

    /**
     *  Compute how far the fish is from the wall in front of it.
     *  @return    distance from front of fish to facing wall
     **/
    protected int fishDistanceToWall()
    {
        int leftEdgeOfFish = centerX - (halfLength + 1);
        int rightEdgeOfFish = centerX + (halfLength + 1);
        if ( isFishFacingRight() )
            return (theFish.aquarium().width() - rightEdgeOfFish);
        else
            return leftEdgeOfFish;    // since left edge of aquarium is 0
    }

    /**
     *  Determine whether the fish is at the surface.
     *  A fish is considered at the surface if it cannot ascend; in other
     *  words, if the distance from the center of the fish to the surface
     *  is less than the fish's height.
     *  @return    <code>true</code> if fish is at the surface;
     *             <code>false</code> otherwise
     **/
    public boolean fishAtSurface()
    {
        int topOfFish = centerY - (halfHeight + 1);
        return (topOfFish <= height);
    }

    /**
     *  Determine whether the fish is at the bottom.
     *  A fish is considered at the bottom if it cannot descend; in other
     *  words, if the distance from the center of the fish to the bottom
     *  is less than the fish's height.
     *  @return    <code>true</code> if fish is at the bottom;
     *             <code>false</code> otherwise
     **/
    public boolean fishAtBottom()
    {
        int bottomOfFish = centerY + (halfHeight + 1);
        return (bottomOfFish >= (theFish.aquarium().height() - height));
    }

    /**
     *  This function is provided primarily for debugging purposes.
     *  @return    a string representation of a fish
     **/
    public String toString()
    {
        String dir = "L";
        if ( isFishFacingRight() )
            dir = "R";
        return " (" + centerX + ", " + centerY + ") " + dir + " ";
    }

    /** Moves the fish <code>distance</code> units to the right.
     *  @param  distance   distance to move right
     **/
    protected void moveFishRight(int distance)
    {
        centerX += distance;
    }

    /** Moves the fish <code>distance</code> units to the left.
     *  @param  distance   distance to move left
     **/
    protected void moveFishLeft(int distance)
    {
        centerX -= distance;
    }

    /** Moves the fish <code>distance</code> units up.
     *  @param  distance   distance to move up
     **/
    protected void raiseFish(int distance)
    {
        centerY -= distance;        // y coordinates get smaller going up
    }

    /** Moves the fish <code>distance</code> units down.
     *  @param  distance   distance to move down
     **/
    protected void sinkFish(int distance)
    {
        centerY += distance;        // y coordinates get bigger going down
    }

}
