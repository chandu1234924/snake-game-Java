import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class snakeGame extends JPanel implements ActionListener, KeyListener {

    // Board
    int boardWidth = 600;
    int boardHeight = 600;
    int tileSize = 25;

    // Snake
    int[] snakeX = new int[100];
    int[] snakeY = new int[100];
    int snakeLength = 3;

    // Direction
    int velocityX = 1;
    int velocityY = 0;

    // Food
    int foodX;
    int foodY;

    // Game
    boolean gameOver = false;
    Timer timer;
    Random random = new Random();

    public snakeGame() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        // Starting position of snake
        snakeX[0] = 300;
        snakeY[0] = 300;

        snakeX[1] = 275;
        snakeY[1] = 300;

        snakeX[2] = 250;
        snakeY[2] = 300;

        placeFood();

        timer = new Timer(100, this);
        timer.start();
    }

    // Place food randomly
    public void placeFood() {

        foodX = random.nextInt(boardWidth / tileSize) * tileSize;
        foodY = random.nextInt(boardHeight / tileSize) * tileSize;
    }

    // Draw everything
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(foodX, foodY, tileSize, tileSize);

        // Draw snake
        g.setColor(Color.GREEN);

        for (int i = 0; i < snakeLength; i++) {
            g.fillRect(snakeX[i], snakeY[i], tileSize, tileSize);
        }

        // Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        g.drawString("Score: " + (snakeLength - 3), 10, 25);

        // Game Over
        if (gameOver) {

            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER", 160, 280);

            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Press ENTER to restart", 170, 330);
        }
    }

    // Game movement
    public void move() {

        // Move body
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        // Move head
        snakeX[0] = snakeX[0] + velocityX * tileSize;
        snakeY[0] = snakeY[0] + velocityY * tileSize;
    }

    // Check food
    public void checkFood() {

        if (snakeX[0] == foodX && snakeY[0] == foodY) {

            snakeLength++;
            placeFood();
        }
    }

    // Check collisions
    public void checkCollision() {

        // Hit wall
        if (snakeX[0] < 0 ||
            snakeX[0] >= boardWidth ||
            snakeY[0] < 0 ||
            snakeY[0] >= boardHeight) {

            gameOver = true;
        }

        // Hit itself
        for (int i = 1; i < snakeLength; i++) {

            if (snakeX[0] == snakeX[i] &&
                snakeY[0] == snakeY[i]) {

                gameOver = true;
            }
        }

        if (gameOver) {
            timer.stop();
        }
    }

    // Timer calls this every 100 milliseconds
    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameOver) {

            move();
            checkFood();
            checkCollision();
        }

        repaint();
    }

    // Keyboard controls
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

        // Restart
        if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {

            snakeLength = 3;

            snakeX[0] = 300;
            snakeY[0] = 300;

            snakeX[1] = 275;
            snakeY[1] = 300;

            snakeX[2] = 250;
            snakeY[2] = 300;

            velocityX = 1;
            velocityY = 0;

            gameOver = false;

            placeFood();

            timer.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Main method
    public static void main(String[] args) {

        JFrame frame = new JFrame("Snake Game");

        snakeGame game = new snakeGame();

        frame.add(game);

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setResizable(false);

        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}