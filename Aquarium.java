
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * Aquarium Lab Series: <br>
 *  The Aquarium class defines an Aquarium and its properties.  An Aquarium
 *  object provides methods that report the aquarium's dimensions and that
 *  return random valid coordinates.  These methods are useful for creating a
 *  new fish and for moving fish, to make sure that they are always fully
 *  within the aquarium.  An Aquarium object also keeps track of the fish in
 *  the aquarium, with methods for adding a new fish or returning a list of
 *  all the fish.  The latter method is used by the display to display all
 *  the fish when it displays the aquarium.
 * <br> <br>
 * Created: <br>
 *   10 July 2002,  Alyce Brady<br>
 * <br>
 * Modifications: <br>
 *   22 March 2008, Alyce Brady, Added randomCenterX and randomCenterY
 *                               methods, moving that logic from the old
 *                               AquaFish class to the Aquarium class.<br>
 *   23 March 2008, Alyce Brady, Added list of fish to the aquarium to simplify
 *                               the display of an aquarium containing fish.<br>
 *  @author  Alyce Brady
 *  @version 23 March 2008
 **/
public class Aquarium
{
    // STATE

    // Instance Variables: Encapsulated data (or state) of an aquarium
    private int myWidth;            // width of the aquarium
    private int myHeight;           // height of the aquarium
    private int borderPadding;      // space to leave around edge of aquarium
    private Color myColor;          // background color to represent aquarium
    private ArrayList<AquaFish> listOfFish;         // fish in the aquarium
    private final Random generator = new Random();  // Random number generator

    // OPERATIONS (constructor and methods)

    /**
     *  Constructs an Aquarium with user-specified size.
     *  @param    width    width of the aquarium when displayed (in pixels)
     *  @param    height   height of the aquarium when displayed (in pixels)
     */
    public Aquarium(int width, int height)
    {
        if (width > 0)
            myWidth = width;
        else
            myWidth = 640;
        if (height > 0)
            myHeight = height;
        else
            myHeight = 480;

        borderPadding = 10;

        myColor = new Color(0.0f, .6f, 1.0f);

        listOfFish = new ArrayList<AquaFish>();
    }

    /**
     *  Adds the given fish to this aquarium.
     *  @param  fish    the fish to add to this aquarium
     */
    public void add(AquaFish fish)
    {
        listOfFish.add(fish);
    }

    /**
     *  Determines the width of the aquarium.
     *  @return    the width of the aquarium
     */
    public int width()
    {
        return myWidth;
    }

    /**
     *  Determines the height of the aquarium.
     *  @return    the height of the aquarium
     */
    public int height()
    {
        return myHeight;
    }

    /**
     *  Determines the color of the aquarium (water color).
     *  @return    the Color of the aquarium
     */
    public Color color()
    {
        return myColor;
    }

    /**
     *  Returns a list of the fish in this aquarium.
     *    @return  list of fish in this aquarium
     */
    public ArrayList<AquaFish> getFish()
    {
        return listOfFish;
    }

    /**
     *  Determines whether the given coordinates specify
     *      a valid location (one that exists within the bounds of the
     *      aquarium).
     *  @param     xCoord   x coordinate of location to be checked
     *  @param     yCooord  y coordinate of location to be checked
     *  @return    true if the specified location is within the bounds
     *             of the aquarium
     */
    public boolean validLoc(int xCoord, int yCoord)
    {
        if ((0 <= xCoord && xCoord < myWidth) && 
                (0 <= yCoord && yCoord < myHeight))
            return true;
        return false;
    }

    /**
     *  Determines a valid random X coordinate along the x axis to be used
     *  for the centerpoint of an object with the given length.
     *  Precondition: this aquarium must be big enough to accomodate
     *  the object with the given length, plus 10 pixels of padding in
     *  each direction.
     *  @param objectLength length of object to be placed in aquarium
     *  @return a random X coordinate that could be used as a part of a
     *  valid centerpoint for an object of the given length in this aquarium
     */
    public int randomCenterX(int objectLength)
    {
        // The entire object should fit within the aquarium, so its
        // center x coordinate should be in the range.
        //   halfLength ... (aquariumWidth - halfLength)
        // where halfLength is half the side-to-side length or width
        // of the object.  We also want some padding on each side, so
        // the actual range is
        //   (halfLength + borderPadding) ...
        //                  (aquariumWidth - halfLength - borderPadding)
        // The size of the range, then, is
        //   aquariumWidth - length - 2 * borderPadding
        int rangeSize = this.width() - objectLength - (2 * borderPadding);
        int x = generator.nextInt(rangeSize);

        // Shift the range right so it starts at halfLength + borderPadding.
        int halfLength = (int)Math.round(objectLength/2.0);
        x += (halfLength + borderPadding);
        return x;
    }

    /**
     *  Determines a valid random y coordinate along the y axis to be used
     *  for the centerpoint of an object with the given width or height.
     *  Precondition: this aquarium must be big enough to accomodate
     *  the object with the given height, plus 10 pixels of padding
     *  above and below.
     *  @param objectHeight height of object to be placed in aquarium
     *  @return a random Y coordinate that could be used as a part of a
     *  valid centerpoint for an object of the given height in this aquarium
     */
    public int randomCenterY(int objectHeight)
    {
        // The entire object should fit within the aquarium, so its
        // center y coordinate should be in the range.
        //   halfHeight ... (aquariumHeight - halfHeight)
        // where halfHeight is half the top-to-bottem width or height
        // of the object.  We also want some padding on each above and
        // below, so the actual range is
        //   (halfHeight + borderPadding) ...
        //                  (aquariumHeight - halfHeight - borderPadding)
        // The size of the range, then, is
        //   aquariumHeight - height - 2 * borderPadding
        int rangeSize = this.height() - objectHeight - (2 * borderPadding);
        int y = generator.nextInt(rangeSize);

        // Shift the range down so it starts at halfHeight + borderPadding.
        int halfHeight = (int)Math.round(objectHeight/2.0);
        y += (halfHeight + borderPadding);
        return y;
    }

}    //end Aquarium class
