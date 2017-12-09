import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.lang.Math;

public class Game extends JPanel implements Runnable, KeyListener{
    private Color backGroundColor = Color.BLUE, borderColor = Color.RED, pColor = Color.YELLOW;
    private int pX = 500, pY = 300, whiteX = 100, whiteY = 200, score = 0, highScore = 0;
    private int[] junctionX = new int[10000]; // whiteX and whiteY called that because the pill was
    private int[] junctionY = new int[10000];    // initially white. Now it's green.
    private int[] turnDir = new int[10000];
    private ArrayList<Integer> snakePosX = new ArrayList<Integer>(0);
    private ArrayList<Integer> snakePosY = new ArrayList<Integer>(score + 1);
    private int pWidth = 20;
    private int juncDir = 0; // 1 = up , 2 = down, 3 = left, 4 = right;
    private int turnCount = 0;
    private boolean gameStart = false, pUp = false, pDown = false, pLeft = false, pRight = false,
    suspended = false, startSnake = true;
    private Thread thread;
    
    public void updateSnakePosX(){
        snakePosX.add(0, pX);
        if(startSnake && snakePosX.size() > 1){
            for(int i = 1; i < snakePosX.size(); i++){
                snakePosX.remove(i);
            }
        }
        if((score + 1)*3*(score+1) < snakePosX.size()){
            for(int i = (score+1)*3*(score+1); i < snakePosX.size(); i++){
                snakePosX.remove(i);
            }
        }
    }
    public void updateSnakePosY(){
        snakePosY.add(0, pY);
        if(startSnake && snakePosY.size() > 1){
            for(int i = 1; i < snakePosY.size(); i++){
                snakePosY.remove(i);
            }
        }
        if((score + 1)*3*(score+1) < snakePosY.size()){
            for(int i = (score+1)*3*(score+1); i < snakePosY.size(); i++){
                snakePosY.remove(i);
            }
        }
    }
    
    public void checkAlive(){
        boolean xSame = false;
        boolean ySame = false;
        
        for(int i = 1; i < snakePosX.size(); i++){
            if(pX == snakePosX.get(i)){
                xSame = true;
            }
            if( pY == snakePosY.get(i)){
                ySame = true;
            }
            if(xSame && ySame){
                lose();
            }else{
                xSame = false;
                ySame = false;
            }
        }
    }
    
    public void arrayShift(int[] array){
        for(int i = 0; i < array.length; i++){
            if(i == array.length - 1){
                array[i] = 0;
                break;
            }else{
                array[i] = array[i + 1];
            }
        }
    }

    public Game(){
        super();
        this.addKeyListener(this);   // IMPORTANT NEEDS 'this.' PREFIX
        this.setFocusable(true);     // IMPORTANT NEEDS TO BE FOCUSABLE
        this.requestFocusInWindow(); // IMPORTANT NEEDS FOCUS
    }

    public void lose(){
        run();
    }

    public void roll(){
        boolean valid = false;
        do{
            do{
                whiteX = (int)(Math.random()*940 + 20);
            }while(whiteX % 20 != 0);
            do{
                whiteY = (int)(Math.random()*650 + 20);
            }while(whiteY % 20 != 0);
            if(!snakePosX.contains(whiteX) && !snakePosY.contains(whiteY)){
                valid = true;
            }
        }while(!valid);
    }
    
    public void addNotify(){
        super.addNotify();
        thread = new Thread(this);
        thread.start();
    }

    public void run(){
        pX = 500;
        pY = 300;
        for(int i = 0; i < junctionX.length; i++){
            junctionX[i] = 0;
        }
        for(int i = 0; i < junctionX.length; i++){
            junctionY[i] = 0;
        }
        for(int i = 0; i < junctionX.length; i++){
            turnDir[i] = 0;
        }
        for(int i = 0; i < snakePosX.size(); i++){
            snakePosX.clear();
            snakePosY.clear();
        }
        juncDir = 0;
        turnCount = 0;
        pWidth = 20;
        whiteX = 100;
        whiteY = 200;
        startSnake = true;
        score = 0;
        pUp = false;
        pDown = false;
        pLeft = false;
        pRight = false;
        while(true){
            try{
                Thread.sleep(12);
            }catch(Exception e){}
            keyChecks();
            if(pUp){
                pY -= 1;
            }
            if(pDown){
                pY += 1;
            }
            if(pLeft){
                pX -= 1;
            }
            if(pRight){
                pX += 1;
            }
            if(pX + 20 > whiteX && pX < whiteX + 20 && pY + pWidth > whiteY && pY < whiteY + 20){
                score ++;
                startSnake = false;
                if(score > highScore){
                    highScore = score;
                }
                if(pUp){
                }else if(pDown){
                }else if(pLeft){
                }else if(pRight){
                }
                roll();
            }
            updateSnakePosX();
            updateSnakePosY();
            checkAlive();
            repaint();
        }
    }

    public void paint(Graphics g){
        super.paint(g);
        this.setFocusable(true);

        int scoreRemainder = 0;
        int limit;

        g.setColor(borderColor);
        g.fillRect(0, 0, 1000, 825);

        g.setColor(backGroundColor);
        g.fillRect(20, 20, 960, 680);

        g.setColor(Color.GREEN);
        g.fillRect(whiteX, whiteY, 20, 20);

        g.setColor(pColor);
        if(startSnake){
            limit = 1;
        }else{
            limit = snakePosX.size()-1;
        }
        if(startSnake && turnCount == 0){
            g.fillRect(pX, pY, pWidth, pWidth);
        }
        if(snakePosX.size() != 0){
            for(int i = 0; i < snakePosX.size()-1; i++){
                g.fillRect(snakePosX.get(i), snakePosY.get(i), pWidth, pWidth);
            }
        }
        
        g.setColor(Color.WHITE);
        g.drawString("pX = " + pX, 30, 720);
        g.drawString("pY = " + pY, 30, 740);
        g.drawString("wX = " + whiteX, 30, 760);                          // pill X value
        g.drawString("wY = " + whiteY, 30, 780);                          // pill Y value
        g.drawString("score = " + score, 200, 720);
        g.drawString("highScore = " + highScore, 200, 740);
        g.drawString("startSnake = " + startSnake, 200, 760);
        g.drawString("junctionX = " + junctionX[0], 370, 720);            // most recent turn point
        g.drawString("junctionY = " + junctionY[0], 370, 740);            // most recent turn point
        g.drawString("turnCount = " + turnCount, 370, 760);
        g.drawString("snakePosX.size() = " + snakePosX.size(), 540, 720); // Number of X-value frames
        g.drawString("snakePosY.size() = " + snakePosY.size(), 540, 740); // Number of Y-value frames
        if(suspended){
            g.drawString("PAUSED", 500, 300);
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_UP){

        }
        if(key == KeyEvent.VK_DOWN){

        }
        if(key == KeyEvent.VK_LEFT){

        }
        if(key == KeyEvent.VK_RIGHT){

        }
    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_UP){
            if(pX % 20 == 0 && startSnake){
                if(pLeft){
                    juncDir = 3;
                    turnDir[turnCount] = 3;
                    turnCount ++;
                }else if(pRight){
                    juncDir = 4;
                    turnDir[turnCount] = 4;
                    turnCount ++;
                }
                junctionX[0] = pX; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = true;
                pDown = false;
                pRight = false;
                pLeft = false;
            }else if(pX % 20 == 0 && !pDown){
                if(pLeft){
                    juncDir = 3;
                    turnDir[turnCount] = 3;
                    turnCount ++;
                }else if(pRight){
                    juncDir = 4;
                    turnDir[turnCount] = 4;
                    turnCount ++;
                }
                junctionX[0] = pX; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = true;
                pDown = false;
                pRight = false;
                pLeft = false;
            }else{
                if(pRight){
                    juncDir = 4;
                    turnDir[turnCount] = 4;
                    turnCount ++;
                    pX += 20-(pX % 20);
                    junctionX[0] = pX; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = true;
                    pDown = false;
                    pRight = false;
                    pLeft = false;
                }
                if(pLeft){
                    juncDir = 3;
                    turnDir[turnCount] = 3;
                    turnCount ++;
                    pX -= (pX % 20);
                    junctionX[0] = pX; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = true;
                    pDown = false;
                    pRight = false;
                    pLeft = false;
                }
                if(pDown && startSnake){
                    junctionX[0] = pX; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = true;
                    pDown = false;
                    pRight = false;
                    pLeft = false;
                }
            }
        }
        if(key == KeyEvent.VK_DOWN){
            if(pX % 20 == 0 && startSnake){
                if(pLeft){
                    juncDir = 3;
                    turnDir[turnCount] = 3;
                    turnCount ++;
                }else if(pRight){
                    juncDir = 4;
                    turnDir[turnCount] = 4;
                    turnCount ++;
                }
                junctionX[0] = pX; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = false;
                pDown = true;
                pRight = false;
                pLeft = false;
            }else if(pX % 20 == 0 && !pUp){
                if(pLeft){
                    juncDir = 3;
                    turnDir[turnCount] = 3;
                    turnCount ++;
                }else if(pRight){
                    juncDir = 4;
                    turnDir[turnCount] = 4;
                    turnCount ++;
                }
                junctionX[0] = pX; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = false;
                pDown = true;
                pRight = false;
                pLeft = false;
            }else{
                if(pRight){
                    juncDir = 4;
                    turnDir[turnCount] = 4;
                    turnCount ++;
                    pX += 20-(pX % 20);
                    junctionX[0] = pX; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = true;
                    pRight = false;
                    pLeft = false;
                }
                if(pLeft){
                    juncDir = 3;
                    turnDir[turnCount] = 3;
                    turnCount ++;
                    pX -= (pX % 20);
                    junctionX[0] = pX; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = true;
                    pRight = false;
                    pLeft = false;
                }
                if(pUp && startSnake){
                    junctionX[0] = pX; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = true;
                    pRight = false;
                    pLeft = false;
                }
            }
        }
        if(key == KeyEvent.VK_LEFT){
            if(pY % 20 == 0 && startSnake){
                if(pUp){
                    juncDir = 1;
                    turnDir[turnCount] = 1; //new method of turning
                    turnCount ++; 
                }else if(pDown){
                    juncDir = 2;
                    turnDir[turnCount] = 2; //new method of turning
                    turnCount ++;
                }
                junctionY[0] = pY; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = false;
                pDown = false;
                pRight = false;
                pLeft = true;
            }else if(pY % 20 == 0 && !pRight){
                if(pUp){
                    juncDir = 1;
                    turnDir[turnCount] = 1;
                    turnCount ++;
                }else if(pDown){
                    juncDir = 2;
                    turnDir[turnCount] = 2;
                    turnCount ++;
                }
                junctionY[0] = pY; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = false;
                pDown = false;
                pRight = false;
                pLeft = true;
            }else{
                if(pRight && startSnake){
                    junctionY[0] = pY; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = false;
                    pRight = false;
                    pLeft = true;
                }
                if(pUp){
                    juncDir = 1;
                    turnDir[turnCount] = 1;
                    turnCount ++;
                    pY -= (pY % 20);
                    junctionY[0] = pY; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = false;
                    pRight = false;
                    pLeft = true;
                }
                if(pDown){
                    juncDir = 2;
                    turnDir[turnCount] = 2;
                    turnCount ++;
                    pY += 20-(pY % 20);
                    junctionY[0] = pY; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = false;
                    pRight = false;
                    pLeft = true;
                }
            }
        }
        if(key == KeyEvent.VK_RIGHT){
            if(pY % 20 == 0 && startSnake){
                if(pUp){
                    juncDir = 1;
                    turnDir[turnCount] = 1;
                    turnCount ++;
                }else if(pDown){
                    juncDir = 2;
                    turnDir[turnCount] = 2;
                    turnCount ++;
                }
                junctionY[0] = pY; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = false;
                pDown = false;
                pRight = true;
                pLeft = false;
            }else if(pY % 20 == 0 && !pLeft){
                if(pUp){
                    juncDir = 1;
                    turnDir[turnCount] = 1;
                    turnCount ++;
                }else if(pDown){
                    juncDir = 2;
                    turnDir[turnCount] = 2;
                    turnCount ++;
                }
                junctionY[0] = pY; // 0
                snakePosX.add(0, pX);
                snakePosY.add(0, pY);
                pUp = false;
                pDown = false;
                pRight = true;
                pLeft = false;
            }else{
                if(pLeft && startSnake){
                    junctionY[0] = pY; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = false;
                    pRight = true;
                    pLeft = false;
                }
                if(pUp){
                    juncDir = 1;
                    turnDir[turnCount] = 1;
                    turnCount ++;
                    pY -= (pY % 20);
                    junctionY[0] = pY; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = false;
                    pRight = true;
                    pLeft = false;
                }
                if(pDown){
                    juncDir = 2;
                    turnDir[turnCount] = 2;
                    turnCount ++;
                    pY += 20-(pY % 20);
                    junctionY[0] = pY; // 0
                    snakePosX.add(0, pX);
                    snakePosY.add(0, pY);
                    pUp = false;
                    pDown = false;
                    pRight = true;
                    pLeft = false;
                }
            }
        }
        if(key == KeyEvent.VK_ESCAPE){
            if(!suspended){
                suspended = true;
                repaint();
                thread.suspend();
            }else{
                thread.resume();
                suspended = false;
            }
        }
    }

    public void keyChecks(){
        if(pUp && pY > 20){
            pY -= 2;
        }else if(pY < 20){
            lose();
        }
        if(pDown && pY < 700 - pWidth){
            pY += 2;
        }else if(pY > 700 - pWidth){
            lose();
        }
        if(pLeft && pX > 20){
            pX -= 2;
        }else if(pX < 20){
            lose();
        }
        if(pRight && pX < 980 - pWidth){
            pX += 2;
        }else if(pX > 980 - pWidth){
            lose();
        }
    }

    public void keyTyped(KeyEvent e){
    }
}