package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initService() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenUserRegistrationAndRedirectToVacancies() {
        var user = new User(1, "email@email.ru", "name", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(user, model);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenSomeExceptionThrowThenTetErrorPageWithMessage() {
        var expectedException = "Пользователь с такой почтой уже существует";
        when(userService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.register(new User(), model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException);
    }

    @Test
    public void whenUserLoginAndRedirectVacancies() {
        var user = new User(1, "email@email.ru", "name", "password");
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var request = new MockHttpServletRequest();
        var view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenUserNotLoginThenReturnLoginView() {
        var user = new User(1, "email@email.ru", "name", "password");
        var expectedException = "Почта или пароль введены неверно";
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var request = new MockHttpServletRequest();
        var view = userController.loginUser(user, model, request);
        var actualExceptionError = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionError).isEqualTo(expectedException);
    }

    @Test
    public void whenRequestLoginPageThenLoginView() {
        var view = userController.getLoginPage();
        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenRequestLogoutPageThenLoginView() {
        var session = new MockHttpSession();
        var view = userController.logout(session);
        assertThat(view).isEqualTo("redirect:/users/login");
    }
}