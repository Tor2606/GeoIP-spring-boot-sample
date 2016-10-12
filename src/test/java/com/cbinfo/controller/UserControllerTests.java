package com.cbinfo.controller;

import com.cbinfo.dto.form.UserForm;
import com.cbinfo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {
    private static final String CREATE_USER_VIEW = "users/create";
    private static final String REDIRECT_APP = "redirect:/app";
    private static final String EDIT_USER_VIEW = "users/edit";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String LAST_NAME = "last name";
    private static final String NOT_VALID_PASSWORD = "123";
    private static final String NOT_VALID_EMAIL = "wrong_email";

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void postCreateUserWhenRedirectedTest() throws Exception {
        when(userService.isEmailRegistered(any())).thenReturn(Boolean.FALSE);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", EMAIL)
                .param("password", PASSWORD)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_LOGIN))
                .andExpect(redirectedUrl("/login"));

        Mockito.verify(userService, times(1)).isEmailRegistered(anyString());
        Mockito.verify(userService, times(1)).createUser(any(), any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postCreateUserWhenEmailIsRegisteredTest() throws Exception {
        when(userService.isEmailRegistered(any())).thenReturn(Boolean.TRUE);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .requestAttr("userForm", getUserForm())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_USER_VIEW))
                .andExpect(forwardedUrl(CREATE_USER_VIEW))
                .andExpect(model().attribute("errorMessage", "Email is not available!"));

        Mockito.verify(userService, times(1)).isEmailRegistered(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postCreateUserWhenFieldErrorsTest() throws Exception {
        when(userService.isEmailRegistered(any())).thenReturn(Boolean.FALSE);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", NOT_VALID_EMAIL)
                .param("password", NOT_VALID_PASSWORD)
                .requestAttr("userForm", getUserForm())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_USER_VIEW))
                .andExpect(forwardedUrl(CREATE_USER_VIEW))
                .andExpect(model().attributeHasFieldErrors("userForm", "email"))
                .andExpect(model().attributeHasFieldErrors("userForm", "password"));

        Mockito.verify(userService, times(1)).isEmailRegistered(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getCreateUserTest() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_USER_VIEW))
                .andExpect(forwardedUrl(CREATE_USER_VIEW))
                .andExpect(model().attributeExists("userForm"));

        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getEditUserTest() throws Exception {
        UserForm userForm = getUserForm();
        when(userService.getCurrentSessionUserToForm()).thenReturn(userForm);

        mockMvc.perform(get("/users/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_USER_VIEW))
                .andExpect(forwardedUrl(EDIT_USER_VIEW))
                .andExpect(model().attribute("userForm", is(userForm)));

        verify(userService, times(1)).getCurrentSessionUserToForm();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserTest() throws Exception {
        mockMvc.perform(post("/users/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_APP))
                .andExpect(redirectedUrl("/app"));

        verify(userService, times(1)).updateCurrentUser(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserWithExceptionWhenUserUpdatingTest() throws Exception {
        doThrow(new Exception("Error")).when(userService).updateCurrentUser(any());
        mockMvc.perform(post("/users/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_USER_VIEW))
                .andExpect(forwardedUrl(EDIT_USER_VIEW))
                .andExpect(model().attribute("errorMessage", is("Error")));

        verify(userService, times(1)).updateCurrentUser(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserWhenPasswordIsNullTest() throws Exception {
        mockMvc.perform(post("/users/edit")
                .param("password", "")
                .param("email", EMAIL)
                .requestAttr("userForm", getUserForm())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_APP))
                .andExpect(redirectedUrl("/app"));

        verify(userService, times(1)).updateCurrentUser(any());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserWithNotValidFieldsTest() throws Exception {
        mockMvc.perform(post("/users/edit")
                .param("password", NOT_VALID_PASSWORD)
                .param("email", NOT_VALID_EMAIL)
                .requestAttr("userForm", getUserForm())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_USER_VIEW))
                .andExpect(forwardedUrl(EDIT_USER_VIEW))
                .andExpect(model().attributeHasFieldErrors("userForm", "email"))
                .andExpect(model().attributeHasFieldErrors("userForm", "password"))
                .andExpect(model().attributeExists("userForm"));

        verifyNoMoreInteractions(userService);
    }

    private UserForm getUserForm() {
        UserForm result = new UserForm();
        result.setEmail(EMAIL);
        result.setPassword(PASSWORD);
        result.setFirstName(NAME);
        result.setLastName(LAST_NAME);
        System.out.println(result);
        return result;
    }
}
