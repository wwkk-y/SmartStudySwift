import lombok.SneakyThrows;

import java.time.LocalDateTime;

public class Test {
    @SneakyThrows
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            System.out.println(LocalDateTime.now());
            Thread.sleep(10);
        }

    }
}
