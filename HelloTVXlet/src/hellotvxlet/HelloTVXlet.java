package hellotvxlet;

import javax.tv.xlet.*;
import javax.swing.*;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.dvb.event.*;
import org.dvb.ui.*;
import org.havi.ui.*;
import org.havi.ui.event.*;
import org.havi.ui.HScene;


public class HelloTVXlet implements Xlet,HActionListener,ActionListener,UserEventListener {

  static HScene scene = null;
  
  private XletContext actualXletContext;
          
  private HText start;
  private HText end;
  private HText pointsText;
  private HText livesText;
  private HText sceneText;
  private HText validPopup;
  private HText again;
  
  private boolean hasStarted = false;
  private boolean hasEnded = false;
  private boolean oneIsSelected = true;
  private boolean twoIsSelected = false;
  private boolean threeIsSelected = false;
  
  private HTextButton btn1;
  private HTextButton btn2;
  private HTextButton btn3;
  
  int lives = 5;
  int points = 0;
  int winningPoint;
  
  private Random rnd;
  
  
    public HelloTVXlet() {
        
    }
    
    public static HScene getScene(){
        return scene;
    }

    public void initXlet(XletContext context) {
     scene = HSceneFactory.getInstance().getDefaultHScene();
     startScreen();
    }
    public void startScreen(){
        scene.removeAll();
        oneIsSelected = true;
        points = 0;
        lives = 5;
        hasStarted = false;
        hasEnded = false;
        start = new HText("press enter to try your luck");
        start.setLocation(100,10);
        start.setSize(500,100);
        start.setBordersEnabled(false);
        scene.add(start);
        
        scene.repaint();
        scene.validate();
        scene.setVisible(true);
    }
    
    public void setGameScreen(){
        scene.removeAll();
        oneIsSelected = true;
        twoIsSelected = false;
        threeIsSelected = false;
        scene = HSceneFactory.getInstance().getDefaultHScene();
        winningPoint = rnd.nextInt(3)+1;
        
        pointsText = new HText("points: "+ points);
        pointsText.setLocation(600,10);
        pointsText.setBordersEnabled(false);
        pointsText.setSize(100,50);
        scene.add(pointsText);
        
        livesText = new HText("LIVES: "+ lives);
        livesText.setLocation(450,10);
                
        sceneText = new HText("Behind wich number lies the prize?");
        sceneText.setBackground(Color.BLUE);
        sceneText.setBackgroundMode(HVisible.BACKGROUND_FILL);
        sceneText.setBordersEnabled(false);
        sceneText.setLocation(100,100);
        sceneText.setSize(500,80);
        
        btn1 = new HTextButton("1");
        btn1.setLocation(125,250);
        btn1.setSize(150,75);
        btn1.setBackground(Color.GREEN);
        btn1.setBackgroundMode(HVisible.BACKGROUND_FILL);
        btn1.setActionCommand("one");
        btn1.addHActionListener(this);
        scene.add(btn1);
        
        btn2 = new HTextButton("2");
        btn2.setLocation(325,250);
        btn2.setSize(150,75);
        btn2.setBackground(Color.RED);
        btn2.setBackgroundMode(HVisible.BACKGROUND_FILL);
        btn2.setActionCommand("two");
        btn2.addHActionListener(this);
        scene.add(btn2);
        
        btn3 = new HTextButton("3");
        btn3.setLocation(425,250);
        btn3.setSize(150,75);
        btn3.setBackground(Color.RED);
        btn3.setBackgroundMode(HVisible.BACKGROUND_FILL);
        btn3.setActionCommand("three");
        btn3.addHActionListener((this));
        scene.add(btn3);
        
        scene.repaint();
        scene.validate();
        scene.setVisible(true);
    }
    
    public void actionPreformed(ActionEvent e){
        String action = e.getActionCommand();
        if(action.equals("one")){
            btn1.setBackground(Color.red);
            btn1.repaint();
            scene.add(btn1);
            scene.validate();
            scene.repaint();
        }
    }
    public void startXlet() {
        EventManager manager = EventManager.getInstance();
        UserEventRepository repos = new UserEventRepository("Keys");
        repos.addKey(org.havi.ui.event.HRcEvent.VK_LEFT);
        repos.addKey(org.havi.ui.event.HRcEvent.VK_RIGHT);
        repos.addKey(org.havi.ui.event.HRcEvent.VK_ENTER);
        manager.addUserEventListener(this, repos);
    }
        
        public void userEventRecieved(UserEvent e){
            if(e.getType() == KeyEvent.KEY_PRESSED){
                switch(e.getCode()){
                    case HRcEvent.VK_LEFT:
                        if(oneIsSelected){
                            //do nothing
                        }
                        if(twoIsSelected){
                            btn2.setBackground(Color.RED);
                            btn2.repaint();
                            btn1.setBackground(Color.GREEN);
                            btn1.repaint();
                            
                            oneIsSelected = true;
                            twoIsSelected = false;
                            threeIsSelected = false;
                        }
                        if(threeIsSelected){
                            btn3.setBackground(Color.RED);
                            btn3.repaint();
                            btn2.setBackground(Color.GREEN);
                            btn2.repaint();
                            
                            oneIsSelected = false;
                            twoIsSelected = true;
                            threeIsSelected = false;
                        }
                        break;
                    case HRcEvent.VK_RIGHT:
                        if(oneIsSelected){
                            btn1.setBackground(Color.RED);
                            btn1.repaint();
                            btn2.setBackground(Color.GREEN);
                            btn2.repaint();
                            
                            oneIsSelected = false;
                            twoIsSelected = true;
                            threeIsSelected = false;
                        }
                        if(twoIsSelected){
                            btn2.setBackground(Color.RED);
                            btn2.repaint();
                            btn3.setBackground(Color.GREEN);
                            btn3.repaint();
                            
                            oneIsSelected = false;
                            twoIsSelected = false;
                            threeIsSelected = true;
                        }
                        if(threeIsSelected){
                            //do nothing
                        }
                        break;
                    case HRcEvent.VK_ENTER:
                        if(hasEnded){
                            startScreen();
                        }
                        if(hasStarted){
                            checkAnswer();
                        }
                        if(!hasStarted){
                            setGameScreen();
                            hasStarted = true;
                        }
                        break;
                }
            }
        }
        
        public void checkAnswer(){
            int userAnswer = 0;
            
            scene.removeAll();
            
            if (oneIsSelected){
                userAnswer = 1;
            }
            if (twoIsSelected){
                userAnswer = 2;
            }
            if (threeIsSelected){
                userAnswer = 3;
            }
            
            if(userAnswer == winningPoint){
                points = points +10;
                validPopup = new HText("Correct");
                validPopup.setLocation(200,300);
                validPopup.setSize(250,150);
                validPopup.setBordersEnabled(false);
                validPopup.setBackground(Color.GREEN);
                scene.add(validPopup);
                scene.repaint();
                scene.validate();
                scene.setVisible(true);
                
                try{
                    Thread.sleep(1000);
                } catch(InterruptedException ex){
                    gameOver();
                }
                scene.setVisible(true);
                if(lives > 0){
                    setGameScreen();
                }
                else{
                    gameOver();
                    hasEnded = true;
                }
            }
            else{
                validPopup = new HText("wrong the correct answer was "+winningPoint);
                validPopup.setLocation(100,300);
                validPopup.setSize(500,150);
                validPopup.setBordersEnabled(false);
                validPopup.setBackground(Color.RED);
                scene.popToFront(validPopup);
                scene.add(validPopup);
                points = points +0;
                lives--;
                
                scene.repaint();
                scene.validate();
                scene.setVisible(true);
                if(lives > 0){
                    setGameScreen();
                }
                else{
                    gameOver();
                    hasEnded = true;
                }
            }
        }
        
    public void gameOver(){
        scene.removeAll();
        scene.popToFront(end);
        scene.remove(validPopup);
        end = new HText("Game Over. You gained "+ points +" points.");
        end.setLocation(100,10);
        end.setSize(500,100);
        end.setBordersEnabled(false);
        scene.add(end);
        
        again = new HText("Press ENTER to try again");
        again.setLocation(100,30);
        again.setSize(500,100);
        again.setBordersEnabled(false);
        scene.add(again);
        
        scene.repaint();
        scene.validate();
        scene.setVisible(true);
    }
    public void pauseXlet() {
     
    }

    public void destroyXlet(boolean unconditional) {
     
    }

    public void actionPerformed(ActionEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void userEventReceived(UserEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
