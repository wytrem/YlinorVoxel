package com.ylinor.library.api;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;


public class Tests {
    public static void main(String[] args) {

        //		StateProperty<Facing> a = PropertyEnum.create("toto", Facing.class);
        //		StateProperty<Facing> b = PropertyEnum.create("tata", Facing.class);
        //		StateProperty<Boolean> c = PropertyBool.create("coucou");
        //		BlockStateFactory factory = new BlockStateFactory(null, new StateProperty<?>[] {a, b, c});
        //	
        //		BlockState state = factory.getOneState();
        //		System.out.println(state.toString());
        //		state = state.with(a, Facing.DOWN);
        //		System.out.println(state.toString());
        //		state = state.with(c, true);
        //		System.out.println(state.toString());
        //		state = state.with(c, false);
        //		System.out.println(state.toString());
        //		
        //		System.out.println(state.propertiesToString());

        Quaternionf quat = new Quaternionf();
        quat.setAngleAxis(-Math.toRadians(90), 0, 1, 0);
        
        Quaternionf inv = quat.invert(new Quaternionf());
        Vector3f origin = new Vector3f(0.5f, 0.0f, 0.5f);
        Vector3f negatedOrigin = origin.negate(new Vector3f());
        Vector3f transformedOrigin = inv.transform(origin, new Vector3f());
        Vector3f transformedNegatedOrigin = inv.transform(negatedOrigin, new Vector3f());

        System.out.println("origin = " + origin);
        System.out.println("transformedOrigin = " + transformedOrigin);
        System.out.println("negatedOrigin = " + negatedOrigin);
        System.out.println("transformedNegatedOrigin = " + transformedNegatedOrigin);
        
        Matrix4f rot = new Matrix4f().rotation(quat);
        
        Matrix4f negatedOriginMat = new Matrix4f().translation(negatedOrigin);
        Matrix4f originMat = new Matrix4f().translation(origin);
        Vector3f testVec = new Vector3f(2.0f);
        
        Matrix4f test = new Matrix4f().translation(testVec);

        Matrix4f matrix = new Matrix4f();
        
        matrix.translate(testVec).translate(origin).rotate(quat).translate(negatedOrigin);
        
//        matrix.mul(test).mul(originMat).mul(rot).mul(negatedOriginMat);

        System.out.println(matrix.toString());
        
        System.out.println(matrix.transform(1, 0, 1, 1, new Vector4f()));
    }
}
