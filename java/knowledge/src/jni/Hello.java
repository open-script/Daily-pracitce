package jni;

/**
 * Created by zzt on 4/5/15.
 */
public class Hello {
    static {
        System.loadLibrary("hello"); // Load native library at runtime
        // hello.dll (Windows) or libhello.so (Unixes)
    }

    // Declare a native method sayHello() that receives nothing and returns void
    private native void sayHello();

    // Test Driver
    public static void main(String[] args) {
        new Hello().sayHello();  // invoke the native method
    }
}
