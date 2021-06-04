import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GameLoginScreen extends JPanel implements MouseListener, WindowListener, KeyListener {
    private ArrayList<GameInfo> gameInfos = new ArrayList<>();
    private JFrame gameScreen;
    private CardLayout cardLayout;
    private JTable gamerTable;
    private JScrollPane scrollPane;


    public GameLoginScreen(JFrame gameScreen, CardLayout cardLayout) {
        gameScreen.setBounds(400, 100, 500, 500);
        this.gameScreen = gameScreen;
        this.cardLayout = cardLayout;
        gameScreen.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        gameScreen.addWindowListener(this);
        readSavedGames();

        String[] columns = {"Name", "Hight Score", "Level", "Health", "Time"};

        gamerTable = new JTable();
        gamerTable.addMouseListener(this);

        gamerTable.setModel(new javax.swing.table.DefaultTableModel() {
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        ((DefaultTableModel) gamerTable.getModel()).setColumnIdentifiers(columns);


        for (GameInfo gameInfo : gameInfos) {
            ((DefaultTableModel) gamerTable.getModel()).addRow(new String[]{gameInfo.getGamer(), String.valueOf(gameInfo.getHeightScore()), String.valueOf(gameInfo.getLevel()), gameInfo.getHealth() + "/5", String.valueOf(gameInfo.getTime())});
        }

        gamerTable.setColumnSelectionAllowed(false);
        gamerTable.setDragEnabled(false);

        //gamerTable.setPreferredScrollableViewportSize(new Dimension(400, 100));
        gamerTable.setFillsViewportHeight(true);
        gamerTable.getTableHeader().setReorderingAllowed(false);
        gamerTable.getTableHeader().setResizingAllowed(false);

        scrollPane = new JScrollPane(gamerTable);

        gamerTable.setOpaque(false);
        gamerTable.setBackground(Color.gray);
        gamerTable.getTableHeader().setBackground(Color.gray);
        scrollPane.getViewport().setBackground(Color.gray);
        scrollPane.setBorder(new LineBorder(Color.lightGray, 1));
        gamerTable.getTableHeader().setBorder(new LineBorder(Color.lightGray, 1));
        gamerTable.setSelectionBackground(Color.lightGray);
        gamerTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray));
                label.setOpaque(true);
                label.setBackground(Color.gray);

                if (row == -1) {
                    label.setText(value.toString());
                    return label;
                } else {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });

        JButton newGame = new JButton("New Game");
        newGame.setBounds(200, 450, 100, 20);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userRandomName = new Random().nextInt();
                String gamerName = JOptionPane.showInputDialog(GameLoginScreen.this, "Enter User Name", "Guest-" + (userRandomName < 0 ? -userRandomName : userRandomName));
                if (gamerName != null) {
                    int difficulyLevel = showDifficultyDialog();
                    if (difficulyLevel > 0) {
                        GameInfo gameInfo = new GameInfo(1, 120 / difficulyLevel, 0, 0, gamerName, 60, difficulyLevel, 120 / difficulyLevel, 60, 5, 5);
                        startGame(gameInfo);
                    }
                }
            }
        });

        newGame.setContentAreaFilled(false);

        newGame.setBorder(new RoundedBorder(5));

        newGame.setVerticalAlignment(SwingConstants.CENTER);
        newGame.setBackground(Color.gray);
        setBackground(Color.gray);
        gamerTable.setAutoCreateRowSorter(true);

        repaint();
        add(scrollPane);
        add(newGame);

        addKeyListener(this);
        gameScreen.setResizable(false);
    }

    private int showDifficultyDialog() {
        int result = JOptionPane.showOptionDialog(this, "Choose Difficulty Level", "Difficulty Level", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Chicken.getChickenIcon()), new String[]{"Beginner", "Easy", "Medium", "Hard", "Very Hard"}, "Medium");
        if (result == 0)
            return 1;
        else if (result == 1)
            return 2;
        else if (result == 2)
            return 3;
        else if (result == 3)
            return 4;
        else if (result == 4)
            return 5;
        else
            //System.exit(0);
            return -1;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(new Font("Time", Font.BOLD, 18));
        g.setColor(Color.ORANGE);
        if (gameInfos.size() == 0) {
            scrollPane.setVisible(false);
            String message = "No Saved Game";
            g.drawString(message, getWidth() / 2 - message.length() * 4, getHeight() / 2);
        } else {
            scrollPane.setVisible(true);
        }
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    private void readSavedGames() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("gameinfos"))));
            gameInfos = (ArrayList<GameInfo>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeSavedGames() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("gameinfos"))));
            objectOutputStream.writeObject(gameInfos);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gamerTable.getSelectedRow() >= 0) {
            int result = JOptionPane.showOptionDialog(this, gameInfos.get(gamerTable.getSelectedRow()).getGamer(), "Flying Chicken", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Chicken.getChickenIcon()), new String[]{"Resume", "Delete Carrier", "New Game"}, "Resume");
            if (result == 0) {
                GameInfo gameInfo = gameInfos.get(gamerTable.getSelectedRow());
                String message = "";
                if (gameInfo.getLevel() >= 3) {
                    message = "You Have Already Complete The Game\nDo You Want To Start A New Game?";
                    int messageResult = showMessage("New Game", message);
                    if (messageResult == 1){
                        newGame();
                    }
                } else {
                    if (gameInfo.getTime() <= 0) {
                        message = "Time Is Up\nDo You Want To Play Again";
                        int messageResult = showMessage("Play Again", message);
                        if (messageResult == 1){
                            gameInfo.setTime(gameInfo.getFullTime());
                            gameInfo.setScore(0);
                            gameInfo.setEggNumber(gameInfo.getFullEggNumber());
                            startGame(gameInfo);
                        }
                    } else if (gameInfo.getHealth() <= 0) {
                        message = "There Is No Enought Health\nDo You Want To Start A New Game?";
                        int messageResult = showMessage("New Game", message);
                        if (messageResult == 1){
                            newGame();
                        }
                    } else if (gameInfo.getEggNumber() <= 0) {
                        message = "There is No Egg\nDo You Want To Play Again";
                        int messageResult = showMessage("Play Again", message);
                        if (messageResult == 1){
                            gameInfo.setTime(gameInfo.getFullTime());
                            gameInfo.setScore(0);
                            gameInfo.setEggNumber(gameInfo.getFullEggNumber());
                            startGame(gameInfo);
                        }
                    } else {
                        startGame(gameInfo);
                    }
                }

            } else if (result == 1) {
                gameInfos.remove(gamerTable.getSelectedRow());
                ((DefaultTableModel) gamerTable.getModel()).removeRow(gamerTable.getSelectedRow());
                writeSavedGames();
                repaint();
            } else if (result == 2) {
                newGame();
            }
        }
    }

    private void newGame() {
        String gamerName = JOptionPane.showInputDialog(GameLoginScreen.this, "Enter User Name", gameInfos.get(gamerTable.getSelectedRow()).getGamer());
        if (gamerName != null) {
            int difficultyLevel = showDifficultyDialog();
            if (difficultyLevel > 0) {
                GameInfo gameInfo = new GameInfo(1, 120 / difficultyLevel, 0, 0, "Guest", 60, difficultyLevel, 120 / difficultyLevel, 60, 5, 5);
                gameInfo.setGamer(gameInfos.get(gamerTable.getSelectedRow()).getGamer());
                startGame(gameInfo);
            }
        }
    }

    private void startGame(GameInfo gameInfo) {
        gameScreen.removeWindowListener(GameLoginScreen.this);
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

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int result = showMessage("Exit", "Are You Sure?");
        if (result == 1) {
            System.exit(0);
        }
    }

    private int showMessage(String title, String message) {
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(Chicken.getChickenIcon()));

        if (result == JOptionPane.OK_OPTION) {
            return 1;
        } else if (result == JOptionPane.CANCEL_OPTION) {
            return 0;
        } else {
            return -1;
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
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            int result = showMessage("Exit", "Are You Sure?");
            if (result == 1) {
                System.exit(0);
            }
        }
    }
}

class RoundedBorder implements Border {

    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}