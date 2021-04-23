package it.unibo.oop.lab.reactivegui02;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;


public class ConcurrentGUI extends JFrame {
    
    
    private final JButton stop = new JButton("Stop");
    private final JButton up = new JButton("Up");
    private final JButton down = new JButton("Down");
    private final JLabel display = new JLabel();
    
    
    private static final long serialVersionUID = 1L;
    private static final double WIDTH = 0.2;
    private static final double HEIGHT = 0.2;
    
    public ConcurrentGUI() {
        
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH), (int) (screenSize.getHeight() * HEIGHT));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel canvas = new JPanel();
        canvas.add(display);
        canvas.add(down);
        canvas.add(stop);
        canvas.add(up);
        this.getContentPane().add(canvas);
        this.setVisible(true);
        
        final Agent agent = new Agent();
        new Thread(agent).start();
        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
              
                agent.stopCounting();
                up.setEnabled(false);
                down.setEnabled(false);
                stop.setEnabled(false);
            }
        });
        
        up.addActionListener(new ActionListener() {
            
            public void actionPerformed(final ActionEvent e) {
                agent.countUp();
            }
        });
        down.addActionListener(new ActionListener() {
            
            public void actionPerformed(final ActionEvent e) {
                agent.countDown();
            }
        });
        
    }
    
    
    private class Agent implements Runnable {
        
        private volatile boolean stop;
        private volatile boolean up = true;
        private volatile boolean down;
        private volatile int counter;
        
        @Override
        public void run() {
            while(!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter)));
                
                Thread.sleep(100);
                if(this.down) {
                    this.counter--;
                }
                
                if(this.up) {
                    this.counter++;
                    
                }
                } catch (InvocationTargetException | InterruptedException ex) {
              
                    ex.printStackTrace();
                }
                
           
            }
            
        }
        
        public void stopCounting() {
            this.stop = true;
        }
        
        public void countUp() {
            this.down = false;
            this.up = true;
        }
        
        public void countDown() {
            this.up = false;
            this.down = true; 
        }
        
        
    }


}
