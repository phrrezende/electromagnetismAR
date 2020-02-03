package com.example.electromagnetismar;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.IOException;

public class MagneticField extends TemplateObjectAr {

    private  String fragmentShaderCode =
            "precision mediump float;"+
                    "void main() {"+
                    "gl_FragColor = vec4(1, 0, 0, 1.0);"+
                    "}";

    public MagneticField(Context context, String objectPath ) throws IOException {
        super(context, objectPath);
        super.glLinkProgram(this.getFragmentShaderCode());
    }

    public String getFragmentShaderCode() {
        return fragmentShaderCode;
    }
}
