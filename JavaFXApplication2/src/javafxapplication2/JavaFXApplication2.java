/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import javafx.animation.* ;  // AnimationTimer, etc.
import javafx.geometry.* ;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text ;
import javafx.stage.Stage; 
import javafx.scene.shape.* ;
import javafx.scene.paint.Color;
import javafx.scene.input.* ;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

/**
 * Ohjelma sisältää luokan Pommi, josta löytyy metodit alusta, rajahda ja move
 * move() ohjaa ammuksen kimpoilua, muuttaa sen väriä ja kutsuu tarvittaessa muita metodeja
 * rajahda() räjäyttää ammuksen, alusta() palauttaa sen lähtöpaikkaansa
 * nuolinäppäimet kääntävät tykin piippua ja paikallaan olevan ammuksen suuntaa
 * välilyönti laukaisee tykin
 * animation-timerin handle-metodi kutsuu move-metodia kunnes boolean "game over"
 * saa arvon true, jolloin animaatio loppuu
 *
 * @author Joona Martinkauppi 
 */

class Pommi extends Group{
    public double nopeus = 0.0;
    public double suunta = Math.PI / 2 -0.01;
    public int bounceCounter = 0;
    public boolean voitto = false;
    public boolean tappio = false;
    public boolean game_over = false;
    Circle tausta;
    Rectangle bouncing_area;
    double last_movement_x, last_movement_y;
    
    public Pommi(Point2D given_position,
                 Color given_color,
                 Rectangle given_bouncing_area){
        tausta = new Circle(given_position.getX(),
                            given_position.getY(),
                            6,given_color);
        bouncing_area = given_bouncing_area;
        tausta.setFill(Color.WHITE);
        getChildren().add(tausta);
    }
    public void alusta(){
            tausta.setRadius(5);
            nopeus = 0;
            tausta.setCenterX(400);
            tausta.setCenterY(580);
            bounceCounter = 0;
            suunta = Math.PI / 2 -0.01;
            tausta.setFill(Color.WHITE);
    }
    public void rajahda(){
        tausta.setRadius(tausta.getRadius()+4);
        tausta.setFill(new Color(1,0,0,1.0));
            if(tausta.getRadius()>30){
                tausta.setFill(Color.GOLD);
            }
            if(tausta.getRadius()>50){
                tausta.setFill(Color.LIGHTGOLDENRODYELLOW);
            }
            if(tausta.getRadius()>60){
                alusta();
                    if(voitto==true){
                        game_over =true;
                    }
                    if(tappio==true){
                        game_over = true;
                    }
            }
    }

    public void move(){
        last_movement_x = nopeus * Math.cos(suunta);
        last_movement_y = -nopeus * Math.sin(suunta);
        tausta.setCenterX(tausta.getCenterX()+last_movement_x);
        tausta.setCenterY(tausta.getCenterY()+last_movement_y);
        switch(bounceCounter){
            case 0:
                break;
            case 1:
                tausta.setFill(Color.YELLOW);
                break;
            case 2:
                tausta.setFill(Color.ORANGERED);
                break;
            case 3:
                tausta.setFill(Color.RED);
                break;
        }
        if (bounceCounter > 4){
            nopeus = 0;
            rajahda();
        }
        if(tausta.getCenterY()- tausta.getRadius()<=bouncing_area.getY()){
            suunta = 2 * Math.PI - suunta;
            bounceCounter++;
        }
        
        if(tausta.getCenterY()>= 140 && tausta.getCenterY() <= 160 &&
            300 <= tausta.getCenterX()&& tausta.getCenterX() <= 500){
            suunta = 2 * Math.PI - suunta;
            bounceCounter++;
        }
            if(tausta.getCenterY()>= 530 && tausta.getCenterY() <= 600 &&
            370 <= tausta.getCenterX()&& tausta.getCenterX() <= 430){
                if(bounceCounter >=1){
                    nopeus = 0;
                    rajahda();
                    tappio = true;
                }      
        }
        
        if(tausta.getCenterY()>= 100 && tausta.getCenterY() <= 150 &&
            370 <= tausta.getCenterX()&& tausta.getCenterX() <= 430){
            nopeus = 0;
            voitto = true;
            rajahda();
        }
        
        if(tausta.getCenterX()-tausta.getRadius() <= bouncing_area.getX()){
            suunta = Math.PI - suunta;
            bounceCounter++;
        }
        if(tausta.getCenterY()+tausta.getRadius() 
                >=(bouncing_area.getY() + bouncing_area.getHeight())){
            suunta = 2* Math.PI -suunta;
            bounceCounter++;
        }
        if(tausta.getCenterX() + tausta.getRadius()
                >=(bouncing_area.getX()+ bouncing_area.getWidth())){
            suunta = Math.PI - suunta;
            bounceCounter++;
        }
    }
}

    
public class JavaFXApplication2 extends Application {
    KeyCode code_of_last_pressed_key  =  KeyCode.EXCLAMATION_MARK ;
    AnimationTimer animation_timer;
    public int paikkaX = 400;
    public int paikkaY = 580;

    
   public void start( Stage stage )
   {
      stage.setTitle( "Harjoitus/JoonaMartinkauppi.java" ) ;
      
      Image taustakuva = new Image("javafxapplication2/landscape.jpg");
      Image tankkikuva = new Image("javafxapplication2/tank.png");
      Image kohde = new Image("javafxapplication2/target.png");
      Image ripkuva = new Image("javafxapplication2/rip.png");
      
      ImageView rip = new ImageView(ripkuva);
      rip.setX(360);
      rip.setY(520);
      ImageView kuvaus = new ImageView (taustakuva);
      kuvaus.setX(0);
      kuvaus.setY(0);
      ImageView tankki1 = new ImageView (tankkikuva);
      tankki1.setX(360);
      tankki1.setY(520);
      ImageView target = new ImageView(kohde);
      target.setX(370);
      target.setY(80);
      
      Rectangle  bouncing_area  =  new Rectangle( 0, 0, 800, 590 ) ;

      Group muotoryhma = new Group() ;
      
      Font font = new Font ("Tahoma", 25);
      Font ohjefont = new Font ("Helvetica", 18);
      Text voittoviesti = new Text (250, 300, "ONNEKSI OLKOON, VOITIT!");
      voittoviesti.setFont(font);
      
      Text tappioviesti = new Text (250, 300, "AMMUIT ITSESI, HÄVISIT!");
      tappioviesti.setFont(font);
      
      
      Arc piippu = new Arc (400, 580, 45, 45, 85 ,10);
      piippu.setType(ArcType.ROUND);
      piippu.setFill(Color.BLACK);
      

      Scene scene = new Scene( muotoryhma, 800, 600 ) ;
      
      
      Pommi eka = new Pommi(new Point2D(paikkaX,paikkaY), Color.WHITE, bouncing_area);
      muotoryhma.getChildren().add(kuvaus);
      muotoryhma.getChildren().add(eka);
      animation_timer = new AnimationTimer(){
          public void handle(long timestamp_of_current_frame){
              eka.move();
              if (eka.game_over == true){
                  animation_timer.stop();
                  if(eka.voitto == true){
                      muotoryhma.getChildren().add(voittoviesti);
                  }
                  if(eka.tappio == true){
                      muotoryhma.getChildren().add(tappioviesti);
                      muotoryhma.getChildren().removeAll(tankki1, piippu, eka);
                      muotoryhma.getChildren().add(rip);
                  }
              }
          };
      };
      animation_timer.start();

      scene.setFill( Color.LIGHTYELLOW ) ;

      Text ohjeet = new Text( 10, 30, "Tähtää nuolinäppäimillä"
              + "\nAmmu välilyönnillä\nAmmus pomppaa neljästi\nVaro kimmokkeita!" ) ;
      ohjeet.setFont(ohjefont);

      
      Line taso = new Line (300, 150, 500, 150 );
      taso.setStroke(Color.BLACK);
      taso.setStrokeWidth( 7 );
      
      muotoryhma.getChildren().addAll( ohjeet,
                                       piippu,
                                       taso,
                                       target,
                                       tankki1
                                     ) ;

      stage.setScene( scene ) ;
      stage.setResizable(false);
      stage.show();
        scene.setOnKeyPressed( ( KeyEvent event ) ->
      {
         if(event.getCode() == KeyCode.RIGHT){
             if(piippu.getStartAngle()>10){
                      piippu.setStartAngle(piippu.getStartAngle()-5);
             }
             if(eka.nopeus == 0 && piippu.getStartAngle()>10){
                 eka.suunta = eka.suunta - 0.1;
             }
         }
         if (event.getCode() == KeyCode.LEFT){
             if(piippu.getStartAngle()<160){
                     piippu.setStartAngle(piippu.getStartAngle()+5);
             }
             if(piippu.getStartAngle()<160 && eka.nopeus == 0){
                 eka.suunta = eka.suunta +0.1;
             }
         }
         if (event.getCode() == KeyCode.SPACE){
             eka.nopeus = 5;
             piippu.setStartAngle(85);
         }
      } ) ;

   }


   public static void main( String[] command_line_parameters )
   {
      launch( command_line_parameters ) ;
   }
}


