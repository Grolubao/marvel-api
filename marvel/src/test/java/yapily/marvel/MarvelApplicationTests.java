package yapily.marvel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yapily.marvel.controller.MarvelController;

@SpringBootTest
class MarvelApplicationTests {

    @Autowired
    private MarvelController marvelController;

    @Test
    void contextLoads() {
        Assertions.assertThat(marvelController).isNotNull();
    }
}
