import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class MyApp {

    static final int SIZE = 10_000_000;
    static final int HALF = SIZE/2;
    private float[] arr;

    public MyApp(float[] arr) {
        this.arr = arr;
    }

    public static void main(String[] args) {
        float[] stArray = new float[SIZE];
        MyApp app = new MyApp(stArray);
        app.play1();
        //app.play2();
    }

    private void play2() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1f;
        }
        method2();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1f;
        }
        method1();
    }

    public void play1(){
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1f;
        }
        method1();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1f;
        }
        method2();
    }

    public void method1(){
        System.out.println("========= Метод 1 =========");
        long a = System.currentTimeMillis();
        workArray();
        long b = System.currentTimeMillis();
    }


    public void method2(){
        System.out.println("========= Метод 2 =========");
        int part = HALF;
        float[] arrHalf1 = new float[arr.length - part];
        float[] arrHalf2 = new float[part];
        long a = System.currentTimeMillis();
        System.arraycopy(arr,0,arrHalf1,0,arrHalf1.length);
        System.arraycopy(arr,arrHalf1.length,arrHalf2,0,arrHalf2.length);
        long b = System.currentTimeMillis();
        System.out.printf("разделение массива произведено за %d милисекунд\n", (b-a));

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                MyApp myAppThread1 = new MyApp(arrHalf1);
                myAppThread1.workArray();
            }
        });
        t1.start();
        MyApp myAppMain = new MyApp(arrHalf2);
        myAppMain.workArray();

        boolean threadWorked;
        do {
            threadWorked = t1.isAlive();
        } while (threadWorked);

        a = System.currentTimeMillis();
        System.arraycopy(arrHalf1,0,arr,0,arrHalf1.length);
        System.arraycopy(arrHalf2,0,arr,arrHalf1.length,arrHalf2.length);
        b = System.currentTimeMillis();
        System.out.printf("Склейка массива произведена за %d милисекунд\n", (b-a));
    }

    private void workArray() {
        long a = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            float j = (float) i;
            arr[i] = (float)(arr[i] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
        }
        long b = System.currentTimeMillis();
        System.out.printf("поток %s просчитал массив за %d милисекунд \n", Thread.currentThread().getName(), (b-a));
    }
}