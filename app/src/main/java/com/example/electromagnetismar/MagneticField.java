package com.example.electromagnetismar;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.IOException;

public class MagneticField extends TemplateObjectAr {

    private  String fragmentShaderCode;

    public MagneticField(Context context, String objectPath, String fragmentShaderCode ) throws IOException {
        super(context, objectPath);
        this.fragmentShaderCode = fragmentShaderCode;
        super.glLinkProgram(this.getFragmentShaderCode());

    }

    public String getFragmentShaderCode() {
        return fragmentShaderCode;
    }
}
