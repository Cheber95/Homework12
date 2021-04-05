import java.util.concurrent.Callable;

public class MyApp {

    static class MyRunnableClass implements Runnable {

        @Override
        public void run() {
        }
    }

    static final int SIZE = 10000000;
    static final int HALF = SIZE/2;
    private static float[] arr = new float[SIZE];

    public static void main(String[] args) {
        
        MyApp app = new MyApp();
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1f;
        }
        app.method1();
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1f;
        }
        app.method2();
    }

    public void method1(){
        long a = System.currentTimeMillis();
        arr = MyApp.workArray(arr);
        long b = System.currentTimeMillis();
        System.out.printf("Метод 1 отработал за %d милисекунд\n", (b-a));
    }

    public void method2(){
        long a = System.currentTimeMillis();
        float[] arrHalf1 = new float[HALF];
        float[] arrHalf2 = new float[HALF];
        System.arraycopy(arr,0,arrHalf1,0,HALF);
        System.arraycopy(arr,HALF,arrHalf2,0,HALF);

        Thread t1 = new Thread( () -> MyApp.workArray(arrHalf1) );
        t1.start();
        Thread t2 = new Thread( () -> MyApp.workArray(arrHalf2) );
        t2.start();

        while (!(t1.isAlive() && t2.isAlive())) {

        }
        System.arraycopy(arrHalf1,0,arr,0,HALF);
        System.arraycopy(arrHalf2,0,arr,HALF,HALF);
        long b = System.currentTimeMillis();
        System.out.printf("Метод 2 отработал за %d милисекунд\n", (b-a));
    }

    private static float[] workArray(float[] inArray) {
        for (int i = 0; i < inArray.length; i++) {
            inArray[i] = (float)(inArray[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        return inArray;
    }
}