public class Test {
    public static void main(String[] args) {
        new Thread(new Thread(){{
                super.run();
            }
        });
    }
}
