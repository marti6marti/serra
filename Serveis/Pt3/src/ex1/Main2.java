package ex1;

public class Main2 {
    public static void main(String[] args) {
        Ratoli mickey = new Ratoli("Mickey");
        Ratoli minnie = new Ratoli("Minnie");
        Ratoli jerry = new Ratoli("Jerry");



        Thread tMickey = new Thread(mickey);
        Thread tMinnie = new Thread(minnie);
        Thread tJerry = new Thread(jerry);

        tMickey.setName("tJoan");
        tMinnie.setName("tMaria");
        tJerry.setName("tTom");

        tMickey.setPriority(1);
        tMinnie.setPriority(3);
        tJerry.setPriority(1);

        tMinnie.start();
        tMickey.start();
        tJerry.start();

        System.out.println(tMickey.getName());
        System.out.println(tMinnie.getName());
        System.out.println(tJerry.getName());


    }
}


