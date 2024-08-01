package com.osroyale.engine;

import com.osroyale.Bounds;
import com.osroyale.Client;
import com.osroyale.ProducingGraphicsBuffer;
import com.osroyale.engine.impl.KeyHandler;
import com.osroyale.engine.impl.MouseHandler;
import com.osroyale.engine.impl.MouseWheelHandler;
import com.osroyale.engine.task.Clock;
import com.osroyale.engine.task.TaskHandler;
import com.osroyale.engine.task.TaskUtils;
import com.osroyale.engine.task.impl.MilliClock;
import com.osroyale.engine.task.impl.NanoClock;
import net.runelite.api.KeyCode;
import net.runelite.api.events.CanvasSizeChanged;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.util.ImageUtil;
import net.runelite.rs.api.RSGameEngine;

import java.applet.Applet;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.net.URL;

public abstract class GameEngine extends Applet implements Runnable, WindowListener, RSGameEngine, FocusListener {

    MouseWheelHandler mouseWheelHandler;
    public static TaskHandler taskHandler;
    static GameEngine gameEngine;
    public static int canvasWidth;
    public static int canvasHeight;
    static int threadCount;
    static long stopTimeMs;
    static boolean isKilled;
    static int cycleDurationMillis;

    private Thread thread;

    @Override
    public Thread getClientThread() {
        return thread;
    }

    @Override
    public boolean isClientThread() {
        return thread == Thread.currentThread();
    }

    static int fiveOrOne;
    protected static int fps;
    static long[] graphicsTickTimes;
    static long[] clientTickTimes;
    static int field209;
    static int field199;
    static volatile boolean volatileFocus;
    static long garbageCollectorTime;
    static long lastGarbageCollect;

    boolean hasErrored;
    protected int contentWidth;
    protected int contentHeight;
    int canvasX;
    int canvasY;
    int maxCanvasWidth;
    int maxCanvasHeight;

    Frame frame;
    public java.awt.Canvas canvas;

    volatile boolean fullRedraw;
    boolean resizeCanvasNextFrame;
    volatile boolean isCanvasInvalid;
    volatile long field185;
    final EventQueue eventQueue;

    private static final long serialVersionUID = 1L;
    private static int viewportColor;

    static {
        gameEngine = null;
        threadCount = 0;
        stopTimeMs = 0L;
        isKilled = false;
        cycleDurationMillis = 20;
        fiveOrOne = 1;
        fps = 0;
        graphicsTickTimes = new long[32];
        clientTickTimes = new long[32];
        field199 = 500;
        volatileFocus = true;
        garbageCollectorTime = -1L;
        lastGarbageCollect = -1L;
    }

    protected GameEngine() {
        hasErrored = false;
        canvasX = 0;
        canvasY = 0;
        fullRedraw = true;
        resizeCanvasNextFrame = false;
        isCanvasInvalid = false;
        field185 = 0L;
        EventQueue queue = null;

        try {
            queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        this.backgroundImage = ImageUtil.loadImageResource(ClientUI.class, "/background.png");

        eventQueue = queue;

    }


    protected final void setMaxCanvasSize(int width, int height) {
        /*if (Client.instance.isStretchedEnabled() && Client.instance.isResized()) {
            return;
        }*/

        if (maxCanvasWidth != width || height != maxCanvasHeight) {
            flagResize();
        }

        maxCanvasWidth = width;
        maxCanvasHeight = height;

    }

    public final void post(Object var1) {
        if (!Client.instance.isGpu()) {
            if (eventQueue != null) {
                for (int var2 = 0; var2 < 50 && eventQueue.peekEvent() != null; ++var2) {
                    TaskUtils.sleep(1L);
                }

                if (var1 != null) {
                    eventQueue.postEvent(new ActionEvent(var1, 1001, "dummy"));
                }

            }
        }
        DrawCallbacks drawCallbacks = Client.instance.getDrawCallbacks();
        if (drawCallbacks != null) {
            drawCallbacks.draw(viewportColor);
        }
    }

    @Override
    public java.awt.Canvas getCanvas() {
        return canvas;
    }

    public final void resizeCanvas() {
        if (Client.instance.isStretchedEnabled()) {
            Client.instance.invalidateStretching(false);

            if (Client.instance.isResized()) {
                Dimension realDimensions = Client.instance.getRealDimensions();

                setMaxCanvasWidth(realDimensions.width);
                setMaxCanvasHeight(realDimensions.height);
            }
        }

        Container container = container();
        if (container != null) {
            Bounds contentBounds = getFrameContentBounds();
            contentWidth = Math.max(contentBounds.highX, 0);
            contentHeight = Math.max(contentBounds.highY, 0);
            if (contentWidth <= 0) {
                contentWidth = 1;
            }

            if (contentHeight <= 0) {
                contentHeight = 1;
            }

            if (Client.instance.isResized()) {
                setMaxCanvasSize(contentWidth, contentHeight);
            }

            canvasWidth = Math.min(contentWidth, maxCanvasWidth);
            canvasHeight = Math.min(contentHeight, maxCanvasHeight);
            Client.instance.getCallbacks().post(CanvasSizeChanged.INSTANCE);
            canvasX = (contentWidth - canvasWidth) / 2;
            canvasY = 0;
            canvas.setSize(canvasWidth, canvasHeight);
            Client.rasterProvider = new ProducingGraphicsBuffer(canvasWidth, canvasHeight, canvas);
            if (container == frame) {
                Insets insets = frame.getInsets();
                canvas.setLocation(canvasX + insets.left, insets.top + canvasY);
            } else {
                canvas.setLocation(canvasX, canvasY);
            }

            fullRedraw = true;
            resizeGame();
        }
    }

    protected abstract void resizeGame();

    void clearBackground() {
        int canvasX = this.canvasX;
        int canvasY = this.canvasY;
        int width = contentWidth - canvasWidth - canvasX;
        int height = contentHeight - canvasHeight - canvasY;
        if (canvasX > 0 || width > 0 || canvasY > 0 || height > 0) {
            try {
                Container container = container();
                int left = 0;
                int top = 0;
                if (container == frame) {
                    Insets var8 = frame.getInsets();
                    left = var8.left;
                    top = var8.top;
                }

                Graphics graphics = container.getGraphics();
                graphics.setColor(Color.black);
                if (canvasX > 0) {
                    graphics.fillRect(left, top, canvasX, contentHeight);
                }

                if (canvasY > 0) {
                    graphics.fillRect(left, top, contentWidth, canvasY);
                }

                if (width > 0) {
                    graphics.fillRect(left + contentWidth - width, top, width, contentHeight);
                }

                if (height > 0) {
                    graphics.fillRect(left, top + contentHeight - height, contentWidth, height);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public int keyPressed;

    @Override
    public boolean isResizeCanvasNextFrame() {
        return resizeCanvasNextFrame;
    }

    @Override
    public void setResizeCanvasNextFrame(boolean resize) {
        resizeCanvasNextFrame = resize;
    }

    @Override
    public boolean isReplaceCanvasNextFrame() {
        return isCanvasInvalid;
    }

    @Override
    public void setReplaceCanvasNextFrame(boolean replace) {
        isCanvasInvalid = replace;
    }

    @Override
    public void setMaxCanvasWidth(int width) {
        maxCanvasWidth = width;
    }

    @Override
    public void setMaxCanvasHeight(int height) {
        maxCanvasHeight = height;
    }

    @Override
    public void setFullRedraw(boolean fullRedraw) {
        this.fullRedraw = fullRedraw;
    }


    final void replaceCanvas() {
        if (Client.instance != null && Client.instance.isGpu()) {
            setFullRedraw(false);
            return;
        }
        this.canvas.removeMouseListener(MouseHandler.instance);
        this.canvas.removeMouseMotionListener(MouseHandler.instance);
        this.canvas.removeFocusListener(MouseHandler.instance);
        MouseHandler.currentButton = 0;
        if (this.mouseWheelHandler != null) {
            this.mouseWheelHandler.removeFrom(this.canvas);
        }
        addCanvas();
        this.addMouseListener(MouseHandler.instance);
        this.addMouseMotionListener(MouseHandler.instance);
        this.addFocusListener(MouseHandler.instance);
        if (this.mouseWheelHandler != null) {
            this.mouseWheelHandler.addTo(this.canvas);
        }

        flagResize();
    }

    protected final void startThread(int var1, int var2, int var3, int var4) {
        try {
            if (gameEngine != null) {
                ++threadCount;
                if (threadCount >= 3) {
                    error("alreadyloaded");
                    return;
                }

                getAppletContext().showDocument(getDocumentBase(), "_self");
                return;
            }

            gameEngine = this;
            canvasWidth = var1;
            canvasHeight = var2;
            Client.instance.getCallbacks().post(CanvasSizeChanged.INSTANCE);
            if (taskHandler == null) {
                taskHandler = new TaskHandler();
            }
            taskHandler.newThreadTask(this, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            error("crash");
        }

    }

    final synchronized void addCanvas() {
        Container container = container();
        if (canvas != null) {
            canvas.removeFocusListener(this);
            container.remove(canvas);
        }

        canvasWidth = Math.max(container.getWidth(), 0);
        canvasHeight = Math.max(container.getHeight(), 0);
        Client.instance.getCallbacks().post(CanvasSizeChanged.INSTANCE);
        Insets insets;
        if (frame != null) {
            insets = frame.getInsets();
            canvasWidth -= insets.left + insets.right;
            canvasHeight -= insets.bottom + insets.top;
        }

        canvas = new Canvas(this);
        setupMouse();
        setupKeys();
        setupMouseWheel();
        container.setBackground(Color.BLACK);
        container.setLayout(null);
        container.add(canvas);
        canvas.setFocusTraversalKeysEnabled(false);
        canvas.setSize(canvasWidth, canvasHeight);
        canvas.setVisible(true);
        canvas.setBackground(Color.BLACK);
        if (container == frame) {
            insets = frame.getInsets();
            canvas.setLocation(insets.left + canvasX, canvasY + insets.top);
        } else {
            canvas.setLocation(canvasX, canvasY);
        }

        canvas.addFocusListener(this);
        canvas.requestFocus();
        fullRedraw = true;
        if (Client.rasterProvider != null && canvasWidth == Client.rasterProvider.width && canvasHeight == Client.rasterProvider.height) {
            ((ProducingGraphicsBuffer) Client.rasterProvider).setComponent(canvas);
            Client.rasterProvider.drawFull(0, 0);
        } else {
            Client.rasterProvider = new ProducingGraphicsBuffer(canvasWidth, canvasHeight, canvas);

        }
        isCanvasInvalid = false;
        field185 = method2692();
    }


    void clientTick() {
        if(!hasStartedUp) return;

        long var1 = method2692();
        clientTickTimes[field209] = var1;
        field209 = field209 + 1 & 31;
        MouseHandler.clickMode3 = MouseHandler.instance.clickMode1;
        MouseHandler.instance.clickMode1 = 0;
        processGameLoop();
    }

    static int field4319;

    void graphicsTick() {
        Container var1 = container();
        long var2 = method2692();
        long var4 = graphicsTickTimes[field4319];
        graphicsTickTimes[field4319] = var2;
        field4319 = field4319 + 1 & 31;
        if (var4 != 0L && var2 > var4) {
            int var6 = (int) (var2 - var4);
            fps = ((var6 >> 1) + 32000) / var6;
        }

        if (++field199 - 1 > 50) {
            field199 -= 50;
            fullRedraw = true;
            canvas.setSize(canvasWidth, canvasHeight);
            canvas.setVisible(true);
            if (var1 == frame) {
                Insets var7 = frame.getInsets();
                canvas.setLocation(var7.left + canvasX, canvasY + var7.top);
            } else {
                canvas.setLocation(canvasX, canvasY);
            }
        }

        if (isCanvasInvalid) {
            replaceCanvas();
        }

        doResize();
        draw(fullRedraw);
        if (fullRedraw) {
            clearBackground();
        }

        fullRedraw = false;
    }

    final void doResize() {
        Bounds bounds = this.getFrameContentBounds();
        if (this.contentWidth != bounds.highX || bounds.highY != this.contentHeight || this.resizeCanvasNextFrame) {
            resizeCanvas();
            resizeCanvasNextFrame = false;
        }

    }

    final void flagResize() {
        resizeCanvasNextFrame = true;
    }

    final synchronized void kill() {
        if (!isKilled) {
            isKilled = true;

            try {
                canvas.removeFocusListener(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                cleanUpForQuit();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (frame != null) {
                try {
                    System.exit(0);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }

            if (taskHandler != null) {
                try {
                    taskHandler.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            vmethod1099();
        }
    }

    protected abstract void startUp();

    protected abstract void processGameLoop();

    public Thread startRunnable(Runnable runnable, int i) {
        Thread thread = new Thread(runnable);
        thread.start();
        thread.setPriority(i);
        return thread;
    }

    protected abstract void draw(boolean var1);

    protected abstract void cleanUpForQuit();

    static Font fontHelvetica;
    static FontMetrics loginScreenFontMetrics;
    private final Image backgroundImage;

    protected final void drawInitial(int var1, String var2, boolean clear) {
        try {
            Graphics graphics = canvas.getGraphics();
            if (fontHelvetica == null) {
                fontHelvetica = new Font("Helvetica", Font.BOLD, 13);
                loginScreenFontMetrics = canvas.getFontMetrics(fontHelvetica);
            }

            if (clear) {
                graphics.setColor(Color.black);
                graphics.fillRect(0, 0, canvasWidth, canvasHeight);
            }

            try {
                graphics.drawImage(backgroundImage, 0, 0, null);

                graphics.setFont(fontHelvetica);
                graphics.setColor(Color.white);
                graphics.drawString(var2, (canvasWidth / 2 - 152) + (304 - loginScreenFontMetrics.stringWidth(var2)) / 2, (canvasHeight / 2 - 18) + 22);
            } catch (Exception exception) {
                exception.printStackTrace();

                int centerX = canvasWidth / 2 - 152;
                int centerY = canvasHeight / 2 - 18;
                graphics.setColor(new Color(140, 17, 17));
                graphics.drawRect(centerX, centerY, 303, 33);
                graphics.fillRect(centerX + 2, centerY + 2, var1 * 3, 30);
                graphics.setColor(Color.black);
                graphics.drawRect(centerX + 1, centerY + 1, 301, 31);
                graphics.fillRect(var1 * 3 + centerX + 2, centerY + 2, 300 - var1 * 3, 30);
                graphics.setFont(fontHelvetica);
                graphics.setColor(Color.white);
                graphics.drawString(var2, centerX + (304 - loginScreenFontMetrics.stringWidth(var2)) / 2, centerY + 22);
            }
        } catch (Exception exception) {
            canvas.repaint();
            exception.printStackTrace();
        }

    }

    protected void error(String var1) {
        if (!hasErrored) {
            hasErrored = true;
            System.out.println("error_game_" + var1);

            try {
                getAppletContext().showDocument(new URL(getCodeBase(), "error_game_" + var1 + ".ws"), "_self");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    Container container() {
        return frame != null ? frame : this;
    }

    protected Bounds getFrameContentBounds() {
        Container container = container();
        int boundsX = Math.max(container.getWidth(), 0);
        int boundsY = Math.max(container.getHeight(), 0);
        if (frame != null) {
            Insets var4 = frame.getInsets();
            boundsX -= var4.left + var4.right;
            boundsY -= var4.top + var4.bottom;
        }

        return new Bounds(boundsX, boundsY);
    }

    protected final boolean hasFrame() {
        return frame != null;
    }

    protected abstract void vmethod1099();

    public final void destroy() {
        if (this == gameEngine && !isKilled) {
            stopTimeMs = method2692();
            TaskUtils.sleep(5000L);
            kill();
        }
    }

    public final synchronized void paint(Graphics var1) {
        if (this == gameEngine && !isKilled) {
            fullRedraw = true;
            if (method2692() - field185 > 1000L) {
                Rectangle var2 = var1.getClipBounds();
                if (var2 == null || var2.width >= canvasWidth && var2.height >= canvasHeight) {
                    isCanvasInvalid = true;
                }
            }

        }
    }

    public void onReplaceCanvasNextFrameChanged(int idx) {
        if (Client.instance != null && Client.instance.isGpu() && isReplaceCanvasNextFrame()) {
            setReplaceCanvasNextFrame(false);
            setResizeCanvasNextFrame(true);
        }
    }

    static int gameCyclesToDo;
    static Clock clock;

    public static Clock getClock() {
        try {
            return new NanoClock();
        } catch (Throwable throwable) {
            return new MilliClock();
        }
    }

    private volatile boolean hasStartedUp;
    public void run() {
        try {
            thread = Thread.currentThread();
            thread.setName("Client");

            setFocusCycleRoot(true);
            addCanvas();

            clock = getClock();
            boolean startUpThread = false;

            while (0L == stopTimeMs || method2692() < stopTimeMs) {
                if(!startUpThread && canvas.getGraphics() != null) {
                    startUpThread = true;
                    //new Thread(() -> {
                        startUp();
                        hasStartedUp = true;
                    //}).start();
                }

                gameCyclesToDo = clock.wait(cycleDurationMillis, fiveOrOne);

                for (int cycles = 0; cycles < gameCyclesToDo; ++cycles) {
                    clientTick();
                }

                graphicsTick();
                post(canvas);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            error("crash");
        }

        kill();
    }

    public final void start() {
        if (this == gameEngine && !isKilled) {
            stopTimeMs = 0L;
        }
    }

    public final void focusGained(FocusEvent var1) {
        volatileFocus = true;
        fullRedraw = true;
        final FocusChanged focusChanged = new FocusChanged();
        focusChanged.setFocused(true);
        Client.instance.getCallbacks().post(focusChanged);
    }

    public final void focusLost(FocusEvent var1) {
        volatileFocus = false;
    }

    public final void windowActivated(WindowEvent var1) {
    }

    public final void windowDeactivated(WindowEvent var1) {
    }

    public final void windowDeiconified(WindowEvent var1) {
    }

    public final void windowOpened(WindowEvent var1) {
    }


    public final void stop() {
        if (this == gameEngine && !isKilled) {
            stopTimeMs = method2692() + 4000L;
        }
    }

    public static synchronized long method2692() {
        long var0 = System.currentTimeMillis();
        if (var0 < field3170) {
            field4425 += field3170 - var0;
        }

        field3170 = var0;
        return field4425 + var0;
    }

    public final void setupMouse() {
        this.canvas.addMouseListener(MouseHandler.instance);
        this.canvas.addMouseMotionListener(MouseHandler.instance);
        this.canvas.addFocusListener(MouseHandler.instance);
    }

    public final void setupMouseWheel() {
        if (this.mouseWheelHandler == null) {
            this.mouseWheelHandler = new MouseWheelHandler();
            this.mouseWheelHandler.addTo(this.canvas);
        }
    }


    public Clipboard clipboard;

    public final void setupKeys() {
        this.canvas.addKeyListener(KeyHandler.instance);
        this.canvas.addFocusListener(KeyHandler.instance);
        setUpClipboard();
    }

    protected void setUpClipboard() {
        this.clipboard = this.getToolkit().getSystemClipboard();
    }

    protected void setClipboardText(String text) {
        this.clipboard.setContents(new StringSelection(text), (ClipboardOwner)null);
    }

    static long field3170;
    static long field4425;

    public final void windowIconified(WindowEvent var1) {
    }

    public final void windowClosed(WindowEvent var1) {
    }

    public final void windowClosing(WindowEvent var1) {
        destroy();
    }

    public final void update(Graphics var1) {
        paint(var1);
    }



    public boolean isKeyPressed(int keycode) {

        if (keycode == KeyCode.KC_SHIFT) {
            return Client.instance.shiftIsDown;
        }

        return KeyHandler.keyArray[keycode] == 1;
    }
}
