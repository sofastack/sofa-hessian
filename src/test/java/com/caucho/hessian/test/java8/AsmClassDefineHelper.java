/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.caucho.hessian.test.java8;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *
 * the helper will help build a wrapper class
 * say,
 * public class TWrapper extends AbstractWrapper {
 *     T t
 *     Object[] getFields() {
 *         return new Object[] {t};
 *     }
 * }
 *
 *
 * @author junyuan
 * @version AsmClassDefineHelper.java, v 0.1 2023年03月16日 20:57 junyuan Exp $
 */
public class AsmClassDefineHelper {

    public static AsmClassLoader ASM_CLASS_LOADER = new AsmClassLoader();

    public static void defineClass(String classResource, String superClassResource, String fieldResource) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, classResource, null, superClassResource, null);
        writer.visitField(Opcodes.ACC_PROTECTED, "_field", fieldResource, null, null);
        //constructor.newInstance("ja", "JP", "JP")

        {
            // build constructor
            MethodVisitor mv = writer.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superClassResource, "<init>", "()V", false);
            mv.visitInsn(Opcodes.RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            // build getFields methods
            // ref to com.caucho.hessian.test.java8.AbstractWrapper.getFields
            MethodVisitor mv = writer.visitMethod(Opcodes.ACC_BRIDGE | Opcodes.ACC_SYNTHETIC, "getFields",
                    "()[Ljava/lang/Object;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
            mv.visitInsn(Opcodes.DUP);
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, classResource, "_field", fieldResource);
            mv.visitInsn(Opcodes.AASTORE);
            mv.visitInsn(Opcodes.ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }

        writer.visitEnd();

        ASM_CLASS_LOADER.defineClass(classResource.replace("/", "."), writer.toByteArray());
    }

    public static class AsmClassLoader extends ClassLoader {
        public AsmClassLoader() {
            super(Thread.currentThread().getContextClassLoader());
        }

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length, this.getClass().getProtectionDomain());
        }
    }
}