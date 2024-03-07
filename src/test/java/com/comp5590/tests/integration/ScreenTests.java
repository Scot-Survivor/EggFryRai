package com.comp5590.tests.integration;

import com.comp5590.App;
import com.comp5590.screens.AbstractScreen;
import com.comp5590.tests.basic.SetupTests;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)  // TestFX Extension
public class ScreenTests extends SetupTests {
    App app;

    @Start  // This is similar to @BeforeAll it will run before all tests,
            // this is where we can get the stage and start the application
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
        stage.show();
    }

    private Reflections getScreenList() {
        return new Reflections("com.comp5590.screens", Scanners.SubTypes);
    }

    @Test
    public void testAllScreensWereRegistered() {
        Reflections reflections = getScreenList();
        long screenCount = reflections.getSubTypesOf(AbstractScreen.class).size();
        Assertions.assertThat(app.getScreenManager().getScreens().size()).isEqualTo(screenCount);
    }
}
