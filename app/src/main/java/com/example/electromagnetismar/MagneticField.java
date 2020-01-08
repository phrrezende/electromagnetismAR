package com.example.electromagnetismar;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import org.artoolkitx.arx.arxj.rendering.OpenGLShader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MagneticField {
    private List<String> verticesList;
    private List<String> facesList;
    private FloatBuffer verticesBuffer;
    private ShortBuffer facesBuffer;
    private int program;
    private Context context;
    private final String vertexShaderCode =
            "attribute vec4 position;"+
                    "uniform mat4 matrix;"+
                    "void main() {"+
                    " gl_Position = matrix * position;"+
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;"+
                    "void main() {"+
                    "gl_FragColor = vec4(0, 0, 0.78999, 1.0);"+
                    "}";



    public MagneticField(Context context) throws IOException {
        verticesList = new ArrayList<>();
        facesList = new ArrayList<>();
        this.context = context;

        Scanner scanner = new Scanner(context.getAssets().open("campo_magnetico_22.obj"));

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.startsWith("v ")) {
                // Add vertex line to list of vertices
                verticesList.add(line);
            } else if(line.startsWith("f ")) {
                // Add face line to faces list
                facesList.add(line);
            }
        }
        scanner.close();

        ByteBuffer buffer1 = ByteBuffer.allocateDirect(verticesList.size() * 3 * 4);
        buffer1.order(ByteOrder.nativeOrder());
        verticesBuffer = buffer1.asFloatBuffer();

        ByteBuffer buffer2 = ByteBuffer.allocateDirect(facesList.size() * 3 * 2);
        buffer2.order(ByteOrder.nativeOrder());
        facesBuffer = buffer2.asShortBuffer();

        for(String vertex: verticesList) {
            String coords[] = vertex.split(" "); // Split by space
            float x = Float.parseFloat(coords[1]);
            float y = Float.parseFloat(coords[2]);
            float z = Float.parseFloat(coords[3]);
            verticesBuffer.put(x);
            verticesBuffer.put(y);
            verticesBuffer.put(z);
        }
        verticesBuffer.position(0);

        for(String face: facesList) {
            String vertexIndices[] = face.split(" ");
            short vertex1 = Short.parseShort(vertexIndices[1]);
            short vertex2 = Short.parseShort(vertexIndices[2]);
            short vertex3 = Short.parseShort(vertexIndices[3]);
            facesBuffer.put((short)(vertex1 - 1));
            facesBuffer.put((short)(vertex2 - 1));
            facesBuffer.put((short)(vertex3 - 1));
        }
        facesBuffer.position(0);

//        InputStream vertexShaderStream = context.getResources().openRawResource(R.raw.vertex_shader);
//        this.vertexShaderCode = IOUtils.toString(vertexShaderStream, Charset.defaultCharset());
//        vertexShaderStream.close();
//        InputStream fragmentShaderStream = context.getResources().openRawResource(R.raw.fragment_shader);
//        this.fragmentShaderCode = IOUtils.toString(fragmentShaderStream, Charset.defaultCharset());
//        fragmentShaderStream.close();

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShader,vertexShaderCode );

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShader, fragmentShaderCode);

        GLES20.glCompileShader(vertexShader);
        GLES20.glCompileShader(fragmentShader);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);

        GLES20.glLinkProgram(program);
        //

    }

    public int getProjectionMatrixHandle() {
        return GLES20.glGetUniformLocation(program, OpenGLShader.projectionMatrixString);
    }

    public int getModelViewMatrixHandle() {
        return GLES20.glGetUniformLocation(program, OpenGLShader.modelViewMatrixString);
    }
    public void draw(float[] projectionMatrix, float[] viewMatrix){
        GLES20.glUseProgram(program);
        int position = GLES20.glGetAttribLocation(program, "position");
        GLES20.glEnableVertexAttribArray(position);

        GLES20.glVertexAttribPointer(position,
                3, GLES20.GL_FLOAT, false, 3 * 4, verticesBuffer);


        float[] productMatrix = new float[16];
        Matrix.multiplyMM(productMatrix, 0,
                projectionMatrix, 0,
                viewMatrix, 0);
        int matrix = GLES20.glGetUniformLocation(program, "matrix");
        GLES20.glUniformMatrix4fv(matrix, 1, false, productMatrix, 0);
        GLES20.glUniformMatrix4fv(getProjectionMatrixHandle(), 1, false, projectionMatrix, 0);
        GLES20.glUniformMatrix4fv(getModelViewMatrixHandle(), 1, false, viewMatrix, 0);
        GLES20.glDrawElements(GLES20.GL_LINES,
                facesList.size() * 3, GLES20.GL_UNSIGNED_SHORT, facesBuffer);
        GLES20.glDisableVertexAttribArray(position);
    }
}
