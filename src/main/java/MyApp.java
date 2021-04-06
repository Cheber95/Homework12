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
        app.play();
    }

    public void play(){
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
        long a = System.currentTimeMillis();
        workArray();
        long b = System.currentTimeMillis();
        System.out.printf("Метод 1 отработал за %d милисекунд\n", (b-a));
    }


    public void method2(){
        long a = System.currentTimeMillis();
        int part = HALF;
        float[] arrHalf1 = new float[arr.length - part];
        float[] arrHalf2 = new float[part];
        System.arraycopy(arr,0,arrHalf1,0,arrHalf1.length);
        System.arraycopy(arr,arrHalf1.length,arrHalf2,0,arrHalf2.length);


        Thread t1 = new Thread( () -> new MyApp(arrHalf1).workArray() );
        t1.start();
        Thread t2 = new Thread( () -> new MyApp(arrHalf2).workArray() );
        t2.start();

        while (t1.isAlive() || t2.isAlive()) {
            continue;
        }
        System.arraycopy(arrHalf1,0,arr,0,arrHalf1.length);
        System.arraycopy(arrHalf2,0,arr,arrHalf1.length,arrHalf2.length);
        long b = System.currentTimeMillis();
        System.out.printf("Метод 2 отработал за %d милисекунд\n", (b-a));
    }

    private void workArray() {
        for (int i = 0; i < arr.length; i++) {
            float j = (float) i;
            arr[i] = (float)(arr[i] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
        }
    }
}