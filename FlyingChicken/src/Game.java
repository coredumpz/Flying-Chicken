import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class Game extends JPanel implements KeyListener, ActionListener, ComponentListener, WindowListener, MouseListener {

    private JFrame gameScreen;
    private GameInfo gameInfo;
    private int remainingTime = 3, remainingTimeFont, remainingTimeX, remainingTimeY, windowWidth, windowHeight;
    private Timer timer;
    private Random randomGenerator;
    private int timerCounter = 0;
    private CardLayout cardLayout;

    //Values for chicken
    private Chicken chicken;
    private Vector<GoldenEgg> eggs;
    private Vector<GoldenEgg> eggPool = new Vector<>();

    //Values for Cats
    private Vector<Cat> cats;
    private Vector<Cat> catPool = new Vector<>();

    //Values for Polygons
    Vector<Polygon> polygons;
    Vector<Polygon> polygonPool = new Vector<>();

    //Values For clouds
    Vector<Cloud> clouds;
    Vector<Cloud> cloudPool = new Vector<>();


    public Game(JFrame gameScreen1, GameInfo gameInfo1, CardLayout cardLayout1) {
        timer = new Timer(10 - gameInfo1.getDifficultyLevel(), Game.this);
        timer.setInitialDelay(10 - gameInfo1.getDifficultyLevel());
        timer.start();
        gameScreen1.setSize(700, 600);
        gameScreen1.setBounds(350, 100, 700, 600);
        EventQueue.invokeLater(() -> {
            setBackground(new Color(0, 87, 241));
            gameInfo = gameInfo1;
            cardLayout = cardLayout1;
            gameScreen = gameScreen1;
            clouds = new Vector<>();

            //Window values
            windowWidth = 500;
            windowHeight = 500;

            remainingTimeFont = (int) (windowHeight * 1.5);
            remainingTimeY = windowHeight - 10;
            remainingTimeX = windowWidth / 2 - (int) (remainingTimeFont / 6);

            //Cat Values
            randomGenerator = new Random();
            cats = new Vector<>();

            //Chicken Values
            chicken = new Chicken(windowWidth / 2, 10, true);
            chicken.setReloading(false);
            eggs = new Vector<>();

            //Polygon Values
            polygons = new Vector<>();


            gameScreen.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            gameScreen.addWindowListener(Game.this);
            Game.this.setVisible(true);

            createCounterThread().start();
            Game.this.addMouseListener(Game.this);
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (remainingTime > 0) {
            if (chicken != null)
                g.drawImage(Chicken.getChickenLeft(), chicken.getXCoordinate(), chicken.getYCoordinate(), Chicken.getChickenLeft().getWidth() / 5, Chicken.getChickenLeft().getHeight() / 5, this);
            g.setFont(new Font("Time", Font.PLAIN, remainingTimeFont));
            g.setColor(Color.pink);
            g.drawString(String.valueOf(remainingTime), remainingTimeX, remainingTimeY);
            remainingTimeFont -= 4;
            remainingTimeX += 1;
            remainingTimeY -= 1;
            return;
        }

        Iterator<Cloud> cloudIterator = clouds.iterator();
        while (cloudIterator.hasNext()) {
            Cloud cloud = cloudIterator.next();
            cloud.setYCoordinate(cloud.getYCoordinate() - 2);
            if (cloud.getYCoordinate() < -Cloud.getCloud().getHeight() / 2) {
                cloudPool.add(cloud);
                cloudIterator.remove();
            } else {
                g.drawImage(Cloud.getCloud(), cloud.getXCoordinate(), cloud.getYCoordinate(), windowWidth / 3, windowHeight / 3, this);
            }
        }

        if (chicken.isReloading()) {
            if (timerCounter % 20 == 0) {
                if (chicken.isChickenBlock()) {
                    g.drawImage(Chicken.getParachute(), chicken.getXCoordinate() - 20, chicken.getYCoordinate() - Chicken.getParachute().getHeight() / 8, Chicken.getParachute().getWidth() / 6, Chicken.getParachute().getHeight() / 6, this);
                }
                if (chicken.isInLeft()) {
                    g.drawImage(Chicken.getChickenLeft(), chicken.getXCoordinate(), chicken.getYCoordinate(), Chicken.getChickenLeft().getWidth() / 5, Chicken.getChickenLeft().getHeight() / 5, this);
                } else {
                    g.drawImage(Chicken.getChickenRight(), chicken.getXCoordinate(), chicken.getYCoordinate(), Chicken.getChickenRight().getWidth() / 5, Chicken.getChickenRight().getHeight() / 5, this);
                }
            }
        } else {
            if (chicken.isChickenBlock()) {
                g.drawImage(Chicken.getParachute(), chicken.getXCoordinate() - 20, chicken.getYCoordinate() - Chicken.getParachute().getHeight() / 8, Chicken.getParachute().getWidth() / 6, Chicken.getParachute().getHeight() / 6, this);
            }

            if (chicken.isInLeft()) {
                g.drawImage(Chicken.getChickenLeft(), chicken.getXCoordinate(), chicken.getYCoordinate(), Chicken.getChickenLeft().getWidth() / 5, Chicken.getChickenLeft().getHeight() / 5, this);
            } else {
                g.drawImage(Chicken.getChickenRight(), chicken.getXCoordinate(), chicken.getYCoordinate(), Chicken.getChickenRight().getWidth() / 5, Chicken.getChickenRight().getHeight() / 5, this);
            }
        }

        Iterator<Cat> catIterator = cats.iterator();
        while (catIterator.hasNext()) {
            Cat cat = catIterator.next();
            cat.setYCoordinate(cat.getYCoordinate() - 3);
            if (cat.getYCoordinate() < -50) {
                catPool.add(cat);
                catIterator.remove();
                continue;
            }
            if (cat.isInLeft()) {
                g.drawImage(Cat.getCatLeft(), cat.getXCoordinate(), cat.getYCoordinate(), Cat.getCatLeft().getWidth() / 4, Cat.getCatLeft().getHeight() / 4, this);
            } else {
                g.drawImage(Cat.getCatRight(), cat.getXCoordinate(), cat.getYCoordinate(), Cat.getCatRight().getWidth() / 4, Cat.getCatRight().getHeight() / 4, this);
            }
        }

        Iterator<GoldenEgg> eggsIterator = eggs.iterator();
        while (eggsIterator.hasNext()) {
            GoldenEgg egg = eggsIterator.next();
            if (egg.getXCoordinate() < 0 || egg.getXCoordinate() > windowWidth) {
                eggPool.add(egg);
                eggsIterator.remove();
            } else {
                if (egg.isInLeft()) {
                    egg.setXCoordinate(egg.getXCoordinate() - 18);
                } else {
                    egg.setXCoordinate(egg.getXCoordinate() + 18);
                }
                if (checkPolygon(egg)) {
                    eggPool.add(egg);
                    eggsIterator.remove();
                }
                g.drawImage(GoldenEgg.getGoldenEgg(), egg.getXCoordinate(), egg.getYCoordinate() + 20, GoldenEgg.getGoldenEgg().getWidth() / 35, GoldenEgg.getGoldenEgg().getHeight() / 35, this);
            }
        }

        Iterator<Polygon> polygonIterator = polygons.iterator();
        while (polygonIterator.hasNext()) {
            Polygon polygon = polygonIterator.next();

            if (polygon.isInLeft() && (polygon.getXCoordinate() < 0)) {
                polygon.setXCoordinate(polygon.getXCoordinate() + 4);
            } else if (!polygon.isInLeft() && (polygon.getXCoordinate() > windowWidth - Polygon.getPolygon().getWidth() / (12 - 2 * polygon.getScaleRate()))) {
                polygon.setXCoordinate(polygon.getXCoordinate() - 4);
            }
            polygon.setYCoordinate(polygon.getYCoordinate() - 2);

            if (polygon.getYCoordinate() < -Polygon.getPolygon().getHeight() / (12 - 2 * polygon.getScaleRate())) {
                polygonPool.add(polygon);
                polygonIterator.remove();
            } else {
                g.drawImage(Polygon.getPolygon(), polygon.getXCoordinate(), polygon.getYCoordinate(), Polygon.getPolygon().getWidth() / (12 - 2 * polygon.getScaleRate()), Polygon.getPolygon().getHeight() / (12 - 2 * polygon.getScaleRate()), this);
            }
        }

        g.setFont(new Font("Time", Font.PLAIN, 18));
        g.drawString("Time : " + gameInfo.getTime() + "   Score : " + gameInfo.getScore() + "  Egg : " + gameInfo.getEggNumber() + "  Level : " + gameInfo.getLevel(), windowWidth / 4, 20);

        for (int i = 1; i <= gameInfo.getFullHealth(); i++) {
            if (i <= gameInfo.getHealth()) {
                g.drawImage(Chicken.getHealth(), Chicken.getHealth().getWidth() / 30 * (i - 1), 0, Chicken.getHealth().getWidth() / 30, Chicken.getHealth().getHeight() / 30, this);
            } else {
                g.drawImage(Chicken.getHealthEmpty(), Chicken.getHealthEmpty().getWidth() / 30 * (i - 1), 0, Chicken.getHealthEmpty().getWidth() / 30, Chicken.getHealthEmpty().getHeight() / 30, this);
            }
        }

    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        timerCounter += 10;

        if (remainingTime > 0) {
            if (timerCounter % 20 == 0)
                repaint();
            return;
        }

        if (remainingTime == 0) {
            timerCounter = 0;
            repaint();
            remainingTime = -1;
            return;
        }

        ///****************************************************************

        //***** For chicken
        if (!chicken.isChickenBlock()) {
            chicken.setYCoordinate(chicken.getYCoordinate() + 2);
            if (chicken.getYCoordinate() > windowHeight - Chicken.getChickenLeft().getHeight() / 5 - 100)
                chicken.setYCoordinate(windowHeight - Chicken.getChickenLeft().getHeight() / 5 - 100);
        } else {
            if (chicken.getYCoordinate() > 0)
                chicken.setYCoordinate(chicken.getYCoordinate() - 2);
            else {
                chicken.setChickenBlock(false);
            }
        }

        //***** For Cats
        if (timerCounter % (2000 - 250 * (gameInfo.getLevel() - 1)) == 0) {
            generateCats();
        }

        //**** For Polygons
        if (timerCounter % (1000 + 250 * (gameInfo.getLevel() - 1)) == 0) {
            int scaleRate = randomGenerator.nextInt(3) + 1;
            int XCoor = randomGenerator.nextInt(2) == 1 ? 0 : windowWidth - Polygon.getPolygon().getWidth() / (10 - scaleRate);
            int YCoor = randomGenerator.nextInt(((windowHeight / 2) - (Polygon.getPolygon().getHeight() / (12 - 2 * scaleRate)))) + (windowHeight / 2);
            if (polygonPool.isEmpty())
                polygons.add(new Polygon(XCoor == 0 ? 10 - Polygon.getPolygon().getWidth() / (12 - 2 * scaleRate) : windowWidth - 10 + Polygon.getPolygon().getWidth() / (12 - 2 * scaleRate), YCoor, XCoor == 0, scaleRate));
            else {
                Polygon polygon = polygonPool.lastElement();
                polygon.setXCoordinate(XCoor == 0 ? 10 - Polygon.getPolygon().getWidth() / (12 - 2 * scaleRate) : windowWidth - 10 + Polygon.getPolygon().getWidth() / (12 - 2 * scaleRate));
                polygon.setYCoordinate(YCoor);
                polygon.setScaleRate(scaleRate);
                polygon.setInLeft(XCoor == 0);
                polygonPool.remove(polygonPool.size() - 1);
            }
        }

        //**** For Clouds
        if (timerCounter % 500 == 0) {
            if (cloudPool.size() < 2) {
                clouds.add(new Cloud(randomGenerator.nextInt(windowWidth / 2 + 100) - 100, windowHeight, true));
                clouds.add(new Cloud(randomGenerator.nextInt(windowWidth / 2) + 250, windowHeight, true));
            } else {
                Cloud cloud = cloudPool.lastElement();
                cloudPool.remove(cloudPool.size() - 1);
                cloud.setXCoordinate(randomGenerator.nextInt(windowWidth / 2 + 100) - 100);
                cloud.setYCoordinate(windowHeight);
                cloud.setInLeft(true);
                clouds.add(cloud);
                cloud = cloudPool.lastElement();
                cloudPool.remove(cloudPool.size() - 1);
                cloud.setXCoordinate(randomGenerator.nextInt(windowWidth / 2 + 100) - 250);
                cloud.setYCoordinate(windowHeight);
                cloud.setInLeft(true);
                clouds.add(cloud);
            }
        }

        if (gameInfo.getScore() >= 15) {
            timer.stop();
            if (gameInfo.getLevel() >= 3) {
                int result = JOptionPane.showOptionDialog(Game.this, "You Won, Do you want to level up", "Congratulations", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Chicken.getChickenIcon()), new Object[]{"Cancel", "Start Again"}, "Start Again");
                if (result == JOptionPane.CLOSED_OPTION || result == 0) {
                    saveAndExit();
                } else if (result == 1) {
                    gameInfo.setLevel(1);
                    startGameAgain(gameInfo.getFullHealth());
                }
            } else {
                int result = JOptionPane.showOptionDialog(Game.this, "You Won, Do you want to level up", "Congratulations", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Chicken.getChickenIcon()), new Object[]{"Cancel", "Play Again", "Level Up"}, "Play Again");
                if (result == JOptionPane.CLOSED_OPTION || result == 0) {
                    saveAndExit();
                } else if (result == 1) {
                    startGameAgain(gameInfo.getHealth());
                } else if (result == 2) {
                    gameInfo.setLevel(gameInfo.getLevel() + 1);
                    startGameAgain(gameInfo.getHealth());
                }
            }
        }

        if (gameInfo.getEggNumber() <= 0) {
            int result = showMessage("Egg Number", "There is no other EGG \n Do you want to play again?");
            if (result == 1) {
                startGameAgain(gameInfo.getFullHealth());
            } else if (result == 0) {
                saveAndExit();
            }
        }
        checkIsGameOver();

        //******************************************************

    }
    
    

    // ****************** Personal Functions *********************
    private void generateCats() {
        int catX;
        ArrayList<Integer> blocks = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            catX = randomGenerator.nextInt(windowWidth - Cat.getCatLeft().getHeight() / 4);
            if (blocks.contains(catX % Cat.getCatLeft().getHeight() / 4)) {
                if (catPool.isEmpty())
                    cats.add(new Cat(catX + Cat.getCatLeft().getHeight() / 4, windowHeight - Cat.getCatLeft().getHeight() / 10, catX < windowWidth / 2));
                else {
                    Cat cat = catPool.lastElement();
                    cat.setXCoordinate(catX + Cat.getCatLeft().getHeight() / 4);
                    cat.setYCoordinate(windowHeight - Cat.getCatLeft().getHeight() / 10);
                    cat.setInLeft(catX < windowWidth / 2);
                    cats.add(cat);
                    catPool.remove(catPool.size() - 1);
                }
            } else {
                blocks.add(catX % Cat.getCatLeft().getHeight() / 4);
                if (catPool.isEmpty()) {
                    cats.add(new Cat(catX, windowHeight - Cat.getCatLeft().getHeight() / 10, catX < windowWidth / 2));
                } else {
                    Cat cat = catPool.lastElement();
                    cat.setXCoordinate(catX);
                    cat.setYCoordinate(windowHeight - Cat.getCatLeft().getHeight() / 10);
                    cat.setInLeft(catX < windowWidth / 2);
                    cats.add(cat);
                    catPool.remove(catPool.size() - 1);
                }
            }
        }
    }

    private boolean checkPolygon(GoldenEgg egg) {
        Rectangle eggRectangle = new Rectangle(egg.getXCoordinate(), egg.getYCoordinate(), GoldenEgg.getGoldenEgg().getWidth() / 35, GoldenEgg.getGoldenEgg().getHeight() / 35);
        Rectangle polygonRectangle;
        Polygon polygon;
        Iterator<Polygon> polygonIterator = polygons.iterator();

        while (polygonIterator.hasNext()) {
            polygon = polygonIterator.next();
            polygonRectangle = new Rectangle(polygon.getXCoordinate(), polygon.getYCoordinate(), Polygon.getPolygon().getWidth() / (12 - 2 * polygon.getScaleRate()), Polygon.getPolygon().getHeight() / (12 - 2 * polygon.getScaleRate()));
            if (eggRectangle.intersects(polygonRectangle) && ((egg.isInLeft() && polygon.isInLeft()) || (!egg.isInLeft() && !polygon.isInLeft()))) {
                gameInfo.setScore(gameInfo.getScore() + 4 - polygon.getScaleRate());
                polygonPool.add(polygon);
                polygonIterator.remove();
                gameInfo.setHeightScore(Math.max(gameInfo.getHeightScore(), gameInfo.getScore()));
                return true;
            }
        }
        return false;
    }

    private void checkIsGameOver() {
        Rectangle chickenRectangle = new Rectangle(this.chicken.getXCoordinate(), this.chicken.getYCoordinate(), Chicken.getChickenLeft().getWidth() / 5, Chicken.getChickenLeft().getHeight() / 5);
        Rectangle catRectangle;
        Iterator<Cat> catIterator = cats.iterator();
        while (catIterator.hasNext()) {
            Cat cat = catIterator.next();
            catRectangle = new Rectangle(cat.getXCoordinate(), cat.getYCoordinate(), Cat.getCatLeft().getWidth() / 5, Cat.getCatLeft().getHeight() / 5);
            if (chickenRectangle.intersects(catRectangle) && !chicken.isReloading()) {
                gameInfo.setHealth(gameInfo.getHealth() - 1);
                if (gameInfo.getHealth() <= 0) {
                    int result = showMessage("Game Over", "Do you want to start again?");
                    if (result == 1) {
                        gameInfo.setLevel(1);
                        startGameAgain(gameInfo.getFullHealth());
                    } else if (result == 0) {
                        saveAndExit();
                    }
                }
                catIterator.remove();
                chicken.startReloading(gameInfo.getTime(), 1);
                chicken.setYCoordinate(10);
                chicken.setXCoordinate(windowWidth / 2);
                chicken.setChickenBlock(false);
                return;
            }
        }
    }

    private void startGameAgain(int health) {
        gameInfo.setTime(gameInfo.getFullTime());
        gameInfo.setScore(0);
        gameInfo.setEggNumber(gameInfo.getFullEggNumber());
        gameInfo.setHealth(health);
        gameScreen.removeWindowListener(Game.this);
        Game game = new Game(gameScreen, gameInfo, cardLayout);
        gameScreen.getContentPane().add("game", game);
        game.requestFocusInWindow();
        game.addKeyListener(game);
        game.addComponentListener(game);
        game.setFocusable(true);
        game.setFocusTraversalKeysEnabled(false);
        gameScreen.setVisible(false);
        gameScreen.setVisible(true);
        gameScreen.setResizable(true);
        cardLayout.next(gameScreen.getContentPane());
        gameScreen.getContentPane().remove(0);
    }

    private Thread createCounterThread() {
        return new Thread(() -> {
            while (timer.isRunning()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                if (remainingTime > 0) {
                    remainingTime -= 1;
                    remainingTimeFont = (int) (windowHeight * 1.5);
                    remainingTimeY = getHeight();
                    remainingTimeX = getWidth() / 2 - (int) (remainingTimeFont / 3.5);
                } else {
                    gameInfo.setTime(gameInfo.getTime() - 1);
                    if (chicken.getStartingTime() - chicken.getInterval() > gameInfo.getTime())
                        chicken.setReloading(false);
                    if (gameInfo.getTime() <= 0) {
                        int result = showMessage("Time is up", "Do you want to play again?");
                        if (result == 1) {
                            startGameAgain(gameInfo.getHealth());
                        } else if (result == 0) {
                            saveAndExit();
                        }
                    }
                }
            }
        });
    }

    private int showMessage(String title, String message) {
        timer.stop();
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Chicken.getChickenIcon()));

        if (result == JOptionPane.OK_OPTION) {
            return 1;
        } else if (result == JOptionPane.CANCEL_OPTION) {
            return 0;
        } else {
            saveAndExit();
        }
        return -1;
    }

    private void saveAndExit() {

        timer.stop();
        gameInfo.saveGameInfo();
        gameScreen.removeWindowListener(this);
        gameScreen.getContentPane().add(new GameLoginScreen(gameScreen, cardLayout));
        cardLayout.next(gameScreen.getContentPane());
        gameScreen.getContentPane().remove(0);
    }

    //******************************  Functions For keyboard event *************************************
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_A) {
            chicken.setInLeft(true);
            if (chicken.getXCoordinate() > 0)
                chicken.setXCoordinate(chicken.getXCoordinate() - 20);
        } else if (keyCode == KeyEvent.VK_D) {
            chicken.setInLeft(false);
            if (chicken.getXCoordinate() + Chicken.getChickenLeft().getWidth() / 5 < windowWidth)
                chicken.setXCoordinate(chicken.getXCoordinate() + 20);
        } else if (keyCode == KeyEvent.VK_CONTROL) {
            if (eggPool.isEmpty()) {
                eggs.add(new GoldenEgg(chicken.getXCoordinate(), chicken.getYCoordinate(), chicken.isInLeft()));
            } else {
                GoldenEgg egg = eggPool.lastElement();
                eggPool.remove(eggPool.size() - 1);
                egg.setXCoordinate(chicken.getXCoordinate());
                egg.setYCoordinate(chicken.getYCoordinate());
                egg.setInLeft(chicken.isInLeft());
                eggs.add(egg);
            }
            gameInfo.setEggNumber(gameInfo.getEggNumber() - 1);
        } else if (keyCode == KeyEvent.VK_SPACE) {
            chicken.setChickenBlock(!chicken.isChickenBlock());
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            if (gameScreen.isUndecorated()) {
                gameScreen.dispose();
                gameScreen.setUndecorated(false);
                gameScreen.setVisible(true);
            } else {
                int result = showMessage("Exit", "Are You Sure?");
                if (result == 1) {
                    saveAndExit();
                } else if (result == 0) {
                    createCounterThread().start();
                    timer.start();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //********************************** Screen Resized Listener Methots  *********************
    @Override
    public void componentResized(ComponentEvent e) {
        Dimension windowDimension = e.getComponent().getSize();
        windowWidth = windowDimension.width;
        windowHeight = windowDimension.height;

        chicken.setXCoordinate(windowWidth / 2);

        polygons.clear();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    // ********** Window State ***********

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int result = showMessage("Exit", "Are You Sure?");
        if (result == 1) {
            gameInfo.saveGameInfo();
            gameScreen.removeWindowListener(this);
            gameScreen.getContentPane().add(new GameLoginScreen(gameScreen, cardLayout));
            cardLayout.next(gameScreen.getContentPane());
            gameScreen.getContentPane().remove(0);
        } else if (result == 0) {
            timer.start();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (eggPool.isEmpty()) {
            eggs.add(new GoldenEgg(chicken.getXCoordinate(), chicken.getYCoordinate(), chicken.isInLeft()));
        } else {
            GoldenEgg egg = eggPool.lastElement();
            eggPool.remove(eggPool.size() - 1);
            egg.setXCoordinate(chicken.getXCoordinate());
            egg.setYCoordinate(chicken.getYCoordinate());
            egg.setInLeft(chicken.isInLeft());
            eggs.add(egg);
        }
        gameInfo.setEggNumber(gameInfo.getEggNumber() - 1);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
