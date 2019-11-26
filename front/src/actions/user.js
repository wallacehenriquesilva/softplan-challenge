import {API_BASE_URL} from "../constants";
import React from "react";

export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const SIGNUP_REQUEST = 'SIGNUP_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';
export const LOGOUT_REQUEST = 'LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';

function requestLogin(creds) {
    return {
        type: LOGIN_REQUEST,
        isFetching: true,
        isAuthenticated: false,
        creds,
    };
}

export function receiveLogin(user) {
    return {
        type: LOGIN_SUCCESS,
        isFetching: false,
        isAuthenticated: true,
        id_token: user.id_token,
    };
}

function loginError(message) {
    return {
        type: LOGIN_FAILURE,
        isFetching: false,
        isAuthenticated: false,
        message,
    };
}

function requestLogout() {
    return {
        type: LOGOUT_REQUEST,
        isFetching: true,
        isAuthenticated: true,
    };
}

export function receiveLogout() {


    return {
        type: LOGOUT_SUCCESS,
        isFetching: false,
        isAuthenticated: false,

    };
}

export function logoutUser() {
    var regex = new RegExp('(http)?s?:?(\/\/)?\w+:\d{4}');
    var result = regex.exec(window.location.href);
    console.log("HREF " + result)
    window.location.href = "/";

    return dispatch => {
        dispatch(requestLogout());
        localStorage.removeItem('id_token');
        document.cookie = 'id_token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        dispatch(receiveLogout());
    };
}

export function loginUser(creds) {
    const config = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        credentials: 'include',
        body: JSON.stringify({'email': creds.login, 'password': creds.password}),
    };

    return dispatch => {
        dispatch(requestLogin(creds));

        return fetch(API_BASE_URL + '/api/v1/auth/generate/token', config)
            .then(response => response.json().then(user => ({user, response})))
            .then(({user, response}) => {
                if (!response.ok) {
                    alert(user.message);
                    dispatch(loginError(user.message));
                    return Promise.reject(user);
                }
                localStorage.setItem('id_token', user.token);
                dispatch(receiveLogin(user));
                return Promise.resolve(user);
            })
            .catch(err => console.error('Error: ', err));
    };
}
