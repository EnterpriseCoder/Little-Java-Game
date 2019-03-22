package a;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import ameno.Screen;
import ameno.SpriteSheet;

public class Game extends Canvas implements Runnable{


	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH/12*9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";
	
	private JFrame frame;
	
	public boolean running=false;
	public int tickCount =0;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int [] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
    public Screen screen;
    public InputHandler input;
public Game() {
	setMinimumSize(new Dimension( WIDTH*SCALE , HEIGHT*SCALE));
	setMaximumSize(new Dimension (WIDTH*SCALE, HEIGHT*SCALE));
	setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
	frame = new JFrame(NAME);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BorderLayout());
	frame.add(this,BorderLayout.CENTER);
	frame.pack();
	frame.setResizable(true);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
}

public void init() {
	screen = new Screen(WIDTH,HEIGHT,new SpriteSheet("/sprite_sheet.png"));
input = new InputHandler(this);
}
public synchronized void start() {
	running = true;
	new Thread(this).start();
	
}
public synchronized void stop() {
	running = false;
}

	public void run() {
		
		long lastTime =System.nanoTime();
		double nsPerTick=1000000000D/60D;
		int ticks = 0;
		int frames = 0;
long lastTimer = System.currentTimeMillis();
double delta = 0;
init();
		while(running) {
			long now=System.nanoTime();
			delta +=(now -lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while(delta>=1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(shouldRender) {
				frames++;
				render();
				
			}
		
		if(System.currentTimeMillis()-lastTimer>=1000) {
			lastTimer +=1000;
			System.out.println("  "+ticks+","+frames);
			frames = 0;
			ticks = 0;
			
		}
		}
	}
    
	public void tick() {
		tickCount++;
		
		if(input.up.isPressed()) {
			screen.yOffset--;
			
		}
		if(input.down.isPressed()) {
			screen.yOffset++;
			
		}
		if(input.left.isPressed()) {
			screen.xOffset--;
			
		}
		if(input.right.isPressed()) {
			screen.xOffset++;
			
		}
		//screen.xOffset++;
		//screen.yOffset++;
		for(int i=0;i<pixels.length;i++) {
			pixels[i] = (i * tickCount)^2222/tickCount;
			
			
		}
	}
	public void render() {
		BufferStrategy ba = getBufferStrategy();
		if(ba==null) {
			createBufferStrategy(3);
			return;
		}
		screen.render(pixels, 0, WIDTH);
		Graphics g = ba.getDrawGraphics();
	
		g.drawImage(image, 0, 0, getWidth(),getHeight(), null);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
	   g.dispose();
	   ba.show();
	}
	
	public static void main(String[] args){
		new Game().start();
		
	}}
	
