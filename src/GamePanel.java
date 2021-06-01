import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private Image image;
    private Graphics graphics;
    private Paddle paddle1;
    private Paddle paddle2;
    private Ball ball;
    private Score score;



    public GamePanel(){

        newPaddles();
        newBall();
        score = new Score(Def.GAME_WIDTH, Def.GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new actionListener());
        this.setPreferredSize(Def.SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void newBall(){
        Random random = new Random();
        ball = new Ball((Def.GAME_WIDTH/2)-(Def.BALL_DIAMETER/2),
                random.nextInt(Def.GAME_HEIGHT- Def.BALL_DIAMETER),
                Def.BALL_DIAMETER, Def.BALL_DIAMETER);

    }
    public void newPaddles(){

        paddle1 = new Paddle
                (       0,
                        (Def.GAME_HEIGHT/2)-(Def.PADDLE_HEIGHT/2), Def.PADDLE_WIDTH, Def.PADDLE_HEIGHT,
                        1);

        paddle2 = new Paddle
                (       Def.GAME_WIDTH- Def.PADDLE_WIDTH,
                        (Def.GAME_HEIGHT/2)-(Def.PADDLE_HEIGHT/2), Def.PADDLE_WIDTH, Def.PADDLE_HEIGHT,
                        2);


    }

    public void paint(Graphics g){

        image = createImage(getWidth(),getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0,this);

    }
    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

    }
    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();


    }
    public void checkCollision(){
        if(ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.y >= Def.GAME_HEIGHT- Def.BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);
        }
        if(ball.intersects(paddle1)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // optional for more difficulty
            if (ball.yVelocity>0){
                ball.yVelocity++;
            } else {ball.yVelocity--;}
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if(ball.intersects(paddle2)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; // optional for more difficulty
            if (ball.yVelocity>0){
                ball.yVelocity++;
            } else {ball.yVelocity--;}
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if(paddle1.y<=0){
            paddle1.y = 0;
        }
        if(paddle1.y>= (Def.GAME_HEIGHT- Def.PADDLE_HEIGHT)){
            paddle1.y = Def.GAME_HEIGHT- Def.PADDLE_HEIGHT;
        }
        if(paddle2.y<=0){
            paddle2.y = 0;
        }
        if(paddle2.y>= (Def.GAME_HEIGHT- Def.PADDLE_HEIGHT)){
            paddle2.y = Def.GAME_HEIGHT- Def.PADDLE_HEIGHT;
        }
        //give a player 1 point and creates new paddles & ball
        if(ball.x <= 0){
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2:" + score.player2);
        }
        if(ball.x >= Def.GAME_WIDTH- Def.BALL_DIAMETER){
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1:" + score.player1);
        }



    }
    public void run(){

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double nanoSeconds = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true ){
            long now = System.nanoTime();
            delta += (now - lastTime)/nanoSeconds;
            lastTime = now;
            if(delta>= 1){
                move();
                checkCollision();
                repaint();
                delta--;
            }

        }


    }

    public class actionListener extends KeyAdapter {
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);

        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}



