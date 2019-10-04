
/*
 *  Aquarium Lab Series
 *
 *  Class: AquaSimGUI
 *
 *  License:
 *      This program is free software; you can redistribute it
 *      and/or modify it under the terms of the GNU General Public
 *      License as published by the Free Software Foundation.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 */

import edu.neu.ccs.gui.ActionsPanel;
import edu.neu.ccs.gui.BufferedPanel;
import edu.neu.ccs.gui.Display;
import edu.neu.ccs.gui.DisplayCollection;
import edu.neu.ccs.gui.DisplayPanel;
import edu.neu.ccs.gui.DisplayWrapper;
import edu.neu.ccs.gui.JPTFrame;
import edu.neu.ccs.gui.SimpleAction;
import edu.neu.ccs.gui.TextFieldView;
import edu.neu.ccs.util.JPTUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import javax.swing.Action;

/**
 *  Aquarium Lab Series: <br>
 *  The AquaSimGUI class provides a graphical user interface
 *  to the Aquarium Lab Series.  This class uses the Java
 *  Power Tools (JPT) classes from Northeastern University to
 *  build the graphical interface.  In particular, it inherits
 *  the <code>repaint</code> method (which does not appear in
 *  the specification for this class) from the JPT DisplayPanel
 *  class.  The <code>repaint</code> method draws the
 *  components in the graphical user interface.
 * <br> <br>
 * Created: <br>
 *   10 July 2002,  Alyce Brady<br>
 * <br>
 * Modifications: <br>
 *   22 March 2008, Alyce Brady, Simplified by removing references to
 *                               AquaPoint class and by folding many show
 *                               methods into one that shows an aquarium
 *                               and the fish it contains. <br>
 * 
 *  @author Alyce Brady
 *  @version 22 March 2008
 **/
public class AquaSimGUI extends DisplayPanel
{

    /////////////////////////////////////////////////////////
    // Static Data: Constants not tied to any one instance //
    /////////////////////////////////////////////////////////

    private static final int DEFAULT_FISH = 10;  // default # of fish
    private static final int DEFAULT_STEPS = 15; // # steps to run simulation
    private static final int VIEW_TIME = 1000;  // allow viewer to see display
    private static final int WAIT_TIME = 100;   // between Start button checks


    ////////////////////////
    // Instance Variables //
    ////////////////////////

    private Aquarium aqua = null;             // aquarium in which fish swim
    private AquaView drawingObject = null;    // to draw fish in aquarium
    private int numFish = DEFAULT_FISH;       // number of fish in aquarium
    private int numSteps = DEFAULT_STEPS;     // number of sim. steps to run
//    private Simulation simulation = null;     // controls timesteps
    private boolean started = false;          // has simulation started yet?


    /////////////////////////////////////////////
    // GUI Instance Variables:                 //
    //    Graphical User Interface components  //
    //    for controlling simulation execution //
    /////////////////////////////////////////////

    // Text area in which to display console-type output.
    private TextArea consoleOutput;

    // Display containing the control panel.
    Display controlPanelDisplay;

    // Text field to prompt for number of fish.
    private TextFieldView numFishTF =
        new TextFieldView(
            "" + DEFAULT_FISH,           // initial value displayed in the TFV
            "Number must be positive:",  // prompt for correcting input
            "Incorrect input");          // title for the error dialog box

    // Text field to prompt for number of simulation steps.
    private TextFieldView numStepsTF =
        new TextFieldView(
            "" + DEFAULT_STEPS,          // initial value displayed in the TFV
            "Number must be positive:",  // prompt for correcting input
            "Incorrect input");          // title for the error dialog box

    // Action button to start the simulation and action panel to put it in.
    private SimpleAction start =
        new SimpleAction("Start") {
           public void perform(){ start(); }
        };
    private Action[] startButtonList = {start};
    private ActionsPanel startPanel = new ActionsPanel(startButtonList);

    // Action buttons to execute one step of the simulation and to
    // run the simulation continuously, and action panel to put them in.
/*
    private SimpleAction step =
        new SimpleAction("Single Step") {
           public void perform(){ step(); }
        };
    private SimpleAction run =
        new SimpleAction("Run") {
           public void perform(){ run(); }
        };
    private Action[] runButtonsList = {step, run};
    private ActionsPanel runButtonsPanel = new ActionsPanel(runButtonsList);
*/

    //////////////////
    // Constructors //
    //////////////////

    /** Constructs a simple graphical user interface for the Aquarium
     *  Simulation program.
     *      @param  aquarium    the aquarium in which the fish swim
     **/
    public AquaSimGUI(Aquarium aquarium)
    {
        this(aquarium, false, false, false);
    }

    /** Constructs a simple graphical user interface for the Aquarium
     *  Simulation program, with or without prompts for the number of
     *  simulation steps.
     *      @param  aquarium    the aquarium in which the fish swim
     *      @param  promptForSimSteps   <code>true</code> if GUI should
     *                                  prompt for number of simulation steps
     **/
    public AquaSimGUI(Aquarium aquarium, boolean promptForSimSteps)
    {
        this(aquarium, promptForSimSteps, false, false);
    }

    /** Constructs a graphical user interface for the Aquarium
     *  Simulation program, with or without prompts for the number of
     *  simulation steps and the number of fish.
     *      @param  aquarium    the aquarium in which the fish swim
     *      @param  promptForSimSteps   <code>true</code> if GUI should
     *                                  prompt for number of simulation steps
     *      @param  promptForNumFish    <code>true</code> if GUI should
     *                                  prompt for number of fish
     **/
    public AquaSimGUI(Aquarium aquarium,
                      boolean promptForSimSteps,
                      boolean promptForNumFish)
    {
        this(aquarium, promptForSimSteps, promptForNumFish, false);
    }

    /** Constructs a graphical user interface for the Aquarium
     *  Simulation program, with or without prompts for the number of
     *  simulation steps and the number of fish.
     *      @param  aquarium    the aquarium in which the fish swim
     *      @param  promptForSimSteps   <code>true</code> if GUI should
     *                                  prompt for number of simulation steps
     *      @param  promptForNumFish    <code>true</code> if GUI should
     *                                  prompt for number of fish
     *      @param  useSimulationObj    <code>true</code> if GUI should
     *                                  construct and use a Simulation object
     **/
    private AquaSimGUI(Aquarium aquarium,
                      boolean promptForSimSteps,
                      boolean promptForNumFish,
                      boolean useSimulationObj)
    {
        // Save aquarium info. in an instance variable.
        aqua = aquarium;

        // Set layout for entire panel.
        setLayout(new BorderLayout());

        // Create two displays: one in which to view the aquarium
        // and one to contain the control panel.  Add them to the
        // main panel of the GUI.
        add(getViewWindow(), BorderLayout.EAST);
        add(getControlPanel(promptForSimSteps, promptForNumFish,
                            useSimulationObj), 
            BorderLayout.WEST);
        consoleOutput = new TextArea("", 5, 60);
        add(consoleOutput, BorderLayout.SOUTH);

        // Clear window.
        reset();
        
        // Put the GUI in a window, giving the window a title.
        JPTFrame.createQuickJPTFrame("Aquarium Lab Series", this);

        // Create the Simulation object (if appropriate) and tell the
        // control panel about it.
/*
        if ( useSimulationObj )
        {
            int numFish = getNumberOfFish();
            simulation = new Simulation (aqua, numFish, this);

            // View the initial configuration.
            // Draw the aquarium and fish, redisplay the user interface in the
            // window so that users can see what was drawn.
            show(simulation.getAllFish());
            repaint();
            pauseToView();
        }
 */
    }


    //////////////////////////////////////////////////////////
    // User Interaction Methods (Dealing with controlPanel) //
    //////////////////////////////////////////////////////////

    /**
     *  Waits for start button to be pushed.
     **/
    public void waitForStart()
    {
        while ( ! started )
            JPTUtilities.pauseThread(WAIT_TIME);
    }

    /**
     *  Gets the number of fish to put in the aquarium from user input.
     *      @return     the number of fish specified by the user
     **/
    public int getNumberOfFish()
    {
        waitForStart();
        return numFish;
    }

    /**
     *  Gets the number of steps to run from user input.
     *      @return     the number of steps specified by the user
     **/
    public int getNumberOfSteps()
    {
        waitForStart();
        return numSteps;
    }


    //////////////////////////////////////////////////
    // Method to write text to console-type output. //
    //////////////////////////////////////////////////

    /**
     *  Prints the given string to the console-type output window.
     *      @param  s   the string to print
     */
    public void print(String s)
    {
        consoleOutput.append(s);
    }

    /**
     *  Prints the given string to the console-type output window,
     *  followed by a newline.
     *      @param  s   the string to print, followed by a newline
     */
    public void println(String s)
    {
        consoleOutput.append(s + "\n");
    }


    //////////////////////////////////////////////////
    // Drawing Methods (Delegated to drawingObject) //
    //////////////////////////////////////////////////

    /**
     *  Displays the aquarium and its contents: paints the aquarium blue to
     *  cover up old fish, then paints the fish in their current locations.  
     **/
    public void showAquarium()
    {
        drawingObject.showAquarium();
    }

    /**
     *  Pauses so user can view the display.
     **/
    private void pauseToView()
    {
        JPTUtilities.pauseThread(VIEW_TIME);
    } 


    ////////////////////////
    // Actions            //
    ////////////////////////

    /** Starts the simulation.  (Activated by the start button.)
     **/
    public void start()
    {
        // Get the number of fish and the number of steps.
        numFish = numFishTF.demandInt();
        numSteps = numStepsTF.demandInt();

        // Record that simulation has started and modify what control
        // components are active.
        started = true;
        controlPanelDisplay.setEnabled(false);
//        runButtonsPanel.setEnabled(true);
    }

    /** Executes one step of the simulation.  (Activated by the step button.)
     */
/*
    public void step()
    {
        if ( simulation == null )
            return;

        // Execute a step of the simulation.
        simulation.step();
        
        // View the new configuration.
        show(simulation.getAllFish());
        repaint();
    }
*/

    /** Starts running the simulation.  (Activated by the run button.)
     **/
/*
    public void run()
    {
        if ( simulation == null )
            return;

        Thread myThread = new Thread()
        {
            public void run ()
            {
                runButtonsPanel.setEnabled(false);

                // Move the fish numSteps times.
                for ( int step = 0; step < numSteps; step++ )
                {
                    step();
                    pauseToView();
                }

                runButtonsPanel.setEnabled(true);
            }
        };

        myThread.start();
    }
*/


    //////////////////////////////
    // Private Helper Methods   //
    //////////////////////////////

    /**
     *  Constructs and initializes display in which to view aquarium.
     **/
    private Display getViewWindow()
    {
        // Create the panel in which to view the aquarium
        // and disable it (view panel is not interactive).
        // then put it in a display with a title.
        BufferedPanel aquaViewPanel =
            new BufferedPanel(aqua.width(), aqua.height());
        aquaViewPanel.setEnabled(false);

        // Construct an object that knows how to draw the
        // aquarium in the viewing panel (used by other parts
        // of the Aquarium Simulation program as well).
        drawingObject = new AquaView(aquaViewPanel, aqua);

        // Put the view panel in a titled display and return.
        return new Display(aquaViewPanel, null, "Aquarium");
    }

    /**
     *  Constructs and initializes display that contains control panel.
     *      @param  promptForSimSteps   <code>true</code> if GUI should
     *                                  prompt for number of simulation steps
     *      @param  promptForNumFish    <code>true</code> if GUI should
     *                                  prompt for number of fish
     *      @param  useSimulationObj    <code>true</code> if GUI should
     *                                  construct and use a Simulation object
     **/
    private Display getControlPanel(boolean promptForSimSteps,
                       boolean promptForNumFish, boolean useSimulationObj)
    {
        DisplayCollection controlPanel = new DisplayCollection();

        // Disable the control panel to start off.
        controlPanel.setEnabled(false);

        // Set up text field views in which to prompt for number
        // of fish and number of simulation steps.
        numFishTF.setPreferredWidth(50);
        numFishTF.getInputProperties().setSuggestion("" + DEFAULT_FISH);
        numStepsTF.setPreferredWidth(50);
        numStepsTF.getInputProperties().setSuggestion("" + DEFAULT_STEPS);

        // Add text field views if appropriate.
        if ( promptForNumFish )
        {
            numFishTF.setEnabled(true);
            controlPanel.add(new DisplayWrapper(
                    new Display(numFishTF, "Number of Fish:", null) ) );
        }
        if ( promptForSimSteps )
        {
            numStepsTF.setEnabled(true);
            controlPanel.add(new DisplayWrapper(
                    new Display(numStepsTF, "Number of Simulation Steps:", 
                                null) ) );
        }

        // Always include start button.
        startPanel.setEnabled(true);
        controlPanel.add(getStartPanel());

        // Add step and run buttons if appropriate.
/*
        if ( useSimulationObj )
        {
            runButtonsPanel.setEnabled(false);
            controlPanel.add(new Display(runButtonsPanel, null, "Run Simulation"));
        }
*/
        // Put the control panel in an untitled display and return.
        this.controlPanelDisplay = new Display(controlPanel, null, null);
        return this.controlPanelDisplay;
    }

    /** Constructs action panel for start button (in a separate thread).
     **/
    private Display getStartPanel()
    {
        // Create the Start action panel in a separate thread.
        Thread myThread = new Thread()
        {
            public void run ()
            {
                startPanel = new ActionsPanel(startButtonList);
            }
        };

        // Start parallel thread for start button.
        myThread.start();
        waitForStartPanel();
        return new Display(startPanel, null, null);
    }

    /**
     *  Waits for Start button action panel to be created.
     **/
    private void waitForStartPanel()
    {
        while ( startPanel == null )
            JPTUtilities.pauseThread(WAIT_TIME);
    }

    
    /** Aquarium Lab Series:
     *  An AquaView object provides a graphical view of fish
     *  in an aquarium.
     *
     *  @author  Alyce Brady
     *  @version 10 July 2002
     *  @see Aquarium
     *  @see BaseFish
     **/
    private class AquaView
    {
        // Encapsulated data
        private BufferedPanel displayPanel;   // where to display
        private Aquarium theAquarium;         // the aquarium to display
    
        /** Constructs an AquaView object to display a particular
         *  aquarium.  
         *      @param panel  the graphical panel in which to display environment
         *      @param a      the aquarium to display
         **/
        public AquaView(BufferedPanel panel, Aquarium a)
        {
            displayPanel = panel;
            theAquarium = a;
    
            displayPanel.setBackground(theAquarium.color());
    
        }
    
    
        /**
         *  Shows the fish in the aquarium.
         *  Paints the aquarium blue to cover up old fish and displays
         *  the fish in the aquarium.
         **/
        public void showAquarium()
        {
            // Redraw the environment to paint over previous positions of fish.
            displayPanel.fillPanel(theAquarium.color());

            // Get graphics context in which everything is displayed.
            Graphics2D drawingSurface = displayPanel.getBufferGraphics();
            drawingSurface.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    
            // Draw all of the fish.
            for ( AquaFish fish : theAquarium.getFish() )
            {
                if ( fish != null )
                    drawFish(drawingSurface, fish);
            }

            // Show the redrawn aquarium & fish on the screen.
            repaint();
            pauseToView();
        }
    
    
        /**
         *  Helper function that displays a single BaseFish, given
         *  a graphics context.
         *  @param    drawingSurface   context in which to display fish
         *  @param    fish             the fish to be displayed
         **/
        private void drawFish(Graphics2D drawingSurface, AquaFish fish)
        {        
            // Get color of fish.
            drawingSurface.setPaint(fish.color());
    
            // Get fish size and location from the fish itself.  Find its
            // outline based on the fish size and location.
            double fishLength = fish.length();
            double fishHeight = fish.height();
            double leftEndOfFish = fish.xCoord() - fishLength / 2.0;
            double topOfFish = fish.yCoord() - fishHeight / 2.0;
            double rightEndOfFish = leftEndOfFish + fishLength;
            double bottomOfFish = topOfFish + fishHeight;
            double verticalCenter = fish.yCoord();
            // Fish body parts are drawn to scale.
            double bodyLength = 0.8 * fishLength;
            double leftEndOfBody;        // value depends on fish's direction
    
            double eyeSize = 0.1 * fishLength;
            double topOfEye = verticalCenter - (0.1 * fishLength)
                              - eyeSize / 2;
            double leftEndOfEye;        // value depends on fish's direction
    
            double tailLength = 0.25 * fishLength;
            double tailHeightOffset = 0.12 * fishLength;
            double topOfTail = verticalCenter - tailHeightOffset;
            double bottomOfTail = verticalCenter + tailHeightOffset;
            double endOfTail;        // value depends on fish's direction
            double tailMeetsBody;    // value depends on fish's direction
    
            if (fish.isFacingRight())    //draw the fish facing right
            {
                leftEndOfBody = rightEndOfFish - bodyLength;
                leftEndOfEye = rightEndOfFish - 0.26 * fishLength;
                endOfTail = leftEndOfFish;
                tailMeetsBody = endOfTail + tailLength;
            }
            else
            {
                leftEndOfBody = leftEndOfFish;
                leftEndOfEye = leftEndOfFish + (0.26 * fishLength)
                                  - eyeSize;
                endOfTail = rightEndOfFish;
                tailMeetsBody = endOfTail - tailLength;
            }
    
            // Draw the body of the fish as an oval.
            Ellipse2D.Double body
                 = new Ellipse2D.Double(leftEndOfBody, topOfFish,
                     bodyLength, fishHeight);
            drawingSurface.fill(body);
    
            // Draw the tail as a triangle (filled path with three points).
            GeneralPath tailOutline = new GeneralPath();
            tailOutline.moveTo((float) endOfTail, (float) topOfTail);
            tailOutline.lineTo((float) endOfTail, (float) bottomOfTail);
            tailOutline.lineTo((float) tailMeetsBody, (float) verticalCenter);
            tailOutline.closePath();
            drawingSurface.fill(tailOutline);
    
            // Draw the eye as a small circle.
            drawingSurface.setPaint(Color.BLACK);
            Ellipse2D.Double eye
                 = new Ellipse2D.Double(leftEndOfEye, topOfEye,
                     eyeSize, eyeSize);
            drawingSurface.fill(eye);
         }
    
    }

}
