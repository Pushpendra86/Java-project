import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener, ActionListener {

    private final int BOX_SIZE = 20; // Size of each square box
    private final int NUM_BOXES = 20; // Number of boxes in the game area
    private final int GAME_WIDTH = BOX_SIZE * NUM_BOXES; // Width of the game area
    private final int GAME_HEIGHT = BOX_SIZE * NUM_BOXES; // Height of the game area
    private final int DELAY = 100; // Delay for timer

    private ArrayList<Point> snake; // Snake body
    private Point food; // Food position
    private char direction; // Current direction of the snake
    private Timer timer; // Timer for game loop
    private boolean gameOver; // Flag to track game over state

    public SnakeGame() {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(NUM_BOXES / 2, NUM_BOXES / 2)); // Snake starts at the center
        direction = 'R'; // Snake starts moving right
        placeFood();
        timer = new Timer(DELAY, this);
        timer.start();
        gameOver = false;
    }

    private void placeFood() {
        Random random = new Random();
        int x = random.nextInt(NUM_BOXES);
        int y = random.nextInt(NUM_BOXES);
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * BOX_SIZE, food.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * BOX_SIZE, point.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }

        // Display game over message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            checkCollision();
            repaint();
        }
    }

    private void move() {
        // Move the snake by adding a new head in the direction of movement
        Point head = snake.get(0);
        Point newHead = new Point(head);
        switch (direction) {
            case 'U':
                newHead.y--;
                break;
            case 'D':
                newHead.y++;
                break;
            case 'L':
                newHead.x--;
                break;
            case 'R':
                newHead.x++;
                break;
        }
        snake.add(0, newHead);

        // Check if snake has eaten food
        if (newHead.equals(food)) {
            placeFood();
        } else {
            snake.remove(snake.size() - 1); // Remove tail if food not eaten
        }
    }

    private void checkCollision() {
        // Check if snake has collided with itself or with the perimeter
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= NUM_BOXES || head.y < 0 || head.y >= NUM_BOXES) {
            gameOver = true;
            timer.stop();
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                timer.stop();
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') // Prevent snake from moving opposite to its current direction
                    direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U')
                    direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R')
                    direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L')
                    direction = 'R';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
