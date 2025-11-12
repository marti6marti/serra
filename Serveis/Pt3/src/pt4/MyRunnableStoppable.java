package pt4;

public class MyRunnableStoppable implements Runnable{
    private boolean timeToStop = false;

    @Override
    public void run() {
        while (!timeToStop){
            try {
                System.out.println("Sóc el thread "
                        + Thread.currentThread().getName());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRunning(){
        timeToStop = true;
    }
}

