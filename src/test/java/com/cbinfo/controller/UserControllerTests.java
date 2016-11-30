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
    private static final String REDIRECT_LOGIN = "redirect:/";

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String LAST_NAME = "last name";
    private static final String NOT_VALID_PASSWORD = "123";
    private static final String NOT_VALID_EMAIL = "wrong_email";
    private static final String BLANK_PASSWORD = "   ";
    private static final String WRONG_REENTERED = "wrong_reentered";
    private static final String EXCEPTION_MESSAGE_ON_WRONG_REENTERING = "Exception on reentering";
    private static final String NEW_COMPANY_NAME = "New company name";

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
                .param("reenteredPassword", PASSWORD)
                .param("newCompanyName", "")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_APP))
                .andExpect(redirectedUrl("/app"));

        Mockito.verify(userService, times(1)).isEmailRegistered(EMAIL);
        Mockito.verify(userService, times(1)).createUser(any(), any());
        Mockito.verify(userService, times(1)).checkReenteredPassword(PASSWORD, PASSWORD);
        Mockito.verify(userService, times(1)).setNewCompanyToUserForm(any(),anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postCreateUserWhenEmailIsRegisteredTest() throws Exception {
        when(userService.isEmailRegistered(EMAIL)).thenReturn(Boolean.TRUE);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("reenteredPassword", PASSWORD)
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
                .param("reenteredPassword", NOT_VALID_PASSWORD)
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
    public void postCreateUserWhenPasswordsAreNotTheSameTest() throws Exception {
        when(userService.isEmailRegistered(any())).thenReturn(Boolean.FALSE);
        doThrow(new Exception(EXCEPTION_MESSAGE_ON_WRONG_REENTERING)).when(userService).checkReenteredPassword(PASSWORD,WRONG_REENTERED);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("reenteredPassword", WRONG_REENTERED)
                .requestAttr("userForm", getUserForm())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(CREATE_USER_VIEW))
                .andExpect(forwardedUrl(CREATE_USER_VIEW))
                .andExpect(model().attribute("errorMessage", EXCEPTION_MESSAGE_ON_WRONG_REENTERING));

        Mockito.verify(userService, times(1)).isEmailRegistered(EMAIL);
        Mockito.verify(userService, times(1)).checkReenteredPassword(PASSWORD, WRONG_REENTERED);
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
    public void postEditUserOKTest() throws Exception {
        mockMvc.perform(post("/users/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("reenteredPassword", PASSWORD)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_APP))
                .andExpect(redirectedUrl("/app"));

        verify(userService, times(1)).updateCurrentUser(any(), anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserWithExceptionWhenUserUpdatingTest() throws Exception {
        doThrow(new Exception("Error")).when(userService).updateCurrentUser(any(), anyString());
        mockMvc.perform(post("/users/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("reenteredPassword", PASSWORD)
        )
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_USER_VIEW))
                .andExpect(forwardedUrl(EDIT_USER_VIEW))
                .andExpect(model().attribute("errorMessage", is("Error")));

        verify(userService, times(1)).updateCurrentUser(any(), anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserWhenPasswordIsNullTest() throws Exception {
        mockMvc.perform(post("/users/edit")
                .param("password", BLANK_PASSWORD)
                .param("email", EMAIL)
                .param("reenteredPassword", BLANK_PASSWORD)
                .requestAttr("userForm", getUserForm())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(REDIRECT_APP))
                .andExpect(redirectedUrl("/app"));

        verify(userService, times(1)).updateCurrentUser(any(), anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void postEditUserWithNotValidFieldsTest() throws Exception {
        mockMvc.perform(post("/users/edit")
                .param("password", NOT_VALID_PASSWORD)
                .param("reenteredPassword", PASSWORD)
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
