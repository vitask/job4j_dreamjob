package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class IndexControllerTest {

    private IndexController indexController;

    @BeforeEach
    public void init() {
        indexController = new IndexController();
    }

    @Test
    public void whenGetIndexThenReturnIndex() {
        var expected = "index";

        var model = new ConcurrentModel();
        var view = indexController.getIndex(model);

        assertThat(view).isEqualTo(expected);
    }
}