package controller;

import dao.SpaceInvadersDAOText;
import dao.UserDetailsDAOText;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.*;
import model.events.EntityDiedEvent;
import model.events.EntityEventListener;
import model.events.EntityHitEvent;
import model.events.EntityMoveEvent;
import view.UserEvent;
import view.UserEventListener;
import view.UserViewInterface;

/**
 * The main hook of our game. This class will both perform the necessary updates directly on the display of the actual
 * game playing area and act as a manager for the user control interface (the VIEW) and central mediator for the game
 * logic. It is the CONTROLLER.
 *
 * In presents the game playing area and contains the game loop that cycles round all entities in the game asking them
 * to update their state (at least updateState) and then drawing them in the appropriate place. With the help of an
 * inner class it will also allow the player to control the main ship (updateState and shoot).
 *
 * As the Controller it will be informed when the user wants something to happen via the user control interface and will
 * also respond when entities within our game detect events (e.g. alien killed, player died) and will take appropriate
 * game actions.
 *
 * @author Santi Ruiz based on work by Kevin Glass
 */
public class Game implements Runnable, EntityEventListener, UserEventListener {

    /**
     * Keep a list of events that occur while the entities are updating their state to be processed after the game View
     * is updated each time through the game loop
     */
    private java.util.List<EventObject> gameEvents = null;
    /**
     * The strategy that allows us to use accelerated page flipping
     */
    private BufferStrategy strategy;
    /**
     * True if the game is currently "running", i.e. the game loop is looping
     */
    private boolean gameRunning = true;
    /**
     * The entities in this game
     */
    private ShipEntity ship;
    private AlienArmyArray aliens;
    private EntityListInterface shotsFiredByShip;
    private BorderEntity rightBorder;
    private BorderEntity leftBorder;
    private BorderEntity bottomBorder;
    private BorderEntity topBorder;
    /**
     * The time at which last fired a shot
     */
    private long lastPlayerFire = 0;
    private long lastAlienFire = 0;
    /**
     * Double barrel shots off by default
     */
    private boolean doubleShot = false;
    /**
     * Flags to keep track of movement key presses
     */
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    /**
     * True if we are firing
     */
    private boolean firePressed = false;
    /**
     * Flag for pausing the game - no time is elapsing when paused is true
     */
    private boolean paused = true;
    /**
     * The canvas used for drawing the actual game and the panel that canvas will be in (as distinct from the user
     * interface that allows user to change settings etc.)
     */
    private Canvas gameCanvas;
    private JPanel theGamePanel;
    /**
     * The entity objects currentGame - details of the current game eg shots fired userDetails - details for the user eg
     * their name and high score gameSettings - details of the settings for playing eg. updateState speed
     */
    private GameDetails currentGame;
    private UserViewInterface theView;
    /**
     * backgroundImg stores the background of the playing area
     */
    private Image backgroundImg;
    /**
     * keep track of game state
     */
    private boolean gameOver = false;

    /**
     * Construct our game.
     *
     * @param userDetails User details object
     * @param gameSettings game setting object
     * @param userControlPanel
     * @throws SQLException
     */
    public Game(UserDetails userDetails, GameSettings gameSettings, UserViewInterface userControlPanel) throws
        SQLException, Exception {

        this.theView = userControlPanel;
        theGamePanel = new JPanel();

        // Set up the model. Users details and initial game settings are provided. The details for the game
        // about to be played are stored in currentGame.
        currentGame = new GameDetails(userDetails, gameSettings);
        currentGame.resetTheScoringDetails();
        // Ask the GUI to update itself
        this.theView.setUpForNewGame(currentGame);
        this.theView.adjustInterfaceOptionsDueToWantingIntroInfoChange(userDetails.isWantingIntroInfo());

        // set up the list of game playing events (ships crashing, shots being fored etc.)
        gameEvents = new LinkedList<EventObject>();

        // set up the resolution of the game
        theGamePanel.setPreferredSize(new Dimension(800, 520));
        theGamePanel.setLayout(null);

        // setup our canvas for displaying the sprites
        gameCanvas = new Canvas();
        gameCanvas.setBounds(0, 0, 800, 520);
        gameCanvas.setVisible(true);

        theGamePanel.add(gameCanvas);

        // attempt to load the background image
        backgroundImg = loadBG();

        // load sounds
        loadBackGroundGameSound();

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that ourselves in accelerated mode
        gameCanvas.setIgnoreRepaint(true);

        // add a key input system (defined below) to our canvas
        // so we can respond to key presses for moving and shooting
        gameCanvas.addKeyListener(new KeyInputHandler());
        setUpForNewGame();
    }

    /**
     * loadBG
     *
     * attempt to load a background image
     *
     * @return Image
     */
    private Image loadBG() {
        Image image = null;
        BufferedImage sourceImage;
        try {
            // The ClassLoader.getResource() ensures we get the sprite
            // from the appropriate place, this helps with deploying the game
            // with things like webstart. You could equally do a file look
            // up here.
            URL url = this.getClass().getClassLoader().getResource("sprites/bg.gif");
            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
            // create an accelerated image of the right size to store our sprite in
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
            image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
            // draw our source image into the accelerated image
            image.getGraphics().drawImage(sourceImage, 0, 0, null);

        } catch (IOException ex) {
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE, "Unable to open background image file", ex);
        }
        return image;
    }

    /**
     * Get all the game sounds ready to use
     */
    private void loadBackGroundGameSound() {
    }

    /**
     * run the game
     */
    public void run() {
        // Start the main game loop, note: this method will not
        // return until the game has finished running. We are now
        // running in its own thread
        gameLoop();
    }

    public final void setUpForNewGame() {

        // New game is always paused until user starts  it via user contol view
        setPaused(true);

        // Get rid of any left over game events
        gameEvents.clear();

        // re initialise entities
        initEntities();

        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        firePressed = false;

        //get ready for starting the new game
        gameOver = false;

        try {
            // Clear out game Details
            currentGame.resetTheScoringDetails();
        } catch (Exception ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Ask the view to update itself accordingly
        theView.setUpForNewGame(this.currentGame);
    }

    /**
     * Initialise the starting state of the entities (ship,aliens, shots and borders).
     */
    private void initEntities() {
        // create the player ship and place it roughly in the center of the screen
        ship = new ShipEntity(370, 470);
        ship.addEntityEventListener(this);

        //create the alien army and register as a listener for events
//    aliens = new AlienArmyLinked(this, 5, 12);
        aliens = new AlienArmyArray(this, 5, 12);
        aliens.addEntityEventListener(this);

        //create the list of shots
        //   shotsFiredByShip = new EntityListLinked();
        shotsFiredByShip = new EntityListArray();
        shotsFiredByShip.addEntityEventListener(this);

        rightBorder = new BorderEntity("RIGHT", 800, 0, 10, 650);
        rightBorder.addEntityEventListener(this);
        leftBorder = new BorderEntity("LEFT", 0, 0, 10, 650);
        leftBorder.addEntityEventListener(this);
        bottomBorder = new BorderEntity("BOTTOM", 0, 510, 850, 10);
        bottomBorder.addEntityEventListener(this);
        topBorder = new BorderEntity("TOP", 0, 0, 850, 10);
        topBorder.addEntityEventListener(this);
    }

    /**
     * Every time an entity is moved we need to check if there is a collision with another entity in this game. Note the
     * move may have caused multiple collisions, except for borders only the first one is recorded
     *
     * @param e EntityMoveEvent object
     */
    private void checkForCollision(EntityMoveEvent e) {
        Entity theEntity;
        Entity otherEntity = null;

        // An alien can't collide with another alien in this game so only check for
        // certain collisions
        theEntity = e.getEntity();

        if (theEntity instanceof AlienEntity) {
            otherEntity = checkAlienMoveCollision((AlienEntity) theEntity);
        } else // check if ship has collided with one of the aliens
        {
            if (theEntity instanceof ShipEntity) {
                otherEntity = checkShipMoveCollision((ShipEntity) theEntity);
            } else // check if shot has collided with one of the aliens
            {
                if (theEntity instanceof ShotEntity) {
                    otherEntity = checkShotMoveCollision((ShotEntity) theEntity);
                }
            }
        }

        // Inform all that want to know that the collision has occured (usually the entity will
        // have a reaction to the collision and sometime the game will - both would be registered
        // as listeners for this event)
        if (otherEntity != null) {
            // Ensure ship collision is registered first as the event
            if (theEntity instanceof ShipEntity) {
                theEntity.fireCollisionHasOccured(new EntityHitEvent(theEntity, otherEntity));
                otherEntity.fireCollisionHasOccured(new EntityHitEvent(otherEntity, theEntity));
            } else {
                otherEntity.fireCollisionHasOccured(new EntityHitEvent(otherEntity, theEntity));
                theEntity.fireCollisionHasOccured(new EntityHitEvent(theEntity, otherEntity));
            }
        }
        checkCollisionsWithBorders(theEntity);
    }

    private Entity checkShotMoveCollision(ShotEntity shotEntity) {
        Entity collidedWith = null;

        // If it is an AlienShotEntity then only check for collision with ship not
        // other aliens
        if (shotEntity instanceof AlienShotEntity) {
            if (shotEntity.collidesWith(ship)) {
                collidedWith = ship;
            }
            if (collidedWith == null) {
                collidedWith = shotsFiredByShip.collidesWith(shotEntity);
            }
        } else {
            // Otherwise it must be a shot by the ship hence need to check for collisions
            // with aliens.
            collidedWith = aliens.collidesWith(shotEntity);
        }

        return collidedWith;
    }

    // check if alien has collided with either the mother ship or a shot
    private Entity checkAlienMoveCollision(AlienEntity theAlien) {
        Entity collidedWith = null;

        if (theAlien.collidesWith(ship)) {
            collidedWith = ship;
        }
        if (collidedWith == null) {
            collidedWith = shotsFiredByShip.collidesWith(theAlien);
            // Ignore alien shots that register as colliding with other aliens
            if (collidedWith instanceof AlienShotEntity) {
                collidedWith = null;
            }
        }
        return collidedWith;
    }

    private Entity checkShipMoveCollision(ShipEntity ship) {
        Entity collidedWith;

        collidedWith = aliens.collidesWith(ship);
        if (collidedWith == null) {
            collidedWith = shotsFiredByShip.collidesWith(ship);
        }
        return collidedWith;
    }

    private void checkCollisionsWithBorders(Entity entity) {
        Entity collidedWith;

        if (entity.collidesWith(rightBorder)) {
            entity.fireCollisionHasOccured(new EntityHitEvent(entity, rightBorder));
            rightBorder.fireCollisionHasOccured(new EntityHitEvent(rightBorder, entity));
        }

        if (entity.collidesWith(leftBorder)) {
            entity.fireCollisionHasOccured(new EntityHitEvent(entity, leftBorder));
            leftBorder.fireCollisionHasOccured(new EntityHitEvent(leftBorder, entity));
        }

        if (entity.collidesWith(bottomBorder)) {
            entity.fireCollisionHasOccured(new EntityHitEvent(entity, bottomBorder));
            bottomBorder.fireCollisionHasOccured(new EntityHitEvent(bottomBorder, entity));
        }

        if (entity.collidesWith(topBorder)) {
            entity.fireCollisionHasOccured(new EntityHitEvent(entity, topBorder));
            topBorder.fireCollisionHasOccured(new EntityHitEvent(topBorder, entity));
        }
    }

    /**
     * Attempt to fire a shot from the player. Its called "try" since we must first check that the player can fire at
     * this point, i.e. has he/she waited long enough between shots
     */
    public void tryToFire() {
        ShotEntity shot;
        // check that we have waited between shots long enough to be allowed to fire
        if (System.currentTimeMillis() - lastPlayerFire < currentGame.getGameSettings().getFiringInterval()) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastPlayerFire = System.currentTimeMillis();
        int shotLocationX;
        int shotLocationY;

        // Ensure shot is positioned away form the shooter!
        shotLocationX = ship.getX() + ship.getSprite().getWidth() / 2;
        shotLocationY = ship.getY() - (ship.getSprite().getHeight() + 10);

        if (doubleShot) {
            shot = new ShotEntity(shotLocationX, shotLocationY);
            shotsFiredByShip.add(shot);
            shot = new ShotEntity(shotLocationX + 20, shotLocationY);
            shotsFiredByShip.add(shot);
            currentGame.setShotsFired(currentGame.getShotsFired() + 2);
        } else {
            shot = new ShotEntity(shotLocationX, shotLocationY);
            shotsFiredByShip.add(shot);
            shot.addEntityEventListener(this);
            currentGame.setShotsFired(currentGame.getShotsFired() + 1);
        }
        currentGame.setScore(currentGame.calculateScoreChanges());
        theView.displayGameDetails();
    }

    /**
     * Randomly choose an alien to fire at the players ship. Only allow this a a certain speed (ie time between shots)
     */
    public void randomAlienFire() {
        ShotEntity shot;
        Entity alien;

//    if (true) {
//      return;
//    }
        // check that we have waited between shots long enough to be allowed to fire
        // TODO: The time (ms) between alien shots should not be hard-coded here. It should
        // be part of the GameSettings.
        if (System.currentTimeMillis() - lastAlienFire < 1000) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastAlienFire = System.currentTimeMillis();

        // Choose the alien that will shoot
        alien = this.aliens.getRandom();

        if (alien != null) {
            shot = new AlienShotEntity(alien.getX() + 10, alien.getY() + 10, 300);
            shotsFiredByShip.add(shot);
            shot.addEntityEventListener(this);
        }
    }

    /**
     * The main game loop. This loop is running during all game play and is responsible for the following activities:
     * <p>
     * - Working out the elapsed time (in milliseconds) to update moves - Moving the game entities - Drawing the screen
     * contents (entities, text) - Updating game events
     * <p>
     */
    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        gameCanvas.createBufferStrategy(2);
        strategy = gameCanvas.getBufferStrategy();

        // keep looping until the game ends
        while (gameRunning) {

            // work out how long its been since the last update (delta), this
            // will be used to calculate how far the entities should
            // updateState this loop
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            //reset delta to zero if paused otherwise ensure canvas has the focus
            //for keyboard game control
            if (isPaused()) {
                delta = 0;
            } else {
                this.gameCanvas.requestFocus();
            }

            // Get hold of a graphics context for the accelerated
            // surface and draw bg or blank it out if no bg loaded
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            if (backgroundImg == null) {
                g.setColor(Color.black);
                g.fillRect(0, 0, 800, 520);
            } else {
                g.drawImage(backgroundImg, 0, 0, null);
            }

            // Move the ship, aliens and shots if any time has lapsed
            if (delta != 0) {
                ship.updateState(delta);
                aliens.updateState(delta);
                shotsFiredByShip.updateState(delta);
            }

            // cycle round drawing all the entities we have in the game
            ship.draw(g);
            aliens.draw(g);
            shotsFiredByShip.draw(g);
//      rightBorder.draw(g);
//      leftBorder.draw(g);
            bottomBorder.draw(g);

            // finally, we've completed drawing so clear up the graphics
            // and flip the buffer over
            g.dispose();
            strategy.show();

            // Now that the user has seen the next state of the game we can process the events that
            // have occurred since the last state was displayed
            this.processGameEvents();

            // resolve the movement of the ship. First assume the ship
            // isn't moving. If either cursor key is pressed then
            // update the movement appropriately
            ship.setHorizontalMovement(0);
            ship.setVerticalMovement(0);

            double moveSpeed;
            moveSpeed = currentGame.getGameSettings().getMoveSpeed();

            if ((leftPressed) && (!rightPressed)) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed)) {
                ship.setHorizontalMovement(moveSpeed);
            } else if ((upPressed) && (!downPressed)) {
                ship.setVerticalMovement(-moveSpeed);
            } else if ((downPressed) && (!upPressed)) {
                ship.setVerticalMovement(moveSpeed);
            }

            // if we're pressing fire, attempt to fire
            if (firePressed) {
                tryToFire();
            }
            if (!isPaused()) {
                randomAlienFire();
            }

        } //while game running

    }

    /**
     *
     * @return The panel the game is played in.
     */
    public JPanel getGamePanel() {
        return this.theGamePanel;
    }

    /**
     * An important method as it goes through the list of events that the entities have caused in the last game time
     * cycle (last time through the game loop). This is part of being the Controller. For example only the game can
     * detect entities colliding as it is the one that knows what entities there are in the game. Only the game knows if
     * its a win or lose situation for the user.
     *
     * The list is treated like a queue - the first event that got into the list will be the first event that is
     * processed and so on. Note that processing an event may in itself add something to the event queue. Those events
     * will be actioned in the next time cycle (game loop iteration).
     */
    private void processGameEvents() {
        int i;
        EventObject e;
        int numEvents;

        boolean weWon = false;

        // We need to use a local copy of the size of the of the list as its size may be changed by the
        // events themselves
        numEvents = gameEvents.size();
        i = 0;
        gameOver = false;
        while (!gameOver && i < numEvents) {
            e = gameEvents.get(0);
            // Something moved - check for collision with other game enitites
            if (e instanceof EntityMoveEvent) {
                checkForCollision((EntityMoveEvent) e);
            }
            // Something died - we may be finished
            if (e instanceof EntityDiedEvent) {
                if (e.getSource() instanceof AlienEntity) {
                    // We have destroyed a ship!
                    // Update the gameDetails data and ask the view to update
                    currentGame.setShipsDestroyed(currentGame.getShipsDestroyed() + 1);
                    currentGame.setScore(currentGame.calculateScoreChanges());
                    theView.displayGameDetails();

                    // We have destroyed all ships so game is over - no need to process any more game events
                    if (aliens.getNumEntities() == 0) {
                        gameOver = true;
                        weWon = true;
                    }
                } // We have been destroyed so game is over - no need to process any more game events
                else if (e.getSource() instanceof ShipEntity) {
                    //SR - need a new event - EntityStartsToDie event
                    if (((Entity) e.getSource()).getCurrentState() == State.DEAD) {
                        gameOver = true;
                        weWon = false;
                    }
                }
            }
            gameEvents.remove(0);
            i++;
        }

        // After all events, if the game is over then update the view accordingly and start a new game
        if (gameOver) {
            if (weWon) {
                theView.notifyWin();
            } else if (ship.getCurrentState() == State.DEAD) {
                theView.notifyDeath();
            } else {
                // Aliens have got to end (or some other game event has finished us off!)
                theView.notifyGameOver();
            }

            // see if new high score achieved
            if (currentGame.isNewHighScore()) {
                theView.notifyNewHighScore();
                saveCurrentGameResults(new UserEvent(this));
            }

            setUpForNewGame();

        }
    }

    /**
     * ****************Entity Event Handling Methods *************************
     */
    /**
     * Something in the game has died so register this as a game event (by adding it to the game events queue) so that
     * the game can respond (will need to check if its the ship which means we LOSE or if its the last alien which means
     * we WIN!)
     *
     * @param e The updateState events details
     */
    public void entityDied(EntityDiedEvent e) {
        this.gameEvents.add(e);
    }

    /**
     * Something in the game has moved so register this as an event (by adding it to the game events queue) so that the
     * game can respond (will need to check for collision with other game entities)
     *
     * @param e The updateState events details
     */
    public void entityMoved(EntityMoveEvent e) {
        this.gameEvents.add(e);
    }

    /**
     * Only game logic needed when an entity hits another entity is when an AlienEntity hits the bottom border. Then the
     * game finishes.
     *
     * @param e EntityHitEvent
     */
    @Override
    public void collisionHasOccured(EntityHitEvent e) {
        // If a collision between an Alien and the bottom border is detected
        if ((e.getHitBy() == bottomBorder && e.getHit() instanceof AlienEntity)
            || (e.getHitBy() instanceof AlienEntity && e.getHit() == bottomBorder)) {
            this.gameEvents.add(new EntityDiedEvent(ship));

            //gameOver = true;
        }
    }

    /**
     * ****************************** Features ********************************
     */
    /**
     * Exits the game
     *
     * @param e a UserEvent
     */
    public void exitGame(UserEvent e) {
        this.gameRunning = false;
        endGame();
    }

    /**
     * *********************************************************************
     */
    /**
     * Method called as a result of a UserEvent - wants to start the game
     *
     * @param e details of the View that the event came from
     */
    @Override
    public void startPlaying(UserEvent e) {
        this.gameCanvas.requestFocusInWindow();
        this.gameCanvas.requestFocus();
        this.setPaused(false);
    }

    /**
     * Needed to be a UserEventListener Method called as a result of a UserEvent - wants to pause the game
     *
     * @param e details of the View that the event came from
     */
    @Override
    public void pauseGame(UserEvent e) {
        this.setPaused(true);
    }

    /**
     * This method is needed in order to be a UserEventListener Method called as a result of a UserEvent - user wants to
     * save the setting for this game
     *
     * @param e details of the View that the event came from but at the moment the data provide is not actually needed
     */
    public void saveCurrentGameResults(UserEvent e) {
        this.saveCurrentGameResults();
    }

    private void saveCurrentGameResults() {

        try {
            // Check if have new high score
            if (currentGame.calculateScoreChanges() > currentGame.getHighScore()) {
                currentGame.setHighScore(currentGame.calculateScoreChanges());
            }
            currentGame.saveData();
            theView.displayGameDetails();
        } catch (Exception Ex) {
            theView.displaySaveGameSettingsError();
        }

    }

    /**
     * changeFiringInterval
     *
     * @param e UserEvent
     */
    public void changeFiringInterval(UserEvent e) {
        this.currentGame.getGameSettings().setFiringInterval(e.getFiringInterval());
        this.currentGame.setHighScore(0);
        setUpForNewGame();
    }

    /**
     * changeMoveSpeed
     *
     * @param e UserEvent
     */
    @Override
    public void changeMoveSpeed(UserEvent e) {
        if (Math.abs((e.getMoveSpeed() - currentGame.getGameSettings().getMoveSpeed())) >= 0.01) {
            //if (theView.confirmResetGame()) {
            currentGame.getGameSettings().setMoveSpeed(e.getMoveSpeed());
            currentGame.setHighScore(0);
            setUpForNewGame();
            //}
        }
    }

    private void endGame() {
        System.exit(0);
    }

    public void undoLastSave(UserEvent userEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Gets the users high scores from the database then asks the View to display the high scores
     *
     * @param userEvent Contains the users details ie the username
     * @throws java.lang.Exception
     */
    @Override
    public void getUsersHighScores(UserEvent userEvent) throws Exception {
        java.util.List<GameDetails> highScores;
        UserDetails theUser;

        try {
            theUser = new UserDetails(userEvent.getUserName());
            highScores = UserDetailsDAOText.getHighScores(theUser);
            this.theView.displayUsersHighScores(theUser, highScores);
        } catch (SQLException ex) {
            this.theView.displayErrorDatabaseConnectionLost();
        }

    }

    /**
     * Gets the game high scores from the database then asks the View to display the high scores
     */
    @Override
    public void getAllHighScores() {
        java.util.List<GameDetails> allHighScores;
        try {
            allHighScores = SpaceInvadersDAOText.getGameHighScores();
            this.theView.displayAllHighScores(allHighScores);
        } catch (Exception ex) {
            this.theView.displayErrorDatabaseConnectionLost();
        }
    }

    @Override
    public void changeSettingForWantingIntroInfo(boolean wantIntroInfo) {
        UserDetails theUser;
        theUser = this.currentGame.getUserDetails();
        try {
            theUser.setWantingIntroInfo(wantIntroInfo);
            theUser.saveData();
            this.theView.adjustInterfaceOptionsDueToWantingIntroInfoChange(wantIntroInfo);
        } catch (Exception ex) {
            String message = " when trying to turn intro info " + (theUser.isWantingIntroInfo() ? " on" : " off");
            this.theView.displayErrorUnableToSaveUserDetails(message);
            Logger.getLogger(MyLogger.LOGGER_NAME).log(Level.SEVERE,
                "Could not save user details during organiseForNoMoreIntroScreen - " + message, ex);
        }
    }

    /**
     * *********************************************************************************************
     */
    /**
     * An inner class to handle keyboard input from the user. The class handles both dynamic input during game play,
     * i.e. left/right and shoot, and more static type input (i.e. press any key to continue)
     *
     * This has been implemented as an inner class to allow access to the outer classes state - such as the leftPressed,
     * rightPressed, waitingForKeyPress etc. variables.
     *
     * @author Santi Ruiz based on work by Kevin Glass
     */
    private class KeyInputHandler extends KeyAdapter {

        /**
         * Notification from AWT that a key has been pressed. Note that a key being pressed is equal to being pushed
         * down but *NOT* released. Thats where keyTyped() comes in.
         *
         * @param e The details of the key that was pressed
         */
        @Override
        public void keyPressed(KeyEvent e) {
            // if we're paused ignore key presses
            if (Game.this.paused) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }
        }

        /**
         * Notification from AWT that a key has been released.
         *
         * @param e The details of the key that was released
         */
        @Override
        public void keyReleased(KeyEvent e) {
            // if we're paused ignore key events
            if (Game.this.paused) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }

        /**
         * Notification from AWT that a key has been typed. Note that typing a key means to both press and then release
         * it.
         *
         * @param e The details of the key that was typed.
         */
        @Override
        public void keyTyped(KeyEvent e) {
            // if we hit escape, then quit the game
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                endGame();
            }
        }
    }

    /**
     * *********************** Setters and Getters ***************************
     */
    /**
     * Method returns the value of the variable paused.
     *
     * @return true if paused false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Method pauses the game to allow changes to the current game (eg firing interval) to be implemented.
     *
     * @param paused used to set whether game is paused or not
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
