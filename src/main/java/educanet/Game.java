package educanet;

import educanet.utils.Colors;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;



public class Game {

    private static final int windowWidth = Main.width;
    private static final int windowHeight = Main.height;

    private static final float aspectRatio = (float)windowWidth / windowHeight;
    private static final float aspectRatio2 = (float)windowHeight / windowWidth;


    private static final float baseSpeed = 0.0001f;
    private static final float xSpeed = baseSpeed * aspectRatio;
    private static final float ySpeed = baseSpeed * aspectRatio2;

    private static boolean xDir = false;
    private static boolean yDir = false;



    private static final float[] vertices = {
            0.5f, 0.5f, 0.0f, // 0 -> Top right
            0.5f, -0.5f, 0.0f, // 1 -> Bottom right
            -0.5f, -0.5f, 0.0f, // 2 -> Bottom left
            -0.5f, 0.5f, 0.0f, // 3 -> Top left
    };

    private static float[] color = Colors.INTERPOLATED;

    private static final float[] textures = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f
    };

    private static final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    private static int squareVaoId;
    private static int squareVboId;
    private static int squareEboId;
    private static int colorsId;
    private static int textureIndicesId;

    private static int textureId;


    static FloatBuffer fb;
    static FloatBuffer cb;


    public static void init(long window) {
        System.out.println("AR1: " + aspectRatio + " | AR2: " + aspectRatio2);
        System.out.println("xSpeed: " + xSpeed + " | ySpeed: " + ySpeed);

        offsetObjectBy(vertices, 0.3f);
        scaleDownBy(vertices, 3f);






        OpenGlShit();
    }

    public static void offsetObjectBy(float[] vertices, float value) {
        for (int i = 0; i < vertices.length; i++) {
            if(i % 3 == 0) vertices[i] -= value;
        }
    }

    public static void scaleDownBy(float[] vertices, float value) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] /= value;
        }
    }


    public static void render(long window) {
        GL33.glUseProgram(Shaders.shaderProgramId);

        // Draw using the glDrawElements function
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public static void update(long window) {
        checkCollision();
        moveObject(vertices);
        OGLverts();
    }

    public static void moveObject(float[] vertices) {

        for (int i = 0; i < vertices.length; i++) {
            float element = vertices[i];

            //x
            if(i % 3 == 0) {
                if(xDir) element += xSpeed; else element -= xSpeed;
            }
            //y
            if(i % 3 == 1) {
                if(yDir) element += ySpeed; else element -= ySpeed;
            }

            vertices[i] = element;
        }

    }

    public static void checkCollision() {
        float[] topRight = { vertices[0], vertices[1 ] };
        float[] botRight = { vertices[3], vertices[4 ] };
        float[] botLeft  = { vertices[6], vertices[7 ] };
        float[] topLeft  = { vertices[9], vertices[10] };




        if((botLeft[0] < -1f || botLeft[1] < -1f) && (topLeft[0] < -1f || topLeft[1] > 1f)) {
            xDir = !xDir;
            changeColor();
            return;
        }


        if((botRight[0] > 1f || botRight[1] < -1f) && (topRight[0] > 1f || topRight[1] > 1f)) {
            xDir = !xDir;
            changeColor();
            return;
        }

        if((topLeft[0] < -1f || topLeft[1] > 1f) && (topRight[0] > 1f || topRight[1] > 1f)) {
            yDir = !yDir;
            changeColor();
            return;
        }

        if((botLeft[0] < -1f || botLeft[1] < -1f) && (botRight[0] > 1f || botRight[1] < -1f)) {
            changeColor();
            yDir = true;
        }

    }


    public static void changeColor() {

        Colors.add(color, 0.1f, "R");
        Colors.add(color, -0.2f, "G");
        Colors.add(color, 0.3f, "B");

        OGLcolors();
    }


    private static void loadImage() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            String path = "D:\\skola\\GDV\\OpenGL-Examples\\DVD_Screen\\src\\main\\resources\\dvd.png";

            ByteBuffer img = STBImage.stbi_load(path, w, h, comp, 4);
            if (img != null) {
                img.flip();


                GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
                GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, w.get(), h.get(), 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, img);
                GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);


                STBImage.stbi_image_free(img);
            }
        }
    }

    public static void OpenGlShit() {
        // Setup shaders
        Shaders.initShaders();
        OGLgenerateVars();

        loadImage();

        fb = BufferUtils.createFloatBuffer(vertices.length);
        cb = BufferUtils.createFloatBuffer(  color.length);
        OGLindices();
        OGLverts();
        OGLcolors();
        OGLtextures();
    }
    private static void OGLgenerateVars() {
        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();
        colorsId = GL33.glGenBuffers();
        textureIndicesId = GL33.glGenBuffers();
        textureId = GL33.glGenTextures();
    }
    private static void OGLindices() {
        GL33.glBindVertexArray(squareVaoId);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);
    }
    private static void OGLverts() {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        fb.clear().put(vertices).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

    }
    private static void OGLcolors() {

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);

        cb.clear().put(color).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

    }
    private static void OGLtextures() {

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);

        FloatBuffer tb = BufferUtils.createFloatBuffer(textures.length)
                .put(textures)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(2);

        MemoryUtil.memFree(tb);
    }
}
